/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;
import java.util.Properties;

/**
 *
 * @author duo
 * 
 * @see http://pontov.com.br/site/java/48-java2d/327-criando-uma-interface-ao-usuario-usando-nifty-gui
 */
public class LoginAppState extends AbstractAppState implements Controller {
    Main app;
    TextField tfEmail;
    TextField tfPassword;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (Main) app;
        NiftyJmeDisplay niftyJmeDisplay = 
                new NiftyJmeDisplay(this.app.getAssetManager(), 
                this.app.getInputManager(), 
                this.app.getAudioRenderer(), this.app.getViewPort());
        Nifty nifty = niftyJmeDisplay.getNifty();
        nifty.fromXml("Interface/nifty/magicallyousGUI.xml", "start");
        //nifty.setDebugOptionPanelColors(true);
        this.app.getGuiViewPort().addProcessor(niftyJmeDisplay);
        this.app.getInputManager().setCursorVisible(true);
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
    }

    public void login() {
        System.out.println("Login pressed. Email: " + tfEmail.getDisplayedText() + 
                " password: " + tfPassword.getDisplayedText());
    }

    public void exit() {
        System.out.println("Exit pressed!");
    }

    @Override
    public void bind(Nifty nifty, Screen screen, Element element, Properties parameter, Attributes controlDefinitionAttributes) {
        tfEmail = (TextField) element.findNiftyControl("tfEmail", TextField.class);
        tfPassword = (TextField) element.findNiftyControl("tfPassword", TextField.class);
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
