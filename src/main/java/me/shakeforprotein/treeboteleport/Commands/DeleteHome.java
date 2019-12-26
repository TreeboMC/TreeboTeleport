package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DeleteHome {

    private TreeboTeleport pl;

    public DeleteHome(TreeboTeleport main) {
        this.pl = main;
    }

    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Allows player to delete their homes");
                    this.setUsage("/DeleteHome or /DelHome - requires tbteleport.player.delhome");
                    this.setPermission("tbteleport.player.delhome");
                    if(sender.hasPermission(this.getPermission())) {

                    Player p = (Player) sender;
                        File homesYml = new File(pl.getDataFolder() + File.separator + "homes", File.separator + p.getUniqueId() + ".yml");
                        if (!homesYml.exists()) {
                            p.sendMessage(pl.err + "Homes file not found. Attempting to Recover.");
                            try {
                                homesYml.createNewFile();
                                FileConfiguration homes = YamlConfiguration.loadConfiguration(homesYml);
                                try {
                                    homes.options().copyDefaults();
                                    homes.save(homesYml);
                                } catch (FileNotFoundException e) {
                                    pl.makeLog(e);
                                }
                            } catch (IOException e) {
                                pl.makeLog(e);
                                p.sendMessage(pl.err + "Creating Homes file failed");
                            }
                        }
                        FileConfiguration homes = YamlConfiguration.loadConfiguration(homesYml);

                        if (args.length == 0) {
                            p.sendMessage(pl.err + "You must provide the home name to delete");
                        } else if (args.length > 1) {
                            p.sendMessage(pl.err + "Too many arguments");
                        } else {
                            args[0] = args[0].toLowerCase();
                            homes.set("homes." + args[0] + ".name", null);
                            homes.set("homes." + args[0] + ".world", null);
                            homes.set("homes." + args[0] + ".x", null);
                            homes.set("homes." + args[0] + ".y", null);
                            homes.set("homes." + args[0] + ".z", null);
                            homes.set("homes." + args[0] + ".pitch", null);
                            homes.set("homes." + args[0] + ".yaw", null);
                            homes.set("homes." + args[0], null);
                            try {
                                homes.save(homesYml);
                                p.sendMessage(pl.badge + "If a home existed with name: " + ChatColor.GOLD + args[0] + ChatColor.RESET + " it has now been deleted.");
                            } catch (IOException e) {
                                pl.makeLog(e);
                                p.sendMessage(pl.err + "Saving homes file failed");
                            }
                        }

                    }
                    else{
                        sender.sendMessage(ChatColor.RED + "You do not have access to this command. You require permission node " + ChatColor.GOLD + this.getPermission());
                    }

                    return true;
                }
            };
            pl.registerNewCommand(pl.getDescription().getName(), item2);
        }
        return true;
    }
}
