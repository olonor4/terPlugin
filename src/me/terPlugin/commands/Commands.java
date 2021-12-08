package me.terPlugin.commands;

import me.olonor.OlonorPlugin.OlonorPlugin;
import me.terPlugin.utils.GUI;
import me.terPlugin.utils.GUIAdmin;
import me.terPlugin.utils.Territory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Commands implements CommandExecutor {

    private OlonorPlugin plugin;
    public Commands(OlonorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (label.equalsIgnoreCase("flyspeed")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage("эта команда только для игроков");
                return true;
            }
            if (!sender.hasPermission("op")) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "вы не можете использовать эту команду");
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (args.length == 1) {
                target = (Player) sender;
                target.setFlySpeed(Float.parseFloat(args[0]));
            } else {
                target.setFlySpeed(Float.parseFloat(args[1]));
            }
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "скорость полёта изменена");
            return true;
        }

        if (label.equalsIgnoreCase("setter")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage("эта команда только для игроков");
                return true;
            }
            if (!sender.hasPermission("op")) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "вы не можете использовать эту команду");
                return true;
            }
            if (args.length != 1) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "неправильное количество аргументов");
                return true;
            }
            String ID = args[0];
            if(plugin.terData.existsID(ID)) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "такой ID уже используется");
                return true;
            }
            Location[] l = plugin.clickPoses.get(sender);
            String pos1X = String.valueOf(l[1].getBlockX());
            String pos1Z = String.valueOf(l[1].getBlockZ());
            String pos2X = String.valueOf(l[0].getBlockX());
            String pos2Z = String.valueOf(l[0].getBlockZ());
            plugin.terData.create(ID, pos2X, pos2Z, pos1X, pos1Z, "0");
            plugin.ters.add(new Territory(ID, new String[] {pos1X, pos1Z, pos2X, pos2Z}));
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "территория установлена ");
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "ID - " + ID + ", (" + pos2X + ", "
                    + pos2Z + "), (" + pos1X + ", " + pos1Z + ")");
            return true;
        }

        if (label.equalsIgnoreCase("checkter")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("эта команда только для игроков");
                return true;
            }
            Location playerLoc = ((Player) sender).getLocation();
            String posX = String.valueOf(playerLoc.getBlockX());
            String posZ = String.valueOf(playerLoc.getBlockZ());
            String ID = plugin.terHelper.findTer(posX, posZ).getID();
            if(ID.equals("-1")) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "это место незарегистрировано");
                return true;
            }
            String name = plugin.terData.getPlayerByID(ID);
            if (name.equals("0")) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "ID - " + ID + ", никому не принадлежит");
                return true;
            }
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "ID - " + ID  + ", принадлежит " + name);
            return true;
        }

        if (label.equalsIgnoreCase("delallters")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("эта команда только для игроков");
                return true;
            }
            if (!sender.hasPermission("op")) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "вы не можете использовать эту команду");
                return true;
            }
            plugin.terData.delAllTers();
            plugin.ters.clear();
            plugin.onlineTers.clear();
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "все территории удалены");
            return true;
        }

        if (label.equalsIgnoreCase("assign")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("эта команда только для игроков");
                return true;
            }
            String name = sender.getName();
            if (plugin.terData.getNumNotesPlayer(name) >= plugin.numTersData.getNum(name)) {

                sender.sendMessage(ChatColor.LIGHT_PURPLE + "у вас уже достигнут лимит территорий");
                return true;
            }
            Location playerLoc = ((Player) sender).getLocation();
            String posX = String.valueOf(playerLoc.getBlockX());
            String posZ = String.valueOf(playerLoc.getBlockZ());
            String ID = plugin.terHelper.findTer(posX, posZ).getID();
            if (ID.equals("-1")) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "вы не можете присвоить эту территорию");
                return true;
            }
            String p = plugin.terData.getPlayerByID(ID);
            if (!(p.equals("") || p.equals("0"))) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "эта территория уже занята");
                return true;
            }
            plugin.terData.setName(ID, name);
            int i = 0;
            for (Territory t: plugin.ters) {
                if(t.ID.equals(ID)) {
                    if (plugin.onlineTers.containsKey(name)) {
                        plugin.onlineTers.get(name).add(t);
                    } else {
                        plugin.onlineTers.put(name, new ArrayList<>());
                        plugin.onlineTers.get(name).add(t);
                    }
                    plugin.ters.get(i).setPlayer(name);
                    break;
                }
                i++;
            }
            //plugin.numTersData.setNum(name, plugin.numTersData.getNum(name) + 1);
            //plugin.onlineNumTers.put(name, plugin.numTersData.getNum(name));
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "территория присвоена");
            return true;
        }

        if (label.equalsIgnoreCase("allters")) {
            for (Territory ter : plugin.ters) {
                String s;
                if (!(ter.getPlayer().equals("0") || ter.getPlayer().equals(""))) {
                    s = ", игрок: " + ter.getPlayer() + " , ";
                } else {
                    s = ", никому не принадлежит, ";
                }
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "ID : " + ter.getID() + s
                        + "(" + ter.getPoses()[0] + ", " + ter.getPoses()[1] + "), ("
                        + ter.getPoses()[2] + ", " + ter.getPoses()[3] + ")");
            }
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "всего территорий - " + plugin.ters.size());
            return true;
        }

        if (label.equalsIgnoreCase("allunassignedters")) {
            short i = 0;
            for (Territory ter : plugin.ters) {
                if (ter.getPlayer().equals("0") || ter.getPlayer().equals("")) {
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "ID : " + ter.getID() + ", ("
                            + ter.getPoses()[0] + ", " + ter.getPoses()[1] + "), ("
                            + ter.getPoses()[2] + ", " + ter.getPoses()[3] + ")");
                    i++;
                }
            }
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "всего неприсвоенных территорий - " + i);
            return true;
        }

        if (label.equalsIgnoreCase("delterbyid")) {
            if (!sender.hasPermission("op")) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "вы не можете использовать эту команду");
                return true;
            }
            String printedID = args[0];
            if (!(plugin.terData.existsID(printedID))) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "этого ID и так нет");
                return true;
            }
            plugin.terHelper.updateArrays();
            plugin.terData.deleteTerByID(printedID);
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "территория " + printedID + " удалена");
            return true;
        }

        if (label.equalsIgnoreCase("terlimit")) {
            if (!sender.hasPermission("op")) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "вы не можете использовать эту команду");
                return true;
            }
            String printedPlayer = args[0];
            if (plugin.numTersData.existsPlayer(printedPlayer)) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "этого игрока нет");
                return true;
            }
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "" + plugin.numTersData.getNum(printedPlayer));
            return true;
        }

        if (label.equalsIgnoreCase("setterlimit")) {
            if (!sender.hasPermission("op")) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "вы не можете использовать эту команду");
                return true;
            }
            String printedPlayer = args[0];
            int printedLimit = Integer.parseInt(args[1]);
            if (!plugin.numTersData.existsPlayer(printedPlayer)) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "этого игрока нет");
                return true;
            }
            if (printedLimit < plugin.terData.getNumNotesPlayer(printedPlayer)) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "лимит территорий не изменен, так как игрок уже" +
                        " имеет больше территорий");
                return true;
            }
            plugin.numTersData.setNum(printedPlayer, printedLimit);
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "лимит территорий "+ printedPlayer + " изменен на " + printedLimit);
            return true;
        }

        if (label.equalsIgnoreCase("delowner")) {
            if (!sender.hasPermission("op")) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "вы не можете использовать эту команду");
                return true;
            }
            Location playerLoc = ((Player) sender).getLocation();
            String posX = String.valueOf(playerLoc.getBlockX());
            String posZ = String.valueOf(playerLoc.getBlockZ());
            String ID = plugin.terHelper.findTer(posX, posZ).getID();
            if (ID.equals("-1")) {
                return true;
            }
            if (plugin.terData.getPlayerByID(ID).equals("0")) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "эта территория и так никому не принадлежит");
                return true;
            }
            plugin.terData.setName(ID, "0");
            for (Territory t : plugin.ters) {
                if (t.ID.equals(ID)) {
                    t.setPlayer("0");
                    break;
                }
            }
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "владелец " + ID + " удален");
            return true;
        }

        if (label.equalsIgnoreCase("tool")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage("эта команда только для игроков");
                return true;
            }
            Player player = (Player) sender;
            GUI menu = new GUI();
            int terlim = plugin.numTersData.getNum(sender.getName());
            int numters = plugin.terData.getNumNotesPlayer(sender.getName());
            menu.setName(" у вас " + numters + " из " + terlim + " территорий");
            menu.openInventory(player);
            return true;
        }

        if (label.equalsIgnoreCase("admintool")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage("эта команда только для игроков");
                return true;
            }
            if (!sender.hasPermission("op")) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "вы не можете использовать эту команду");
                return true;
            }
            Player player = (Player) sender;
            GUIAdmin menu = new GUIAdmin();
            menu.openInventory(player);

            return true;
        }

        if (label.equalsIgnoreCase("numters")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage("эта команда только для игроков");
                return true;
            }
            int terlim = plugin.numTersData.getNum(sender.getName());
            terlim = plugin.numTersData.getNum(sender.getName());
            int numters = plugin.terData.getNumNotesPlayer(sender.getName());

            sender.sendMessage(ChatColor.LIGHT_PURPLE + " у вас " + numters + " из " + terlim + " территорий");

            return true;
        }

        if (label.equalsIgnoreCase("delter")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage("эта команда только для игроков");
                return true;
            }
            if (!sender.hasPermission("op")) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "вы не можете использовать эту команду");
                return true;
            }
            Location playerLoc = ((Player) sender).getLocation();
            String posX = String.valueOf(playerLoc.getBlockX());
            String posZ = String.valueOf(playerLoc.getBlockZ());
            String ID = plugin.terHelper.findTer(posX, posZ).getID();
            if (ID.equals("-1")) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "эта территория не установлена");
                return true;
            }
            plugin.terData.deleteTerByID(ID);
            plugin.terHelper.updateArrays();
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "территория удалена");
            return true;
        }

        return true;
    }
}
