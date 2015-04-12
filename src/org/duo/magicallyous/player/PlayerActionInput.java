/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.player;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.scene.Node;
import org.duo.magicallyous.Main;
import org.duo.magicallyous.utils.AnimationStateEnum;

/**
 *
 * @author duo
 */
public class PlayerActionInput extends AbstractAppState implements ActionListener, AnalogListener {
    Main app;
    AppStateManager stateManager;
    Node actualScene;
    Node player;
    Node rightHandNode;
    Node swordNode;
    PlayerActionControl playerActionControl;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (Main) app;
        this.stateManager = stateManager;
        setupKeys(app.getInputManager());
        actualScene = (Node) this.app.getRootNode().getChild(this.app.getActualSceneName());
        player = (Node) actualScene.getChild("player");
        rightHandNode = (Node) player.getChild("hand.R_attachnode");
        swordNode = (Node) rightHandNode.getChild("sword01");
        playerActionControl = player.getControl(PlayerActionControl.class);
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
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
        if (playerActionControl.getAnimationStateEnum() != AnimationStateEnum.BATTLE) {
            switch (name) {
                case PlayerActionMapping.MAP_MOVEFOWARD :
                    playerActionControl.setMoveFoward(isPressed);
                    break;
                case PlayerActionMapping.MAP_MOVEBACKWARD :
                    playerActionControl.setMoveBackward(isPressed);
                    break;
                case PlayerActionMapping.MAP_STOP :
                    playerActionControl.setStopped(isPressed);
                    break;
                case PlayerActionMapping.MAP_TURNRIGHT :
                    playerActionControl.setRotateRight(isPressed);
                    break;
                case PlayerActionMapping.MAP_TURNLEFT :
                    playerActionControl.setRotateLeft(isPressed);
                    break;
                case PlayerActionMapping.MAP_TOGGLEWALKSTATE :
                    if(!isPressed) {
                        playerActionControl.toggleRunning();
                    }
                    break;
                case PlayerActionMapping.MAP_INCREASEHEALTH:
                    if (isPressed) {
                        playerActionControl.setIncreaseHealth(true);
                    }
                    break;
                case PlayerActionMapping.MAP_DECREASEHEALTH:
                    if (isPressed) {
                        playerActionControl.setDecreaseHealth(true);
                    }
                    break;
                case PlayerActionMapping.MAP_INCREASEDAMAGE:
                    if (isPressed) {
                        playerActionControl.setIncreaseDamage(true);
                    }
                    break;
                case PlayerActionMapping.MAP_DECREASEDAMAGE:
                    if (isPressed) {
                        playerActionControl.setDecreaseDamage(true);
                    }
                    break;
                case PlayerActionMapping.MAP_INCREASEDEFENSE:
                    if (isPressed) {
                        playerActionControl.setIncreaseDefense(true);
                    }
                    break;
                case PlayerActionMapping.MAP_DECREASEDEFENSE:
                    if (isPressed) {
                        playerActionControl.setDecreaseDefense(true);
                    }
                    break;
                case PlayerActionMapping.MAP_USESWORD :
                    if (isPressed) {
                        if (rightHandNode.hasChild(swordNode)) {
                            rightHandNode.detachChild(swordNode);
                        } else {
                            if(swordNode == null) {
                                swordNode = player.getUserData("swordNode");
                            }
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
        if (playerActionControl.getAnimationStateEnum() != AnimationStateEnum.BATTLE) {
            switch (name) {
                case PlayerActionMapping.MAP_TURNLEFT :
                    playerActionControl.setRotateValue(value);
                    break;
                case PlayerActionMapping.MAP_TURNRIGHT :
                    playerActionControl.setRotateValue(value);
                    break;
            }
        }
    }

    private void setupKeys(InputManager inputManager) {
        inputManager.addMapping(PlayerActionMapping.MAP_MOVEFOWARD,
                PlayerActionTrigger.TRIGGER_MOVEFOWARD_KEY_UP,
                PlayerActionTrigger.TRIGGER_MOVEFOWARD_KEY_I);
        inputManager.addListener(this, PlayerActionMapping.MAP_MOVEFOWARD);

        inputManager.addMapping(PlayerActionMapping.MAP_TURNRIGHT,
                PlayerActionTrigger.TRIGGER_TURNRIGHT_KEY_RIGHT);
                inputManager.addListener(this, PlayerActionMapping.MAP_TURNRIGHT);

        inputManager.addMapping(PlayerActionMapping.MAP_TURNLEFT,
                PlayerActionTrigger.TRIGGER_TURNLEFT_KEY_LEFT);
        inputManager.addListener(this, PlayerActionMapping.MAP_TURNLEFT);

        inputManager.addMapping(PlayerActionMapping.MAP_MOVEBACKWARD,
                PlayerActionTrigger.TRIGGER_MOVEBACKWARD_KEY_DOWN);
        inputManager.addListener(this, PlayerActionMapping.MAP_MOVEBACKWARD);

        inputManager.addMapping(PlayerActionMapping.MAP_STOP, 
                PlayerActionTrigger.TRIGGER_STOP_KEY_SPACE);
        inputManager.addListener(this, PlayerActionMapping.MAP_STOP);

        inputManager.addMapping(PlayerActionMapping.MAP_TOGGLEWALKSTATE, 
                PlayerActionTrigger.TRIGGER_TOGGLEWALKSTATE_KEY_R);
        inputManager.addListener(this, PlayerActionMapping.MAP_TOGGLEWALKSTATE);

        inputManager.addMapping(PlayerActionMapping.MAP_USESWORD, 
                PlayerActionTrigger.TRIGGER_USESWORD_KEY_U);
        inputManager.addListener(this, PlayerActionMapping.MAP_USESWORD);

        inputManager.addMapping(PlayerActionMapping.MAP_INCREASEHEALTH, 
                PlayerActionTrigger.TRIGGER_INCREASEHEALTH_KEY_1);
        inputManager.addListener(this, PlayerActionMapping.MAP_INCREASEHEALTH);

        inputManager.addMapping(PlayerActionMapping.MAP_DECREASEHEALTH, 
                PlayerActionTrigger.TRIGGER_DECREASEHEALTH_KEY_2);
        inputManager.addListener(this, PlayerActionMapping.MAP_DECREASEHEALTH);

        inputManager.addMapping(PlayerActionMapping.MAP_INCREASEDAMAGE, 
                PlayerActionTrigger.TRIGGER_INCREASEDAMAGE_KEY_3);
        inputManager.addListener(this, PlayerActionMapping.MAP_INCREASEDAMAGE);

        inputManager.addMapping(PlayerActionMapping.MAP_DECREASEDAMAGE, 
                PlayerActionTrigger.TRIGGER_DECREASEDAMAGE_KEY_4);
        inputManager.addListener(this, PlayerActionMapping.MAP_DECREASEDAMAGE);

        inputManager.addMapping(PlayerActionMapping.MAP_INCREASEDEFENSE, 
                PlayerActionTrigger.TRIGGER_INCREASEDEFENSE_KEY_5);
        inputManager.addListener(this, PlayerActionMapping.MAP_INCREASEDEFENSE);

        inputManager.addMapping(PlayerActionMapping.MAP_DECREASEDEFENSE, 
                PlayerActionTrigger.TRIGGER_DECREASEDEFENSE_KEY_6);
        inputManager.addListener(this, PlayerActionMapping.MAP_DECREASEDEFENSE);
    }

    public void cleanupKeys(InputManager inputManager) {
        inputManager.deleteTrigger(PlayerActionMapping.MAP_MOVEFOWARD,
                PlayerActionTrigger.TRIGGER_MOVEFOWARD_KEY_UP);
        inputManager.deleteTrigger(PlayerActionMapping.MAP_MOVEFOWARD,
                PlayerActionTrigger.TRIGGER_MOVEFOWARD_KEY_I);

        inputManager.deleteTrigger(PlayerActionMapping.MAP_TURNRIGHT,
                PlayerActionTrigger.TRIGGER_TURNRIGHT_KEY_RIGHT);

        inputManager.deleteTrigger(PlayerActionMapping.MAP_TURNLEFT,
                PlayerActionTrigger.TRIGGER_TURNLEFT_KEY_LEFT);

        inputManager.deleteTrigger(PlayerActionMapping.MAP_MOVEBACKWARD,
                PlayerActionTrigger.TRIGGER_MOVEBACKWARD_KEY_DOWN);

        inputManager.deleteTrigger(PlayerActionMapping.MAP_STOP, 
                PlayerActionTrigger.TRIGGER_STOP_KEY_SPACE);

        inputManager.deleteTrigger(PlayerActionMapping.MAP_TOGGLEWALKSTATE, 
                PlayerActionTrigger.TRIGGER_TOGGLEWALKSTATE_KEY_R);

        inputManager.deleteTrigger(PlayerActionMapping.MAP_USESWORD, 
                PlayerActionTrigger.TRIGGER_USESWORD_KEY_U);

        inputManager.deleteTrigger(PlayerActionMapping.MAP_INCREASEHEALTH, 
                PlayerActionTrigger.TRIGGER_INCREASEHEALTH_KEY_1);

        inputManager.deleteTrigger(PlayerActionMapping.MAP_DECREASEHEALTH, 
                PlayerActionTrigger.TRIGGER_DECREASEHEALTH_KEY_2);

        inputManager.deleteTrigger(PlayerActionMapping.MAP_INCREASEDAMAGE, 
                PlayerActionTrigger.TRIGGER_INCREASEDAMAGE_KEY_3);

        inputManager.deleteTrigger(PlayerActionMapping.MAP_DECREASEDAMAGE, 
                PlayerActionTrigger.TRIGGER_DECREASEDAMAGE_KEY_4);

        inputManager.deleteTrigger(PlayerActionMapping.MAP_INCREASEDEFENSE, 
                PlayerActionTrigger.TRIGGER_INCREASEDEFENSE_KEY_5);

        inputManager.deleteTrigger(PlayerActionMapping.MAP_DECREASEDEFENSE, 
                PlayerActionTrigger.TRIGGER_DECREASEDEFENSE_KEY_6);
    }
}
