package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
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

public class SetWorldSpawn implements CommandExecutor {

    private TreeboTeleport pl;

    public SetWorldSpawn(TreeboTeleport main) {
        this.pl = main;
    }

    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Reloads the TreeboTeleport Config from file");
                    this.setUsage("/setworldspawn - requires tbteleport.admin");
                    this.setPermission("tbteleport.admin");
                    if (sender.hasPermission(this.getPermission())) {

                        File spawnsYml = new File(pl.getDataFolder(), "spawns.yml");
                        if (!spawnsYml.exists()) {
                            sender.sendMessage("Spawns file not found");
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
                                sender.sendMessage(pl.badge + ChatColor.RED + "ERROR:" + ChatColor.RESET + "Creating Spawns file failed");
                            }
                        }
                        FileConfiguration spawns = YamlConfiguration.loadConfiguration(spawnsYml);
                        Player p = (Player) sender;
                        Location loc = p.getLocation();
                        String world = loc.getWorld().getName();
                        double x = loc.getX();
                        double y = loc.getY();
                        double z = loc.getZ();
                        float pitch = loc.getPitch();
                        float yaw = loc.getYaw();
                        if (args.length < 2) {
                            String name = world;
                            spawns.set("spawns." + name + ".name", name);
                            spawns.set("spawns." + name + ".world", world);
                            spawns.set("spawns." + name + ".x", x);
                            spawns.set("spawns." + name + ".y", y);
                            spawns.set("spawns." + name + ".z", z);
                            spawns.set("spawns." + name + ".pitch", pitch);
                            spawns.set("spawns." + name + ".yaw", yaw);
                            sender.sendMessage(pl.badge + "Spawn location set");
                            if (args.length == 1 && args[0].equalsIgnoreCase("true")) {
                                pl.getConfig().set("onJoinSpawn." + p.getWorld().getName() + ".world", world);
                                pl.getConfig().set("onJoinSpawn." + p.getWorld().getName() + ".x", x);
                                pl.getConfig().set("onJoinSpawn." + p.getWorld().getName() + ".y", y);
                                pl.getConfig().set("onJoinSpawn." + p.getWorld().getName() + ".z", z);
                                pl.getConfig().set("onJoinSpawn." + p.getWorld().getName() + ".pitch", pitch);
                                pl.getConfig().set("onJoinSpawn." + p.getWorld().getName() + ".yaw", yaw);
                                sender.sendMessage(pl.badge + "Enabled on join spawn for this world");
                            }
                            pl.saveConfig();


                            try {
                                spawns.save(spawnsYml);
                                p.sendMessage(pl.badge + "World spawn saved for world: " + ChatColor.YELLOW + "[" + ChatColor.GOLD + name + ChatColor.YELLOW + "]");
                            } catch (IOException e) {
                                pl.makeLog(e);
                                p.sendMessage(pl.err + "Saving spawns file Unsuccessful");
                            }
                        } else if (args.length > 1) {
                            p.sendMessage(pl.err + "Too many arguments");
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

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!pl.getConfig().getBoolean("disabledCommands.setworldspawn")) {

        } else {
            sender.sendMessage(pl.err + "The command /" + cmd + " has been disabled on this server");
        }

        return true;
    }
}
