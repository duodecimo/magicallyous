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
        if (connection == null) {
            connection = DriverManager.
                    getConnection("jdbc:derby:database/magicallyousDB;create=true",
                    "server", "magiservercallyous");
        }
    }

    private void checkStatement() throws SQLException {
        checkConnection();
        if (statement == null) {
            statement = connection.createStatement();
        }
    }

    private void checkTables() {
        checkAccountTable();
        checkPlayerTable();
    }

    private void checkAccountTable() {
        String cmd = "";
        try {
            cmd = "CREATE TABLE account ("
                    + "id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                    + "name VARCHAR(255), "
                    + "password VARCHAR(255), "
                    + "email VARCHAR(255)"
                    + ")";
            checkStatement();
            statement.execute(cmd);
        } catch (SQLException ex) {
            System.out.println("Error creating table account: " + ex);
            System.out.println("  >> sql: " + cmd);
        }
    }

    private void checkPlayerTable() {
        String cmd = "";
        try {
            cmd = "CREATE TABLE player("
                    + "id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                    + "name VARCHAR(255), "
                    + "assetName VARCHAR(255), "
                    + "level INT, "
                    + "points INT, "
                    + "damage INT, "
                    + "defense INT, "
                    + "account INT NOT NULL CONSTRAINT account_foreign_key "
                    + "REFERENCES account ON DELETE CASCADE ON UPDATE RESTRICT"
                    + ")";
            checkStatement();
            statement.execute(cmd);
        } catch (SQLException ex) {
            System.out.println("Error creating table player: " + ex);
            System.out.println("  >> sql: " + cmd);
        }
    }

    public MagicallyousAccount getAccount(String email, String password) {
        MagicallyousAccount magicallyousAccount = null;
        checkTables();
        String cmd = "SELECT * FROM account "
                + "WHERE email = '" + email.trim() + "' AND "
                + "password = '" + password.trim() + "'";
        System.out.println("Researching account with command: " + cmd);
        try {
            ResultSet resultSet = statement.executeQuery(cmd);
            System.out.println("Looking for account. Command: " + cmd);
            int count = 0;
            while (resultSet.next()) {
                count++;
                magicallyousAccount = new MagicallyousAccount();
                magicallyousAccount.setId(resultSet.getInt("id"));
                magicallyousAccount.setName(resultSet.getString("name"));
                magicallyousAccount.setPassword(resultSet.getString("password"));
                magicallyousAccount.setEmail(resultSet.getString("email"));
            }
            if (count == 0) {
                System.out.println("No account found!");
            }
        } catch (SQLException sQLException) {
            magicallyousAccount = null;
            System.out.println("Error getting account : " + sQLException);
            System.out.println("sql command: " + cmd);
        }
        return magicallyousAccount;
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
            System.out.println("Error getting account : " + ex);
            System.out.println("sql command: " + cmd);
        }
        return magicallyousPlayers;
    }

    public void createAccount(MagicallyousAccount magicallyousAccount) {
        checkTables();
        String cmd = "INSERT INTO account values("
                + "default, "
                + "'" + magicallyousAccount.getName() + "'" + ", "
                + "'" + magicallyousAccount.getPassword() + "'" + ", "
                + "'" + magicallyousAccount.getEmail() + "'" + ")";
        try {
            statement.execute(cmd);
        } catch (SQLException ex) {
            System.out.println("Error creating account : " + ex);
            System.out.println("sql command: " + cmd);
        }
    }

    public List<MagicallyousAccount> getAccounts() {
        List<MagicallyousAccount> accounts = null;
        String cmd = "SELECT * FROM account ORDER BY name ";
        checkTables();
        try {
            MagicallyousAccount magicallyousAccount;
            ResultSet resultSet = statement.executeQuery(cmd);
            while (resultSet.next()) {
                magicallyousAccount = new MagicallyousAccount();
                magicallyousAccount.setId(resultSet.getInt("id"));
                magicallyousAccount.setName(resultSet.getString("name"));
                magicallyousAccount.setEmail(resultSet.getString("email"));
                magicallyousAccount.setPassword(resultSet.getString("password"));
                if (accounts == null) {
                    accounts = new ArrayList<>();
                }
                accounts.add(magicallyousAccount);
            }
        } catch (SQLException ex) {
            System.out.println("Error getting accounts : " + ex);
            System.out.println("sql command: " + cmd);
        }
        return accounts;
    }

    public void updateAccount(MagicallyousAccount magicallyousAccount) {
        checkTables();
        String cmd = "UPDATE account "
                + "SET name = "
                + "'" + magicallyousAccount.getName() + "'" + ", "
                + "password = "
                + "'" + magicallyousAccount.getPassword() + "'" + ", "
                + "email = "
                + "'" + magicallyousAccount.getEmail() + "' "
                + "WHERE id = " + magicallyousAccount.getId();
        try {
            statement.execute(cmd);
        } catch (SQLException ex) {
            System.out.println("Error updating account : " + ex);
            System.out.println("sql command: " + cmd);
        }
    }

    public void deleteAccount(MagicallyousAccount magicallyousAccount) {
        checkTables();
        String cmd = "DELETE FROM account "
                + "WHERE id = " + magicallyousAccount.getId();
        try {
            statement.execute(cmd);
        } catch (SQLException ex) {
            System.out.println("Error deleting account : " + ex);
            System.out.println("sql command: " + cmd);
        }
    }

    public void createPlayer(MagicallyousPlayer magicallyousPlayer) {
        checkTables();
        String cmd = "INSERT INTO player values("
                + "default, "
                + "'" + magicallyousPlayer.getName() + "'" + ", "
                + "'" + magicallyousPlayer.getPlayerAssetName() + "'" + ", "
                + " " + magicallyousPlayer.getLevel() + " " + ", "
                + " " + magicallyousPlayer.getPoints() + " " + ", "
                + " " + magicallyousPlayer.getDamage() + " " + ", "
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
                + "'" + magicallyousPlayer.getPlayerAssetName() + "'" + ", "
                + "level = "
                + " " + magicallyousPlayer.getLevel() + " " + ", "
                + "points = "
                + " " + magicallyousPlayer.getPoints() + " " + ", "
                + "damage = "
                + " " + magicallyousPlayer.getDamage() + " " + ", "
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
