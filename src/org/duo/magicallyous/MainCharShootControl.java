/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous;

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
 * Controls one shoot
 */
public class MainCharShootControl extends AbstractControl {
    private Spatial target;
    Node handNode;
    Spatial shoot;
    Vector3f initialPosition;
    private final Quaternion lookRotation = new Quaternion();

    @Override
    protected void controlUpdate(float tpf) {
        for(Node node : ((Node) spatial).descendantMatches(Node.class)) {
            switch (node.getName()) {
                case "hand.R_attachnode":
                    handNode = node;
                    break;
            }
        }
        if (handNode != null && target != null) {
            if (shoot == null) {
                shoot = (Spatial) spatial.getUserData("shoot");
                ((Node) (spatial.getParent())).attachChild(shoot);
                shoot.setLocalTranslation(handNode.getWorldTranslation());
                System.out.println("Shoot started at: " + shoot.getLocalTranslation());
            }
            if (shoot != null) {
                Vector3f aim = new Vector3f(target.getWorldTranslation());
                Vector3f dist = aim.subtract(shoot.getWorldTranslation());
                System.out.println("shoot is at: " + shoot.getLocalTranslation() 
                        + " aim to: " + aim + " distance: " + dist.length());
                if(dist.length() < 0.05f) {
                    System.out.println("shoot ended at distance: " + dist.length());
                    ((Node) (spatial.getParent())).detachChild(shoot);
                    spatial.removeControl(this);
                } else {
                    dist.normalizeLocal();
                    lookRotation.lookAt(dist, Vector3f.UNIT_Y);
                    shoot.setLocalRotation(lookRotation);
                    shoot.move(dist.multLocal(0.5f * tpf));
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
        MainCharShootControl control = new MainCharShootControl();
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
