/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous;

import org.duo.magicallyous.utils.ActionState;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector3f;
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
    private Node terrainNode;
    private Node mainChar;
    private Node handNode;
    private Node swordNode;
    private Spatial barrel;
    private ChaseCamera chaseCamera;

    public Spatial getMainChar() {
        return mainChar;
    }

    public Spatial getTerrainNode() {
        return terrainNode;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        // multiplayer, so, it should not pause on lost focus
        this.app.setPauseOnLostFocus(false);
        // physics
        BulletAppState bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        setupKeys(app.getInputManager());
        this.app.getRootNode().attachChild(app.getAssetManager().loadModel("Scenes/Scene01.j3o"));
        for(Spatial spatial : this.app.getRootNode().getChildren()) {
            System.out.println("Root children: " + spatial.getName());
        }
        scene = this.app.getRootNode().getChild("Scene01");
        terrainNode = (Node) ((Node) scene).getChild("terrainNode");
        
        // We set up collision detection for the scene by creating a
        // compound collision shape and a static RigidBodyControl with mass zero.
        CollisionShape terrainShape =
            CollisionShapeFactory.createMeshShape(terrainNode);
        RigidBodyControl landscape = new RigidBodyControl(terrainShape, 0);
        terrainNode.addControl(landscape);
        bulletAppState.getPhysicsSpace().add(landscape);
        bulletAppState.getPhysicsSpace().addAll(terrainNode);
        mainChar = (Node) app.getAssetManager().loadModel("Models/kelum.j3o");
        BetterCharacterControl betterCharacterControl = new BetterCharacterControl(0.5f, 2.5f, 80f);
        mainChar.addControl(betterCharacterControl);
        mainChar.addControl(new MainCharControl());
        mainChar.addControl(new TerrainHeightControl());
        betterCharacterControl.setGravity(new Vector3f(0.0f, -9.8f, 0.0f));
        betterCharacterControl.warp(new Vector3f(0.0f, 10.0f, 0.0f));
        ((Node) scene).attachChild(mainChar);
        bulletAppState.getPhysicsSpace().add(betterCharacterControl);
        bulletAppState.getPhysicsSpace().addAll(mainChar);
        for(Spatial spatial : ((Node) scene).getChildren()) {
            System.out.println("scene children: " + spatial.getName());
        }
        for(Node node : mainChar.descendantMatches(Node.class)) {
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
        barrel = app.getAssetManager().loadModel("Models/barrel.j3o");
        ((Node) scene).attachChild(barrel);
        barrel.setLocalTranslation(0.0f,  -0.0f, -6.0f);
        RigidBodyControl barrelRigidBodyControl = new RigidBodyControl(0.0f);
        barrel.addControl(barrelRigidBodyControl);
        bulletAppState.getPhysicsSpace().add(barrelRigidBodyControl);
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
        switch (name) {
            case "Forward":
                if (isPressed) {
                    mainCharControl.setActionState(ActionState.WALK);
                } else {
                    mainCharControl.setActionState(ActionState.IDLE);
                }
                break;
            case "Stop":
                mainCharControl.setActionState(ActionState.IDLE);
                break;
            case "Rotate Left":
                if(isPressed) {
                    mainCharControl.setTurningLeft(true);
                } else {
                    mainCharControl.setTurningLeft(false);
                }
                break;
            case "Rotate Right":
                if(isPressed) {
                    mainCharControl.setTurningRight(true);
                } else {
                    mainCharControl.setTurningRight(false);
                }
                break;
            case "toggle walk state":
                if(isPressed) {
                    mainCharControl.toggleWalkState();
                }
                break;
            case "pick target":
                if (isPressed) {
                    if (handNode != null && swordNode != null) {
                        if (handNode.hasChild(swordNode)) {
                            handNode.detachChild(swordNode);
                        } else {
                            handNode.attachChild(swordNode);
                        }
                    }
                }
                break;
            default:
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
        inputManager.addMapping("Stop",
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
        inputManager.addListener(this, "Forward", "Stop");
        inputManager.addListener(this, "Jump", "Duck", "Lock View");
        inputManager.addListener(this, "jumpWalk");
        inputManager.addListener(this, "pick target");
        inputManager.addListener(this, "toggle walk state");
    }
}
