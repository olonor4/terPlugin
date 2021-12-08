package me.terPlugin.utils;

import me.olonor.OlonorPlugin.OlonorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClickEvent implements Listener {
    private GUI menu;
    private GUIAdmin adminMenu;

    private static OlonorPlugin plugin;
    public ClickEvent(OlonorPlugin plugin) {
        this.plugin = plugin;
        menu = new GUI();
        adminMenu = new GUIAdmin();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().equals(menu.getInventory())) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;
            if (event.getCurrentItem().getItemMeta() == null) return;
            if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
            Player player = (Player) event.getWhoClicked();
            if (event.getSlot() == 4) {
                Territory[] t = plugin.terHelper.getAllTersByPlayer(player.getName());
                for (Territory t1 : t) {
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "ID: " + t1.ID + ", (" + t1.poses[0] + ", " + t1.poses[1] + "), (" + t1.poses[2] + ", " + t1.poses[3] + ")");
                }
                player.closeInventory();
                return;
            }
            if (event.getSlot() == 10) {
                Bukkit.dispatchCommand(player, "checkter");
                player.closeInventory();
                return;
            }
            if (event.getSlot() == 16) {
                Bukkit.dispatchCommand(player, "assign");
                player.closeInventory();
                return;
            }

        }
        else if (event.getInventory().equals(adminMenu.getInventory())) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            if (event.getSlot() == 4) {
                Bukkit.dispatchCommand(player, "delter");
                player.closeInventory();
                return;
            }
            if (event.getSlot() == 10) {
                Bukkit.dispatchCommand(player, "checkter");
                player.closeInventory();
                return;
            }
            if (event.getSlot() == 16) {
                Bukkit.dispatchCommand(player, "allters");
                player.closeInventory();
                return;
            }
            if (event.getSlot() == 22) {
                Bukkit.dispatchCommand(player, "allunassignedters");
                player.closeInventory();
                return;
            }
        }
    }
}
