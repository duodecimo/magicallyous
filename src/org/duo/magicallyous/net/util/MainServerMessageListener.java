/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.net.util;

import org.duo.magicallyous.net.message.WelcomeMessage;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import java.util.List;
import org.duo.magicallyous.net.MainServer;
import org.duo.magicallyous.net.message.AccountRegisterMessage;
import org.duo.magicallyous.net.message.LoginRequestMessage;
import org.duo.magicallyous.net.message.PlayerActionStateMessage;
import org.duo.magicallyous.net.message.ServerServiceOutcomeMessage;
import org.duo.magicallyous.player.PlayerActionControl;
import org.duo.magicallyous.player.PlayerAppState;
import org.duo.magicallyous.utils.MagicallyousApp;

/**
 *
 * @author aluno
 */
public class MainServerMessageListener implements MessageListener<HostedConnection> {
    DatabaseManager databaseManager;
    MainServer mainServer;
    private MagicallyousApp magicallyousApp;

    public MainServerMessageListener(MainServer mainServer) {
        this.mainServer = mainServer;
    }

    @Override
    public void messageReceived(HostedConnection source, Message message) {
        if (message instanceof WelcomeMessage) {
            // do something with the message
            WelcomeMessage gameMessage = (WelcomeMessage) message;
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
                source.send(new ServerServiceOutcomeMessage(
                        ServerServiceOutcomeMessage.Service.ACCOUNT_REGISTER_OK));
                List<MagicallyousAccount> accounts = databaseManager.getAccounts();
                System.out.println("There are " + accounts.size() + " accounts registered.");
                for(MagicallyousAccount account : accounts) {
                    System.out.println("  name    : " + account.getName());
                    System.out.println("  email   : " + account.getEmail());
                    System.out.println("  password: " + account.getPassword());
                }
            } catch (Exception e) {
                source.send(new ServerServiceOutcomeMessage(
                        ServerServiceOutcomeMessage.Service.ACCOUNT_REGISTER_FAIL));
                System.out.println("Error creating account!" + e);
            }
        } else if (message instanceof LoginRequestMessage) {
            MagicallyousAccount magicallyousAccount = ((LoginRequestMessage) message).getMagicallyousUser();
            System.out.println("Server received login request message!");
            System.out.println("  >> email   : " + magicallyousAccount.getEmail());
            System.out.println("  >> password: " + magicallyousAccount.getPassword());
            try {
                if (databaseManager == null) {
                    databaseManager = new DatabaseManager();
                }
                MagicallyousAccount loginAccount = 
                        databaseManager.getAccount(magicallyousAccount.getEmail(), 
                        magicallyousAccount.getPassword());
                ServerServiceOutcomeMessage serverServiceOutcomeMessage;
                if(loginAccount != null) {
                    serverServiceOutcomeMessage = new ServerServiceOutcomeMessage(
                            ServerServiceOutcomeMessage.Service.LOGIN_ACCEPTED);
                    serverServiceOutcomeMessage.setResponse("Welcome " + loginAccount.getName() + "."
                            + " You are logged in Magicallyous.");
                } else {
                    serverServiceOutcomeMessage = new ServerServiceOutcomeMessage(
                            ServerServiceOutcomeMessage.Service.LOGIN_FAILED);
                    serverServiceOutcomeMessage.setResponse("Login failed!!! You may try again ...");
                }
                source.send(serverServiceOutcomeMessage);
            } catch (Exception e) {
                source.send(new ServerServiceOutcomeMessage(
                        ServerServiceOutcomeMessage.Service.LOGIN_FAILED));
                System.out.println("Error logging in >>> : " + e);
                System.out.println("Check accounts: ");
                List<MagicallyousAccount> accounts = databaseManager.getAccounts();
                System.out.println("There are " + accounts.size() + " accounts registered.");
                for(MagicallyousAccount account : accounts) {
                    System.out.println("  name    : " + account.getName());
                    System.out.println("  email   : " + account.getEmail());
                    System.out.println("  password: " + account.getPassword());
                }
            }
        } else if (message instanceof PlayerActionStateMessage) {
            PlayerActionStateMessage playerActionStateMessage = (PlayerActionStateMessage) message;
            System.out.println("Receiving input message: " + playerActionStateMessage.isMoveFoward());
            PlayerAppState playerAppState = 
                    ((MainServer)getMagicallyousApp()).getStateManager().getState(PlayerAppState.class);
            PlayerActionControl playerActionControl = 
                    playerAppState.getPlayers().get(0).getControl(PlayerActionControl.class);
            // set all that we got from input (see PlayerSendActionInput)
            playerActionControl.setMoveFoward(playerActionStateMessage.isMoveFoward());
            playerActionControl.setMoveBackward(playerActionStateMessage.isMoveBackward());
            playerActionControl.setStopped(playerActionStateMessage.isStopped());
            playerActionControl.setRotateRight(playerActionStateMessage.isRotateRight());
            playerActionControl.setRotateLeft(playerActionStateMessage.isRotateLeft());
            if(playerActionStateMessage.isRequestToggleRunning()) {
                playerActionControl.toggleRunning();
            }
            playerActionControl.setIncreaseHealth(playerActionStateMessage.isIncreaseHealth());
            playerActionControl.setDecreaseHealth(playerActionStateMessage.isDecreaseHealth());
            playerActionControl.setIncreaseDamage(playerActionStateMessage.isIncreaseDamage());
            playerActionControl.setDecreaseDamage(playerActionStateMessage.isDecreaseDamage());
            playerActionControl.setIncreaseDefense(playerActionStateMessage.isIncreaseDefense());
            playerActionControl.setDecreaseDefense(playerActionStateMessage.isDecreaseDefense());
            playerActionControl.setRotateValue(playerActionStateMessage.getRotateValue());
            playerActionControl.setAnimationStateEnum(playerActionStateMessage.getAnimationStateEnum());
        }
    }

    public MagicallyousApp getMagicallyousApp() {
        return magicallyousApp;
    }

    public void setMagicallyousApp(MagicallyousApp magicallyousApp) {
        this.magicallyousApp = magicallyousApp;
    }
}