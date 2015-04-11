package org.duo.magicallyous;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;

/**
 *
 * @author duo
 */
public class Main extends SimpleApplication {
    private MagicallyousAppState magicallyousAppState;
    private UnderworldAppState underworldAppState;
    boolean isUnderworld = true;

    
    public Main() {
    }

    public void switchAppState() {
        if(isUnderworld) {
            stateManager.detach(underworldAppState);
            stateManager.attach(magicallyousAppState);
        } else {
            stateManager.detach(magicallyousAppState);
            stateManager.attach(underworldAppState);
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
        underworldAppState = new UnderworldAppState();
        magicallyousAppState = new MagicallyousAppState();
        stateManager.attach(underworldAppState);
    }
  }

