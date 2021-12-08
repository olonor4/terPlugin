package me.terPlugin.utils;

public class Territory {
    public String ID;
    String player;
    public String[] poses;

    public Territory(String ID, String player, String[] poses) {
        this.ID = ID;
        this.player = player;
        this.poses =  poses;
    }

    public Territory(String ID, String[] poses) {
        this.ID = ID;
        this.player = "0";
        this.poses =  poses;
    }

    public Territory() {
        this.ID = "-1";
    }

    public String getID() {
        return this.ID;
    }

    public String getPlayer() {
        return this.player;
    }

    public String[] getPoses() {
        return this.poses;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

}
