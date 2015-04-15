/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.net.util;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import org.duo.magicallyous.net.MainServer;

/**
 *
 * @author duo
 */
public class ServerSceneAppState extends AbstractAppState {
    MainServer app;
    List<MagicallyousScene> scenes;
    MagicallyousScene magicallyousScene;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (MainServer) app;
        scenes = new ArrayList<>();
        // add underworld
        magicallyousScene = new MagicallyousScene(app.getAssetManager().loadModel("Scenes/Underworld.j3o"));
        scenes.add(magicallyousScene);
        // add Scene01
        magicallyousScene = new MagicallyousScene(app.getAssetManager().loadModel("Scenes/Scene01.j3o"));
        scenes.add(magicallyousScene);
    }

    // returns a scene spatial to a client
    public Spatial getScene(int index) {
        Spatial scene = scenes.get(index).getScene();
        return scene;
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
