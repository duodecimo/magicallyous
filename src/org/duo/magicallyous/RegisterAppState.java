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
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.duo.magicallyous.net.message.AccountRegisterMessage;
import org.duo.magicallyous.net.message.ServerServiceOutcomeMessage;
import org.duo.magicallyous.net.util.MagicallyousAccount;

/**
 *
 * @author duo
 * 
 * @see http://pontov.com.br/site/java/48-java2d/327-criando-uma-interface-ao-usuario-usando-nifty-gui
 */
public class RegisterAppState extends AbstractAppState implements ScreenController, MessageListener<Client> {
    Main app;
    TextField tfName;
    TextField tfEmail;
    TextField tfPassword;
    TextField tfPassword2;
    Label lbCheck;
    NiftyJmeDisplay niftyJmeDisplay;
    Nifty nifty;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (Main) app;
        this.app.getFlyByCamera().setEnabled(false);

        niftyJmeDisplay = 
                new NiftyJmeDisplay(this.app.getAssetManager(), 
                this.app.getInputManager(), 
                this.app.getAudioRenderer(), this.app.getViewPort());
        nifty = niftyJmeDisplay.getNifty();
        nifty.fromXml("Interface/nifty/registerMagicallyousGUI.xml", "start", this);
        //nifty.setDebugOptionPanelColors(true);
        this.app.getGuiViewPort().addProcessor(niftyJmeDisplay);
        this.app.getInputManager().setCursorVisible(true);
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

    public synchronized void register() {
        if (this.app != null) {
            if (tfPassword.getRealText().equals(tfPassword2.getRealText())) {
                System.out.println("Register pressed. Name: " + tfName.getDisplayedText()
                        + " Email: " + tfEmail.getDisplayedText()
                        + " password: " + tfPassword.getRealText()
                        + " password2: " + tfPassword2.getRealText());
                MagicallyousAccount magicallyousUser = new MagicallyousAccount();
                magicallyousUser.setId(new Integer(0));
                magicallyousUser.setEmail(tfEmail.getDisplayedText());
                magicallyousUser.setName(tfName.getDisplayedText());
                magicallyousUser.setPassword(tfPassword.getRealText());
                this.app.getMagicallyousClient().send(new AccountRegisterMessage(magicallyousUser));
                lbCheck.setText("Request sent!");
            } else {
                lbCheck.setText("Passwords must be equal, try again!");
            }
        }
    }

    public void back() {
        System.out.println("Back pressed!");
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        tfName = (TextField) nifty.getScreen("start").findNiftyControl("tfName", TextField.class);
        tfEmail = (TextField) nifty.getScreen("start").findNiftyControl("tfEmail", TextField.class);
        tfPassword = (TextField) nifty.getScreen("start").findNiftyControl("tfPassword", TextField.class);
        tfPassword2 = (TextField) nifty.getScreen("start").findNiftyControl("tfPassword2", TextField.class);
        lbCheck = (Label) nifty.getScreen("start").findNiftyControl("lbCheck", Label.class);
    }

    @Override
    public void onEndScreen() {
    }

    @Override
    public void messageReceived(Client source, Message message) {
        if(message instanceof ServerServiceOutcomeMessage) {
            ServerServiceOutcomeMessage serverServiceOutcomeMessage = (ServerServiceOutcomeMessage) message;
            if(serverServiceOutcomeMessage.getService() == 
                    ServerServiceOutcomeMessage.Service.ACCOUNT_REGISTER_OK) {
                lbCheck.setText("Account registering success!");
                System.out.println("Account registering confirmation received!!!!!");
            } else if(serverServiceOutcomeMessage.getService() == 
                    ServerServiceOutcomeMessage.Service.ACCOUNT_REGISTER_FAIL) {
                lbCheck.setText("Account registering failure!");
                System.out.println("Account registering failure received!!!!!");
            }
        }
    }
}
