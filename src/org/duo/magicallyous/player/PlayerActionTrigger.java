/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.player;

import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;

/**
 *
 * @author duo
 */
public class PlayerActionTrigger {
    public final static Trigger TRIGGER_MOVEFOWARD_KEY_UP = new KeyTrigger(KeyInput.KEY_UP);
    public final static Trigger TRIGGER_MOVEFOWARD_KEY_I = new KeyTrigger(KeyInput.KEY_I);
    public final static Trigger TRIGGER_MOVEBACKWARD_KEY_DOWN = new KeyTrigger(KeyInput.KEY_DOWN);
    public final static Trigger TRIGGER_STOP_KEY_SPACE = new KeyTrigger(KeyInput.KEY_SPACE);
    public final static Trigger TRIGGER_TURNRIGHT_KEY_RIGHT = new KeyTrigger(KeyInput.KEY_RIGHT);
    public final static Trigger TRIGGER_TURNLEFT_KEY_LEFT = new KeyTrigger(KeyInput.KEY_LEFT);
    public final static Trigger TRIGGER_TOGGLEWALKSTATE_KEY_R = new KeyTrigger(KeyInput.KEY_R);
    public final static Trigger TRIGGER_USESWORD_KEY_U = new KeyTrigger(KeyInput.KEY_U);
    public final static Trigger TRIGGER_INCREASEHEALTH_KEY_1 = new KeyTrigger(KeyInput.KEY_1);
    public final static Trigger TRIGGER_DECREASEHEALTH_KEY_2 = new KeyTrigger(KeyInput.KEY_2);
    public final static Trigger TRIGGER_INCREASEDAMAGE_KEY_3 = new KeyTrigger(KeyInput.KEY_3);
    public final static Trigger TRIGGER_DECREASEDAMAGE_KEY_4 = new KeyTrigger(KeyInput.KEY_4);
    public final static Trigger TRIGGER_INCREASEDEFENSE_KEY_5 = new KeyTrigger(KeyInput.KEY_5);
    public final static Trigger TRIGGER_DECREASEDEFENSE_KEY_6 = new KeyTrigger(KeyInput.KEY_6);
}
