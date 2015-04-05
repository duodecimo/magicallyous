/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.player;

import org.duo.magicallyous.utils.ActionStateEnum;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;
import org.duo.magicallyous.utils.CharacterMovementControl;

/**
 *
 * @author duo
 */
public class PlayerBattleControl extends AbstractControl implements AnimEventListener {
    private AnimControl animControl;
    private AnimChannel animChannel;
    private ActionStateEnum actionState = ActionStateEnum.IDLE;
    private double timeCounter = 0d;
    private double attackTimer = 0d;
    private boolean startAttack = false;
    private boolean waitingForCast = false;
    private boolean waitingForPrecast = false;
    private Spatial target;
    private Quaternion lookRotation;

    public ActionStateEnum getActionState() {
        return actionState;
    }

    public void setActionState(ActionStateEnum actionState) {
        this.actionState = actionState;
    }

    public void setStartAttack(boolean startAttack) {
        this.startAttack = startAttack;
    }

    @Override
    protected void controlUpdate(float tpf) {
        timeCounter += (double) tpf;
        if(checkControl()) {
            if(actionState == ActionStateEnum.BATTLE) {
                if(startAttack) {
                    startAttack = false;
                    Node player = (Node) spatial;
                    CharacterMovementControl characterMovementControl = 
                            player.getControl(CharacterMovementControl.class);
                    // in order to stop if moving
                   characterMovementControl.setWalkDirection(Vector3f.ZERO);
                    // face target
                    if (target != null) {
                        Vector3f aim = target.getWorldTranslation();
                        Vector3f dist = aim.subtract(spatial.getWorldTranslation());
                        lookRotation = new Quaternion();
                        lookRotation.lookAt(dist, Vector3f.UNIT_Y);
                        characterMovementControl.setViewDirection(
                                lookRotation.getRotationColumn(1).negate());
                        //spatial.setLocalRotation(lookRotation);
                        System.out.println("Starting fight with target at: "
                                + target.getWorldTranslation() + " distance: "
                                + dist.length());
                    } else {
                        System.out.println("Starting fight NULL target !!!!!");
                    }
                    // change anim
                    if(animChannel.getAnimationName().compareTo("Precast")!=0) {
                        animChannel.setAnim("Precast");
                        animChannel.setSpeed(1.0f);
                        animChannel.setLoopMode(LoopMode.DontLoop);
                    }
                } else {
                    // perform animations according to time
                    if(waitingForCast) {
                        //System.out.println("Waiting for cast from: " +  
                        //        attackTimer + " now: " + timeCounter + 
                        //        " difference: " + (timeCounter - attackTimer));
                        if (timeCounter - attackTimer > 0.5d) {
                            waitingForCast = false;
                            animChannel.setAnim("Cast");
                            animChannel.setSpeed(1.0f);
                            animChannel.setLoopMode(LoopMode.DontLoop);
                        }
                    } else if (waitingForPrecast) {
                        if (timeCounter - attackTimer > 0.8d) {
                            waitingForPrecast = false;
                            animChannel.setAnim("Precast");
                            animChannel.setSpeed(1.0f);
                            animChannel.setLoopMode(LoopMode.DontLoop);
                        }
                    }
                }
            } else if (actionState == ActionStateEnum.DIE) {
                if(target != null) target = null;
                if (animChannel.getAnimationName().compareTo("Die") != 0) {
                    animChannel.setAnim("Die");
                    animChannel.setSpeed(0.5f);
                    animChannel.setLoopMode(LoopMode.DontLoop);
                }
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }
    
    @Override
    public Control cloneForSpatial(Spatial spatial) {
        PlayerBattleControl control = new PlayerBattleControl();
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
        if(animName.compareTo("Precast") == 0) {
            //System.out.println("Start waiting for cast!");
            waitingForCast = true;
            waitingForPrecast = false;
            PlayerShootControl playerShootControl = new PlayerShootControl();
            playerShootControl.setTarget(target);
            spatial.addControl(playerShootControl);
            attackTimer = timeCounter;
        } else if(animName.compareTo("Cast")==0) {
            waitingForPrecast = true;
            waitingForCast = false;
            attackTimer = timeCounter;
        } else if(animName.compareTo("Die")==0) {
            // respawn
            spatial.setUserData("health", 100);
            spatial.move(0.0f, 0.0f, 0.0f);
            spatial.setLocalTranslation(0.0f, 0.0f, 0.0f);
            System.out.println("Player revived on " + spatial.getLocalTranslation());
            actionState = ActionStateEnum.IDLE;
        }
    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }

    protected boolean checkControl() {
        AnimControl control = spatial.getControl(AnimControl.class);
        if(control != animControl) {
            animControl = control;
            animControl.addListener(this);
            animChannel = animControl.createChannel();
            animChannel.setAnim("Idle");
            animChannel.setLoopMode(LoopMode.Loop);
        }
        return animControl != null;
    }

    public void setTarget(Spatial target) {
        this.target = target;
    }
}
