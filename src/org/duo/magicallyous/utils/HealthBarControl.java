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
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Sphere;
import java.io.IOException;

/**
 *
 * @author duo
 */
public class HealthBarControl extends AbstractControl {

    private SimpleApplication app;
    private Node healthSphereNode;

    public HealthBarControl(SimpleApplication app, Spatial spatial) {
        this.app = app;
        float spatialHeight = ((BoundingBox) spatial.getWorldBound()).getYExtent() * 3.0f + (10.0f - (spatial.getLocalScale().y) * 10.0f);
        float spatialWidth  = 0.2f + (10.0f - (spatial.getLocalScale().x) * 10.0f)/4;
        System.out.println("spatial height = " + spatialHeight + " widht = " + spatialWidth + 
                " scale = " + spatial.getLocalScale());
        Sphere health = new Sphere(16, 16, spatialWidth + 0.1f);
        Sphere healthBg = new Sphere(16, 16, spatialWidth + 0.2f);
        Geometry healthSphere = new  Geometry("healthSphere", health);
        Geometry healthSphereBg = new Geometry("healthSphereBg", healthBg);
        Material material = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        Material materialBg = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", new ColorRGBA(1.0f, 0.0f, 0.0f, 0.8f));
        materialBg.setColor("Color", new ColorRGBA(0.5f, 0.5f, 0.5f, 0.5f));
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        materialBg.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        healthSphere.setMaterial(material);
        healthSphereBg.setMaterial(materialBg);
        healthSphere.setQueueBucket(RenderQueue.Bucket.Transparent);
        healthSphereBg.setQueueBucket(RenderQueue.Bucket.Transparent);
        healthSphereNode = new Node("healthSphereNode");
        healthSphereNode.attachChild(healthSphere);
        healthSphereNode.attachChild(healthSphereBg);
        ((Node) spatial).attachChild(healthSphereNode);
        // spatial height
        healthSphereNode.move(0.0f, spatialHeight , 0.0f);
        spatial.setUserData("health", 100);
    }

    @Override
    protected void controlUpdate(float tpf) {
        Spatial healthSphere = healthSphereNode.getChild("healthSphere");
        int health = spatial.getUserData("health");
        float scaleFactor = (float) health/100;
        healthSphere.setLocalScale(scaleFactor);
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
