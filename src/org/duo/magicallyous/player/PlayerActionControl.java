/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.player;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import org.duo.magicallyous.utils.AnimationStateEnum;
import org.duo.magicallyous.utils.GeneralStateEnum;
import org.duo.magicallyous.utils.MagicallyousApp;

/**
 *
 * @author duo
 */
public class PlayerActionControl extends BetterCharacterControl implements AnimEventListener {
    private MagicallyousApp app;
    private GeneralStateEnum generalStateEnum;
    private AnimationStateEnum animationStateEnum;
    private AnimControl animControl;
    private AnimChannel animChannel;
    private double timeCounter = 0d;
    private PlayerShootControl playerShootControl;

    private boolean moveFoward;
    private boolean moveBackward;
    private boolean stopped;
    private boolean rotateRight;
    private boolean rotateLeft;
    private boolean rightStrafe;
    private boolean leftStrafe;
    private boolean ableToRun;
    private boolean running;
    private float runSpeed;
    private float moveSpeed;
    private float rotateValue;
    private double attackTime = 0d;
    private double deathTime = 0d;
    private boolean startAttack = false;
    private boolean waitingForCast = false;
    private boolean waitingForPrecast = false;
    private Spatial target;
    private Quaternion lookRotation;
    private boolean increaseHealth;
    private boolean decreaseHealth;
    private boolean increaseDamage;
    private boolean decreaseDamage;
    private boolean increaseDefense;
    private boolean decreaseDefense;

    public PlayerActionControl() {
    }

    public PlayerActionControl(MagicallyousApp app, float radius, float height, float mass) {
        super(radius, height, mass);
        this.app = app;
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        timeCounter += tpf;
        updatePlayerSate();
        animate();
    }

