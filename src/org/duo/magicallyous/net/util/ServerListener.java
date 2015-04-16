/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.net.util;

import org.duo.magicallyous.net.message.GameMessage;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import java.util.List;
import org.duo.magicallyous.net.message.AccountRegisterMessage;

/**
 *
 * @author aluno
 */
public class ServerListener implements MessageListener<HostedConnection> {
    DatabaseManager databaseManager;

    @Override
    public void messageReceived(HostedConnection source, Message message) {
        if (message instanceof GameMessage) {
            // do something with the message
            GameMessage gameMessage = (GameMessage) message;
            System.out.println("Server received '"
                    + gameMessage.getGameMessage()
                    + "' from client #" + source.getId());
        } else if (message instanceof AccountRegisterMessage) {
            MagicallyousAccount magicallyousAccount = ((AccountRegisterMessage) message).getMagicallyousUser();
            System.out.println("Server received account register request message!");
            System.out.println("  >> name    : " + magicallyousAccount.getName());
            System.out.println("  >> email   : " + magicallyousAccount.getEmail());
            System.out.println("  >> password: " + magicallyousAccount.getPassword());
            try {
                if (databaseManager == null) {
                    databaseManager = new DatabaseManager();
                }
                databaseManager.createAccount(magicallyousAccount);
                List<MagicallyousAccount> accounts = databaseManager.getAccounts();
                System.out.println("There are " + accounts.size() + " accounts registered.");
                for(MagicallyousAccount account : accounts) {
                    System.out.println("  name    : " + account.getName());
                    System.out.println("  email   : " + account.getEmail());
                    System.out.println("  password: " + account.getPassword());
                }
            } catch (Exception e) {
                System.out.println("Error creating account!" + e);
            }
        }
    }
}