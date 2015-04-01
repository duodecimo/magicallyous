/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.npc;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.List;
import org.duo.magicallyous.utils.HealthBarControl;
import org.duo.magicallyous.utils.TerrainHeightControl;

/**
 *
 * @author duo
 */
public class NpcAppState extends AbstractAppState {
    SimpleApplication app;
    List<Node> spiders;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
        this.app = (SimpleApplication) app;
        Node scene = (Node) ((SimpleApplication) app).getRootNode().getChild("Scene01");
        Node player = (Node) scene.getChild("player");
        if(player == null) {
            System.err.println("player is NULL, ending game!");
            System.exit(1);
        }
        NpcMotionControl npcMovementControl;
        // atach spiders
        spiders = new ArrayList<>();
        int x = -50;
        int z = 50;
        Vector3f vector3f;
        Vector2f vector2f;
        Node spider;
        for(int i=0; i<21; i++) {
            spider  = (Node) app.getAssetManager().loadModel("Models/spider.j3o");
            if (i<20) {
                spider.scale(0.2f);
                spider.setUserData("damage", 8);
                spider.setUserData("defense", 5);
            } else {
                spider.setUserData("damage", 20);
                spider.setUserData("defense", 10);
            }
            spider.setUserData("health", 100);
            npcMovementControl = new NpcMotionControl(this.app);
            npcMovementControl.setNpcTerrainBounds(-30.0f, -250.0f, 250.0f, 30.0f);
            npcMovementControl.setNpcTerrainCenter(-100.0f, 80.0f);
            /*if(i==0) {
             * npcMovementControl.setDebugPosition(true);
             * }*/
            ((Node) scene).attachChild(spider);
            if(player != null) {
                npcMovementControl.setMainChar(player);
            }
            vector3f = new Vector3f(x, 0.0f, z);
            spider.addControl(npcMovementControl);
            vector2f = npcMovementControl.getSpawnLocation();
            //System.out.println("spider spawn location = " + vector2f);
            if (vector2f.x < -30.0f && vector2f.y < 250.0f)  {
                vector3f.x = vector2f.x;
                vector3f.z = vector2f.y;
            }
            spider.setLocalTranslation(vector3f);
            npcMovementControl.setActive(true);
            spider.addControl(new HealthBarControl(this.app, spider));
            spider.addControl(new TerrainHeightControl());
            spiders.add(spider);
            x-=3;
            z+=3;
        }
    }
    
    @Override
    public void update(float tpf) {
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
}
