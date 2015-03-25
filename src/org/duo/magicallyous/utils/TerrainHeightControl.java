/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.utils;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Ray;
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
 */
public class TerrainHeightControl extends AbstractControl {
    //Any local variables should be encapsulated by getters/setters so they
    //appear in the SDK properties window and can be edited.
    //Right-click a local variable to encapsulate it with getters and setters.
    private final Ray ray = new Ray(Vector3f.ZERO.clone(), new Vector3f(0, -1, 0));
    private final Vector3f up = new Vector3f(0.0f, 50.0f, 0.0f);
    private final float offset = 0.1f;
    private final CollisionResults collisionResults = new CollisionResults();
    private Spatial terrain;

    @Override
    protected void controlUpdate(float tpf) {
        terrain = ((Node) spatial.getParent()).getChild("terrain-freeGame01Scene");
        if(terrain != null) {
            ray.setOrigin(spatial.getWorldTranslation().add(up));
            ray.setLimit(200.0f);
            collisionResults.clear();
            terrain.collideWith(ray, collisionResults);
            for(CollisionResult collisionResult : collisionResults) {
                Vector3f collisionLocal = collisionResult.getContactPoint();
                spatial.setLocalTranslation(spatial.getLocalTranslation().setY(collisionLocal.getY() + offset));
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
        TerrainHeightControl control = new TerrainHeightControl();
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
}
