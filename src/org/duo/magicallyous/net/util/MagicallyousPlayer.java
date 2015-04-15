/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.net.util;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author duo
 */
public class MagicallyousPlayer extends Node {
    private Integer playerId;
    private String playerName;
    private String playerAssetName;
    private Integer level;
    private Integer points;
    private Integer damage;
    private Integer defense;
    private Spatial playerAsset;
    private Integer sceneId;

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }

    public Integer getDefense() {
        return defense;
    }

    public void setDefense(Integer defense) {
        this.defense = defense;
    }

    public String getPlayerAssetName() {
        return playerAssetName;
    }

    public void setPlayerAssetName(String playerAssetName) {
        this.playerAssetName = playerAssetName;
    }

    public Spatial getPlayerAsset() {
        return playerAsset;
    }

    public void setPlayerAsset(Spatial playerAsset) {
        this.playerAsset = playerAsset;
    }
}
