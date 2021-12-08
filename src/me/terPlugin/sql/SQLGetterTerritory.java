package me.terPlugin.sql;

import me.olonor.OlonorPlugin.OlonorPlugin;
import me.terPlugin.utils.Territory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLGetterTerritory {

    private OlonorPlugin plugin;
    public SQLGetterTerritory(OlonorPlugin plugin) {
        this.plugin = plugin;
    }

    public void createTable() {
        PreparedStatement ps;
        try {
            ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS territories "
                    + "(ID VARCHAR(100), POS1X VARCHAR(100), POS1Z VARCHAR(100), POS2X VARCHAR(100), POS2Z VARCHAR(100), NAME VARCHAR(100), PRIMARY KEY(ID))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delAllTers() {
        PreparedStatement ps;
        try {
            ps = plugin.SQL.getConnection().prepareStatement("DROP TABLE territories");
            ps.executeUpdate();
            this.createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTerByID(String ID) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("DELETE FROM territories"
                    + " WHERE ID=?");
            ps.setString(1, ID);
            ps.executeUpdate();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void create(String ID, String pos1X, String pos1Z, String pos2X, String pos2Z, String name) {
        try {
            if (!existsID(ID)) {
                PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("INSERT INTO territories"
                        + " (ID, POS1X, POS1Z, POS2X, POS2Z,  NAME) VALUES (?, ?, ?, ?, ?, ?)");
                ps.setString(1, ID);
                ps.setString(4, pos1X);
                ps.setString(5, pos1Z);
                ps.setString(2, pos2X);
                ps.setString(3, pos2Z);
                ps.setString(6, name);
                ps.executeUpdate();
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean existsID(String ID) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM territories WHERE ID=?");
            ps.setString(1, ID);
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

    public boolean existsPlayer(String player) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM territories WHERE NAME=?");
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

    public String[] getPosesByName(String player) {
        String[] poses = new String[4];
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM territories WHERE NAME=?");
            ps.setString(1, player);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                poses[0] = rs.getString("POS1X");
                poses[1] = rs.getString("POS1Z");
                poses[2] = rs.getString("POS2X");
                poses[3] = rs.getString("POS2Z");
                return poses;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return poses;
    }

    public String[] getPosesByID(String ID) {
        String[] poses = new String[4];
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT POS1X FROM territories WHERE ID=?");
            ps.setString(1, ID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                poses[0] = rs.getString("POS1X");
                poses[1] = rs.getString("POS1Z");
                poses[2] = rs.getString("POS2X");
                poses[3] = rs.getString("POS2Z");
                return poses;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return poses;
    }

    public String getPlayerByID(String ID) {
        String player = "";
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT NAME FROM territories WHERE ID=?");
            ps.setString(1, ID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                player = rs.getString("NAME");
                return player;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return player;
    }

    public int getNumNotes() {
        try {
            Statement ps = plugin.SQL.getConnection().createStatement();
            ResultSet result = ps.executeQuery("select count(*) from territories");
            while (result.next()) {
                return result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getNumNotesPlayer(String player) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("select count(*) from territories WHERE NAME=?");
            ps.setString(1, player);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void getAll() {
        try {
            Statement ps = plugin.SQL.getConnection().createStatement();
            ResultSet rs = ps.executeQuery("SELECT * FROM territories");
            while (rs.next()) {
                Territory s = new Territory(rs.getString("ID"), rs.getString("NAME"),
                        new String[]{rs.getString("POS1X"), rs.getString("POS1Z"),
                                rs.getString("POS2X"), rs.getString("POS2Z")});
                plugin.ters.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setName(String ID, String player) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE territories SET NAME=? WHERE ID=?");
            ps.setString(1, player);
            ps.setString(2, ID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String[] getAllIDSByPlayer(String player) {
        String[] l = new String[getNumNotesPlayer(player)];
        int i = 0;
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT ID FROM territories WHERE NAME=?");
            ps.setString(1, player);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                l[i] = rs.getString("ID");
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }
}
