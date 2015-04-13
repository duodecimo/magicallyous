/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.net;

import com.jme3.app.SimpleApplication;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.duo.magicallyous.net.util.GameMessage;
import org.duo.magicallyous.net.util.ServerListener;

/**
 *
 * @author aluno
 */
public class MainServer extends SimpleApplication {
    private Server magicallyousServer;
    private int port;

    @Override
    public void simpleInitApp() {
        port = 5465;
        try {
            magicallyousServer = Network.createServer(port);
            magicallyousServer.start();
            Serializer.registerClass(GameMessage.class);
            magicallyousServer.addMessageListener(new ServerListener(), GameMessage.class);
            System.out.println("MagicallyousServer started on port " + port);
        } catch (IOException ex) {
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        MainServer app = new MainServer();
        app.start(JmeContext.Type.Headless);
    }

    public Server getMagicallyousServer() {
        return magicallyousServer;
    }

    public int getPort() {
        return port;
    }
}
