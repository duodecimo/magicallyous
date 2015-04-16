/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.net.util;

import org.duo.magicallyous.net.message.GameMessage;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 *
 * @author aluno
 */
public class ClientListener implements MessageListener<Client> {

    @Override
    public void messageReceived(Client source, Message message) {
        if (message instanceof GameMessage) {
            // do something with the message
            GameMessage gameMessage = (GameMessage) message;
            System.out.println("Client #" + source.getId()
                    + " received: '" + gameMessage.getGameMessage() + "'");
        }
    }
}
