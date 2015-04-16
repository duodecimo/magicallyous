package org.duo.magicallyous;

import com.jme3.app.SimpleApplication;
import com.jme3.network.AbstractMessage;
import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.network.serializing.Serializer;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.duo.magicallyous.net.message.GameMessage;
import org.duo.magicallyous.net.message.PlayerActionStateMessage;
import org.duo.magicallyous.net.message.UserRegisterMessage;
import org.duo.magicallyous.net.util.ClientListener;

/**
 *
 * @author duo
 */
public class Main extends SimpleApplication {

    private Client magicallyousClient;
    private String serverIp;
    private int port;
    private MagicallyousAppState magicallyousAppState;
    private UnderworldAppState underworldAppState;
    private String actualSceneName = "";
    private final String magicallyousSceneName = "Scene01";
    private final String underworldSceneName = "underworldScene";

    public Main() {
        actualSceneName = magicallyousSceneName;
    }

    public void switchAppState() {
        Node node;
        if (actualSceneName.equals(underworldSceneName)) {
            actualSceneName = magicallyousSceneName;
            stateManager.detach(underworldAppState);
            node = (Node) getRootNode().getChild(underworldSceneName);
            if (node != null) {
                getRootNode().detachChild(node);
            }
            underworldAppState.cleanup();
            getInputManager().clearMappings();
            magicallyousAppState = new MagicallyousAppState();
            stateManager.attach(magicallyousAppState);
        } else {
            actualSceneName = underworldSceneName;
            stateManager.detach(magicallyousAppState);
            node = (Node) getRootNode().getChild(magicallyousSceneName);
            if (node != null) {
                getRootNode().detachChild(node);
            }
            magicallyousAppState.cleanup();
            getInputManager().clearMappings();
            underworldAppState = new UnderworldAppState();
            stateManager.attach(underworldAppState);
        }
    }

    public static void main(String[] args) {
        Main app = new Main();
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Magicallyous");
        //settings.setSettingsDialogImage("Interface/splashscreen.png");
        settings.setResolution(1280, 800);
        app.setSettings(settings);
        app.start(JmeContext.Type.Display);
    }

    @Override
    public void simpleInitApp() {
        port = 5465;
        serverIp = "localhost";
        setDisplayStatView(false);
        try {
            magicallyousClient = Network.connectToServer(serverIp, port);
            magicallyousClient.start();
            Serializer.registerClass(GameMessage.class);
            //Serializer.registerClass(PlayerActionStateMessage.class);
            Serializer.registerClass(UserRegisterMessage.class);
            magicallyousClient.addMessageListener(new ClientListener(), GameMessage.class);
            magicallyousClient.send(new GameMessage("New client (me) on Magicallyous!"));
            System.out.println("Application connected to server "
                    + serverIp + " port " + port);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        //LoginAppState loginAppState = new LoginAppState();
        //stateManager.attach(loginAppState);
        RegisterAppState registerAppState = new RegisterAppState();
        stateManager.attach(registerAppState);
        //underworldAppState = new UnderworldAppState();
        //stateManager.attach(underworldAppState);
        //magicallyousAppState = new MagicallyousAppState();
        //stateManager.attach(magicallyousAppState);
    }

    public String getActualSceneName() {
        return actualSceneName;
    }

    public String getMagicallyousSceneName() {
        return magicallyousSceneName;
    }

    public String getUnderworldSceneName() {
        return underworldSceneName;
    }

    public Client getMagicallyousClient() {
        return magicallyousClient;
    }

    public String getServerIp() {
        return serverIp;
    }

    public int getPort() {
        return port;
    }

    public void sendMessage(AbstractMessage message) {
        System.out.println("Main: About to send message: " + message.toString());
        magicallyousClient.send(message);
    }
}
