package me.shakeforprotein.treeboteleport.Commands.ConfigureMethods;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigureMethods {

    private TreeboTeleport pl;
    public File hubFile;
    public FileConfiguration hubYaml;

    public ConfigureMethods(TreeboTeleport main){
        this.pl = main;
    }


    public void notFound(String str, CommandSender s) {
        s.sendMessage(pl.err + "No menu item found at position " + str + ".");
    }

    public boolean setYml(String pos, String selector, String newVal, CommandSender s) {
        hubYaml = YamlConfiguration.loadConfiguration(hubFile);
        boolean found = false;
        for (String menuItem : hubYaml.getConfigurationSection("hubmenu.menuItems").getKeys(false)) {
            if (hubYaml.getInt("hubmenu.menuItems." + menuItem + ".position") == Integer.parseInt(pos)) {
                hubYaml.set("hubmenu.menuItems." + menuItem + "." + selector, newVal);
                pl.saveFile(hubFile, hubYaml, s);
                found = true;
            }
        }
        if (!found) {
            s.sendMessage(pl.err + "No menu item found in position " + pos);
            doHelp(s);
        }
        return false;
    }

    public boolean setRows(String newRows, CommandSender s) {
        hubFile = new File(pl.getDataFolder(), "hubMenu.yml");
        hubYaml = YamlConfiguration.loadConfiguration(hubFile);
        hubYaml.set("hubMenu.menuRows", Integer.parseInt(newRows));
        pl.saveFile(hubFile, hubYaml, s);
        return true;
    }

    public void doHelp(CommandSender s) {
        s.sendMessage(pl.badge + ChatColor.GOLD + "Help for /configureHub");
        s.sendMessage("Usage: /configureHub set <icon | label | command | colour | rows> <existing table position> <New Value>");
    }
}
