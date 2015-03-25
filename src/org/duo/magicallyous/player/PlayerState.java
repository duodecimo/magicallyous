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
import org.duo.magicallyous.utils.TerrainHeightControl;

/**
 *
 * @author duo
 */
public class PlayerState extends AbstractAppState {
    private SimpleApplication app;
    private ChaseCamera chaseCamera;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        Node scene = (Node) this.app.getRootNode().getChild("Scene01");
        Node player = (Node) app.getAssetManager().loadModel("Models/kelum.j3o");
        player.setName("player");
        player.setUserData("fireMagic", getShoot("fireMagic"));
        player.setUserData("waterMagic", getShoot("waterMagic"));
        player.setUserData("earthMagic", getShoot("earthMagic"));
        player.setUserData("health", 100);
        player.addControl(new PlayerControl());
        player.addControl(new TerrainHeightControl());
        player.move(0.0f, 0.0f, 0.0f);
        scene.attachChild(player);
        // start player basic key controls
        stateManager.attach(new PlayerMovement());
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
        //TODO: implement behavior during runtime
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
}
