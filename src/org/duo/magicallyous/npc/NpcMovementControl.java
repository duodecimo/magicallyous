/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.npc;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
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
import org.duo.magicallyous.MainCharControl;
import org.duo.magicallyous.utils.ActionState;

/**
 *
 * @author duo
 */
public class NpcMovementControl extends AbstractControl {
    //Any local variables should be encapsulated by getters/setters so they
    //appear in the SDK properties window and can be edited.
    //Right-click a local variable to encapsulate it with getters and setters.

    private AnimControl animControl;
    private AnimChannel animChannel;
    enum NpcState {
        WALK, RUN, ATTACK, IDLE
    };
    private NpcState npcState = NpcState.WALK;
    private NpcState previousNpcState = NpcState.WALK;
    private boolean walkRandom = true;
    private Random random;
    private final Quaternion lookRotation = new Quaternion();
    private double timeCounter = 0l;
    private boolean active = false;
    // terrain bounds for npcs
    private float xmax, xmin, zmax, zmin;
    // terrain center for npcs
    private float xcenter, zcenter;
    // debug position
    private boolean debugPosition = false;
    long debugPositionTime = 0l;
    Node mainChar;

    public void setNpcTerrainBounds(float xmax, float xmin, float zmax, float zmin) {
        this.xmax = xmax;
        this.xmin = xmin;
        this.zmax = zmax;
        this.zmin = zmin;
    }

    public void setMainChar(Node mainChar) {
        this.mainChar = mainChar;
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
            if(mainChar != null) {
                Vector3f aim = mainChar.getWorldTranslation();
                Vector3f dist = aim.subtract(spatial.getWorldTranslation());
                if (dist.length() < 3.0f) {
                    if(npcState != NpcState.ATTACK) {
                        previousNpcState = npcState;
                        npcState = NpcState.ATTACK;
                        lookRotation.lookAt(dist, Vector3f.UNIT_Y);
                        spatial.setLocalRotation(lookRotation);
                        lookRotation.lookAt(dist.negate(), Vector3f.UNIT_Y);
                        mainChar.setLocalRotation(lookRotation);
                        MainCharControl mainCharControl = mainChar.getControl(MainCharControl.class);
                        mainCharControl.setTarget(spatial);
                        mainCharControl.setActionState(ActionState.ATTACK);
                    }
                } else {
                    // stop attacking
                    if(npcState == NpcState.ATTACK) {
                        npcState = previousNpcState;
                    }
                }
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
                System.out.println("spider location: "
                  + spatial.getLocalTranslation() + " "
                  +                        spatial.getWorldTranslation());
            }
            if (checkControl()) {
                if (npcState == NpcState.WALK) {
                    // check the animation
                    if (animChannel.getAnimationName().compareTo("Walk_1") != 0) {
                        animChannel.setAnim("Walk_1");
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
                    if (animChannel.getAnimationName().compareTo("Strike") != 0) {
                        animChannel.setAnim("Strike");
                        animChannel.setSpeed(1.0f);
                        animChannel.setLoopMode(LoopMode.Loop);
                    }
                }
            }
        }
    }

    protected boolean checkControl() {
        AnimControl control = spatial.getControl(AnimControl.class);
        if (control != animControl) {
            animControl = control;
            animChannel = animControl.createChannel();
            animChannel.setAnim("Walk_1");
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
        NpcMovementControl control = new NpcMovementControl();
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
}
