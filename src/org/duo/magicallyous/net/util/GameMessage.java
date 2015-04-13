/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.net.util;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author aluno
 */
@Serializable
public class GameMessage extends AbstractMessage {
    private String gameMessage;

    public GameMessage() {
    }

    public GameMessage(String gameMessage) {
        this.gameMessage = gameMessage;
    }

    public String getGameMessage() {
        return gameMessage;
    }

}
