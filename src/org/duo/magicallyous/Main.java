package org.duo.magicallyous;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;

/**
 *
 * @author duo
 */
public class Main extends SimpleApplication {

    
    public Main() {
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
        stateManager.attach(new MagicallyousAppState());
  }

}
