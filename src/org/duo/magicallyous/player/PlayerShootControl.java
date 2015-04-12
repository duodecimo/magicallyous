/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.player;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;

/**
 *
 * @author duo
 * Controls one shoot1
 */
public class PlayerShootControl extends AbstractControl {
    private Spatial target;
    Node rightHandNode;
    Node leftHandNode;
    Node targetHitNode;
    Spatial shoot1, shoot2;
    Vector3f initialPosition;
    private final Quaternion rightLookRotation = new Quaternion();
    private final Quaternion leftLookRotation = new Quaternion();

    @Override
    protected void controlUpdate(float tpf) {
        for(Node node : ((Node) spatial).descendantMatches(Node.class)) {
            switch (node.getName()) {
                case "hand.R_attachnode":
                    rightHandNode = node;
                    break;
                case "hand.L_attachnode":
                    leftHandNode = node;
                    break;
            }
        }
        if (target != null) {
            for (Node node : ((Node) target).descendantMatches(Node.class)) {
                if (node != null && node.getName() != null) {
                    switch (node.getName()) {
                        case "hitNode":
                            targetHitNode = node;
                            break;
                    }
                }
            }
        }
        if(targetHitNode == null) {
            targetHitNode = ((Node) target);
        }
        if (rightHandNode != null && target != null) {
            if (shoot1 == null) {
                shoot1 = ((Spatial) spatial.getUserData("earthMagic")).clone();
                shoot1.scale(0.2f);
                ((Node) (spatial.getParent())).attachChild(shoot1);
                shoot1.setLocalTranslation(rightHandNode.getWorldTranslation());
                shoot2 = ((Spatial) spatial.getUserData("waterMagic")).clone();
                shoot2.scale(0.2f);
                ((Node) (spatial.getParent())).attachChild(shoot2);
                shoot2.setLocalTranslation(leftHandNode.getWorldTranslation());
                //System.out.println("Shoot started at: " + shoot1.getLocalTranslation());
            }
            if (shoot1 != null) {
                Vector3f aim = new Vector3f(targetHitNode.getWorldTranslation());
                Vector3f rightDist = aim.subtract(shoot1.getWorldTranslation());
                Vector3f leftDist = aim.subtract(shoot2.getWorldTranslation());
                //System.out.println("shoot1 is at: " + shoot1.getLocalTranslation() 
                //        + " aim to: " + aim + " distance: " + dist.length());
                if(rightDist.length() < 0.05f) {
                    //System.out.println("shoot1 ended at distance: " + dist.length());
                    ((Node) (spatial.getParent())).detachChild(shoot1);
                    ((Node) (spatial.getParent())).detachChild(shoot2);
                    int damage = spatial.getUserData("damage");
                    int defense = target.getUserData("defense");
                    int health = target.getUserData("health");
                    health -= (damage - defense);
                    if(health < 0) {
                        health = 0;
                    }
                    target.setUserData("health", health);
                    spatial.removeControl(this);
                } else {
                    rightDist.normalizeLocal();
                    leftDist.normalizeLocal();
                    rightLookRotation.lookAt(rightDist, Vector3f.UNIT_Y);
                    leftLookRotation.lookAt(leftDist, Vector3f.UNIT_Y);
                    shoot1.setLocalRotation(rightLookRotation);
                    shoot1.move(rightDist.multLocal(4.0f * tpf));
                    shoot2.setLocalRotation(leftLookRotation);
                    shoot2.move(leftDist.multLocal(4.0f * tpf));
                }
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
         if (spatial != null) {
            if (shoot1 != null) {
                ((Node) (spatial.getParent())).detachChild(shoot1);
            }
            if (shoot2 != null) {
                ((Node) (spatial.getParent())).detachChild(shoot2);
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

    public void setTarget(Spatial target) {
        this.target = target;
    }
}
