/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.player;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Node;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.extras.Indicator;
import tonegod.gui.controls.lists.Slider;
import tonegod.gui.controls.lists.Spinner;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Element;
import tonegod.gui.core.Screen;

/**
 *
 * @author duo
 */
public class TestToneGodGuiState extends AbstractAppState {
    SimpleApplication app;
    Node player;
    Node scene;
    public int windowsCount;
    private Screen screen;
    private Window window;
    private Panel panel;
    private Slider slider;
    private Spinner spinner;
    private ButtonAdapter buttonAdapter;
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
        window = new Window(screen, "window", new Vector2f(15, 15));
        panel =  new Panel(screen, "panel", new Vector2f(15, 15));
        slider = new Slider(screen, "slider", new Vector2f(60, 60), Element.Orientation.HORIZONTAL, true) {
            @Override
            public void onChange(int i, Object o) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        slider.setStepIntegerRange(0, 100, 1);
        spinner = new Spinner(screen, "spinner",  new Vector2f(60, 60), Element.Orientation.HORIZONTAL, true) {
            @Override
            public void onChange(int i, String string) {
                
            }
        };
        spinner.setStepFloatRange(0.0f, 100.0f, 1.0f);
        //panel.attachChild(slider);
        buttonAdapter = new ButtonAdapter(screen, "button", new Vector2f(30, 30)){

            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                super.onButtonMouseLeftUp(evt, toggled); //To change body of generated methods, choose Tools | Templates.
            }
            
        };
        indicator = new Indicator(screen, "indicator", new Vector2f(30, 30), Element.Orientation.HORIZONTAL) {
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
        indicator.setBaseImage(screen.getStyle("Window").getString("defaultImg"));
        indicator.setAlphaMap(screen.getStyle("Indicator").getString("alphaImg"));
        indicator.setMaxValue(100);
        indicator.setCurrentValue(100);
        indicator.setDisplayPercentage();
        indicator.setIndicatorColor(ColorRGBA.Red);
        //indicator.setGlobalAlpha(0.5f);
        indicator.setIndicatorPadding(new Vector4f(7,7,7,7));
        
        //panel.attachChild(indicator);
        //panel.addChild(spinner);
        //panel.addChild(buttonAdapter);
        window.attachChild(indicator);
        screen.addElement(window);
    }

    public final void createNewWindow(String someWindowTitle) {
        Window nWin = new Window(screen, "Window", new Vector2f((screen.getWidth() / 2) - 175, 
                (screen.getHeight() / 2) - 100));
        nWin.setWindowTitle(someWindowTitle);
        screen.addElement(nWin);
        windowsCount++;
    }

    @Override
    public void update(float tpf) {
        //Vector3f camPos = this.app.getCamera().getScreenCoordinates(player.getWorldTranslation());
        // BoundingBox box = (BoundingBox)spatial.getWorldBound();
        //BoundingBox boundingBox = (BoundingBox) player.getWorldBound();
        //Vector3f playerCenter = boundingBox.getCenter();
        //float indicatorWidth = indicator.getWidth();
        //float centerX = (playerCenter.x/2) + (indicatorWidth/2);

        int health = getPlayerHealth();
        player.setUserData("health", health);

        //indicator.setLocalTranslation(camPos.x - centerX, camPos.y - indicator.getHeight()*2 , 0);
        indicator.setCurrentValue(getPlayerHealth());
        //ColorRGBA colorRGBA = new ColorRGBA();
        //colorRGBA.interpolate(ColorRGBA.Red, ColorRGBA.Yellow, (indicator.getCurrentValue()*0.01f));
        //indicator.setIndicatorColor(colorRGBA);
        //System.out.println("Indicator Position: " + camPos);
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
