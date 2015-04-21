/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.net;

import com.jme3.app.SimpleApplication;
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
import org.duo.magicallyous.net.message.WelcomeMessage;
import org.duo.magicallyous.net.message.AccountRegisterMessage;
import org.duo.magicallyous.net.message.LoginRequestMessage;
import org.duo.magicallyous.net.message.LoginResponseMessage;
import org.duo.magicallyous.net.message.ServerServiceOutcomeMessage;
import org.duo.magicallyous.net.util.MagicallyousAccount;
import org.duo.magicallyous.net.util.MainServerMessageListener;

/**
 *
 * @author aluno
 */
public class MainServer extends SimpleApplication {
    private Server magicallyousServer;
    private int port;

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
            magicallyousServer.addMessageListener(new MainServerMessageListener(this), 
                    AccountRegisterMessage.class, 
                    MagicallyousAccount.class, 
                    LoginRequestMessage.class);
            magicallyousServer.start();
            System.out.println("MagicallyousServer started on port " + port);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void simpleInitApp() {
    }
    
    public static void main(String[] args) {
        MainServer app = new MainServer();
        app.start(JmeContext.Type.Headless);
    }

    public Server getMagicallyousServer() {
        return magicallyousServer;
    }
}
