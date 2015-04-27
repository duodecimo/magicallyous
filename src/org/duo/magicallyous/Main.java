package org.duo.magicallyous;

import com.jme3.app.SimpleApplication;
import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.network.serializing.Serializer;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.duo.magicallyous.net.message.WelcomeMessage;
import org.duo.magicallyous.net.message.AccountRegisterMessage;
import org.duo.magicallyous.net.message.LoginRequestMessage;
import org.duo.magicallyous.net.message.LoginResponseMessage;
import org.duo.magicallyous.net.message.ServerServiceOutcomeMessage;
import org.duo.magicallyous.net.util.ClientMessageListener;
import org.duo.magicallyous.net.util.MagicallyousAccount;
import org.duo.magicallyous.npc.NpcAppState;
import org.duo.magicallyous.player.PlayerAppState;

/**
 *
 * @author duo
 */
public class Main extends SimpleApplication {
    private Client magicallyousClient;
    private Integer magicallyousClientConnectionId;
    private Integer localPlayerId;
    private MagicallyousAppState magicallyousAppState;
    private UnderworldAppState underworldAppState;
    private String actualSceneName = "";
    private final String magicallyousSceneName = "Scene01";
    private final String underworldSceneName = "underworldScene";

    public Main() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().
                    getResourceAsStream("resources/net/client/network.properties"));
            magicallyousClient = Network.connectToServer(
                    properties.getProperty("server.name", "Magicallyous Server"),
                    Integer.parseInt(properties.getProperty("server.version", "1")),
                    properties.getProperty("server.address", "localhost"),
                    Integer.parseInt(properties.getProperty("server.port", "5465")), 
                    Integer.parseInt(properties.getProperty("server.port", "5466")));
            // register serializable messages classes
            Serializer.registerClass(WelcomeMessage.class);
            Serializer.registerClass(AccountRegisterMessage.class);
            Serializer.registerClass(MagicallyousAccount.class);
            Serializer.registerClass(ServerServiceOutcomeMessage.class);
            Serializer.registerClass(LoginRequestMessage.class);
            Serializer.registerClass(LoginResponseMessage.class);
            // register listeners
            magicallyousClient.addMessageListener(new ClientMessageListener(), WelcomeMessage.class);
            magicallyousClient.start();
            System.out.println("Application connected to server " 
                    + "name: " + properties.getProperty("server.name") 
                    + " address: " + properties.getProperty("server.address") 
                    + " port: " + properties.getProperty("server.port"));
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        actualSceneName = magicallyousSceneName;
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
        //setDisplayStatView(false);
        //NiftyAppState niftyAppState = new NiftyAppState();
        //stateManager.attach(niftyAppState);
        //underworldAppState = new UnderworldAppState();
        //stateManager.attach(underworldAppState);
        magicallyousAppState = new MagicallyousAppState();
        stateManager.attach(magicallyousAppState);
        //stateManager.attach(new PlayerAppState());
        //stateManager.attach(new NpcAppState());
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

    public Integer getMagicallyousClientConnectionId() {
        return magicallyousClientConnectionId;
    }

    public void setMagicallyousClientConnectionId(Integer magicallyousClientConnectionId) {
        this.magicallyousClientConnectionId = magicallyousClientConnectionId;
    }

    public Integer getLocalPlayerId() {
        return localPlayerId;
    }

    public void setLocalPlayerId(Integer localPlayerId) {
        this.localPlayerId = localPlayerId;
    }
}
