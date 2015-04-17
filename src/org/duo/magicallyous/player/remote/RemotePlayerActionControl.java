/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.player.remote;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import org.duo.magicallyous.Main;
import org.duo.magicallyous.player.PlayerActionControl;

/**
 *
 * @author duo
 */
public class RemotePlayerActionControl extends PlayerActionControl {

    public RemotePlayerActionControl() {
    }

    public RemotePlayerActionControl(Main app, float radius, float height, float mass) {
        super(app, radius, height, mass);
    }

    @Override
    public void updatePlayerSate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }
}
