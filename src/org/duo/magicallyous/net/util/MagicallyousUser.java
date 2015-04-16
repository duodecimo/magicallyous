/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.net.util;

import com.jme3.network.serializing.Serializable;

/**
 *
 * @author duo
 */
@Serializable
public class MagicallyousUser {
    private Integer id;
    private String name;
    private String password;
    private String email;

    public Integer getId() {
        if(id == null) {
            id = new Integer(0);
        }
        return id;
    }

    public void setId(Integer id) {
        if(id == null) {
            this.id = new Integer(0);
        } else {
            this.id = id;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
