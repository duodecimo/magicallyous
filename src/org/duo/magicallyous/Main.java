package org.duo.magicallyous;

import com.jme3.app.SimpleApplication;

/**
 *
 * @author duo
 */
public class Main extends SimpleApplication {

    
    public Main() {
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        stateManager.attach(new MagicallyousAppState());
    }

}
