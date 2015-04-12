package org.duo.magicallyous;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

/**
 *
 * @author duo
 */
public class Main extends SimpleApplication {
    private MagicallyousAppState magicallyousAppState;
    private UnderworldAppState underworldAppState;
    private String actualSceneName = "";
    private final String magicallyousSceneName = "Scene01";
    private final String underworldSceneName = "underworldScene";

    
    public Main() {
        actualSceneName = magicallyousSceneName;
    }

    public void switchAppState() {
        Node node;
        if(actualSceneName.equals(underworldSceneName)) {
            actualSceneName = magicallyousSceneName;
            stateManager.detach(underworldAppState);
            underworldAppState.cleanup();
            magicallyousAppState = new MagicallyousAppState();
            stateManager.attach(magicallyousAppState);
            node = (Node) getRootNode().getChild(underworldSceneName);
            if (node != null) {
                getRootNode().detachChild(node);
            }
        } else {
            actualSceneName = underworldSceneName;
            stateManager.detach(magicallyousAppState);
            magicallyousAppState.cleanup();
            underworldAppState = new UnderworldAppState();
            stateManager.attach(underworldAppState);
            node = (Node) getRootNode().getChild(magicallyousSceneName);
            if (node != null) {
                getRootNode().detachChild(node);
            }
        }
    }

    public static void main(String[] args) {
        Main app = new Main();
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Magicallyous");
        //settings.setSettingsDialogImage("Interface/splashscreen.png");
        settings.setResolution(1280, 800);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        setDisplayStatView(false);
        //underworldAppState = new UnderworldAppState();
        //stateManager.attach(underworldAppState);
        magicallyousAppState = new MagicallyousAppState();
        stateManager.attach(magicallyousAppState);
    }

    public String getActualSceneName() {
        return actualSceneName;
    }

    public String getMagicallyousSceneName() {
        return magicallyousSceneName;
    }

    public String getUnderworldSceneName() {
        return underworldSceneName;
    }
  }

