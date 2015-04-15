/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.net.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author duo
 */
public class DatabaseManager {
    private Connection connection;
    private Statement statement;
    
    private void checkConnection() throws SQLException {
        if(connection == null) {
            connection = DriverManager.
                            getConnection("jdbc:derby:database/magicallyousDB;create=true", 
                                    "server", "magiservercallyous");
        }
    }

    private void checkStatement() throws SQLException {
        checkConnection();
        if(statement == null) {
            statement = connection.createStatement();
        }
    }

    private void checkTables() {
        checkUserTable();
        checkPlayerTable();
    }

    private void checkUserTable() {
        try {
            String cmd = "CREATE TABLE user("
                    + "id INT NOT NULL GENERATED ALWAYS AS IDENTITY, "
                    + "name VARCHAR(255), "
                    + "password VARCHAR(255), "
                    + "email VARCHAR(255)"
                    + ")";
            checkStatement();
            statement.execute(cmd);
        } catch (SQLException ex) {
            System.out.println("Error creating table user: " + ex);
        }
    }

    private void checkPlayerTable() {
        try {
            String cmd = "CREATE TABLE player("
                    + "id INT NOT NULL GENERATED ALWAYS AS IDENTITY, "
                    + "name VARCHAR(255), "
                    + "assetName VARCHAR(255), "
                    + "level INT, "
                    + "points INT, "
                    + "damage INT, "
                    + "defense INT, "
                    + "user INT NOT NULL CONSTRAINT user_foreign_key " 
                    + "REFERENCES user ON DELETE CASCADE ON UPDATE RESTRICT"
                    + ")";
            checkStatement();
            statement.execute(cmd);
        } catch (SQLException ex) {
            System.out.println("Error creating table player: " + ex);
        }
    }

    public MagicallyousUser getUser(String name, String password) {
        MagicallyousUser magicallyousUser = null;
            String cmd = "SELECT * FROM user "
                + "WHERE name = " + name + ", "
                    + "password = " + password;
        try {
            ResultSet resultSet = statement.executeQuery(cmd);
            while(resultSet.next()) {
                magicallyousUser = new MagicallyousUser();
                magicallyousUser.setId(resultSet.getInt("id"));
                magicallyousUser.setName(resultSet.getString("name"));
                magicallyousUser.setPassword(resultSet.getString("password"));
                magicallyousUser.setEmail(resultSet.getString("email"));
            }
        } catch (SQLException sQLException) {
            magicallyousUser = null;
            System.out.println("Error getting user : " + sQLException);
            System.out.println("sql command: " + cmd);
        }
        return magicallyousUser;
    }

    public List<MagicallyousPlayer> getPlayers(int client) {
        List<MagicallyousPlayer> magicallyousPlayers = null;
        String cmd = "SELECT * FROM player ORDER BY name "
                + "WHERE id = " + client;
        checkTables();
        try {
            MagicallyousPlayer magicallyousPlayer;
            ResultSet resultSet = statement.executeQuery(cmd);
            while (resultSet.next()) {
                magicallyousPlayer = new MagicallyousPlayer();
                magicallyousPlayer.setPlayerId(resultSet.getInt("id"));
                magicallyousPlayer.setPlayerName(resultSet.getString("name"));
                magicallyousPlayer.setPlayerAssetName(resultSet.getString("assetName"));
                magicallyousPlayer.setLevel(resultSet.getInt("level"));
                magicallyousPlayer.setPoints(resultSet.getInt("points"));
                magicallyousPlayer.setDamage(resultSet.getInt("damage"));
                magicallyousPlayer.setDefense(resultSet.getInt("defense"));
                if (magicallyousPlayers == null) {
                    magicallyousPlayers = new ArrayList<>();
                }
                magicallyousPlayers.add(magicallyousPlayer);
            }
        } catch (SQLException ex) {
            System.out.println("Error getting user : " + ex);
            System.out.println("sql command: " + cmd);
        }
        return magicallyousPlayers;
    }

    public void createUser(MagicallyousUser magicallyousUser) {
        checkTables();
        String cmd = "INSERT INTO user values("
                + "default, "
                + "'" + magicallyousUser.getName() + "'" + ", "
                + "'" + magicallyousUser.getPassword()+ "'" + ", "
                + "'" + magicallyousUser.getEmail()+ "'" + ")";
        try {
            statement.execute(cmd);
        } catch (SQLException ex) {
            System.out.println("Error creating user : " + ex);
            System.out.println("sql command: " + cmd);
        }
    }

    public void updateUser(MagicallyousUser magicallyousUser) {
        checkTables();
        String cmd = "UPDATE user "
                + "SET name = "
                + "'" + magicallyousUser.getName() + "'" + ", "
                + "password = "
                + "'" + magicallyousUser.getPassword() + "'" + ", "
                + "email = "
                + "'" + magicallyousUser.getEmail()+ "' "
                + "WHERE id = " + magicallyousUser.getId();
        try {
            statement.execute(cmd);
        } catch (SQLException ex) {
            System.out.println("Error updating user : " + ex);
            System.out.println("sql command: " + cmd);
        }
    }

    public void deleteUser(MagicallyousUser magicallyousUser) {
        checkTables();
        String cmd = "DELETE FROM user "
                + "WHERE id = " + magicallyousUser.getId();
        try {
            statement.execute(cmd);
        } catch (SQLException ex) {
            System.out.println("Error deleting user : " + ex);
            System.out.println("sql command: " + cmd);
        }
    }

    public void createPlayer(MagicallyousPlayer magicallyousPlayer) {
        checkTables();
        String cmd = "INSERT INTO player values("
                + "default, "
                + "'" + magicallyousPlayer.getName() + "'" + ", "
                + "'" + magicallyousPlayer.getPlayerAssetName()+ "'" + ", "
                + " " + magicallyousPlayer.getLevel()+ " " + ", "
                + " " + magicallyousPlayer.getPoints()+ " " + ", "
                + " " + magicallyousPlayer.getDamage()+ " " + ", "
                + " " + magicallyousPlayer.getDefense() + " " + ")";
        try {
            statement.execute(cmd);
        } catch (SQLException ex) {
            System.out.println("Error creating player : " + ex);
            System.out.println("sql command: " + cmd);
        }
    }

    public void updatePlayer(MagicallyousPlayer magicallyousPlayer) {
        checkTables();
        String cmd = "UPDATE player "
                + "SET name = "
                + "'" + magicallyousPlayer.getName() + "'" + ", "
                + "assetName = "
                + "'" + magicallyousPlayer.getPlayerAssetName()+ "'" + ", "
                + "level = "
                + " " + magicallyousPlayer.getLevel()+ " " + ", "
                + "points = "
                + " " + magicallyousPlayer.getPoints()+ " " + ", "
                + "damage = "
                + " " + magicallyousPlayer.getDamage()+ " " + ", "
                + "defense = "
                + " " + magicallyousPlayer.getDefense() + " " + ", "
                + "WHERE id = " + magicallyousPlayer.getPlayerId();
        try {
            statement.execute(cmd);
        } catch (SQLException ex) {
            System.out.println("Error updating player : " + ex);
            System.out.println("sql command: " + cmd);
        }
    }

    public void deletePlayer(MagicallyousPlayer magicallyousPlayer) {
        checkTables();
        String cmd = "DELETE FROM player "
                + "WHERE id = " + magicallyousPlayer.getPlayerId();
        try {
            statement.execute(cmd);
        } catch (SQLException ex) {
            System.out.println("Error deleting player : " + ex);
            System.out.println("sql command: " + cmd);
        }
    }
}
