/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.player;

import org.duo.magicallyous.utils.AnimationStateEnum;
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
public class PlayerBattleControl extends AbstractControl {
    private AnimationStateEnum animationStateEnum = AnimationStateEnum.IDLE;
    private boolean startAttack = false;
    private Spatial target;
    private Quaternion lookRotation;
    private PlayerAnimationControl playerAnimationControl;

    public AnimationStateEnum getActionState() {
        return animationStateEnum;
    }

    public void setActionState(AnimationStateEnum actionState) {
        this.animationStateEnum = actionState;
    }

    public void setStartAttack(boolean startAttack) {
        this.startAttack = startAttack;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if(playerAnimationControl == null) {
            playerAnimationControl = spatial.getControl(PlayerAnimationControl.class);
        }
        if (animationStateEnum == AnimationStateEnum.BATTLE) {
            if (startAttack) {
                startAttack = false;
                Node player = (Node) spatial;
                CharacterMovementControl characterMovementControl =
                        player.getControl(CharacterMovementControl.class);
                // in order to stop if moving
                characterMovementControl.setWalkDirection(Vector3f.ZERO);
                characterMovementControl.setAnimationStateEnum(AnimationStateEnum.BATTLE);
                // face target
                if (target != null) {
                    Vector3f aim = target.getWorldTranslation();
                    Vector3f dist = aim.subtract(spatial.getWorldTranslation());
                    lookRotation = new Quaternion();
                    lookRotation.lookAt(dist, Vector3f.UNIT_Y);
                    characterMovementControl.setViewDirection(
                            lookRotation.getRotationColumn(1).negate());
                    //spatial.setLocalRotation(lookRotation);
                    System.out.println("Player starting fight with target at: "
                            + target.getWorldTranslation() + " distance: "
                            + dist.length());
                } else {
                    System.out.println("Starting fight NULL target !!!!!");
                }
                // change anim
                playerAnimationControl.setTarget(target);
                playerAnimationControl.setAnimationStateEnum(AnimationStateEnum.BATTLE);
                playerAnimationControl.setStartAttack(true);
            } else {
            }
        } else if (animationStateEnum == AnimationStateEnum.DIE) {
            spatial.getControl(PlayerAnimationControl.class).setAnimationStateEnum(AnimationStateEnum.DIE);
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

    public void setTarget(Spatial target) {
        this.target = target;
    }
}
