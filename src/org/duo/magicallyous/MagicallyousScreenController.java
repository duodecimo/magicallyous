/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import static org.duo.magicallyous.NiftyController.app;
import org.duo.magicallyous.net.message.AccountRegisterMessage;
import org.duo.magicallyous.net.message.LoginRequestMessage;
import org.duo.magicallyous.net.message.ServerServiceOutcomeMessage;
import org.duo.magicallyous.net.util.MagicallyousAccount;

/**
 *
 * @author duo
 */
public class MagicallyousScreenController extends NiftyController implements MessageListener<Client> {
    boolean registeredMessageListener = false;
    // login
    TextField tfEmail;
    TextField tfPassword;
    Label lbCheck;
    // register
    TextField tfRegName;
    TextField tfRegEmail;
    TextField tfRegPassword;
    TextField tfRegPassword2;
    Label lbRegCheck;

    public final void initMessageListener() {
        if (!registeredMessageListener) {
            app.getMagicallyousClient().addMessageListener(this, ServerServiceOutcomeMessage.class);
            registeredMessageListener = true;
        }
    }

    public synchronized void login() {
        if (app != null) {
            initMessageListener();
            System.out.println("Login pressed."
                    + " Email: " + tfEmail.getDisplayedText()
                    + " password: " + tfPassword.getRealText());
            MagicallyousAccount magicallyousUser = new MagicallyousAccount();
            magicallyousUser.setId(new Integer(0));
            magicallyousUser.setEmail(tfEmail.getDisplayedText());
            magicallyousUser.setName("");
            magicallyousUser.setPassword(tfPassword.getRealText());
            app.getMagicallyousClient().send(new LoginRequestMessage(magicallyousUser));
            lbCheck.setText("Login data sent!");
        }
    }

    public synchronized void register() {
        if (app != null) {
            initMessageListener();
            if (tfRegPassword.getRealText().equals(tfRegPassword2.getRealText())) {
                System.out.println("Register pressed. Name: " + tfRegName.getDisplayedText()
                        + " Email: " + tfRegEmail.getDisplayedText()
                        + " password: " + tfRegPassword.getRealText()
                        + " password2: " + tfRegPassword2.getRealText());
                MagicallyousAccount magicallyousUser = new MagicallyousAccount();
                magicallyousUser.setId(new Integer(0));
                magicallyousUser.setEmail(tfRegEmail.getDisplayedText());
                magicallyousUser.setName(tfRegName.getDisplayedText());
                magicallyousUser.setPassword(tfRegPassword.getRealText());
                app.getMagicallyousClient().send(new AccountRegisterMessage(magicallyousUser));
                lbRegCheck.setText("Request sent!");
            } else {
                lbRegCheck.setText("Passwords must be equal, try again!");
            }
        }
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        super.bind(nifty, screen);
        // login
        tfEmail = (TextField) nifty.getScreen("start").
                findNiftyControl("tfEmail", TextField.class);
        tfPassword = (TextField) nifty.getScreen("start").
                findNiftyControl("tfPassword", TextField.class);
        lbCheck = (Label) nifty.getScreen("start").
                findNiftyControl("lbCheck", Label.class);
        // register
        tfRegName = (TextField) nifty.getScreen("start").
                findNiftyControl("tfRegName", TextField.class);
        tfRegEmail = (TextField) nifty.getScreen("start").
                findNiftyControl("tfRegEmail", TextField.class);
        tfRegPassword = (TextField) nifty.getScreen("start").
                findNiftyControl("tfRegPassword", TextField.class);
        tfRegPassword2 = (TextField) nifty.getScreen("start").
                findNiftyControl("tfRegPassword2", TextField.class);
        lbRegCheck = (Label) nifty.getScreen("start").
                findNiftyControl("lbRegCheck", Label.class);
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
            } else if(serverServiceOutcomeMessage.getService() == 
                    ServerServiceOutcomeMessage.Service.ACCOUNT_REGISTER_OK) {
                lbRegCheck.setText("Account registering success!");
                System.out.println("Account registering confirmation received!!!!!");
            } else if(serverServiceOutcomeMessage.getService() == 
                    ServerServiceOutcomeMessage.Service.ACCOUNT_REGISTER_FAIL) {
                lbRegCheck.setText("Account registering failure!");
                System.out.println("Account registering failure received!!!!!");
            }
        }
    }
}
