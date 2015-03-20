/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import org.duo.magicallyous.npc.NpcAppState;

/**
 *
 * @author duo
 */
public class MagicallyousAppState extends AbstractAppState implements ActionListener, AnalogListener {
    private SimpleApplication app;
    private Spatial scene;
    private Spatial mainChar;
    private Spatial terrain;
    private ChaseCamera chaseCamera;
    private Node swordNode;
    private Node handNode;

    public Spatial getMainChar() {
        return mainChar;
    }

    public Spatial getTerrain() {
        return terrain;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        // multiplayer, so, it should not pause on lost focus
        this.app.setPauseOnLostFocus(false);
        setupKeys(app.getInputManager());
        this.app.getRootNode().attachChild(app.getAssetManager().loadModel("Scenes/Scene01.j3o"));
        for(Spatial spatial : this.app.getRootNode().getChildren()) {
            System.out.println("Root children: " + spatial.getName());
        }
        scene = this.app.getRootNode().getChild("Scene01");
        ((Node) scene).attachChild(app.getAssetManager().loadModel("Models/kelum.j3o"));
        for(Spatial spatial : ((Node) scene).getChildren()) {
            System.out.println("scene children: " + spatial.getName());
        }
        terrain = ((Node) scene).getChild("terrain01");
        mainChar = ((Node) scene).getChild("kelum");
        mainChar.addControl(new MainCharControl());
        mainChar.addControl(new TerrainHeightControl());
        for(Node node : ((Node)mainChar).descendantMatches(Node.class)) {
            System.out.println("Nodes from mainChar: " + node.getName());
            switch (node.getName()) {
                case "sword01":
                    swordNode = node;
                    break;
                case "hand.R_attachnode":
                    handNode = node;
                    break;
            }
        }
        // NpcAppState to initialize npc details, like movement
        stateManager.attach(new NpcAppState());
        this.app.getFlyByCamera().setEnabled(false);
        chaseCamera = new ChaseCamera(this.app.getCamera(), mainChar, this.app.getInputManager());
        chaseCamera.setSmoothMotion(true);
        // add a barrel
        //barrel = app.getAssetManager().loadModel("Models/barrel.j3o");
        //((Node) scene).attachChild(barrel);
        //barrel.setLocalTranslation(0.0f,  -0.029427528f, -6.177826f);
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
        MainCharControl mainCharControl = mainChar.getControl(MainCharControl.class);
        if (isPressed) {
            switch (name) {
                case "Forward" :
                    mainCharControl.setActionState(ActionState.WALKFORWARD);
                    break;
                case "Rotate Left" :
                    mainCharControl.setActionState(ActionState.TURNLEFT);
                    break;
                case "Rotate Right" :
                    mainCharControl.setActionState(ActionState.TURNRIGHT);
                    break;
                case "toggle walk state" :
                    mainCharControl.toggleWalkState();
                    break;
                case "pick target" :
                    if(handNode != null && swordNode != null) {
                        if(handNode.hasChild(swordNode)) {
                            handNode.detachChild(swordNode);
                        } else {
                            handNode.attachChild(swordNode);
                        }
                    }
                    break;
                default:
                    mainCharControl.setActionState(ActionState.IDLE);
            }
        } else {
            mainCharControl.setActionState(ActionState.IDLE);
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
    }
    
    private void setupKeys(InputManager inputManager) {
        inputManager.addMapping("Rotate Left",
                new KeyTrigger(KeyInput.KEY_J),
                new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Rotate Right",
                new KeyTrigger(KeyInput.KEY_L),
                new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("Forward",
                new KeyTrigger(KeyInput.KEY_I),
                new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Backward",
                new KeyTrigger(KeyInput.KEY_K),
                new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("Jump",
                new KeyTrigger(KeyInput.KEY_F),
                new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Duck",
                new KeyTrigger(KeyInput.KEY_G),
                new KeyTrigger(KeyInput.KEY_LSHIFT),
                new KeyTrigger(KeyInput.KEY_RSHIFT));
        inputManager.addMapping("jumpWalk", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Lock View",
                new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addMapping("toggle walk state", new KeyTrigger(KeyInput.KEY_R));
        inputManager.addMapping("pick target", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, "Rotate Left", "Rotate Right");
        inputManager.addListener(this, "Forward", "Backward");
        inputManager.addListener(this, "Jump", "Duck", "Lock View");
        inputManager.addListener(this, "jumpWalk");
        inputManager.addListener(this, "pick target");
        inputManager.addListener(this, "toggle walk state");
    }
}
