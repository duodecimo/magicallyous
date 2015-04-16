/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.network.AbstractMessage;
import com.jme3.network.Client;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.duo.magicallyous.net.message.UserRegisterMessage;
import org.duo.magicallyous.net.util.MagicallyousUser;

/**
 *
 * @author duo
 * 
 * @see http://pontov.com.br/site/java/48-java2d/327-criando-uma-interface-ao-usuario-usando-nifty-gui
 */
public class RegisterAppState extends AbstractAppState implements Controller {
    Main app;
    TextField tfName;
    TextField tfEmail;
    TextField tfPassword;
    TextField tfPassword2;
    List<UserRegisterMessage> messages;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        messages = new ArrayList<>();
        this.app = (Main) app;
        this.app.getFlyByCamera().setEnabled(false);

        NiftyJmeDisplay niftyJmeDisplay = 
                new NiftyJmeDisplay(this.app.getAssetManager(), 
                this.app.getInputManager(), 
                this.app.getAudioRenderer(), this.app.getViewPort());
        Nifty nifty = niftyJmeDisplay.getNifty();
        nifty.fromXml("Interface/nifty/registerMagicallyousGUI.xml", "start");
        //nifty.setDebugOptionPanelColors(true);
        this.app.getGuiViewPort().addProcessor(niftyJmeDisplay);
        this.app.getInputManager().setCursorVisible(true);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if(messages == null) {
            System.out.println("messages null in update!!!");
        }
        if(this.app != null) {
            for(UserRegisterMessage userRegisterMessage : messages) {
                System.out.println("Enviando mensagem !!!");
                this.app.sendMessage(userRegisterMessage);
                messages.remove(userRegisterMessage);
            }
        } else {
            System.out.println("app null in update!!!");
        }
    }
    

    @Override
    public void cleanup() {
        super.cleanup();
    }

    public void register() {
        System.out.println("Register pressed. Name: " + tfName.getDisplayedText() +
                " Email: " + tfEmail.getDisplayedText() + 
                " password: " + tfPassword.getDisplayedText() +
                " password2: " + tfPassword2.getDisplayedText());
        MagicallyousUser magicallyousUser = new MagicallyousUser();
        magicallyousUser.setEmail(tfEmail.getDisplayedText());
        magicallyousUser.setName(tfName.getDisplayedText());
        magicallyousUser.setPassword(tfPassword.getDisplayedText());
        UserRegisterMessage userRegisterMessage = new UserRegisterMessage(magicallyousUser);
        if(messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(userRegisterMessage);
    }

    public void back() {
        System.out.println("Back pressed!");
    }

    @Override
    public void bind(Nifty nifty, Screen screen, Element element, Properties parameter, Attributes controlDefinitionAttributes) {
        tfName = (TextField) element.findNiftyControl("tfName", TextField.class);
        tfEmail = (TextField) element.findNiftyControl("tfEmail", TextField.class);
        tfPassword = (TextField) element.findNiftyControl("tfPassword", TextField.class);
        tfPassword2 = (TextField) element.findNiftyControl("tfPassword2", TextField.class);
    }

    @Override
    public void init(Properties parameter, Attributes controlDefinitionAttributes) {
    }

    @Override
    public void onFocus(boolean getFocus) {
    }

    @Override
    public boolean inputEvent(NiftyInputEvent inputEvent) {
        return false;
    }

    @Override
    public void onStartScreen() {
    }
}
