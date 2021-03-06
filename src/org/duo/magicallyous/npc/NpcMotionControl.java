/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.npc;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;
import java.util.Random;
import org.duo.magicallyous.utils.AnimationStateEnum;
import org.duo.magicallyous.player.PlayerActionControl;
import org.duo.magicallyous.player.PlayerActionControl;
import org.duo.magicallyous.utils.GeneralStateEnum;
import org.duo.magicallyous.utils.ParticleBlowControl;

/**
 *
 * @author duo
 */
public class NpcMotionControl extends AbstractControl implements AnimEventListener {
    private AnimControl animControl;
    private AnimChannel animChannel;
    PlayerActionControl playerMovementControl;

    public void setNpcAnimationWalk(String npcAnimationWalk) {
        this.npcAnimationWalk = npcAnimationWalk;
    }

    public void setNpcAnimationRun(String npcAnimationRun) {
        this.npcAnimationRun = npcAnimationRun;
    }

    public void setNpcAnimationAttack(String npcAnimationAttack) {
        this.npcAnimationAttack = npcAnimationAttack;
    }

    public void setNpcAnimationIdle(String npcAnimationIdle) {
        this.npcAnimationIdle = npcAnimationIdle;
    }

    public void setNpcAnimationDead(String npcAnimationDead) {
        this.npcAnimationDead = npcAnimationDead;
    }

    enum NpcState {
        WALK, RUN, ATTACK, IDLE, DEAD
    };
    private String npcAnimationWalk;
    private String npcAnimationRun;
    private String npcAnimationAttack;
    private String npcAnimationIdle;
    private String npcAnimationDead;
    private NpcState npcState = NpcState.WALK;
    private NpcState previousNpcState = NpcState.WALK;
    private Random random;
    private final Quaternion lookRotation = new Quaternion();
    private double timeCounter = 0d;
    private double deathTimeCounter = 0d;
    private double battleStartTime = 0d;
    // terrain bounds for npcs
    private float xmax, xmin, zmax, zmin;
    // terrain center for npcs
    private float xcenter, zcenter;
    private long lastEndOfBattle = 0l;
    private boolean active = false;
    // debug position
    private boolean debugPosition = false;
    private boolean walkRandom = true;
    private boolean animateAttack;
    Node player;
    SimpleApplication app;

    public NpcMotionControl(SimpleApplication app) {
        this.app = app;
    }

    public void setNpcTerrainBounds(float xmax, float xmin, float zmax, float zmin) {
        this.xmax = xmax;
        this.xmin = xmin;
        this.zmax = zmax;
        this.zmin = zmin;
    }

    public void setMainChar(Node player) {
        this.player = player;
    }

    public Vector2f getSpawnLocation() {
        Vector2f vector2f = new Vector2f(0.0f, 0.0f);
        if(random == null) {
            random = new Random();
        }
        float pointX = xmin + 2.0f + random.nextInt(((int) Math.abs(xmax - xmin)) -4)* (xmin/xmin);
        float pointZ = zmin + 2.0f + random.nextInt(((int) Math.abs(zmax - zmin)) -4)* (zmin/zmin);
        vector2f.x = pointX;
        vector2f.y = pointZ;
        return vector2f;
    }
    public void setNpcTerrainCenter(float xcenter, float zcenter) {
        this.xcenter = xcenter;
        this.zcenter = zcenter;
    }

