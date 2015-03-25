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
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;
import org.duo.magicallyous.utils.WalkStateEnum;

/**
 *
 * @author duo
 */
public class PlayerControl extends AbstractControl implements AnimEventListener {
    //Any local variables should be encapsulated by getters/setters so they
    //appear in the SDK properties window and can be edited.
    //Right-click a local variable to encapsulate it with getters and setters.
    AnimControl animControl;
    AnimChannel animChannel;
    private ActionStateEnum actionState = ActionStateEnum.IDLE;
    //private ActionStateEnum previousActionState = ActionStateEnum.IDLE;
    private WalkStateEnum walkState = WalkStateEnum.NORMAL;
    private boolean turningLeft = false;
    private boolean turningRight = false;
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
        if(actionState == ActionStateEnum.ATTACK) {
            //previousActionState = this.actionState;
        }
        this.actionState = actionState;
    }

    public void toggleWalkState() {
        if(walkState == WalkStateEnum.NORMAL) {
            walkState = WalkStateEnum.RUN;
        } else {
            walkState = WalkStateEnum.NORMAL;
        }
    }

    public void setTurningLeft(boolean turningLeft) {
        this.turningLeft = turningLeft;
    }

    public void setTurningRight(boolean turningRight) {
        this.turningRight = turningRight;
    }

    public void setStartAttack(boolean startAttack) {
        this.startAttack = startAttack;
    }

    @Override
    protected void controlUpdate(float tpf) {
        timeCounter += (double) tpf;
        if(checkControl()) {
            if (actionState != ActionStateEnum.ATTACK) {
                if (turningLeft) {
                    spatial.rotate(0.0f, FastMath.DEG_TO_RAD * 0.2f, 0.0f);
                } else if (turningRight) {
                    spatial.rotate(0.0f, FastMath.DEG_TO_RAD * -0.2f, 0.0f);
                }
            }
            if(actionState == ActionStateEnum.WALK) {
                if (walkState == WalkStateEnum.RUN) {
                    if (animChannel.getAnimationName().compareTo("Run") != 0) {
                        animChannel.setAnim("Run");
                        animChannel.setLoopMode(LoopMode.Loop);
                    }
                } else {
                    if (animChannel.getAnimationName().compareTo("Walk") != 0) {
                        animChannel.setAnim("Walk");
                        animChannel.setLoopMode(LoopMode.Loop);
                    }
                }
                // walk foward
                Vector3f walkDirection = Vector3f.ZERO;
                // get foward direction
                Vector3f fowardDirection = spatial.getWorldRotation().getRotationColumn(2);
                if(actionState == ActionStateEnum.WALK) {
                    // add direction
                    walkDirection.addLocal(fowardDirection);
                    // calculate distance to walk
                    if(walkState == WalkStateEnum.RUN) {
                        spatial.move(walkDirection.multLocal(9.0f).multLocal(tpf));
                    } else {
                        spatial.move(walkDirection.multLocal(1.0f).multLocal(tpf));
                    }
                }
            } else if(actionState == ActionStateEnum.IDLE) {
                if(animChannel.getAnimationName().compareTo("Idle")!=0) {
                    animChannel.setAnim("Idle");
                    animChannel.setSpeed(0.5f);
                    animChannel.setLoopMode(LoopMode.Loop);
                    System.out.println("mainchar location: " + spatial.getLocalTranslation()
                            + "    " + spatial.getWorldTranslation());
                }
            } else if(actionState == ActionStateEnum.ATTACK) {
                if(startAttack) {
                    startAttack = false;
                    // face target
                    if (target != null) {
                        Vector3f aim = target.getWorldTranslation();
                        Vector3f dist = aim.subtract(spatial.getWorldTranslation());
                        System.out.println("Starting fight with target at: "
                                + target.getWorldTranslation() + " distance: "
                                + dist.length());
                        lookRotation = new Quaternion();
                        lookRotation.lookAt(dist, Vector3f.UNIT_Y);
                        spatial.setLocalRotation(lookRotation);
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
            }
        }
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

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }
    
    @Override
    public Control cloneForSpatial(Spatial spatial) {
        PlayerControl control = new PlayerControl();
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
        }
    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }

    public void setTarget(Spatial target) {
        this.target = target;
    }
}
