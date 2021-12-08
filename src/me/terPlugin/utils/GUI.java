package me.terPlugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUI {
    public static Inventory INV;

    public void register() {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "tool");

        ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("список моих территорий");
        item.setItemMeta(meta);
        inv.setItem(4, item);

        item = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
        meta = item.getItemMeta();
        meta.setDisplayName("проверить территорию на которой вы находитесь");
        item.setItemMeta(meta);
        inv.setItem(10, item);

        item = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE);
        meta = item.getItemMeta();
        meta.setDisplayName("присвоить территорию");
        item.setItemMeta(meta);
        inv.setItem(16, item);

        setInventory(inv);
    }

    public void setName(String a) {
        ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(a);
        item.setItemMeta(meta);
        INV.setItem(22, item);

    }

    public Inventory getInventory() {
        return  INV;
    }

    private void setInventory(Inventory inv) {
        INV = inv;
    }

    public void openInventory(Player player) {
        player.openInventory(INV);
    }

}
