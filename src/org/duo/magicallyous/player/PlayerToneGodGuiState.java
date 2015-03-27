/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.player;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import tonegod.gui.controls.extras.Indicator;
import tonegod.gui.core.Element;
import tonegod.gui.core.Screen;

/**
 *
 * @author duo
 */
public class PlayerToneGodGuiState extends AbstractAppState {
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
        indicator = new Indicator(screen, "indicator", new Vector2f(30, 90), Element.Orientation.HORIZONTAL) {
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
        screen.addElement(indicator);
    }

    @Override
    public void update(float tpf) {
        Vector3f camPos = this.app.getCamera().getScreenCoordinates(player.getWorldTranslation());
        BoundingBox boundingBox = (BoundingBox) player.getWorldBound();
        Vector3f playerCenter = boundingBox.getCenter();
        float indicatorWidth = indicator.getWidth();
        float centerX = (playerCenter.x/2) + (indicatorWidth/2);

        int health = getPlayerHealth();
        player.setUserData("health", health);

        indicator.setLocalTranslation(camPos.x - centerX, camPos.y - indicator.getHeight()*2 , 0);
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
