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
    boolean isUnderworld = false;

    
    public Main() {
    }

    public void switchAppState() {
        Node node;
        if(isUnderworld) {
            stateManager.detach(underworldAppState);
            underworldAppState.cleanup();
            magicallyousAppState = new MagicallyousAppState();
            stateManager.attach(magicallyousAppState);
            node = (Node) getRootNode().getChild("underworldScene");
            if (node != null) {
                getRootNode().detachChild(node);
                System.out.println("Scene01 detached!");
            }
        } else {
            stateManager.detach(magicallyousAppState);
            underworldAppState = new UnderworldAppState();
            stateManager.attach(underworldAppState);
            node = (Node) getRootNode().getChild("Scene01");
            if (node != null) {
                getRootNode().detachChild(node);
                System.out.println("Underworld detached!");
            }
        }
      isUnderworld = !isUnderworld;  
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
  }

