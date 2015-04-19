/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author duo
 */
public abstract  class NiftyController implements ScreenController {
    Nifty nifty;
    Screen screen;
    boolean magicallyousMenuVisible;
    static Main app;

    public void toggleOptionsMenu() {
        if (magicallyousMenuVisible) {
            nifty.getCurrentScreen().findElementByName("options").show();
        } else {
            nifty.getCurrentScreen().findElementByName("options").hide();
        }
        magicallyousMenuVisible = !magicallyousMenuVisible;
    }

    static void registerApp (Main regapp) {
        app = regapp;
    }

    public void quit() {
        System.out.println("Action quit !!!!");
        app.stop();
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }
}
