/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.player;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;
import org.duo.magicallyous.utils.AnimationStateEnum;

/**
 *
 * @author duo
 */
public class PlayerAnimationControl extends AbstractControl implements AnimEventListener {
    private AnimControl animControl;
    private AnimChannel animChannel;
    private AnimationStateEnum animationStateEnum;
    private double timeCounter = 0d;
    private double attackTimer = 0d;
    private boolean startAttack = false;
    private boolean waitingForCast = false;
    private boolean waitingForPrecast = false;
    private Spatial target;

    @Override
    protected void controlUpdate(float tpf) {
        if (checkControl()) {
            if (animationStateEnum == AnimationStateEnum.IDLE) {
                if (animChannel.getAnimationName().compareTo("Idle") != 0) {
                    animChannel.setAnim("Idle");
                    animChannel.setSpeed(0.2f);
                    animChannel.setLoopMode(LoopMode.Loop);
                }
            } else if (animationStateEnum == AnimationStateEnum.WALK) {
                if (animChannel.getAnimationName().compareTo("Walk") != 0) {
                    animChannel.setAnim("Walk");
                    animChannel.setSpeed(1.0f);
                    animChannel.setLoopMode(LoopMode.Loop);
                }
            } else if (animationStateEnum == AnimationStateEnum.RUN) {
                if (animChannel.getAnimationName().compareTo("Run") != 0) {
                    animChannel.setAnim("Run");
                    animChannel.setSpeed(1.0f);
                    animChannel.setLoopMode(LoopMode.Loop);
                }
            } else if (animationStateEnum == AnimationStateEnum.BATTLE) {
                if(startAttack) {
                    startAttack = false;
                    Node player = (Node) spatial;
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
            } else if (animationStateEnum == AnimationStateEnum.DIE) {
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
        PlayerAnimationControl control = new PlayerAnimationControl();
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
    
    public AnimationStateEnum getAnimationStateEnum() {
        return animationStateEnum;
    }

    public void setAnimationStateEnum(AnimationStateEnum animationStateEnum) {
        this.animationStateEnum = animationStateEnum;
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
            animationStateEnum = AnimationStateEnum.IDLE;
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
