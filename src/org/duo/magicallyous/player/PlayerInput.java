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
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.scene.Node;
import org.duo.magicallyous.utils.AnimationStateEnum;
import org.duo.magicallyous.utils.CharacterMovementControl;

/**
 *
 * @author duo
 */
public class PlayerInput extends AbstractAppState implements ActionListener, AnalogListener {
    SimpleApplication app;
    AppStateManager stateManager;
    Node scene;
    Node player;
    Node rightHandNode;
    Node swordNode;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.stateManager = stateManager;
        setupKeys(app.getInputManager());
        scene = (Node) this.app.getRootNode().getChild("Scene01");
        player = (Node) scene.getChild("player");
        rightHandNode = (Node) player.getChild("hand.R_attachnode");
        swordNode = (Node) rightHandNode.getChild("sword01");
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
        CharacterMovementControl characterMovementControl = 
                player.getControl(CharacterMovementControl.class);
        if (characterMovementControl.getAnimationStateEnum() != AnimationStateEnum.BATTLE) {
            switch (name) {
                case PlayerMovementMapping.MAP_MOVEFOWARD :
                    if (isPressed) {
                        characterMovementControl.setAnimationStateEnum(AnimationStateEnum.WALK);
                    } else {
                        characterMovementControl.setAnimationStateEnum(AnimationStateEnum.IDLE);
                    }
                    break;
                case PlayerMovementMapping.MAP_STOP :
                    characterMovementControl.setAnimationStateEnum(AnimationStateEnum.IDLE);
                    break;
                case PlayerMovementMapping.MAP_TOGGLEWALKSTATE :
                    if (isPressed) {
                        //playerMotionControl.toggleWalkState();
                    }
                    break;
                case PlayerMovementMapping.MAP_USESWORD :
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
                case PlayerMovementMapping.MAP_INCREASEHEALTH:
                    if (isPressed) {
                        int health = player.getUserData("health");
                        health += 5;
                        player.setUserData("health", health);
                        System.out.println("Player health = " + health);
                    }
                    break;
                case PlayerMovementMapping.MAP_DECREASEHEALTH:
                    if (isPressed) {
                        int health = player.getUserData("health");
                        health -= 5;
                        player.setUserData("health", health);
                        System.out.println("Player health = " + health);
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
        inputManager.addMapping(PlayerMovementMapping.MAP_MOVEFOWARD,
                PlayerActionTrigger.TRIGGER_MOVEFOWARD_KEY_UP,
                PlayerActionTrigger.TRIGGER_MOVEFOWARD_KEY_I);
        inputManager.addListener(this, PlayerMovementMapping.MAP_MOVEFOWARD);

        inputManager.addMapping(PlayerMovementMapping.MAP_TURNRIGHT,
                PlayerActionTrigger.TRIGGER_TURNRIGHT_KEY_RIGHT);
                inputManager.addListener(this, PlayerMovementMapping.MAP_TURNRIGHT);

        inputManager.addMapping(PlayerMovementMapping.MAP_TURNLEFT,
                PlayerActionTrigger.TRIGGER_TURNLEFT_KEY_LEFT);
        inputManager.addListener(this, PlayerMovementMapping.MAP_TURNLEFT);

//        inputManager.addMapping(PlayerMovementMapping.MAP_STOP,
//                PlayerActionTrigger.TRIGGER_STOP_KEY_DOWN);
//        inputManager.addListener(this, PlayerMovementMapping.MAP_STOP);

        inputManager.addMapping(PlayerMovementMapping.MAP_TOGGLEWALKSTATE, 
                PlayerActionTrigger.TRIGGER_TOGGLEWALKSTATE_KEY_R);
        inputManager.addListener(this, PlayerMovementMapping.MAP_TOGGLEWALKSTATE);

        inputManager.addMapping(PlayerMovementMapping.MAP_USESWORD, 
                PlayerActionTrigger.TRIGGER_USESWORD_KEY_U);
        inputManager.addListener(this, PlayerMovementMapping.MAP_USESWORD);

        inputManager.addMapping(PlayerMovementMapping.MAP_INCREASEHEALTH, 
                PlayerActionTrigger.TRIGGER_INCREASEHEALTH_KEY_ADD);
        inputManager.addListener(this, PlayerMovementMapping.MAP_INCREASEHEALTH);

        inputManager.addMapping(PlayerMovementMapping.MAP_DECREASEHEALTH, 
                PlayerActionTrigger.TRIGGER_DECREASEHEALTH_KEY_MINUS);
        inputManager.addListener(this, PlayerMovementMapping.MAP_DECREASEHEALTH);
    }
}
