/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.utils;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author duo
 */
public class CharacterMovementControl extends BetterCharacterControl {
    private boolean moveFoward;
    private boolean moveBackward;
    private boolean rotateRight;
    private boolean rotateLeft;
    private boolean rightStrafe;
    private boolean leftStrafe;
    private float moveSpeed;
    private ActionStateEnum actionState;

    public boolean isMoveFoward() {
        return moveFoward;
    }

    public void setMoveFoward(boolean moveFoward) {
        this.moveFoward = moveFoward;
    }

    public boolean isMoveBackward() {
        return moveBackward;
    }

    public void setMoveBackward(boolean moveBackward) {
        this.moveBackward = moveBackward;
    }

    public boolean isRotateRight() {
        return rotateRight;
    }

    public void setRotateRight(boolean rotateRight) {
        this.rotateRight = rotateRight;
    }

    public boolean isRotateLeft() {
        return rotateLeft;
    }

    public void setRotateLeft(boolean rotateLeft) {
        this.rotateLeft = rotateLeft;
    }

    public boolean isRightStrafe() {
        return rightStrafe;
    }

    public void setRightStrafe(boolean rightStrafe) {
        this.rightStrafe = rightStrafe;
    }

    public boolean isLeftStrafe() {
        return leftStrafe;
    }

    public void setLeftStrafe(boolean leftStrafe) {
        this.leftStrafe = leftStrafe;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public ActionStateEnum getActionState() {
        return actionState;
    }

    public void setActionState(ActionStateEnum actionState) {
        this.actionState = actionState;
    }

    public Vector3f getSpatialFowardDir() {
        return spatial.getWorldRotation().mult(Vector3f.UNIT_Z);
    }

    public Vector3f getSpatialLeftDir() {
        return spatial.getWorldRotation().mult(Vector3f.UNIT_X);
    }

    public void walk() {
        walkDirection.set(Vector3f.ZERO);
        if(isMoveFoward()) {
            walkDirection.addLocal(getSpatialFowardDir().mult(getMoveSpeed()));
        } else if(isMoveBackward()) {
            walkDirection.addLocal(getSpatialFowardDir().negate().mult(getMoveSpeed()));
        } else if(isRightStrafe()) {
            walkDirection.addLocal(getSpatialLeftDir().negate().mult(getMoveSpeed()));
        } else if(isLeftStrafe()) {
            walkDirection.addLocal(getSpatialLeftDir().mult(getMoveSpeed()));
        }
        setWalkDirection(walkDirection);
    }

    public void rotate(float value, float tpf, float sensitivity) {
        float rotateBase = 0.0f;
        if(isRotateRight()) {
            rotateBase = -1.0f;
        } else if(isRotateLeft()) {
            rotateBase = 1.0f;
        }
        Quaternion quaternion = new Quaternion().fromAngleAxis(FastMath.PI * 
                (tpf * value * sensitivity * rotateBase), Vector3f.UNIT_Y);
        quaternion.multLocal(viewDirection);
        setViewDirection(viewDirection);
    }
}
