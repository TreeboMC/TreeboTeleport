package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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

public class SendSpawn {

    private TreeboTeleport pl;

    public SendSpawn(TreeboTeleport main) {
        this.pl = main;
    }

    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Forces a player to return to spawn");
                    this.setUsage("/sendspawn <player name> - requires tbteleport.staff.sendspawn");
                    this.setPermission("tbteleport.staff.sendspawn");
                    if (sender.hasPermission(this.getPermission())) {

                        if (args.length != 0 && args.length < 3) {
                            if (Bukkit.getPlayer(args[0]) != null) {
                                Player p = Bukkit.getPlayer(args[0]);
                                File spawnsYml = new File(pl.getDataFolder(), File.separator + "spawns.yml");
                                if (!spawnsYml.exists()) {
                                    sender.sendMessage(pl.err + "Failed to load Spawns data. Attempting to recover");
                                    try {
                                        spawnsYml.createNewFile();
                                        FileConfiguration spawns = YamlConfiguration.loadConfiguration(spawnsYml);
                                        try {
                                            spawns.options().copyDefaults();
                                            spawns.save(spawnsYml);
                                        } catch (FileNotFoundException e) {
                                            pl.makeLog(e);
                                        }
                                    } catch (IOException e) {
                                        pl.makeLog(e);
                                        sender.sendMessage(pl.err + "Loading Spawns Data Unsuccessful");
                                    }
                                }
                                FileConfiguration spawns = YamlConfiguration.loadConfiguration(spawnsYml);
                                String world = p.getWorld().getName();
                                if (args.length == 2) {
                                    world = args[1];
                                }

                                if (spawns.get("spawns." + world + ".x") != null) {
                                    String confWorld = spawns.getString("spawns." + world + ".world");
                                    double x = spawns.getDouble("spawns." + world + ".x");
                                    double y = spawns.getDouble("spawns." + world + ".y");
                                    double z = spawns.getDouble("spawns." + world + ".z");
                                    float pitch = (float) spawns.getDouble("spawns." + world + ".pitch");
                                    float yaw = (float) spawns.getDouble("spawns." + world + ".yaw");
                                    Location loc = new Location(Bukkit.getWorld(confWorld), x, y, z, yaw, pitch);
                                    p.sendMessage(pl.badge + "Returning you to Spawn");
                                    p.teleport(loc);
                                } else {
                                    p.sendMessage(pl.err + "No spawn found for this world");
                                }
                            } else {
                                sender.sendMessage(pl.err + "Player " + args[0] + "not found.");
                            }
                        } else if (args.length == 0) {
                            sender.sendMessage(pl.err + "Not enough arguments. Please specify a player and optionally which worlds spawn to send them to.");
                        }
                    } else {
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
