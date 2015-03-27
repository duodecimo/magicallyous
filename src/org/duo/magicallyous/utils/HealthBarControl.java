/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.utils;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Quad;
import java.io.IOException;

/**
 *
 * @author duo
 */
public class HealthBarControl extends AbstractControl {

    private SimpleApplication app;
    private Geometry healthBar;
    private Geometry healthBarBg;
    private Quaternion lookRotation;
    private Vector3f aim;
    private Vector3f dist;

    public HealthBarControl(SimpleApplication app, Spatial spatialPointer) {
        this.app = app;
        healthBar = new Geometry("healthbar", new Quad(5.0f, 2.0f));
        Material material = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Red);
        healthBar.setMaterial(material);
        //healthBar.setQueueBucket(RenderQueue.Bucket.Translucent);
        healthBar.setLocalTranslation(spatialPointer.getWorldTranslation());
        aim = this.app.getCamera().getLocation();
        dist = aim.subtract(healthBar.getWorldTranslation());
        lookRotation = new Quaternion();
        lookRotation.lookAt(dist, Vector3f.UNIT_Y);
        healthBar.setLocalRotation(lookRotation);
        healthBarBg = new Geometry("healthbarbg", new Quad(5.2f, 2.14f));
        Material materialBg = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        materialBg.setColor("Color", ColorRGBA.Gray);
        healthBarBg.setMaterial(materialBg);
        //healthBarBg.setQueueBucket(RenderQueue.Bucket.Translucent);
        healthBarBg.setLocalTranslation(spatialPointer.getWorldTranslation());
        healthBarBg.setLocalRotation(lookRotation);
        ((Node) spatialPointer).attachChild(healthBar);
        ((Node) spatialPointer).attachChild(healthBarBg);
        spatialPointer.setUserData("health", 100);
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (app.getCamera() != null) {
            healthBar.setLocalTranslation(spatial.getWorldTranslation());
            healthBarBg.setLocalTranslation(spatial.getWorldTranslation());
            aim = this.app.getCamera().getLocation();
            dist = aim.subtract(healthBar.getWorldTranslation());
            lookRotation = new Quaternion();
            lookRotation.lookAt(dist, Vector3f.UNIT_Y);
            healthBar.setLocalRotation(lookRotation);
            healthBarBg.setLocalRotation(lookRotation);
            System.out.println("Spatial    position: " + spatial.getWorldTranslation());
            System.out.println("Health bar position: " + healthBar.getLocalTranslation());
        } else {
            System.out.println("NULL camera in update!");
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        HealthBarControl control = new HealthBarControl(app, spatial);
        return control;
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule in = im.getCapsule(this);
        //TODO: load properties of this Control, e.g.
        //this.value = in.readFloat("name", defaultValue);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule out = ex.getCapsule(this);
        //TODO: save properties of this Control, e.g.
        //out.write(this.value, "name", defaultValue);
    }
}
