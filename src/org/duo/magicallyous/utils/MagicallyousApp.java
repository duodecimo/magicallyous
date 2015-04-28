/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.utils;

import com.jme3.app.SimpleApplication;

/**
 *
 * @author duo
 */
public abstract class MagicallyousApp extends SimpleApplication {
    // this is to allow client and server share a basic instance
    public abstract boolean isServerInstance();
    public abstract String getActualSceneName();
    public abstract String getMagicallyousSceneName();
}
