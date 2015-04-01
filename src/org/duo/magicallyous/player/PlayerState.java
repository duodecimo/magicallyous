/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.player;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.ChaseCamera;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import org.duo.magicallyous.utils.HealthBarControl;
import org.duo.magicallyous.utils.TerrainHeightControl;
import org.duo.magicallyous.utils.ToneGodGuiState;

/**
 *
 * @author duo
 */
public class PlayerState extends AbstractAppState {
    private SimpleApplication app;
    private ChaseCamera chaseCamera;
    private Node player;
    private Spatial barrel;
    private double timeCounter = 0.0d;
    private double timeEvent = 0.0d;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        Node scene = (Node) this.app.getRootNode().getChild("Scene01");
        player = (Node) app.getAssetManager().loadModel("Models/kelum.j3o");
        player.setName("player");
        player.setUserData("fireMagic", getShoot("fireMagic"));
        player.setUserData("waterMagic", getShoot("waterMagic"));
        player.setUserData("earthMagic", getShoot("earthMagic"));
        player.setUserData("name", "Astofoboldo");
        player.setUserData("damage", 8);
        player.setUserData("defense", 5);
        player.addControl(new PlayerMotionControl());
        player.addControl(new TerrainHeightControl());
        player.move(0.0f, 0.0f, 0.0f);
        player.addControl(new HealthBarControl(this.app, player));
        scene.attachChild(player);
        // let sowrd go
        Node rightHandNode = (Node) player.getChild("hand.R_attachnode");
        Node swordNode = (Node) rightHandNode.getChild("sword01");
        if (rightHandNode.hasChild(swordNode)) {
            rightHandNode.detachChild(swordNode);
        }

        // add a barrel
        barrel = app.getAssetManager().loadModel("Models/barrel.j3o");
        scene.attachChild(barrel);
        barrel.setLocalTranslation(0.0f,  0.0f, -6.0f);
        player.setUserData("barrel", barrel);
        // start player basic key controls
        stateManager.attach(new PlayerInput());
        stateManager.attach(new ToneGodGuiState());
        // start camera
        this.app.getFlyByCamera().setEnabled(false);
        chaseCamera = new ChaseCamera(this.app.getCamera(), player, this.app.getInputManager());
        chaseCamera.setSmoothMotion(true);
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
        int health = player.getUserData("health");
        if(timeCounter - timeEvent > 5.0d && health < 100) {
            health +=2;
            player.setUserData("health", health);
            timeEvent = timeCounter;
        }
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
}
