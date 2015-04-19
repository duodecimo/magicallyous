/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;

/**
 *
 * @author duo
 * 
 */
public class NiftyAppState extends AbstractAppState implements ActionListener {
    Main app;
    NiftyJmeDisplay niftyJmeDisplay;
    Nifty nifty;
    public static final String TOGGLE_OPTIONS = "TOGGLE_OPTIONS";
    public static final String GUIDEFAULT = "Gui Default";

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (Main) app;
        this.app.getFlyByCamera().setEnabled(false);
        this.app.getInputManager().deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
        this.app.getInputManager().addMapping(TOGGLE_OPTIONS, new KeyTrigger(KeyInput.KEY_ESCAPE));
        this.app.getInputManager().addListener(this, TOGGLE_OPTIONS);
        this.app.getInputManager().setCursorVisible(true);
        niftyJmeDisplay = 
                new NiftyJmeDisplay(
                this.app.getAssetManager(), 
                this.app.getInputManager(), 
                this.app.getAudioRenderer(),
                this.app.getRenderManager().getPostView(GUIDEFAULT)
                );
        nifty = niftyJmeDisplay.getNifty();
        this.app.getRenderManager().getPostView(GUIDEFAULT).addProcessor(niftyJmeDisplay);
        nifty.fromXml("Interface/nifty/MagicallyousGUI.xml", "start");
        NiftyController.registerApp(this.app);
        //nifty.setDebugOptionPanelColors(true);
        this.app.getInputManager().setCursorVisible(true);
        
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
    }
    

    @Override
    public void cleanup() {
        super.cleanup();
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if(name.equals(TOGGLE_OPTIONS) && isPressed) {
            ((NiftyController) nifty.getCurrentScreen().getScreenController()).toggleOptionsMenu();
        }
    }
}
