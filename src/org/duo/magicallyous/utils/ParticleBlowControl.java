/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.utils;

import com.jme3.app.SimpleApplication;
import org.duo.magicallyous.player.*;
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
 * Controls one shoot1
 */
public class ParticleBlowControl extends AbstractControl {
    SimpleApplication app;
    private Node target;
    private Node origin, targetHitNode;
    Vector3f initialPosition;
    private final Quaternion lookRotation = new Quaternion();
    private Geometry shoot;
    private boolean shootStart = false;

    public ParticleBlowControl(SimpleApplication app, Spatial target) {
        super();
        this.app = app;
        this.target = (Node) target;
        System.out.println("particle system started!");
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (spatial != null) {
            for (Node node : ((Node) spatial).descendantMatches(Node.class)) {
                if (node != null && node.getName() != null) {
                    switch (node.getName()) {
                        case "hitNode":
                            origin = node;
                            System.out.println("hitNode found into spatial node!");
                            break;
                    }
                }
            }
            for (Node node : ((Node) target).descendantMatches(Node.class)) {
                if (node != null && node.getName() != null) {
                    switch (node.getName()) {
                        case "hitNode":
                            targetHitNode = node;
                            System.out.println("targetHitNode found into target node!");
                            break;
                    }
                }
            }
            if (origin == null) {
                origin = (Node) spatial;
            }
            if (targetHitNode == null) {
                targetHitNode = (Node) target;
            }
            if(!shootStart) {
                shootStart = true;
                getShoot("waterMagic");
                app.getRootNode().attachChild(shoot);
                shoot.setLocalTranslation(origin.getWorldTranslation());
            }
            if (origin != null && targetHitNode != null) {
                Vector3f aim = new Vector3f(targetHitNode.getWorldTranslation());
                Vector3f dist = aim.subtract(shoot.getWorldTranslation());
                if (dist.length() < 0.02f) {
                    app.getRootNode().detachChild(shoot);
                    System.out.println("particle ended at distance: " + dist.length());
                    int damage = spatial.getUserData("damage");
                    int health = target.getUserData("health");
                    health -= damage;
                    if (health < 0) {
                        health = 0;
                    }
                    target.setUserData("health", health);
                    spatial.removeControl(this);
                    shoot = null;
                } else {
                    dist.normalizeLocal();
                    lookRotation.lookAt(dist, Vector3f.UNIT_Y);
                    shoot.setLocalRotation(lookRotation);
                    shoot.move(dist.multLocal(4.0f * tpf));
                    System.out.println("particle at: " + shoot.getWorldTranslation()
                            + " distance: " + dist.length());
                }
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }
    
    @Override
    public Control cloneForSpatial(Spatial spatial) {
        PlayerShootControl control = new PlayerShootControl();
        //TODO: copy parameters to new Control
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

    private Spatial getShoot(String shootType) {
        Sphere sphere = new Sphere(16, 16, 0.1f);
        shoot = new Geometry("shoot", sphere);
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
}
