package me.terPlugin.sql;

import me.olonor.OlonorPlugin.OlonorPlugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLGetterNumTers {

    private OlonorPlugin plugin;
    public SQLGetterNumTers(OlonorPlugin plugin) {
        this.plugin = plugin;
    }

    public void createTable() {
        PreparedStatement ps;
        try {
            ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS limters (NAME VARCHAR(100), NUM INTEGER, PRIMARY KEY(NAME))");

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delAll() {
        PreparedStatement ps;
        try {
            ps = plugin.SQL.getConnection().prepareStatement("DROP TABLE IF EXISTS limters");
            ps.executeUpdate();
            this.createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNum(String player) {
        int num = -11;
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT NUM FROM limters WHERE NAME=?");
            ps.setString(1, player);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getInt("NUM");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num;
    }

    public void createPlayer(String player, int num) {
        try {
            if (!existsPlayer(player)) {
                PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("INSERT INTO limters (NAME, NUM) VALUES (?, ?)");
                ps.setString(1, player);
                ps.setInt(2, num);
                ps.executeUpdate();
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean existsPlayer(String player) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM limters WHERE NAME=?");
            ps.setString(1, player);
            ResultSet result = ps.executeQuery();
            return result.next();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setNum(String player, int num) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE limters SET NUM=? WHERE NAME=?");
            ps.setInt(1, num);
            ps.setString(2, player);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
