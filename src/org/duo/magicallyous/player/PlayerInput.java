/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.player;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.scene.Node;
import org.duo.magicallyous.utils.ActionStateEnum;

/**
 *
 * @author duo
 */
public class PlayerInput extends AbstractAppState implements ActionListener, AnalogListener {
    SimpleApplication app;
    AppStateManager stateManager;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.stateManager = stateManager;
        setupKeys(app.getInputManager());
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        Node scene = (Node) app.getRootNode().getChild("Scene01");
        Node player = (Node) scene.getChild("player");
        Node rightHandNode = (Node) player.getChild("hand.R_attachnode");
        Node swordNode = (Node) rightHandNode.getChild("sword01");
        PlayerMotionControl playerControl = player.getControl(PlayerMotionControl.class);
        if (playerControl.getActionState() != ActionStateEnum.ATTACK) {
            switch (name) {
                case PlayerActionMapping.MAP_GOFOWARD :
                    if (isPressed) {
                        playerControl.setActionState(ActionStateEnum.WALK);
                    } else {
                        playerControl.setActionState(ActionStateEnum.IDLE);
                    }
                    break;
                case PlayerActionMapping.MAP_STOP :
                    playerControl.setActionState(ActionStateEnum.IDLE);
                    break;
                case PlayerActionMapping.MAP_TURNLEFT :
                    if (isPressed) {
                        playerControl.setTurningLeft(true);
                    } else {
                        playerControl.setTurningLeft(false);
                    }
                    break;
                case PlayerActionMapping.MAP_TURNRIGHT :
                    if (isPressed) {
                        playerControl.setTurningRight(true);
                    } else {
                        playerControl.setTurningRight(false);
                    }
                    break;
                case PlayerActionMapping.MAP_TOGGLEWALKSTATE :
                    if (isPressed) {
                        playerControl.toggleWalkState();
                    }
                    break;
                case PlayerActionMapping.MAP_USESWORD :
                    if (isPressed) {
                        if (rightHandNode.hasChild(swordNode)) {
                            rightHandNode.detachChild(swordNode);
                        } else {
                            if (swordNode != null) {
                                rightHandNode.attachChild(swordNode);
                            }
                        }
                    }
                    break;
                default:
            }
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
    }

    private void setupKeys(InputManager inputManager) {
        inputManager.addMapping(PlayerActionMapping.MAP_GOFOWARD,
                PlayerActionTrigger.TRIGGER_GOFOWARD_KEY_UP,
                PlayerActionTrigger.TRIGGER_GOFOWARD_KEY_I);
        inputManager.addListener(this, PlayerActionMapping.MAP_GOFOWARD);

        inputManager.addMapping(PlayerActionMapping.MAP_TURNRIGHT,
                PlayerActionTrigger.TRIGGER_TURNRIGHT_KEY_RIGHT);
                inputManager.addListener(this, PlayerActionMapping.MAP_TURNRIGHT);

        inputManager.addMapping(PlayerActionMapping.MAP_TURNLEFT,
                PlayerActionTrigger.TRIGGER_TURNLEFT_KEY_LEFT);
        inputManager.addListener(this, PlayerActionMapping.MAP_TURNLEFT);

        inputManager.addMapping(PlayerActionMapping.MAP_STOP,
                PlayerActionTrigger.TRIGGER_STOP_KEY_DOWN);
        inputManager.addListener(this, PlayerActionMapping.MAP_STOP);

        inputManager.addMapping(PlayerActionMapping.MAP_TOGGLEWALKSTATE, 
                PlayerActionTrigger.TRIGGER_TOGGLEWALKSTATE_KEY_R);
        inputManager.addListener(this, PlayerActionMapping.MAP_TOGGLEWALKSTATE);

        inputManager.addMapping(PlayerActionMapping.MAP_USESWORD, 
                PlayerActionTrigger.TRIGGER_USESWORD_KEY_U);
        inputManager.addListener(this, PlayerActionMapping.MAP_USESWORD);

    }

}
