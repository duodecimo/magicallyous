/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.net.message;

import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import org.duo.magicallyous.utils.AnimationStateEnum;

/**
 *
 * @author aluno
 */
@Serializable
public class PlayerActionControlMessage extends AbstractMessage {
    private Integer playerId;
    private Vector3f moveDirection;
    private Vector3f viewDirection;
    private AnimationStateEnum animationStateEnum;

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Vector3f getMoveDirection() {
        return moveDirection;
    }

    public void setMoveDirection(Vector3f moveDirection) {
        this.moveDirection = moveDirection;
    }

    public Vector3f getViewDirection() {
        return viewDirection;
    }

    public void setViewDirection(Vector3f viewDirection) {
        this.viewDirection = viewDirection;
    }

    public AnimationStateEnum getAnimationStateEnum() {
        return animationStateEnum;
    }

    public void setAnimationStateEnum(AnimationStateEnum animationStateEnum) {
        this.animationStateEnum = animationStateEnum;
    }
}