    public void updatePlayerSate() {
        if (getGeneralStateEnum() == GeneralStateEnum.NORMAL) {
            // not in battle and not dead!
            if (isMoveFoward() || isMoveBackward()) {
                if (ableToRun && running) {
                    setAnimationStateEnum(AnimationStateEnum.RUN);
                } else {
                    setAnimationStateEnum(AnimationStateEnum.WALK);
                }
            } else {
                setAnimationStateEnum(AnimationStateEnum.IDLE);
            }
            if (isStopped()) {
                setAnimationStateEnum(AnimationStateEnum.IDLE);
            }
            // move character
            walk();
            rotate();
            // change health. damage &defense
            if (increaseHealth) {
                increaseHealth = false;
                int health = spatial.getUserData("health");
                health += 5;
                spatial.setUserData("health", health);
            } else if (decreaseHealth) {
                decreaseHealth = false;
                int health = spatial.getUserData("health");
                health -= 5;
                spatial.setUserData("health", health);
            }
            if (increaseDamage) {
                increaseDamage = false;
                int damage = spatial.getUserData("damage");
                damage += 1;
                spatial.setUserData("damage", damage);
            } else if (decreaseDamage) {
                decreaseDamage = false;
                int damage = spatial.getUserData("damage");
                damage -= 1;
                spatial.setUserData("damage", damage);
            }
            if (increaseDefense) {
                increaseDefense = false;
                int defense = spatial.getUserData("defense");
                defense += 1;
                spatial.setUserData("defense", defense);
            } else if (decreaseDefense) {
                decreaseDefense = false;
                int defense = spatial.getUserData("defense");
                defense -= 1;
                spatial.setUserData("defense", defense);
            }
        } else if (getGeneralStateEnum() == GeneralStateEnum.DEAD) {
            if (getAnimationStateEnum() != AnimationStateEnum.DIE) {
                removeShootControls();
                setAnimationStateEnum(AnimationStateEnum.DIE);
                System.out.println("Player dying animation started!");
                deathTime = 0.0d;
            } else if (deathTime > 0 && getTimeCounter() - deathTime > 5.0d) {
                // go to underworld
                //getApp().switchAppState(new MagicallyousAppState());
            }
        } else if (getGeneralStateEnum() == GeneralStateEnum.BATTLE) {
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
                    Vector3f targetDirection = lookRotation.getRotationColumn(1).negate();
                    turnTo(targetDirection);
                    System.out.println("Player starting fight with target at: "
                            + target.getWorldTranslation() + " distance: "
                            + dist.length());
                } else {
                    System.out.println("Starting fight NULL target !!!!!");
                }
                // change anim
                setAnimationStateEnum(AnimationStateEnum.PRECAST);
            } else { // continuing attack
                // perform animations according to time
                if (waitingForCast) {
                    //System.out.println("Waiting for cast from: " +  
                    //        attackTimer + " now: " + timeCounter + 
                    //        " difference: " + (timeCounter - attackTimer));
                    if (getTimeCounter() - attackTime > 0.3d) {
                        waitingForCast = false;
                        // change anim
                        setAnimationStateEnum(AnimationStateEnum.CAST);
                    }
                } else if (waitingForPrecast) {
                    if (getTimeCounter() - attackTime > 0.3d) {
                        waitingForPrecast = false;
                        // change anim
                        setAnimationStateEnum(AnimationStateEnum.PRECAST);
                    }
                }
            }
        }
    }

    protected final void moveTo(Vector3f walkDirection) {
        // this is where the real walk takes place
        setWalkDirection(walkDirection);
    }

    protected final void turnTo(Vector3f viewDirection){
        // this is where the real rotation takes place
        setViewDirection(viewDirection);
    }

    public void animate() {
        if (checkControl()) {
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
            } else if (animationStateEnum == AnimationStateEnum.PRECAST) {
                if (animChannel.getAnimationName().compareTo("Precast") != 0) {
                    animChannel.setAnim("Precast");
                    animChannel.setSpeed(1.0f);
                    animChannel.setLoopMode(LoopMode.DontLoop);
                }
            } else if (animationStateEnum == AnimationStateEnum.CAST) {
                if (animChannel.getAnimationName().compareTo("Cast") != 0) {
                    animChannel.setAnim("Cast");
                    animChannel.setSpeed(1.0f);
                    animChannel.setLoopMode(LoopMode.DontLoop);
                }
            } else if (animationStateEnum == AnimationStateEnum.DIE) {
                if (animChannel.getAnimationName().compareTo("Die") != 0) {
                    animChannel.setAnim("Die");
                    animChannel.setSpeed(0.5f);
                    animChannel.setLoopMode(LoopMode.DontLoop);
                }
            }
        }
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
        moveTo(walkDirection);
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
        turnTo(viewDirection);
    }

    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if(animName.compareTo("Precast") == 0) {
            //System.out.println("Start waiting for cast!");
            waitingForCast = true;
            waitingForPrecast = false;
            setPlayerShootControl(spatial.getControl(PlayerShootControl.class));
            if (getPlayerShootControl() == null && target != null) {
                setPlayerShootControl(new PlayerShootControl());
                getPlayerShootControl().setTarget(target);
                spatial.addControl(getPlayerShootControl());
                attackTime = getTimeCounter();
            }
        } else if(animName.compareTo("Cast")==0) {
            waitingForPrecast = true;
            waitingForCast = false;
            attackTime = getTimeCounter();
        } else if(animName.compareTo("Die")==0) {
            deathTime = getTimeCounter();
        }
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

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public void setRunSpeed(float runSpeed) {
        this.runSpeed = runSpeed;
    }
    public void setAbleToRun(boolean ableToRun) {
        this.ableToRun = ableToRun;
    }

    public void setRotateValue(float rotateValue) {
        this.rotateValue = rotateValue;
    }

    public void setStartAttack(boolean startAttack) {
        this.startAttack = startAttack;
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

    public void toggleRunning() {
        this.running = !running;
    }

    public void setTarget(Spatial target) {
        this.target = target;
    }

    public void setIncreaseHealth(boolean increaseHealth) {
        this.increaseHealth = increaseHealth;
    }

    public void setDecreaseHealth(boolean decreaseHealth) {
        this.decreaseHealth = decreaseHealth;
    }

    public void setIncreaseDamage(boolean increaseDamage) {
        this.increaseDamage = increaseDamage;
    }

    public void setDecreaseDamage(boolean decreaseDamage) {
        this.decreaseDamage = decreaseDamage;
    }

    public void setIncreaseDefense(boolean increaseDefense) {
        this.increaseDefense = increaseDefense;
    }

    public void setDecreaseDefense(boolean decreaseDefense) {
        this.decreaseDefense = decreaseDefense;
    }

    public double getAttackTime() {
        return attackTime;
    }

    public void setAttackTime(double attackTime) {
        this.attackTime = attackTime;
    }

    public double getDeathTime() {
        return deathTime;
    }

    public void setDeathTime(double deathTime) {
        this.deathTime = deathTime;
    }

    public boolean isWaitingForCast() {
        return waitingForCast;
    }

    public void setWaitingForCast(boolean waitingForCast) {
        this.waitingForCast = waitingForCast;
    }

    public boolean isWaitingForPrecast() {
        return waitingForPrecast;
    }

    public void setWaitingForPrecast(boolean waitingForPrecast) {
        this.waitingForPrecast = waitingForPrecast;
    }

    public Quaternion getLookRotation() {
        return lookRotation;
    }

    public void setLookRotation(Quaternion lookRotation) {
        this.lookRotation = lookRotation;
    }

    public Vector3f getSpatialFowardDir() {
        return spatial.getWorldRotation().mult(Vector3f.UNIT_Z);
    }

    public Vector3f getSpatialLeftDir() {
        return spatial.getWorldRotation().mult(Vector3f.UNIT_X);
    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }

    public AnimationStateEnum getAnimationStateEnum() {
        return animationStateEnum;
    }

    public void setAnimationStateEnum(AnimationStateEnum animationStateEnum) {
        this.animationStateEnum = animationStateEnum;
    }

    public void removeShootControls() {
        do {
            playerShootControl = spatial.getControl(PlayerShootControl.class);
            spatial.removeControl(playerShootControl);
        } while (playerShootControl != null);
    }

    public MagicallyousApp getApp() {
        return app;
    }

    public void setApp(MagicallyousApp app) {
        this.app = app;
    }

    public GeneralStateEnum getGeneralStateEnum() {
        return generalStateEnum;
    }

    public void setGeneralStateEnum(GeneralStateEnum generalStateEnum) {
        this.generalStateEnum = generalStateEnum;
    }

    public AnimControl getAnimControl() {
        return animControl;
    }

    public void setAnimControl(AnimControl animControl) {
        this.animControl = animControl;
    }

    public AnimChannel getAnimChannel() {
        return animChannel;
    }

    public void setAnimChannel(AnimChannel animChannel) {
        this.animChannel = animChannel;
    }

    public double getTimeCounter() {
        return timeCounter;
    }

    public void setTimeCounter(double timeCounter) {
        this.timeCounter = timeCounter;
    }

    public PlayerShootControl getPlayerShootControl() {
        return playerShootControl;
    }

    public void setPlayerShootControl(PlayerShootControl playerShootControl) {
        this.playerShootControl = playerShootControl;
    }
}
