/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.duo.magicallyous.net.message.LoginRequestMessage;
import org.duo.magicallyous.net.message.ServerServiceOutcomeMessage;
import org.duo.magicallyous.net.util.MagicallyousAccount;

/**
 *
 * @author duo
 * 
 * @see http://pontov.com.br/site/java/48-java2d/327-criando-uma-interface-ao-usuario-usando-nifty-gui
 */
public class LoginAppState extends AbstractAppState implements ScreenController, MessageListener<Client> {
    Main app;
    TextField tfEmail;
    TextField tfPassword;
    Label lbCheck;
    Element loginPopupElement;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (Main) app;
        this.app.getMagicallyousClient().addMessageListener(this, ServerServiceOutcomeMessage.class);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
    }
    

    @Override
    public void cleanup() {
        super.cleanup();
    }

    public synchronized void login() {
        if (this.app != null) {
            System.out.println("Login pressed."
                    + " Email: " + tfEmail.getDisplayedText()
                    + " password: " + tfPassword.getRealText());
            MagicallyousAccount magicallyousUser = new MagicallyousAccount();
            magicallyousUser.setId(new Integer(0));
            magicallyousUser.setEmail(tfEmail.getDisplayedText());
            magicallyousUser.setName("");
            magicallyousUser.setPassword(tfPassword.getRealText());
            this.app.getMagicallyousClient().send(new LoginRequestMessage(magicallyousUser));
            lbCheck.setText("Login data sent!");
        }
    }

    public void register() {
        System.out.println("Register new user button pressed!");
        //app.switchAppState(new RegisterAppState());
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        tfEmail = (TextField) nifty.getScreen("start").findNiftyControl("tfEmail", TextField.class);
        tfPassword = (TextField) nifty.getScreen("start").findNiftyControl("tfPassword", TextField.class);
        lbCheck = (Label) nifty.getScreen("start").findNiftyControl("lbCheck", Label.class);
    }

    @Override
    public void onEndScreen() {
    }

    @Override
    public void messageReceived(Client source, Message message) {
        if(message instanceof ServerServiceOutcomeMessage) {
            ServerServiceOutcomeMessage serverServiceOutcomeMessage = 
                    (ServerServiceOutcomeMessage) message;
            if(serverServiceOutcomeMessage.getService() == 
                    ServerServiceOutcomeMessage.Service.LOGIN_ACCEPTED) {
                lbCheck.setText(serverServiceOutcomeMessage.getResponse());
                System.out.println("Login success received: !" + 
                        serverServiceOutcomeMessage.getResponse());
            } else if(serverServiceOutcomeMessage.getService() == 
                    ServerServiceOutcomeMessage.Service.LOGIN_FAILED) {
                lbCheck.setText(serverServiceOutcomeMessage.getResponse());
                System.out.println("Login failed : " + 
                        serverServiceOutcomeMessage.getResponse());
            }
        }
    }
}
