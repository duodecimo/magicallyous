/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.utils;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Node;
import tonegod.gui.controls.extras.Indicator;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.core.Element;
import tonegod.gui.core.Screen;

/**
 *
 * @author duo
 */
public class ToneGodGuiState extends AbstractAppState {

    SimpleApplication app;
    Node player;
    Node scene;
    private Screen screen;
    Indicator indicator;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        System.out.println("Starting toneGod!");
        scene = (Node) this.app.getRootNode().getChild("Scene01");
        player = (Node) scene.getChild("player");
        screen = new Screen(this.app);
        this.app.getGuiNode().addControl(screen);
        Panel panel = new Panel(screen, "panel",
                new Vector2f(0, screen.getHeight() * 0.8f),
                new Vector2f(screen.getWidth(), screen.getHeight() * 0.2f),
                new Vector4f(14, 14, 14, 14),
                "tonegod/gui/style/def/Window/panel_x.png");
        screen.addElement(panel);
        indicator = new Indicator(screen, "indicator", new Vector2f(10, 10),
                Element.Orientation.HORIZONTAL) {
            @Override
            public void onChange(float currentValue, float currentPercentage) {
                if (currentPercentage <= 0) {
                    this.setIndicatorColor(ColorRGBA.Red);
                } else {
                    float value = 0.01f * currentPercentage;
                    ColorRGBA newColor = new ColorRGBA();
                    newColor.interpolate(ColorRGBA.Red, ColorRGBA.Green, value);
                    this.setIndicatorColor(newColor);
                }
            }
        };
        indicator.setMaxValue(100);
        indicator.setCurrentValue(100);
        indicator.setDisplayPercentage();
        indicator.setIndicatorColor(ColorRGBA.Red);
        panel.addChild(indicator);
    }

    @Override
    public void update(float tpf) {
        int health = getPlayerHealth();
        player.setUserData("health", health);
        indicator.setCurrentValue(getPlayerHealth());
    }

    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }

    public String getPlayerName() {
        return player.getUserData("name");
    }

    public int getPlayerHealth() {
        return player.getUserData("health");
    }
}
