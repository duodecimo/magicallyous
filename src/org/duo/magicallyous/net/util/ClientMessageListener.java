/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.net.util;

import org.duo.magicallyous.net.message.WelcomeMessage;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.scene.Node;
import java.util.List;
import org.duo.magicallyous.Main;
import org.duo.magicallyous.net.message.PlayerActionStateMessage;
import org.duo.magicallyous.net.message.PlayerNetInputMessage;
import org.duo.magicallyous.player.PlayerActionControl;
import org.duo.magicallyous.player.PlayerActionMapping;
import org.duo.magicallyous.player.PlayerAppState;
import org.duo.magicallyous.utils.GeneralStateEnum;
import org.duo.magicallyous.utils.MagicallyousApp;

/**
 *
 * @author aluno
 */
public class ClientMessageListener implements MessageListener<Client> {
    private MagicallyousApp magicallyousApp;

    @Override
    public void messageReceived(Client source, Message message) {
        if (message instanceof WelcomeMessage) {
            // do something with the message
            WelcomeMessage gameMessage = (WelcomeMessage) message;
            System.out.println("Client #" + source.getId()
                    + " received: '" + gameMessage.getGameMessage() + "'");
        } else if (message instanceof PlayerNetInputMessage) {
            //System.out.println("input received! (PlayerNetInputMessage)");
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
                    //System.out.println("moving foward! (PlayerNetInputMessage)");
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
                    //System.out.println("turning right! (PlayerNetInputMessage)");
                    break;
                case PlayerActionMapping.MAP_TURNLEFT :
                    playerActionControl.setRotateLeft(playerNetInputMessage.isIsPressed());
                    playerActionControl.setRotateValue(playerNetInputMessage.getValue());
                    //System.out.println("turning left! (PlayerNetInputMessage)");
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
        }
    }

    public MagicallyousApp getMagicallyousApp() {
        return magicallyousApp;
    }

    public void setMagicallyousApp(MagicallyousApp magicallyousApp) {
        this.magicallyousApp = magicallyousApp;
    }
}
