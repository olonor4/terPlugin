package me.terPlugin.utils;

import me.olonor.OlonorPlugin.OlonorPlugin;

import java.util.ArrayList;

public class TerHelper {
    private OlonorPlugin plugin;

    public TerHelper(OlonorPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean checkCollision(int[] pos1, int[] pos2, int[] pos3, int[] pos4) {
        return pos1[0] <= pos3[0] + (pos4[0] - pos3[0]) &&
                pos1[0] + (pos2[0] - pos1[0]) >= pos3[0] &&
                pos1[1] <= pos3[1] + (pos4[1] - pos3[1]) &&
                (pos2[1] - pos1[1]) + pos1[1] >= pos3[1];
    }

    public boolean checkCollisions(String pos1X, String pos1Z, String pos2X, String pos2Z) {
        for (int i = 0; i < plugin.ters.size(); i++) {
            Territory a = plugin.ters.get(i);
            int[] pos1 = {Integer.parseInt(a.poses[0]), Integer.parseInt(a.poses[1])};
            int[] pos2 = {Integer.parseInt(a.poses[2]), Integer.parseInt(a.poses[3])};
            int[] pos3 = {Integer.parseInt(pos1X), Integer.parseInt(pos1Z)};
            int[] pos4 = {Integer.parseInt(pos2X), Integer.parseInt(pos2Z)};
            if (this.checkCollision(pos1, pos2, pos3, pos4)) {
                return true;
            }
        }
        return false;
    }

    public Territory findTer(String posX, String posZ) {
        int px = Integer.parseInt(posX);
        int py = Integer.parseInt(posZ);
        int pos1x, pos1y, pos2x, pos2y;
        for (Territory a : plugin.ters) {
            pos1x = Integer.parseInt(a.poses[0]);
            pos1y = Integer.parseInt(a.poses[1]);
            pos2x = Integer.parseInt(a.poses[2]);
            pos2y = Integer.parseInt(a.poses[3]);
            if (((pos1x > px && pos2x < px) || (pos1x < px && pos2x > px)) && ((pos1y > py && pos2y < py) || (pos1y < py && pos2y > py))) {
                return a;
            }
        }
        return new Territory();
    }

    public void updateArrays() {
        plugin.ters.clear();
        plugin.terData.getAll();
    }

    public Territory getTerByID(String ID) {
        for (Territory t : plugin.ters) {
            if (t.ID.equals(ID)) {
                return t;
            }
        }
        return new Territory();
    }

    public ArrayList<Territory> getTersByPlayer(String player) {
        ArrayList<Territory> l = new ArrayList<>();
        for (Territory t : plugin.ters) {
            if (t.player.equals(player)) {
                l.add(t);
            }
        }
        return l;
    }

    public boolean checkCollision(Territory t, int px, int py) {
        if ((((Integer.parseInt(t.poses[0]) > px && Integer.parseInt(t.poses[2]) < px) || (Integer.parseInt(t.poses[0])
                < px && Integer.parseInt(t.poses[2]) > px)) && ((Integer.parseInt(t.poses[1]) >
                py && Integer.parseInt(t.poses[3]) < py) || (Integer.parseInt(t.poses[1]) <
                py && Integer.parseInt(t.poses[3]) > py)))) {
            return true;
        }
        return false;
    }

    public Territory[] getAllTersByPlayer(String player) {
        Territory[] t = new Territory[plugin.terData.getNumNotesPlayer(player)];
        for (short i = 0; i < t.length; i++) {
            if (plugin.ters.get(i).player.equals(player)) {
                t[i] = plugin.ters.get(i);
            }
        }
        return t;
    }
}
