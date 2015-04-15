/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.text.Password;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.core.Screen;

/**
 *
 * @author duo
 */
public class LoginAppState extends AbstractAppState {
    Main app;
    Screen screen;
    Label lbLogin;
    Label lbEmail;
    TextField tfEmail;
    Label lbPassword;
    Password pwPassword;
    Panel leftLoginPanel;
    Panel rightLoginPanel;
    Panel loginPanel;
    
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (Main) app;

        screen = new Screen(this.app);

        screen = new Screen(app);
        float width  = 300f;
        float height = 100f;

        lbLogin = new Label(screen, new Vector2f(0.0f, 0.0f), new Vector2f(width, height));
        lbEmail = new Label(screen, new Vector2f(0.0f, height*2.0f), new Vector2f(width, height));
        lbPassword = new Label(screen, new Vector2f(0.0f, height*3.0f), new Vector2f(width, height));

        leftLoginPanel = new Panel(screen, new Vector2f(20f, 20.f), new Vector2f(width, height*4.0f));

        tfEmail = new TextField(screen, new Vector2f(width + 10.0f, height*2.0f), new Vector2f(width, height));
        pwPassword = new Password(screen, new Vector2f(width + 10.0f, height*3.0f), new Vector2f(width, height));

        rightLoginPanel = new Panel(screen, new Vector2f(width + 30.0f, 20.f), new Vector2f(width, height*4.0f));

        lbLogin.setText("Login");
        lbLogin.setFont("/Interface/Fonts/LiberationSans.fnt");
        lbLogin.setFontColor(ColorRGBA.White);
        lbLogin.setTextAlign(BitmapFont.Align.Center);
        //leftLoginPanel.addChild(lbLogin);

        lbEmail.setText("Email:");
        lbEmail.setFontColor(ColorRGBA.White);
        lbEmail.setTextAlign(BitmapFont.Align.Center);
        //leftLoginPanel.addChild(lbEmail);

        lbPassword.setText("Password:");
        lbPassword.setFontColor(ColorRGBA.White);
        lbPassword.setTextAlign(BitmapFont.Align.Center);
        //leftLoginPanel.addChild(lbPassword);

        tfEmail.setText("");
        tfEmail.setFontColor(ColorRGBA.Yellow);
        //rightLoginPanel.addChild(tfEmail);


        pwPassword.setText("");
        pwPassword.setFontColor(ColorRGBA.Yellow);
        //rightLoginPanel.addChild(pwPassword);

        Panel panel = new Panel(screen,
                Vector2f.ZERO, 
                new Vector2f(screen.getWidth() * 0.95f, screen.getHeight() * 0.95f),
                Vector4f.ZERO, 
                null);

        //panel.addChild(leftLoginPanel);
        //panel.addChild(rightLoginPanel);
        panel.addChild(lbLogin);
        panel.addChild(lbEmail);
        panel.addChild(lbPassword);
        panel.addChild(tfEmail);
        panel.addChild(pwPassword);

        screen.addElement(panel);
        this.app.getGuiNode().addControl(screen);
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
    }
}
