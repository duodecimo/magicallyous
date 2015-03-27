/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.player;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import org.duo.magicallyous.utils.TerrainHeightControl;

/**
 *
 * @author duo
 */
public class PlayerState extends AbstractAppState {
    private SimpleApplication app;
    private ChaseCamera chaseCamera;
    private Node player;
    private Geometry healthbar;
    private Spatial barrel;
    private double timeCounter = 0.0d;
    private double timeEvent = 0.0d;
    BitmapText hudText;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        Node scene = (Node) this.app.getRootNode().getChild("Scene01");
        player = (Node) app.getAssetManager().loadModel("Models/kelum.j3o");
        player.setName("player");
        player.setUserData("fireMagic", getShoot("fireMagic"));
        player.setUserData("waterMagic", getShoot("waterMagic"));
        player.setUserData("earthMagic", getShoot("earthMagic"));
        player.setUserData("name", "Astofoboldo");
        player.setUserData("health", 100);
        player.addControl(new PlayerControl());
        player.addControl(new TerrainHeightControl());
        player.move(0.0f, 0.0f, 0.0f);
        // add healthbar
        BillboardControl billboard = new BillboardControl();
        healthbar = new Geometry("healthbar", new Quad(2.0f, 0.1f));
        Material material = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Red);
        healthbar.setMaterial(material);
        healthbar.setQueueBucket(RenderQueue.Bucket.Translucent);
        player.attachChild(healthbar);
        //this.app.getGuiNode().attachChild(healthbar);
        healthbar.center();
        Vector3f camPos = new Vector3f();
        this.app.getCamera().getScreenCoordinates(camPos.add(player.getLocalTranslation()));
        healthbar.move(camPos.x, camPos.y, 0.0f);
        billboard.setAlignment(BillboardControl.Alignment.Screen);
        healthbar.setBatchHint(Spatial.BatchHint.Never);
        //float x = player.getWorldTranslation().x;
        //float y = player.getWorldTranslation().y+2.1f;
        //float z = player.getWorldTranslation().z;
        //healthbar.setLocalTranslation(x, y, z);
        healthbar.addControl(billboard);
        
        scene.attachChild(player);
        // add a barrel
        barrel = app.getAssetManager().loadModel("Models/barrel.j3o");
        scene.attachChild(barrel);
        barrel.setLocalTranslation(0.0f,  0.0f, -6.0f);
        // start player basic key controls
        stateManager.attach(new PlayerMovement());
        // start camera
        this.app.getFlyByCamera().setEnabled(false);
        chaseCamera = new ChaseCamera(this.app.getCamera(), player, this.app.getInputManager());
        chaseCamera.setSmoothMotion(true);
        // bitmap text
        BitmapFont hudFont = this.app.getAssetManager().loadFont("Interface/Fonts/LiberationSans.fnt");
        hudText = new BitmapText(hudFont, true);
        hudText.setSize(hudFont.getCharSet().getRenderedSize());
        hudText.setColor(ColorRGBA.Blue);
        hudText.setText("You can write any string here");
        hudText.setQueueBucket(RenderQueue.Bucket.Gui);
        hudText.setLocalTranslation(camPos.x, camPos.y, 0);
        System.out.println("hudText postioned in: " + hudText.getLocalTranslation());
        this.app.getGuiNode().attachChild(hudText);
        this.app.getStateManager().attach(new PlayerToneGodGuiState());
    }
    
    Spatial getShoot(String shootType) {
        Sphere sphere = new Sphere(16, 16, 0.2f);
        Geometry shoot = new Geometry("shoot", sphere);
        Material material = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        if(shootType.compareTo("fireMagic") == 0) {
            material.setColor("Color", ColorRGBA.Yellow);
        } else if(shootType.compareTo("waterMagic") == 0) {
            material.setColor("Color", ColorRGBA.Blue);
        } else {
            material.setColor("Color", ColorRGBA.Green);
        }
        shoot.setMaterial(material);
        shoot.setName("shoot");
        return shoot;
    }

    @Override
    public void update(float tpf) {
        timeCounter += tpf;
        int health = player.getUserData("health");
        if(timeCounter - timeEvent > 3.0d) {
            health -=1;
            player.setUserData("health", health);
            timeEvent = timeCounter;
        }
        ((Quad) healthbar.getMesh()).updateGeometry(((float) health / 100) * 2.0f, 0.1f);
        Vector3f camPos = this.app.getCamera().getScreenCoordinates(player.getWorldTranslation());
        //System.out.println("screen position: " + camPos);
        healthbar.move(camPos.x, camPos.y, 0.0f);
        hudText.setLocalTranslation(camPos.x -100, camPos.y , 0);
        //System.out.println("hudText postioned in: " + hudText.getLocalTranslation());
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
}
