/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.net.message;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author duo
 */
@Serializable
public class ServerServiceOutcomeMessage extends AbstractMessage {

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
    public static enum Service {ACCOUNT_REGISTER_OK, ACCOUNT_REGISTER_FAIL, 
    LOGIN_ACCEPTED, LOGIN_FAILED,
    PLAYER_REGISTER_OK, PLAYER_REGISTER_FAIL};
    private Service service;
    private String response;

    public ServerServiceOutcomeMessage() {
    }

    public ServerServiceOutcomeMessage(Service service) {
        this.service = service;
    }

    public Service getService() {
        return service;
    }
}
