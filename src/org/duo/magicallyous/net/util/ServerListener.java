/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.net.util;

import org.duo.magicallyous.net.message.PlayerActionStateMessage;
import org.duo.magicallyous.net.message.GameMessage;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import org.duo.magicallyous.net.message.UserRegisterMessage;

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
        } else if (message instanceof UserRegisterMessage) {
            UserRegisterMessage userRegisterMessage =
                    (UserRegisterMessage) message;
            System.out.println("Server received user register request message!");
            System.out.println("  >> name    : " + userRegisterMessage.getMagicallyousUser().getName());
            System.out.println("  >> email   : " + userRegisterMessage.getMagicallyousUser().getEmail());
            System.out.println("  >> password: " + userRegisterMessage.getMagicallyousUser().getPassword());
        }
    }
}