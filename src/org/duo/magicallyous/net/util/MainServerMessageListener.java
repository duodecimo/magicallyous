/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.net.util;

import org.duo.magicallyous.net.message.WelcomeMessage;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.scene.Node;
import java.util.List;
import org.duo.magicallyous.MagicallyousAppState;
import org.duo.magicallyous.Main;
import org.duo.magicallyous.net.MainServer;
import org.duo.magicallyous.net.message.AccountRegisterMessage;
import org.duo.magicallyous.net.message.LoginRequestMessage;
import org.duo.magicallyous.net.message.PlayerActionStateMessage;
import org.duo.magicallyous.net.message.PlayerNetInputMessage;
import org.duo.magicallyous.net.message.ServerServiceOutcomeMessage;
import org.duo.magicallyous.player.PlayerActionControl;
import org.duo.magicallyous.player.PlayerActionMapping;
import org.duo.magicallyous.player.PlayerAppState;
import org.duo.magicallyous.utils.GeneralStateEnum;
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
        } else if (message instanceof PlayerNetInputMessage) {
            System.out.println("input received! (PlayerNetInputMessage)");
            PlayerNetInputMessage playerNetInputMessage = 
                    (PlayerNetInputMessage) message;
            List<Node> players = 
                    getMagicallyousApp().getStateManager().getState(PlayerAppState.class).getPlayers();
            // only player 0 for debug
            Node player = players.get(0);
            PlayerActionControl playerActionControl = player.getControl(PlayerActionControl.class);
            playerActionControl.setGeneralStateEnum(GeneralStateEnum.NORMAL);
            switch(playerNetInputMessage.getName()) {
                case PlayerActionMapping.MAP_MOVEFOWARD :
                    playerActionControl.setMoveFoward(playerNetInputMessage.isIsPressed());
                    break;
                case PlayerActionMapping.MAP_MOVEBACKWARD :
                    playerActionControl.setMoveBackward(playerNetInputMessage.isIsPressed());
                    break;
                case PlayerActionMapping.MAP_STOP :
                    playerActionControl.setStopped(playerNetInputMessage.isIsPressed());
                    break;
                case PlayerActionMapping.MAP_TURNRIGHT :
                    playerActionControl.setRotateRight(playerNetInputMessage.isIsPressed());
                    playerActionControl.setRotateValue(playerNetInputMessage.getValue());
                    break;
                case PlayerActionMapping.MAP_TURNLEFT :
                    playerActionControl.setRotateLeft(playerNetInputMessage.isIsPressed());
                    playerActionControl.setRotateValue(playerNetInputMessage.getValue());
                    break;
                case PlayerActionMapping.MAP_TOGGLEWALKSTATE :
                    if(!playerNetInputMessage.isIsPressed()) {
                        playerActionControl.toggleRunning();
                    }
                    break;
                case PlayerActionMapping.MAP_INCREASEHEALTH:
                    if (playerNetInputMessage.isIsPressed()) {
                        playerActionControl.setIncreaseHealth(true);
                    }
                    break;
                case PlayerActionMapping.MAP_DECREASEHEALTH:
                    if (playerNetInputMessage.isIsPressed()) {
                        playerActionControl.setDecreaseHealth(true);
                    }
                    break;
                case PlayerActionMapping.MAP_INCREASEDAMAGE:
                    if (playerNetInputMessage.isIsPressed()) {
                        playerActionControl.setIncreaseDamage(true);
                    }
                    break;
                case PlayerActionMapping.MAP_DECREASEDAMAGE:
                    if (playerNetInputMessage.isIsPressed()) {
                        playerActionControl.setDecreaseDamage(true);
                    }
                    break;
                case PlayerActionMapping.MAP_INCREASEDEFENSE:
                    if (playerNetInputMessage.isIsPressed()) {
                        playerActionControl.setIncreaseDefense(true);
                    }
                    break;
                case PlayerActionMapping.MAP_DECREASEDEFENSE:
                    if (playerNetInputMessage.isIsPressed()) {
                        playerActionControl.setDecreaseDefense(true);
                    }
                    break;
                case PlayerActionMapping.MAP_USESWORD :
                    if (playerNetInputMessage.isIsPressed()) {
                    }
                    break;
                default:
            }
            // replicate message to client
            source.send(message);
        }
    }

    public MagicallyousApp getMagicallyousApp() {
        return magicallyousApp;
    }

    public void setMagicallyousApp(MagicallyousApp magicallyousApp) {
        this.magicallyousApp = magicallyousApp;
    }
}