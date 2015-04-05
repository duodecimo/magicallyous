/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.utils;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import org.duo.magicallyous.player.PlayerAnimationControl;

/**
 *
 * @author duo
 */
public class CharacterMovementControl extends BetterCharacterControl {
    private boolean moveFoward;
    private boolean moveBackward;
    private boolean stopped;
    private boolean rotateRight;
    private boolean rotateLeft;
    private boolean rightStrafe;
    private boolean leftStrafe;
    private boolean running;
    private boolean ableToRun;
    private float moveSpeed;
    private float runSpeed;
    private float rotateValue;
    private AnimationStateEnum animationStateEnum;
    private PlayerAnimationControl  playerAnimationControl;

    public CharacterMovementControl() {
    }

    public CharacterMovementControl(float radius, float height, float mass) {
        super(radius, height, mass);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if (playerAnimationControl == null) {
            playerAnimationControl = spatial.getControl(PlayerAnimationControl.class);
        }
        if (animationStateEnum != AnimationStateEnum.BATTLE) {
            if (isMoveFoward() || isMoveBackward()) {
                animationStateEnum = AnimationStateEnum.WALK;
            } else {
                animationStateEnum = AnimationStateEnum.IDLE;
            }
            if (isStopped()) {
                animationStateEnum = AnimationStateEnum.IDLE;
            }
            walk();
            rotate();
            if (animationStateEnum == AnimationStateEnum.WALK) {
                if (ableToRun && running) {
                    playerAnimationControl.setAnimationStateEnum(AnimationStateEnum.RUN);
                } else {
                    playerAnimationControl.setAnimationStateEnum(AnimationStateEnum.WALK);
                }
            } else if (animationStateEnum == AnimationStateEnum.IDLE) {
                playerAnimationControl.setAnimationStateEnum(AnimationStateEnum.IDLE);
            }
        }
    }

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

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
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

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public void setRunSpeed(float runSpeed) {
        this.runSpeed = runSpeed;
    }

    public void toggleRunning() {
        this.running = !running;
    }

    public void setAbleToRun(boolean ableToRun) {
        this.ableToRun = ableToRun;
    }

    public void setRotateValue(float rotateValue) {
        this.rotateValue = rotateValue;
    }

    public AnimationStateEnum getActionState() {
        return animationStateEnum;
    }

    public void setActionState(AnimationStateEnum actionState) {
        this.animationStateEnum = actionState;
    }

    public Vector3f getSpatialFowardDir() {
        return spatial.getWorldRotation().mult(Vector3f.UNIT_Z);
    }

    public Vector3f getSpatialLeftDir() {
        return spatial.getWorldRotation().mult(Vector3f.UNIT_X);
    }

    public void walk() {
        walkDirection.set(Vector3f.ZERO);
        float speed;
        if(ableToRun && running) {
            speed = runSpeed;
        } else {
            speed = moveSpeed;
        }
        if(isMoveFoward()) {
            walkDirection.addLocal(getSpatialFowardDir().mult(speed));
        } else if(isMoveBackward()) {
            walkDirection.addLocal(getSpatialFowardDir().negate().mult(speed));
        } else if(isRightStrafe()) {
            walkDirection.addLocal(getSpatialLeftDir().negate().mult(speed));
        } else if(isLeftStrafe()) {
            walkDirection.addLocal(getSpatialLeftDir().mult(speed));
        }
        setWalkDirection(walkDirection);
    }

    public void rotate() {
        float rotateBase = 0.0f;
        if(isRotateRight()) {
            rotateBase = -1.0f;
        } else if(isRotateLeft()) {
            rotateBase = 1.0f;
        }
        Quaternion quaternion = new Quaternion().fromAngleAxis(FastMath.PI * 
                (rotateValue * rotateBase), Vector3f.UNIT_Y);
        quaternion.multLocal(viewDirection);
        setViewDirection(viewDirection);
    }
}
