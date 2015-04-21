/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.net.util;

import org.duo.magicallyous.net.message.WelcomeMessage;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 *
 * @author aluno
 */
public class ClientMessageListener implements MessageListener<Client> {

    @Override
    public void messageReceived(Client source, Message message) {
        if (message instanceof WelcomeMessage) {
            // do something with the message
            WelcomeMessage gameMessage = (WelcomeMessage) message;
            System.out.println("Client #" + source.getId()
                    + " received: '" + gameMessage.getGameMessage() + "'");
        }
    }
}
