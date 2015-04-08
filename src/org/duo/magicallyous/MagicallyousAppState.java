/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import org.duo.magicallyous.npc.NpcAppState;
import org.duo.magicallyous.player.PlayerAppState;

/**
 *
 * @author duo
 */
public class MagicallyousAppState extends AbstractAppState {
    private SimpleApplication app;
    private Node scene;
    private Node terrainNode;

    public Spatial getTerrainNode() {
        return terrainNode;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        // multiplayer, so, it should not pause on lost focus
        this.app.setPauseOnLostFocus(false);
        // use physics
        BulletAppState bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        this.app.getRootNode().attachChild(app.getAssetManager().loadModel("Scenes/Scene01.j3o"));
        for(Spatial spatial : this.app.getRootNode().getChildren()) {
            System.out.println("Root children: " + spatial.getName());
        }
        // disable flycam
        this.app.getFlyByCamera().setEnabled(false);
        scene = (Node) this.app.getRootNode().getChild("Scene01");
        terrainNode = (Node) ((Node) scene).getChild("terrainNode");
        terrainNode.addControl(new RigidBodyControl(0.0f));
        bulletAppState.getPhysicsSpace().add(terrainNode);
        // PlayerAppState to initialize player
        stateManager.attach(new PlayerAppState());
        // NpcAppState to initialize npc
        stateManager.attach(new NpcAppState());
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

}
