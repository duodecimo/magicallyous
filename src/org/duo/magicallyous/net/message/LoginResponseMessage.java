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
public class LoginResponseMessage extends AbstractMessage {
    private MagicallyousAccount magicallyousUser;
    private Integer clientId;

    public LoginResponseMessage() {
    }

    public LoginResponseMessage(MagicallyousAccount magicallyousUser) {
        this.magicallyousUser = magicallyousUser;
    }

    public MagicallyousAccount getMagicallyousUser() {
        return magicallyousUser;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }
}
