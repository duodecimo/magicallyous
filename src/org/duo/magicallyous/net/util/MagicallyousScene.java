/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.net.util;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author duo
 */
public class MagicallyousScene extends Node {
    // scene spatial
    Spatial scene;
    // players on the scene
    // clients should receive messages updating scene state
    List<MagicallyousPlayer> players;

    public MagicallyousScene(Spatial scene) {
        this.scene = scene;
        players = new ArrayList<>();
    }

    public void addPlayer(MagicallyousPlayer magicallyousPlayer) {
        players.add(magicallyousPlayer);
    }

    public void removePlayer(MagicallyousPlayer magicallyousPlayer) {
        players.remove(magicallyousPlayer);
    }

    public Spatial getScene() {
        return scene;
    }
}
