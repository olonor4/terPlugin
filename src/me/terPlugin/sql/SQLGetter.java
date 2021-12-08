package me.terPlugin.sql;

import me.olonor.OlonorPlugin.OlonorPlugin;
import org.bukkit.entity.Player;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLGetter {

    private OlonorPlugin plugin;
    public SQLGetter(OlonorPlugin plugin) {
        this.plugin = plugin;
    }

    public void createTable() {
        PreparedStatement ps;
        try {
            ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS players "
                + "(NAME VARCHAR(100), PASSWORD VARCHAR(100), PRIMARY KEY(NAME))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPlayer(Player player, String password) {
        try {
            if (!exists(player)) {
                PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO players"
                        + " (NAME, PASSWORD) VALUES (?, ?)");
                ps.setString(1, player.getName());
                ps.setString(2, password);
                ps.executeUpdate();
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePlayer(String player) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("DELETE FROM players"
                    + " WHERE NAME=?");
            ps.setString(1, player);
            ps.executeUpdate();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getPassword(Player player) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT PASSWORD FROM players WHERE NAME=?");
            ps.setString(1, player.getName());
            ResultSet rs = ps.executeQuery();
            String password = "";
            if (rs.next()) {
                password = rs.getString("PASSWORD");
                return password;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean exists(Player player) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM players WHERE NAME=?");
            ps.setString(1, player.getName());

            ResultSet result = ps.executeQuery();
            if (result.next()) {
                return true;
            }
            return false;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean exists(String player) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM players WHERE NAME=?");
            ps.setString(1, player);

            ResultSet result = ps.executeQuery();
            if (result.next()) {
                return true;
            }
            return false;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getNumNotes() {
        try {
            Statement ps = plugin.SQL.getConnection().createStatement();
            ResultSet result = ps.executeQuery("select count(*) from players");
            while (result.next()) {
                return result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public List<String> getAllNames() {
        List<String> l = new ArrayList<>();
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT NAME FROM players");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                l.add(rs.getString("NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }
}
