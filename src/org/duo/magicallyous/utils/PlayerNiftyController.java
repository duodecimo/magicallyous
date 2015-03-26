/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.utils;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author duo
 */
public class PlayerNiftyController extends AbstractAppState implements ScreenController {
    SimpleApplication app;
    Node player;
    Node scene;
    Nifty nifty;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        scene = (Node) this.app.getRootNode().getChild("Scene01");
        player = (Node) app.getAssetManager().loadModel("Models/kelum.j3o");
    }
    
    @Override
    public void update(float tpf) {
        if (nifty != null) {
            Element niftyElement = nifty.getCurrentScreen().findElementByName("playerhealth");
            // swap old with new text
            niftyElement.getRenderer(TextRenderer.class).setText("health: " + getPlayerHealth());
            niftyElement = nifty.getCurrentScreen().findElementByName("playername");
            // swap old with new text
            niftyElement.getRenderer(TextRenderer.class).setText("health: " + getPlayerHealth());
        }
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
    }

    @Override
    public void onStartScreen() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onEndScreen() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void charFoward() {
        System.out.println("Nifty called char foward !!!");
    }

    public String getPlayerName() {
        return player.getUserData("name");
    }

    public int getPlayerHealth() {
        return player.getUserData("health");
    }
}
