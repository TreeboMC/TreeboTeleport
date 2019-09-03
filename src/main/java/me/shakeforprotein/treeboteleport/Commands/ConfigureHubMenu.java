package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;


public class ConfigureHubMenu implements CommandExecutor {

    private TreeboTeleport pl;
    private File hubFile;
    private YamlConfiguration hubYaml;

    public ConfigureHubMenu(TreeboTeleport main) {
        this.pl = main;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!pl.getConfig().getBoolean("disabledCommands.configurehub")) {
            if (args.length == 0) {
                sender.sendMessage(pl.err + "This command requires multiple inputs");
                doHelp(sender);
            } else if (args[0].equalsIgnoreCase("set")) {
                if (args[2] != null) {
                    if (pl.isInteger(args[2])) {
                        if (args[1].equalsIgnoreCase("icon")) {
                            if (Material.getMaterial(args[3].toUpperCase()) != null) {
                                setYml(args[2], "icon", args[3].toUpperCase(), sender);
                            } else {
                                sender.sendMessage(pl.err + "Unknown Material -->" + args[3] + "<--");
                            }
                        } else if (args[1].equalsIgnoreCase("label")) {
                            int i;
                            StringBuilder labelText = new StringBuilder();
                            for (i = 3; i < args.length; i++) {
                                labelText.append(args[i] + " ");
                            }
                            setYml(args[2], "label", labelText.toString(), sender);
                        } else if (args[1].equalsIgnoreCase("position")) {

                            setYml(args[2], "position", args[3], sender);
                        } else if (args[1].equalsIgnoreCase("command")) {
                            int i;
                            StringBuilder commandText = new StringBuilder();
                            for (i = 3; i < args.length; i++) {
                                commandText.append(args[i] + " ");
                            }
                            setYml(args[2], "command", commandText.toString(), sender);
                        } else if (args[1].equalsIgnoreCase("colour") || args[1].equalsIgnoreCase("color")) {
                            if (args[1].equalsIgnoreCase("color")) {
                                sender.sendMessage("You seem to have dropped your U. Don't worry, I've made sure to include it with your other letters.");
                            }
                            setYml(args[2], "color", args[3], sender);
                        } else if (args[1].equalsIgnoreCase("rows")) {
                            setRows(args[2], sender);
                        }


                    } else {
                        sender.sendMessage(pl.err + "Expected integer at -->" + args[2] + "<--");
                    }
                } else {
                    sender.sendMessage(pl.err + "Insufficient arguments");
                    doHelp(sender);
                }
            }


            pl.saveFile(hubFile, hubYaml, sender);
        }
        else {
            sender.sendMessage(pl.err + "The command /" + cmd + " has been disabled on this server");
        }
        return true;
    }


    public void notFound(String str, CommandSender s) {
        s.sendMessage(pl.err + "No menu item found at position " + str + ".");
    }

    public boolean setYml(String pos, String selector, String newVal, CommandSender s) {
        hubFile = new File(pl.getDataFolder(), "hubMenu.yml");
        hubYaml = YamlConfiguration.loadConfiguration(hubFile);
        boolean found = false;
        for (String menuItem : hubYaml.getConfigurationSection("hubmenu.menuItems").getKeys(false)) {
            if (hubYaml.getInt("hubmenu.menuItems." + menuItem + ".position") == Integer.parseInt(pos)) {
                if(selector.equalsIgnoreCase("position")){
                    hubYaml.set("hubmenu.menuItems." + menuItem + "." + selector, Integer.parseInt(newVal));
                }
                else{
                    hubYaml.set("hubmenu.menuItems." + menuItem + "." + selector, newVal);
                }
                pl.saveFile(hubFile, hubYaml, s);
                s.sendMessage("Changed " + selector + " to '" + newVal + "' successfully.");
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
        hubYaml.set("hubmenu.menuRows", Integer.parseInt(newRows));
        pl.saveFile(hubFile, hubYaml, s);
        return true;
    }

    public void doHelp(CommandSender s) {
        s.sendMessage(pl.badge + ChatColor.GOLD + "Help for /configureHub");
        s.sendMessage("Usage: /configureHub set <icon | label | command | colour> <existing table position> <New Value>");
        s.sendMessage("       /configurehub set rows <amount> (1 - 6)");
    }
}