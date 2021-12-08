package me.terPlugin.events;

import me.olonor.OlonorPlugin.OlonorPlugin;
import me.terPlugin.utils.Territory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.meta.ItemMeta;


public class Events implements Listener {
    private static OlonorPlugin plugin;

    public Events(OlonorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent event) {
        String player = event.getPlayer().getName();
        event.setJoinMessage("[" + ChatColor.GREEN + "+" + ChatColor.WHITE + "] " + player);
        if (plugin.terData.existsPlayer(player)) {
            plugin.onlineTers.put(player, plugin.terHelper.getTersByPlayer(player));
        }
        plugin.numTersData.createPlayer(player, plugin.tersLimit);
    }

    @EventHandler
    public static void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage("[" + ChatColor.RED + "-" + ChatColor.WHITE + "] " + event.getPlayer().getName());
        Player player = event.getPlayer();
        plugin.clickPoses.remove(player);
        plugin.clickTimeouts.remove(player);
        plugin.clickFlags.remove(player);
        plugin.onlineTers.remove(player.getName());
    }

    @EventHandler
    public static void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("op")) {
            if (!plugin.onlineTers.containsKey(player.getName())) {
                event.setCancelled(true);
            } else {
                int px = event.getBlock().getLocation().getBlockX();
                int py = event.getBlock().getLocation().getBlockZ();
                for (Territory t : plugin.onlineTers.get(player.getName())) {
                    if (plugin.terHelper.checkCollision(t, px, py)) {
                        return;
                    }
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public static void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("op")) {
        } else {
            if (!plugin.onlineTers.containsKey(player.getName())) {
                event.setCancelled(true);
            } else {
                int px = event.getBlock().getLocation().getBlockX();
                int py = event.getBlock().getLocation().getBlockZ();
                for (Territory t : plugin.onlineTers.get(player.getName())) {
                    if (plugin.terHelper.checkCollision(t, px, py)) {
                        return;
                    }
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!(plugin.clickTimeouts.containsKey(player))) {
            plugin.clickTimeouts.put(player, System.currentTimeMillis());
            plugin.clickPoses.put(player, new Location[2]);
            plugin.clickFlags.put(player, 0);
        }
        if (player.hasPermission("op")) {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (player.getInventory().getItemInMainHand().getType() == Material.STONE_AXE) {
                    long m = plugin.clickTimeouts.get(player);
                    if (System.currentTimeMillis() > m + plugin.clickTimeoutInMills) {
                        Location loc = event.getClickedBlock().getLocation();
                        Location[] l = plugin.clickPoses.get(player);
                        int c = plugin.clickFlags.get(player);
                        if (c == 0) {
                            l[0] = loc;
                            plugin.clickFlags.remove(player);
                            plugin.clickFlags.put(player, 1);
                            player.sendMessage("pos 1: X=" + loc.getBlockX() + " Z=" + loc.getBlockZ());
                        } else {
                            l[1] = loc;
                            plugin.clickFlags.remove(player);
                            plugin.clickFlags.put(player, 0);
                            player.sendMessage("pos 2: X=" + loc.getBlockX() + " Z=" + loc.getBlockZ());
                        }
                        plugin.clickPoses.remove(player);
                        plugin.clickPoses.put(player, l);
                        plugin.clickTimeouts.remove(player);
                        plugin.clickTimeouts.put(player, System.currentTimeMillis());
                    }
                }
            }
        }
        if (player.getInventory().getItemInMainHand().getType() == Material.NETHER_STAR) {
            ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
            assert meta != null;
            if (meta.getDisplayName().equals("tool")) {
                Bukkit.dispatchCommand(player, "tool");
            }
            if (meta.getDisplayName().equals("admintool")) {
                Bukkit.dispatchCommand(player, "admintool");
            }
        }

    }

}
