/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.npc;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.List;
import org.duo.magicallyous.TerrainHeightControl;

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
        //BulletAppState bulletAppState = app.getStateManager().getState(BulletAppState.class);
        Node scene = (Node) ((SimpleApplication) app).getRootNode().getChild("Scene01");
        NpcMovementControl npcMovementControl;
        // atach spiders
        spiders = new ArrayList<>();
        int x = -50;
        int z = 50;
        Vector3f vector3f;
        Node spider;
        for(int i=0; i<21; i++) {
            spider = (Node) app.getAssetManager().loadModel("Models/spider.j3o");
            if (i<20) {
                spider.scale(0.2f);
            }
            //BetterCharacterControl betterCharacterControl = new BetterCharacterControl(0.5f, 0.5f, 20.0f);
            //spider.addControl(betterCharacterControl);
            //bulletAppState.getPhysicsSpace().add(betterCharacterControl);
            //bulletAppState.getPhysicsSpace().addAll(spider);
            npcMovementControl = new NpcMovementControl();
            npcMovementControl.setNpcTerrainBounds(-30.0f, -250.0f, 250.0f, 30.0f);
            npcMovementControl.setNpcTerrainCenter(-100.0f, 80.0f);
            if(i==0) {
                npcMovementControl.setDebugPosition(true);
            }
            ((Node) scene).attachChild(spider);
            vector3f = new Vector3f(x, 0.0f, z);
            //betterCharacterControl.warp(vector3f);
            spider.setLocalTranslation(vector3f);
            //spider.move(vector3f);
            /*            if(i==0) {
             * npcMovementControl.setDebugPosition(true);
             * spider.setLocalTranslation(-28.0f, 0.0f, 28.0f);
             * }*/
            spider.addControl(npcMovementControl);
            npcMovementControl.setActive(true);
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
