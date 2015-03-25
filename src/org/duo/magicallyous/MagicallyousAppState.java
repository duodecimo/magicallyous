/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import org.duo.magicallyous.npc.NpcAppState;
import org.duo.magicallyous.player.PlayerState;

/**
 *
 * @author duo
 */
public class MagicallyousAppState extends AbstractAppState {
    private SimpleApplication app;
    private Node scene;
    private Node terrainNode;
    private Spatial barrel;

    public Spatial getTerrainNode() {
        return terrainNode;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        // multiplayer, so, it should not pause on lost focus
        this.app.setPauseOnLostFocus(false);
        this.app.getRootNode().attachChild(app.getAssetManager().loadModel("Scenes/Scene01.j3o"));
        for(Spatial spatial : this.app.getRootNode().getChildren()) {
            System.out.println("Root children: " + spatial.getName());
        }
        scene = (Node) this.app.getRootNode().getChild("Scene01");
        terrainNode = (Node) ((Node) scene).getChild("terrainNode");
        // PlayerState to initialize player
        stateManager.attach(new PlayerState());
        // NpcAppState to initialize npc
        stateManager.attach(new NpcAppState());
        // add a barrel
        barrel = app.getAssetManager().loadModel("Models/barrel.j3o");
        ((Node) scene).attachChild(barrel);
        barrel.setLocalTranslation(0.0f,  0.0f, -6.0f);
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
