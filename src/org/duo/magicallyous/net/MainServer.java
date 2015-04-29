/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.net;

import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.duo.magicallyous.MagicallyousAppState;
import org.duo.magicallyous.net.message.WelcomeMessage;
import org.duo.magicallyous.net.message.AccountRegisterMessage;
import org.duo.magicallyous.net.message.LoginRequestMessage;
import org.duo.magicallyous.net.message.LoginResponseMessage;
import org.duo.magicallyous.net.message.PlayerActionControlMessage;
import org.duo.magicallyous.net.message.PlayerActionStateMessage;
import org.duo.magicallyous.net.message.ServerServiceOutcomeMessage;
import org.duo.magicallyous.net.util.MagicallyousAccount;
import org.duo.magicallyous.net.util.MainServerMessageListener;
import org.duo.magicallyous.utils.MagicallyousApp;

/**
 *
 * @author aluno
 */
public class MainServer extends MagicallyousApp {
    private Server magicallyousServer;
    private int port;
    private MagicallyousAppState magicallyousAppState;
    private MainServerMessageListener mainServerMessageListener;
    private String actualSceneName = "";
    private final String magicallyousSceneName = "Scene01";
    private final String underworldSceneName = "underworldScene";
    private boolean serverInstance;

    public MainServer() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().
                    getResourceAsStream("resources/net/server/serverNetwork.properties"));
            magicallyousServer = Network.createServer(
                    properties.getProperty("server.name", "Magicallyous Server"), 
                    Integer.parseInt(properties.getProperty("server.version", "1")),
                    Integer.parseInt(properties.getProperty("server.port", "5465")),
                    Integer.parseInt(properties.getProperty("server.port", "5466")));
            magicallyousServer.addConnectionListener(new ConnectionListener() {
               @Override
                public void connectionAdded(Server server, HostedConnection conn) {
                }

                @Override
                public void connectionRemoved(Server server, HostedConnection conn) {
                }
            });
            // register messages classes
            Serializer.registerClass(WelcomeMessage.class);
            Serializer.registerClass(AccountRegisterMessage.class);
            Serializer.registerClass(MagicallyousAccount.class);
            Serializer.registerClass(ServerServiceOutcomeMessage.class);
            Serializer.registerClass(LoginRequestMessage.class);
            Serializer.registerClass(LoginResponseMessage.class);
            // client sends input to server
            Serializer.registerClass(PlayerActionStateMessage.class);
            // server processes input message and sends action to client
            Serializer.registerClass(PlayerActionControlMessage.class);
            mainServerMessageListener = new MainServerMessageListener(this);
            magicallyousServer.addMessageListener(mainServerMessageListener, 
                    PlayerActionStateMessage.class,
                    AccountRegisterMessage.class, 
                    MagicallyousAccount.class, 
                    LoginRequestMessage.class);
            magicallyousServer.start();
            System.out.println("MagicallyousServer started on port " + port);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        actualSceneName = magicallyousSceneName;
    }

    @Override
    public void simpleInitApp() {
        // allow check if app is server instance
        setServerInstance(true);
        magicallyousAppState = new MagicallyousAppState();
        mainServerMessageListener.setMagicallyousApp(this);
        stateManager.attach(magicallyousAppState);
    }
    
    public static void main(String[] args) {
        MainServer app = new MainServer();
        app.start(JmeContext.Type.Headless);
    }

    public Server getMagicallyousServer() {
        return magicallyousServer;
    }

    @Override
    public boolean isServerInstance() {
        return serverInstance;
    }

    public void setServerInstance(boolean serverInstance) {
        this.serverInstance = serverInstance;
    }

    @Override
    public String getMagicallyousSceneName() {
        return magicallyousSceneName;
    }

    public String getUnderworldSceneName() {
        return underworldSceneName;
    }

    @Override
    public String getActualSceneName() {
        return actualSceneName;
    }

    public void setActualSceneName(String actualSceneName) {
        this.actualSceneName = actualSceneName;
    }
}
