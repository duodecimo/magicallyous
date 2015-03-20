/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous;

import org.duo.magicallyous.utils.ActionState;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.bullet.control.BetterCharacterControl;
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
import org.duo.magicallyous.utils.WalkState;

/**
 *
 * @author duo
 */
public class MainCharControl extends AbstractControl {
    //Any local variables should be encapsulated by getters/setters so they
    //appear in the SDK properties window and can be edited.
    //Right-click a local variable to encapsulate it with getters and setters.
    AnimControl animControl;
    AnimChannel animChannel;
    private ActionState actionState = ActionState.IDLE;
    private WalkState walkState = WalkState.NORMAL;
    private boolean turningLeft = false;
    private boolean turningRight = false;

    public ActionState getActionState() {
        return actionState;
    }

    public void setActionState(ActionState actionState) {
        this.actionState = actionState;
    }

    public void toggleWalkState() {
        if(walkState == WalkState.NORMAL) {
            walkState = WalkState.RUN;
        } else {
            walkState = WalkState.NORMAL;
        }
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (checkControl()) {
            BetterCharacterControl betterCharacterControl = spatial.
                    getControl(BetterCharacterControl.class);
            // walk direction
            Vector3f walkDirection = Vector3f.ZERO;
            // foward direction
            Vector3f fowardDirection = spatial.getWorldRotation().mult(Vector3f.UNIT_Z);
            // side direction
            Vector3f sideDirection = spatial.getWorldRotation().mult(Vector3f.UNIT_X);
            // view direction
            Vector3f viewDirection = betterCharacterControl.getViewDirection();
            if (turningLeft) {
                walkDirection.addLocal(sideDirection.mult(1.0f));
                Quaternion rotate = new Quaternion().
                        fromAngleAxis(FastMath.PI * tpf, Vector3f.UNIT_Y);
                rotate.multLocal(viewDirection);
            } else if (turningRight) {
                walkDirection.addLocal(sideDirection.negate().mult(1.0f));
                Quaternion rotate = new Quaternion().
                        fromAngleAxis(-FastMath.PI * tpf, Vector3f.UNIT_Y);
                rotate.multLocal(viewDirection);
            }
            if (actionState == ActionState.WALK) {
                // calculate distance to walk
                if (walkState == WalkState.RUN) {
                    walkDirection.addLocal(fowardDirection.multLocal(9.0f));
                } else {
                    walkDirection.addLocal(fowardDirection.multLocal(1.0f));
                }
            }
            betterCharacterControl.setWalkDirection(walkDirection);
            betterCharacterControl.setViewDirection(viewDirection.normalize());
            // set animation
            if (actionState == ActionState.WALK) {
                if (walkState == WalkState.RUN) {
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
            } else {
                if (animChannel.getAnimationName().compareTo("Idle") != 0) {
                    animChannel.setAnim("Idle");
                    animChannel.setSpeed(0.5f);
                    animChannel.setLoopMode(LoopMode.Loop);
                    System.out.println("mainchar location: " + spatial.getLocalTranslation()
                            + "    " + spatial.getWorldTranslation());
                }
            }
        }
    }

    protected boolean checkControl() {
        AnimControl control = spatial.getControl(AnimControl.class);
        if(control != animControl) {
            animControl = control;
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
        MainCharControl control = new MainCharControl();
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

    public void setTurningLeft(boolean turningLeft) {
        this.turningLeft = turningLeft;
    }

    public void setTurningRight(boolean turningRight) {
        this.turningRight = turningRight;
    }
}
