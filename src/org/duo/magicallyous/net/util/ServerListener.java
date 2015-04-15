/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.net.util;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 *
 * @author aluno
 */
public class ServerListener implements MessageListener<HostedConnection> {

    @Override
    public void messageReceived(HostedConnection source, Message message) {
        if (message instanceof GameMessage) {
            // do something with the message
            GameMessage gameMessage = (GameMessage) message;
            System.out.println("Server received '"
                    + gameMessage.getGameMessage()
                    + "' from client #" + source.getId());
        } else if (message instanceof PlayerActionStateMessage) {
            PlayerActionStateMessage playerActionStateMessage =
                    (PlayerActionStateMessage) message;
        }
    }
}