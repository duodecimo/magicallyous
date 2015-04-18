/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.net.message;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import org.duo.magicallyous.net.util.MagicallyousAccount;

/**
 *
 * @author aluno
 */
@Serializable
public class LoginRequestMessage extends AbstractMessage {
    private MagicallyousAccount magicallyousUser;

    public LoginRequestMessage() {
    }

    public LoginRequestMessage(MagicallyousAccount magicallyousUser) {
        this.magicallyousUser = magicallyousUser;
    }

    public MagicallyousAccount getMagicallyousUser() {
        return magicallyousUser;
    }

    public void setMagicallyousUser(MagicallyousAccount magicallyousUser) {
        this.magicallyousUser = magicallyousUser;
    }
}
