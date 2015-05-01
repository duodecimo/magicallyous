/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.net.message;

import com.jme3.math.Quaternion;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.scene.Spatial;
import org.duo.magicallyous.player.PlayerShootControl;
import org.duo.magicallyous.utils.AnimationStateEnum;

/**
 *
 * @author aluno
 */
@Serializable
public class PlayerActionStateMessage extends AbstractMessage {
    private Integer playerId;
    private boolean moveFoward;
    private boolean moveFowardEvent;
    private boolean moveBackward;
    private boolean moveBackwardEvent;
    private boolean stopped;
    private boolean stoppedEvent;
    private boolean rotateRight;
    private boolean rotateRightEvent;
    private boolean rotateLeft;
    private boolean rotateLeftEvent;
    private boolean rightStrafe;
    private boolean rightStrafeEvent;
    private boolean leftStrafe;
    private boolean leftStrafeEvent;
    private boolean requestToggleRunning;
    private boolean increaseHealth;
    private boolean increaseHealthEvent;
    private boolean decreaseHealth;
    private boolean decreaseHealthEvent;
    private boolean increaseDamage;
    private boolean increaseDamageEvent;
    private boolean decreaseDamage;
    private boolean decreaseDamageEvent;
    private boolean increaseDefense;
    private boolean increaseDefenseEvent;
    private boolean decreaseDefense;
    private boolean decreaseDefenseEvent;
    private boolean running;
    private boolean ableToRun;
    private float moveSpeed;
    private float runSpeed;
    private float rotateValue;
    private AnimationStateEnum animationStateEnum;
    private double timeCounter = 0d;
    private double attackTime = 0d;
    private double deathTime = 0d;
    private boolean startAttack = false;
    private boolean waitingForCast = false;
    private boolean waitingForPrecast = false;
    private Spatial target;
    private Quaternion lookRotation;
    private PlayerShootControl playerShootControl;

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
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

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isAbleToRun() {
        return ableToRun;
    }

    public void setAbleToRun(boolean ableToRun) {
        this.ableToRun = ableToRun;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public float getRunSpeed() {
        return runSpeed;
    }

    public void setRunSpeed(float runSpeed) {
        this.runSpeed = runSpeed;
    }

    public float getRotateValue() {
        return rotateValue;
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

    public double getTimeCounter() {
        return timeCounter;
    }

    public void setTimeCounter(double timeCounter) {
        this.timeCounter = timeCounter;
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

    public boolean isStartAttack() {
        return startAttack;
    }

    public void setStartAttack(boolean startAttack) {
        this.startAttack = startAttack;
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

    public Spatial getTarget() {
        return target;
    }

    public void setTarget(Spatial target) {
        this.target = target;
    }

    public Quaternion getLookRotation() {
        return lookRotation;
    }

    public void setLookRotation(Quaternion lookRotation) {
        this.lookRotation = lookRotation;
    }

    public boolean isIncreaseHealth() {
        return increaseHealth;
    }

    public void setIncreaseHealth(boolean increaseHealth) {
        this.increaseHealth = increaseHealth;
    }

    public boolean isDecreaseHealth() {
        return decreaseHealth;
    }

    public void setDecreaseHealth(boolean decreaseHealth) {
        this.decreaseHealth = decreaseHealth;
    }

    public boolean isIncreaseDamage() {
        return increaseDamage;
    }

    public void setIncreaseDamage(boolean increaseDamage) {
        this.increaseDamage = increaseDamage;
    }

    public boolean isDecreaseDamage() {
        return decreaseDamage;
    }

    public void setDecreaseDamage(boolean decreaseDamage) {
        this.decreaseDamage = decreaseDamage;
    }

    public boolean isIncreaseDefense() {
        return increaseDefense;
    }

    public void setIncreaseDefense(boolean increaseDefense) {
        this.increaseDefense = increaseDefense;
    }

    public boolean isDecreaseDefense() {
        return decreaseDefense;
    }

    public void setDecreaseDefense(boolean decreaseDefense) {
        this.decreaseDefense = decreaseDefense;
    }

    public PlayerShootControl getPlayerShootControl() {
        return playerShootControl;
    }

    public void setPlayerShootControl(PlayerShootControl playerShootControl) {
        this.playerShootControl = playerShootControl;
    }

    public boolean isRequestToggleRunning() {
        return requestToggleRunning;
    }

    public void setRequestToggleRunning(boolean requestToggleRunning) {
        this.requestToggleRunning = requestToggleRunning;
    }

    public boolean isMoveFowardEvent() {
        return moveFowardEvent;
    }

    public void setMoveFowardEvent(boolean moveFowardEvent) {
        this.moveFowardEvent = moveFowardEvent;
    }

    public boolean isMoveBackwardEvent() {
        return moveBackwardEvent;
    }

    public void setMoveBackwardEvent(boolean moveBackwardEvent) {
        this.moveBackwardEvent = moveBackwardEvent;
    }

    public boolean isStoppedEvent() {
        return stoppedEvent;
    }

    public void setStoppedEvent(boolean stoppedEvent) {
        this.stoppedEvent = stoppedEvent;
    }

    public boolean isRotateRightEvent() {
        return rotateRightEvent;
    }

    public void setRotateRightEvent(boolean rotateRightEvent) {
        this.rotateRightEvent = rotateRightEvent;
    }

    public boolean isRotateLeftEvent() {
        return rotateLeftEvent;
    }

    public void setRotateLeftEvent(boolean rotateLeftEvent) {
        this.rotateLeftEvent = rotateLeftEvent;
    }

    public boolean isRightStrafeEvent() {
        return rightStrafeEvent;
    }

    public void setRightStrafeEvent(boolean rightStrafeEvent) {
        this.rightStrafeEvent = rightStrafeEvent;
    }

    public boolean isLeftStrafeEvent() {
        return leftStrafeEvent;
    }

    public void setLeftStrafeEvent(boolean leftStrafeEvent) {
        this.leftStrafeEvent = leftStrafeEvent;
    }

    public boolean isIncreaseHealthEvent() {
        return increaseHealthEvent;
    }

    public void setIncreaseHealthEvent(boolean increaseHealthEvent) {
        this.increaseHealthEvent = increaseHealthEvent;
    }

    public boolean isDecreaseHealthEvent() {
        return decreaseHealthEvent;
    }

    public void setDecreaseHealthEvent(boolean decreaseHealthEvent) {
        this.decreaseHealthEvent = decreaseHealthEvent;
    }

    public boolean isIncreaseDamageEvent() {
        return increaseDamageEvent;
    }

    public void setIncreaseDamageEvent(boolean increaseDamageEvent) {
        this.increaseDamageEvent = increaseDamageEvent;
    }

    public boolean isDecreaseDamageEvent() {
        return decreaseDamageEvent;
    }

    public void setDecreaseDamageEvent(boolean decreaseDamageEvent) {
        this.decreaseDamageEvent = decreaseDamageEvent;
    }

    public boolean isIncreaseDefenseEvent() {
        return increaseDefenseEvent;
    }

    public void setIncreaseDefenseEvent(boolean increaseDefenseEvent) {
        this.increaseDefenseEvent = increaseDefenseEvent;
    }

    public boolean isDecreaseDefenseEvent() {
        return decreaseDefenseEvent;
    }

    public void setDecreaseDefenseEvent(boolean decreaseDefenseEvent) {
        this.decreaseDefenseEvent = decreaseDefenseEvent;
    }
}
