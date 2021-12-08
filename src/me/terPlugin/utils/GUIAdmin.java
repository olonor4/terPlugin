package me.terPlugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIAdmin {
    public static Inventory INVAdmin;

    public void register() {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "admin tool");

        ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("удалить территорию");
        item.setItemMeta(meta);
        inv.setItem(4, item);

        item = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
        meta = item.getItemMeta();
        meta.setDisplayName("проверить территорию на которой вы находитесь");
        item.setItemMeta(meta);
        inv.setItem(10, item);

        item = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        meta = item.getItemMeta();
        meta.setDisplayName("список всех территорий");
        item.setItemMeta(meta);
        inv.setItem(16, item);

        item = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
        meta = item.getItemMeta();
        meta.setDisplayName("список всех неприсвоенных территорий");
        item.setItemMeta(meta);
        inv.setItem(22, item);

        setInventory(inv);
    }

    public Inventory getInventory() {
        return  INVAdmin;
    }

    private void setInventory(Inventory inv) {
        INVAdmin = inv;
    }

    public void openInventory(Player player) {
        player.openInventory(INVAdmin);
    }
}
