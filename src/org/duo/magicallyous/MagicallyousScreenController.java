/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous;

import static org.duo.magicallyous.NiftyController.app;

/**
 *
 * @author duo
 */
public class MagicallyousScreenController extends NiftyController {
    public void quit() {
        System.out.println("Action quit !!!!");
        app.stop();
    }
}