    public void setDebugPosition(boolean bol) {
        debugPosition = bol;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    protected void controlUpdate(float tpf) {
        Vector3f walkDirection = Vector3f.ZERO;
        timeCounter += tpf;
        if (this.isActive()) {
            // check if main char is in attack range
            if (playerMovementControl == null) {
                playerMovementControl =
                        player.getControl(PlayerActionControl.class);
            }
            if (playerOnBound()) {
                if (timeCounter - lastEndOfBattle > 10) {
                    // after battle spend at least 10 seconds without engaging
                    lastEndOfBattle = 0l;
                    if (player != null
                            && playerMovementControl.getAnimationStateEnum()
                            != AnimationStateEnum.DIE
                            && playerMovementControl.getGeneralStateEnum()
                            != GeneralStateEnum.BATTLE
                            && npcState != NpcState.DEAD
                            && npcState != NpcState.ATTACK) {
                        Vector3f aim = player.getWorldTranslation();
                        Vector3f dist = aim.subtract(spatial.getWorldTranslation());
                        if (dist.length() < 3.0f) {
                            // avoid attacking a player already beeing attacked
                            if (npcState != NpcState.ATTACK) {
                                previousNpcState = npcState;
                                npcState = NpcState.ATTACK;
                                animateAttack = true;
                                lookRotation.lookAt(dist, Vector3f.UNIT_Y);
                                spatial.setLocalRotation(lookRotation);
                                //lookRotation.lookAt(dist.negate(), Vector3f.UNIT_Y);
                                //player.setLocalRotation(lookRotation);
                                if (playerMovementControl instanceof PlayerActionControl) {
                                    ((PlayerActionControl) 
                                            playerMovementControl).setTarget(spatial);
                                    ((PlayerActionControl) 
                                            playerMovementControl).setStartAttack(true);
                                    ((PlayerActionControl) 
                                            playerMovementControl).setGeneralStateEnum(GeneralStateEnum.BATTLE);
                                }
                                battleStartTime = timeCounter;
                                System.out.println("NPC start attacking player from distance: " + dist.length());
                            }
                        }
                    }
                }
            } else {
            }
            // randomly toggle between walk and idle each 20 seconds
            if (((long) timeCounter) % 20 == 0) {
                timeCounter += 1;
                if (npcState == NpcState.WALK) {
                    if (random == null) {
                        random = new Random();
                    }
                    switch (random.nextInt(10)) {
                        case 0:
                            npcState = NpcState.IDLE;
                            break;
                        default:
                            break;
                    }
                } else if (npcState == NpcState.IDLE) {
                    npcState = NpcState.WALK;
                }
            }
            if (debugPosition) {
                //System.out.println("npc location: "
                //  + spatial.getLocalTranslation() + " "
                //  +                        spatial.getWorldTranslation());
            }
            if (checkControl()) {
                if (npcState == NpcState.WALK) {
                    // check the animation
                    if (animChannel.getAnimationName().compareTo(npcAnimationWalk) != 0) {
                        animChannel.setAnim(npcAnimationWalk);
                        animChannel.setSpeed(0.5f);
                        animChannel.setLoopMode(LoopMode.Loop);
                    }
                    // check terrain limits
                    float x = spatial.getWorldTranslation().x;
                    float z = spatial.getWorldTranslation().z;
                    if (x >= xmax || x <= xmin || z >= zmax || z <= zmin) {
                        walkRandom = false;
                    }
                    if (walkRandom) {
                        // get foward direction
                        Vector3f fowardDirection = spatial.getWorldRotation().getRotationColumn(2);
                        // add direction
                        walkDirection.addLocal(fowardDirection);
                        // calculate distance to walk
                        walkDirection.normalizeLocal();
                        spatial.move(walkDirection.multLocal(1.0f).multLocal(tpf));
                        // randomly turn
                        if (random == null) {
                            random = new Random();
                        }
                        switch (random.nextInt(50)) {
                            case 0:
                                // turn left
                                spatial.rotate(0.0f, FastMath.DEG_TO_RAD * 5f, 0.0f);
                                break;
                            case 1:
                                // turn right
                                spatial.rotate(0.0f, FastMath.DEG_TO_RAD * -5f, 0.0f);
                                break;
                            default:
                                break;
                        }
                    } else {
                        Vector3f aim = new Vector3f(xcenter, 0.0f, zcenter);
                        Vector3f dist = aim.subtract(spatial.getWorldTranslation());
                        if (dist.length() < 1.0f) {
                            walkRandom = true;
                        } else {
                            dist.normalizeLocal();
                            lookRotation.lookAt(dist, Vector3f.UNIT_Y);
                            spatial.setLocalRotation(lookRotation);
                            spatial.move(dist.multLocal(1.0f * tpf));
                        }
                    }
                } else if(npcState == NpcState.IDLE) {
                        // randomly turn
                        if (random == null) {
                            random = new Random();
                        }
                        switch (random.nextInt(5)) {
                            case 0:
                                // turn left
                                spatial.rotate(0.0f, FastMath.DEG_TO_RAD * 5f, 0.0f);
                                break;
                            case 1:
                                // turn right
                                spatial.rotate(0.0f, FastMath.DEG_TO_RAD * -5f, 0.0f);
                                break;
                            default:
                                break;
                        }
                } else if(npcState == NpcState.ATTACK) {
                    // check the animation
                    if (animateAttack) {
                        spatial.addControl(new ParticleBlowControl(app, player));
                        //if (animChannel.getAnimationName().compareTo(npcAnimationAttack) != 0) {
                            animChannel.setAnim(npcAnimationAttack);
                            animChannel.setSpeed(1.0f);
                            animChannel.setLoopMode(LoopMode.DontLoop);
                        //}
                        animateAttack = false;
                    }
                    int health = spatial.getUserData("health");
                    if(health <= 0) {
                        int playerDefense = player.getUserData("defense");
                        playerMovementControl.setAnimationStateEnum(AnimationStateEnum.IDLE);
                        // player get stronger!
                        player.setUserData("defense", playerDefense+2);
                        npcRemoveBlowControls();
                        playerMovementControl.removeShootControls();
                        npcDie();
                    }
                    if((timeCounter - battleStartTime) >= 3.0f) {
                        // cause damage each 3 seconds
                        animateAttack = true;
                        battleStartTime = timeCounter;
                        int damage = spatial.getUserData("damage");
                        int defense = player.getUserData("defense");
                        int playerHealth = player.getUserData("health");
                        playerHealth -= (damage - defense);
                        player.setUserData("health", playerHealth);
                        System.out.println("npc caused " + damage + " of damage to player, health now = " +
                                playerHealth);
                    }
                    if((int) player.getUserData("health") <= 0) {
                        // player must die
                        npcRemoveBlowControls();
                        playerMovementControl.removeShootControls();
                        if (playerMovementControl instanceof PlayerActionControl) {
                            ((PlayerActionControl) 
                                    playerMovementControl).setTarget(null);
                        }
                        playerMovementControl.setAnimationStateEnum(AnimationStateEnum.DIE);
                        System.out.println("NPC killed target!");
                        npcState = previousNpcState;
                        if(npcState == NpcState.ATTACK) {
                            npcState = NpcState.IDLE;
                        }
                    }
                } else if(npcState == NpcState.DEAD) {
                    if(timeCounter - deathTimeCounter > 10.0d) {
                        npcRevive();
                    } else {
                        //System.out.println("Npc will revive in 10: (" + 
                        //        (timeCounter - deathTimeCounter) + ")");
                    }
                }
            }
        }
    }

    protected void npcDie() {
        if (playerMovementControl instanceof PlayerActionControl) {
                            ((PlayerActionControl) 
                                    playerMovementControl).setTarget(null);
        }
        playerMovementControl.setAnimationStateEnum(AnimationStateEnum.IDLE);
        deathTimeCounter = timeCounter;
        npcState = NpcState.DEAD;
        spatial.setCullHint(Spatial.CullHint.Always);
    }

    protected void npcRemoveBlowControls() {
        ParticleBlowControl particleBlowControl;
        do {
            particleBlowControl = spatial.getControl(ParticleBlowControl.class);
            spatial.removeControl(particleBlowControl);
        } while(particleBlowControl!=null);
    }

    protected void npcRevive() {
        spatial.setCullHint(Spatial.CullHint.Inherit);
        spatial.setUserData("health", 100);
        Vector3f vector3f = new Vector3f(0.0f, 0.0f, 0.0f);
        Vector2f vector2f = getSpawnLocation();
        vector3f.x = vector2f.x;
        vector3f.z = vector2f.y;
        spatial.setLocalTranslation(vector3f);
        npcState = previousNpcState;
        System.out.println("npc respawn at: " + vector3f);
    }

    protected boolean checkControl() {
        AnimControl control = spatial.getControl(AnimControl.class);
        if (control != animControl) {
            animControl = control;
            animControl.addListener(this);
            animChannel = animControl.createChannel();
            animChannel.setAnim(npcAnimationWalk);
            animChannel.setLoopMode(LoopMode.Loop);
        }
        return animControl != null;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        NpcMotionControl control = new NpcMotionControl(app);
        //TODO: copy parameters to new Control
        return control;
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule in = im.getCapsule(this);
        //TODO: load properties of this Control, e.g.
        //this.value = in.readFloat("name", defaultValue);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule out = ex.getCapsule(this);
        //TODO: save properties of this Control, e.g.
        //out.write(this.value, "name", defaultValue);
    }

    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if(animName.compareTo("Strike") == 0) {
        }
    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }

    private boolean playerOnBound() {
        Vector3f vector3f = player.getWorldTranslation();
        if(vector3f.x <= xmax && vector3f.x >= xmin && vector3f.z <= zmax && vector3f.z >= zmin) {
            return true;
        }
        return false;
    }
}
