/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.net.message;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import org.duo.magicallyous.net.util.MagicallyousUser;

/**
 *
 * @author aluno
 */
@Serializable
public class UserRegisterMessage extends AbstractMessage {
    private MagicallyousUser magicallyousUser;

    public UserRegisterMessage() {
    }

    public UserRegisterMessage(MagicallyousUser magicallyousUser) {
        this.magicallyousUser = magicallyousUser;
    }

    public MagicallyousUser getMagicallyousUser() {
        return magicallyousUser;
    }

    public void setMagicallyousUser(MagicallyousUser magicallyousUser) {
        this.magicallyousUser = magicallyousUser;
    }
}
