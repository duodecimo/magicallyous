/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.player;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.ChaseCamera;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.List;
import org.duo.magicallyous.net.util.PlayerSendNetInput;
import org.duo.magicallyous.utils.HealthBarControl;
import org.duo.magicallyous.utils.MagicallyousApp;
import org.duo.magicallyous.utils.ToneGodGuiState;
//import tonegod.gui.core.Screen;

/**
 *
 * @author duo
 */
public class PlayerAppState extends AbstractAppState {
    AppStateManager stateManager;
    private MagicallyousApp app;
    Node actualScene;
    private ChaseCamera chaseCamera;
    private List<Node> players;
    private double timeCounter = 0.0d;
    private double timeEvent = 0.0d;
    Vector3f normalGravity;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.stateManager = stateManager;
        this.app = (MagicallyousApp) app;
        actualScene = (Node) this.app.getRootNode().getChild(this.app.getActualSceneName());
        actualScene.setName(this.app.getActualSceneName());
        this.app.getFlyByCamera().setEnabled(false);
        // if app is server isntance
        // no local player is needed
        if(!this.app.isServerInstance()) {
        // local player
        addLocalPlayer("Astofoboldo");
        } else {
            // add extra remove player
            addRemotePlayer("Astofoboldo");
        }
        // one remote player for test
        addRemotePlayer("Miforonaldo");
    }

    private Node addLocalPlayer(String playerName) {
        // create player (model, action control, properties, etc)
        // and attach it to *** actual scene ***
        if(players == null) {
            players = new ArrayList<>();
        }
        Node player = createPlayer(playerName, new Integer(players.size()), true);
        player.addControl(new HealthBarControl(this.app, player));
        // start player basic key controls
        /*
        if (actualScene.getName().equals(this.app.getMagicallyousSceneName())) {
            PlayerActionInput playerActionInput =
                    stateManager.getState(PlayerActionInput.class);
            if (playerActionInput != null) {
                //playerActionInput.cleanupKeys(this.app.getInputManager());
                stateManager.detach(playerActionInput);
                playerActionInput.cleanup();
            }
            stateManager.attach(new PlayerActionInput());
        }
        */
        stateManager.attach(new PlayerSendNetInput());
        //stateManager.attach(new PlayerActionInput());
 
        // start camera
        this.app.getFlyByCamera().setEnabled(false);
        chaseCamera = new ChaseCamera(this.app.getCamera(), player, this.app.getInputManager());
        chaseCamera.setUpVector(Vector3f.UNIT_Y);
        chaseCamera.setSmoothMotion(true);
        ToneGodGuiState toneGodGuiState = stateManager.getState(ToneGodGuiState.class);
        if (toneGodGuiState != null) {
            stateManager.detach(toneGodGuiState);
            //this.app.getGuiNode().removeControl(Screen.class);
            this.app.getGuiNode().detachAllChildren();
        }
        stateManager.attach(new ToneGodGuiState());
        return player;
    }

    private Node addRemotePlayer(String playerName) {
        if(players == null) {
            players = new ArrayList<>();
        }
        Node player = createPlayer(playerName, new Integer(players.size()), true);
        players.add(player);
        return player;
    }

    private Node createPlayer(String playerName, Integer playerId, boolean isRemote) {
        Node player = (Node) app.getAssetManager().loadModel("Models/kelum.j3o");
        player.setName("player");
        player.setUserData("fireMagic", getShoot("fireMagic"));
        player.setUserData("waterMagic", getShoot("waterMagic"));
        player.setUserData("earthMagic", getShoot("earthMagic"));
        player.setUserData("name", playerName);
        player.setUserData("damage", 8);
        player.setUserData("defense", 5);
        player.setUserData("health", 100);
        player.setUserData("level", 1);
        player.setUserData("points", 0);
        PlayerActionControl playerActionControl;
        playerActionControl = new PlayerActionControl(this.app, playerId, 0.5f, 2.5f, 80.0f);
        ((PlayerActionControl) playerActionControl).setMoveSpeed(1.0f);
        ((PlayerActionControl) playerActionControl).setRunSpeed(9.0f);
        ((PlayerActionControl) playerActionControl).setAbleToRun(true);
        normalGravity = new Vector3f(0.0f, 9.81f, 0.0f);
        playerActionControl.setGravity(normalGravity);
        player.move(0.0f, 0.0f, 0.0f);
        BulletAppState bulletAppState = this.app.getStateManager().getState(BulletAppState.class);
        bulletAppState.getPhysicsSpace().add(playerActionControl);
        player.addControl(playerActionControl);
        actualScene.attachChild(player);
        // make sword go away
        Node rightHandNode = (Node) player.getChild("hand.R_attachnode");
        Node swordNode = (Node) rightHandNode.getChild("sword01");
        if (rightHandNode.hasChild(swordNode)) {
            player.setUserData("swordNode", swordNode);
            rightHandNode.detachChild(swordNode);
        }
        return player;
   }

    Spatial getShoot(String shootType) {
        Sphere sphere = new Sphere(16, 16, 0.2f);
        Geometry shoot = new Geometry("shoot", sphere);
        Material material = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        if(shootType.compareTo("fireMagic") == 0) {
            material.setColor("Color", ColorRGBA.Yellow);
        } else if(shootType.compareTo("waterMagic") == 0) {
            material.setColor("Color", ColorRGBA.Blue);
        } else {
            material.setColor("Color", ColorRGBA.Green);
        }
        shoot.setMaterial(material);
        shoot.setName("shoot");
        return shoot;
    }

    @Override
    public void update(float tpf) {
        timeCounter += tpf;
        if (players.get(0) != null) {
            int health = players.get(0).getUserData("health");
            if (timeCounter - timeEvent > 5.0d && health < 100) {
                health += 2;
                players.get(0).setUserData("health", health);
                timeEvent = timeCounter;
            }
        }
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        if(actualScene != null) {
            actualScene.detachAllChildren();
        }
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }

    public List<Node> getPlayers() {
        return players;
    }

    public void setPlayers(List<Node> players) {
        this.players = players;
    }
}
