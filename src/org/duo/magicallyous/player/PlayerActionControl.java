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
import com.jme3.math.Vector3f;
import org.duo.magicallyous.Main;
import org.duo.magicallyous.utils.AnimationStateEnum;
import org.duo.magicallyous.utils.GeneralStateEnum;

/**
 *
 * @author duo
 */
public abstract class PlayerActionControl extends BetterCharacterControl implements AnimEventListener {
    private Main app;
    private GeneralStateEnum generalStateEnum;
    private AnimationStateEnum animationStateEnum;
    private AnimControl animControl;
    private AnimChannel animChannel;
    private double timeCounter = 0d;
    private PlayerShootControl playerShootControl;


    public PlayerActionControl() {
    }

    public PlayerActionControl(Main app, float radius, float height, float mass) {
        super(radius, height, mass);
        this.app = app;
    }

    /**
     * @see LocalPlayerActionControl, RemotePlayerActionControll
     * heir classes must implement this method 
     */
    abstract public void updatePlayerSate();

    @Override
    public void update(float tpf) {
        super.update(tpf);
        timeCounter += tpf;
        updatePlayerSate();
        animate();
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

    public Main getApp() {
        return app;
    }

    public void setApp(Main app) {
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
