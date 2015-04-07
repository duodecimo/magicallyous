/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.utils;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import org.duo.magicallyous.player.PlayerShootControl;

/**
 *
 * @author duo
 */
public class CharacterMovementControl extends BetterCharacterControl implements AnimEventListener {
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
    private AnimControl animControl;
    private AnimChannel animChannel;
    private double timeCounter = 0d;
    private double attackTime = 0d;
    private double deathTime = 0d;
    private boolean startAttack = false;
    private boolean waitingForCast = false;
    private boolean waitingForPrecast = false;
    private Spatial target;
    private Quaternion lookRotation;
    private boolean increaseHealth;
    private boolean decreaseHealth;

    public CharacterMovementControl() {
    }

    public CharacterMovementControl(float radius, float height, float mass) {
        super(radius, height, mass);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        timeCounter += tpf;
        if (checkControl()) {
            if (animationStateEnum != AnimationStateEnum.BATTLE && 
                    animationStateEnum != AnimationStateEnum.DIE) {
                // decide movement state
                if (isMoveFoward() || isMoveBackward()) {
                    if (ableToRun && running) {
                        animationStateEnum = AnimationStateEnum.RUN;
                    } else {
                        animationStateEnum = AnimationStateEnum.WALK;
                    }
                } else {
                    animationStateEnum = AnimationStateEnum.IDLE;
                }
                if (isStopped()) {
                    animationStateEnum = AnimationStateEnum.IDLE;
                }
                // move character
                walk();
                rotate();
            }
            // change health
            if(increaseHealth) {
                increaseHealth = false;
                int health = spatial.getUserData("health");
                health += 5;
                spatial.setUserData("health", health);
                System.out.println("Player increased health = " + health);
                
            } else if(decreaseHealth) {
                decreaseHealth = false;
                int health = spatial.getUserData("health");
                health -= 5;
                spatial.setUserData("health", health);
                System.out.println("Player decreased health = " + health);
            }
            // animate character
            if (animationStateEnum == AnimationStateEnum.IDLE) {
                if (animChannel.getAnimationName().compareTo("Idle") != 0) {
                    animChannel.setAnim("Idle");
                    animChannel.setSpeed(0.2f);
                    animChannel.setLoopMode(LoopMode.Loop);
                }
            } else if (animationStateEnum == AnimationStateEnum.WALK) {
                if (animChannel.getAnimationName().compareTo("Walk") != 0) {
                    animChannel.setAnim("Walk");
                    animChannel.setSpeed(1.0f);
                    animChannel.setLoopMode(LoopMode.Loop);
                }
            } else if (animationStateEnum == AnimationStateEnum.RUN) {
                if (animChannel.getAnimationName().compareTo("Run") != 0) {
                    animChannel.setAnim("Run");
                    animChannel.setSpeed(1.0f);
                    animChannel.setLoopMode(LoopMode.Loop);
                }
            } else if (animationStateEnum == AnimationStateEnum.BATTLE) {
                if (startAttack) {
                    startAttack = false;
                    clearMovements();
                    // stop if moving
                    setWalkDirection(Vector3f.ZERO);
                    // face target
                    if (target != null) {
                        Vector3f aim = target.getWorldTranslation();
                        Vector3f dist = aim.subtract(spatial.getWorldTranslation());
                        lookRotation = new Quaternion();
                        lookRotation.lookAt(dist, Vector3f.UNIT_Y);
                        setViewDirection(lookRotation.getRotationColumn(1).negate());
                        System.out.println("Player starting fight with target at: "
                                + target.getWorldTranslation() + " distance: "
                                + dist.length());
                    } else {
                        System.out.println("Starting fight NULL target !!!!!");
                    }
                    // change anim
                    if (animChannel.getAnimationName().compareTo("Precast") != 0) {
                        animChannel.setAnim("Precast");
                        animChannel.setSpeed(1.0f);
                        animChannel.setLoopMode(LoopMode.DontLoop);
                    }
                } else { // continuing attack
                    // perform animations according to time
                    if (waitingForCast) {
                        //System.out.println("Waiting for cast from: " +  
                        //        attackTimer + " now: " + timeCounter + 
                        //        " difference: " + (timeCounter - attackTimer));
                        if (timeCounter - attackTime > 0.5d) {
                            waitingForCast = false;
                            animChannel.setAnim("Cast");
                            animChannel.setSpeed(1.0f);
                            animChannel.setLoopMode(LoopMode.DontLoop);
                        }
                    } else if (waitingForPrecast) {
                        if (timeCounter - attackTime > 0.8d) {
                            waitingForPrecast = false;
                            animChannel.setAnim("Precast");
                            animChannel.setSpeed(1.0f);
                            animChannel.setLoopMode(LoopMode.DontLoop);
                        }
                    }
                }
            } else if (animationStateEnum == AnimationStateEnum.DIE) {
                if (animChannel.getAnimationName().compareTo("Die") != 0) {
                    animChannel.setAnim("Die");
                    animChannel.setSpeed(0.5f);
                    animChannel.setLoopMode(LoopMode.DontLoop);
                    System.out.println("Player dying animation started!");
                    deathTime = 0.0d;
                } else if (deathTime > 0 && timeCounter - deathTime > 3.0d) {
                    // respawn
                    spatial.setUserData("health", 100);
                    this.warp(Vector3f.ZERO);
                    System.out.println("Player revived on " + spatial.getLocalTranslation());
                    animationStateEnum = AnimationStateEnum.IDLE;
                }
            }
        }
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

    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if(animName.compareTo("Precast") == 0) {
            //System.out.println("Start waiting for cast!");
            waitingForCast = true;
            waitingForPrecast = false;
            PlayerShootControl playerShootControl = new PlayerShootControl();
            playerShootControl.setTarget(target);
            spatial.addControl(playerShootControl);
            attackTime = timeCounter;
        } else if(animName.compareTo("Cast")==0) {
            waitingForPrecast = true;
            waitingForCast = false;
            attackTime = timeCounter;
        } else if(animName.compareTo("Die")==0) {
            deathTime = timeCounter;
        }
    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }

    protected boolean checkControl() {
        AnimControl control = spatial.getControl(AnimControl.class);
        if(control != animControl) {
            animControl = control;
            animControl.addListener(this);
            animChannel = animControl.createChannel();
            animChannel.setAnim("Idle");
            animChannel.setLoopMode(LoopMode.Loop);
        }
        return animControl != null;
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

    public AnimationStateEnum getAnimationStateEnum() {
        return animationStateEnum;
    }

    public void setAnimationStateEnum(AnimationStateEnum animationStateEnum) {
        this.animationStateEnum = animationStateEnum;
    }

    public Vector3f getSpatialFowardDir() {
        return spatial.getWorldRotation().mult(Vector3f.UNIT_Z);
    }

    public Vector3f getSpatialLeftDir() {
        return spatial.getWorldRotation().mult(Vector3f.UNIT_X);
    }

    public void setTarget(Spatial target) {
        this.target = target;
    }

    public void setStartAttack(boolean startAttack) {
        this.startAttack = startAttack;
    }

    private void clearMovements() {
        moveFoward = false;
        moveBackward = false;
        stopped = false;
        rotateLeft = false;
        rotateRight = false;
        rightStrafe = false;
        leftStrafe = false;
        running = false;
    }

    public void setIncreaseHealth(boolean increaseHealth) {
        this.increaseHealth = increaseHealth;
    }

    public void setDecreaseHealth(boolean decreaseHealth) {
        this.decreaseHealth = decreaseHealth;
    }
}
