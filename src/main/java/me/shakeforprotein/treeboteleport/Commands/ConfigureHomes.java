package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class ConfigureHomes implements CommandExecutor {

    private TreeboTeleport pl;
    private File homeFile;
    private YamlConfiguration homeYaml;
    
    public ConfigureHomes(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        homeFile = new File(pl.getDataFolder() + File.separator  + "homes", File.separator + ((Player) sender).getUniqueId().toString() + ".yml");
        homeYaml = YamlConfiguration.loadConfiguration(homeFile);
        boolean found = false;

        if(args.length > 2){
            if(args[0].equalsIgnoreCase("set")){
                if(args[2].equalsIgnoreCase("colour") || args[2].equalsIgnoreCase("color") || args[2].equalsIgnoreCase("icon")){
                    if(args[2].equalsIgnoreCase("colour") || args[2].equalsIgnoreCase("color")){
                        for(String item : homeYaml.getConfigurationSection("homes").getKeys(false)){
                            if(item.equalsIgnoreCase(args[1])){
                                found = true;
                                try{
                                    ChatColor newColour = ChatColor.valueOf(args[3].toUpperCase());
                                    setYml(item, "colour", args[3].toUpperCase(), sender);
                                }
                                catch (IllegalArgumentException e){
                                    sender.sendMessage("Unknown colour: '" + args[3].toUpperCase() + "'");
                                }
                            }
                        }
                    }
                    else if (args[2].equalsIgnoreCase("icon")) {
                        for (String item : homeYaml.getConfigurationSection("homes").getKeys(false)) {
                            if (item.equalsIgnoreCase(args[1])) {
                                found = true;
                                if(Material.getMaterial(args[3].toUpperCase()) != null){
                                    setYml(item, "icon", args[3].toUpperCase(), sender);
                                }
                                else{
                                    sender.sendMessage(pl.err + "Unknown Item: '" + args[3].toUpperCase() + "'");
                                }
                            }
                        }
                    }
                    if(!found){
                        sender.sendMessage(pl.err + "");
                    }
                }
                else {
                    sender.sendMessage(pl.err + "Unknown argument '" + args[2] + "'");
                }
            }
            else{
                sender.sendMessage(pl.err + "Unknown argument '" + args[0] + "'");
            }
        }
        else{
            sender.sendMessage(pl.err + "Insufficient arguments");
            doHelp(sender);
        }
        pl.saveFile(homeFile, homeYaml, sender);
        return true;
    }


    public void notFound(String str, CommandSender s) {
        s.sendMessage(pl.err + "No menu item found at position " + str + ".");
    }

    public boolean setYml(String name, String selector, String newVal, CommandSender s) {
        String uuid = ((Player) s).getUniqueId().toString();
        homeFile = new File(pl.getDataFolder() + File.separator  + "homes", File.separator + uuid + ".yml");
        homeYaml = YamlConfiguration.loadConfiguration(homeFile);
        boolean found = false;
        for (String menuItem : homeYaml.getConfigurationSection("homes").getKeys(false)) {
            if (homeYaml.getString("homes." + menuItem + ".name").equalsIgnoreCase(name)) {
                if(selector.equalsIgnoreCase("colour")){
                    newVal = newVal.toUpperCase();
                }
                homeYaml.set("homes." + menuItem + "." + selector, newVal);
                pl.saveFile(homeFile, homeYaml, s);
                s.sendMessage(pl.badge + "Changed " + selector + " of '" + name + "' to '" + newVal + "' successfully.");
                found = true;
            }
        }
        if (!found) {
            s.sendMessage(pl.err + "No menu item found with name:  " + name);
            doHelp(s);
        }
        return false;
    }


    public void doHelp(CommandSender s) {
        s.sendMessage(pl.badge + ChatColor.GOLD + "Help for /configurehomes");
        s.sendMessage("/configurehome set <current name> <icon |                    x  colour> <New Value>");
        s.sendMessage("/configurehome set default <CurrentName>");
    }
}
