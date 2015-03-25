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
import org.duo.magicallyous.PlayerControl;
import org.duo.magicallyous.utils.ActionState;

/**
 *
 * @author duo
 */
public class PlayerMovement extends AbstractAppState implements ActionListener, AnalogListener {
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
        PlayerControl playerControl = player.getControl(PlayerControl.class);
        if (playerControl.getActionState() != ActionState.ATTACK) {
            switch (name) {
                case PlayerAction.GOFOWARD :
                    if (isPressed) {
                        playerControl.setActionState(ActionState.WALK);
                    } else {
                        playerControl.setActionState(ActionState.IDLE);
                    }
                    break;
                case PlayerAction.STOP :
                    playerControl.setActionState(ActionState.IDLE);
                    break;
                case PlayerAction.TURNLEFT :
                    if (isPressed) {
                        playerControl.setTurningLeft(true);
                    } else {
                        playerControl.setTurningLeft(false);
                    }
                    break;
                case PlayerAction.TURNRIGHT :
                    if (isPressed) {
                        playerControl.setTurningRight(true);
                    } else {
                        playerControl.setTurningRight(false);
                    }
                    break;
                case PlayerAction.TOGGLEWALKSTATE :
                    if (isPressed) {
                        playerControl.toggleWalkState();
                    }
                    break;
                case PlayerAction.PICKSWORD :
                    if (isPressed) {
                        if (rightHandNode != null && swordNode != null) {
                            if (rightHandNode.hasChild(swordNode)) {
                                rightHandNode.detachChild(swordNode);
                            } else {
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
        inputManager.addMapping(PlayerAction.GOFOWARD,
                new KeyTrigger(KeyInput.KEY_I),
                new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addListener(this, PlayerAction.GOFOWARD);

        inputManager.addMapping(PlayerAction.TURNRIGHT,
                new KeyTrigger(KeyInput.KEY_L),
                new KeyTrigger(KeyInput.KEY_RIGHT));
                inputManager.addListener(this, PlayerAction.TURNRIGHT);

        inputManager.addMapping(PlayerAction.TURNLEFT,
                new KeyTrigger(KeyInput.KEY_J),
                new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addListener(this, PlayerAction.TURNLEFT);

        inputManager.addMapping(PlayerAction.STOP,
                new KeyTrigger(KeyInput.KEY_K),
                new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addListener(this, PlayerAction.STOP);

        inputManager.addMapping(PlayerAction.TOGGLEWALKSTATE, 
                new KeyTrigger(KeyInput.KEY_R));
        inputManager.addListener(this, PlayerAction.TOGGLEWALKSTATE);

        inputManager.addMapping(PlayerAction.PICKSWORD, 
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, PlayerAction.PICKSWORD);

    }

}
