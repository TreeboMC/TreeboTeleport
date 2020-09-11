package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SetPersonalWarp {

    private TreeboTeleport pl;

    public SetPersonalWarp(TreeboTeleport main) {
        this.pl = main;
    }

    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Creates a personal warp point with given name, that is accessible to all other players in this gamemode");
                    this.setUsage("/SetPWarp <warp name> - requires tbteleport.player.warps.setpersonalwarp");
                    this.setPermission("tbteleport.player.warps.setpersonalwarp");
                    if (sender.hasPermission(this.getPermission())) {

                        File warpsYml = new File(pl.getDataFolder(), File.separator + "warps.yml");
                        if (!warpsYml.exists()) {
                            sender.sendMessage(pl.err + "Warps data not found. Attempting to recover.");
                            try {
                                warpsYml.createNewFile();
                                FileConfiguration warps = YamlConfiguration.loadConfiguration(warpsYml);
                                try {
                                    warps.options().copyDefaults();
                                    warps.save(warpsYml);
                                } catch (FileNotFoundException e) {
                                    pl.makeLog(e);
                                }
                            } catch (IOException e) {
                                pl.makeLog(e);
                                sender.sendMessage(pl.err + "Creating warps file failed");
                            }
                        }
                        FileConfiguration warps = YamlConfiguration.loadConfiguration(warpsYml);
                        Player p = (Player) sender;
                        Location loc = p.getLocation();
                        String world = loc.getWorld().getName();
                        double x = loc.getX();
                        double y = loc.getY();
                        double z = loc.getZ();
                        float pitch = loc.getPitch();
                        float yaw = loc.getYaw();
                        if (args.length == 0) {
                            p.sendMessage(pl.err + "You must provide a name for your new warp");
                        } else if (args.length > 2) {
                            p.sendMessage(pl.err + "Too many arguments");
                        } else {
                            String name = args[0];
                            args[0] = args[0].toLowerCase();
                            warps.set("playerWarps." + p.getUniqueId().toString() + ".name", name);
                            warps.set("playerWarps." + p.getUniqueId().toString() +  ".world", world);
                            warps.set("playerWarps." + p.getUniqueId().toString() + ".x", x);
                            warps.set("playerWarps." + p.getUniqueId().toString() + ".y", y);
                            warps.set("playerWarps." + p.getUniqueId().toString() + ".z", z);
                            warps.set("playerWarps." + p.getUniqueId().toString() + ".pitch", pitch);
                            warps.set("playerWarps." + p.getUniqueId().toString() + ".yaw", yaw);

                            try {
                                warps.save(warpsYml);
                                p.sendMessage(pl.badge + "Player Warp with name: " + ChatColor.GOLD + args[0] + ChatColor.RESET + " has been saved.");
                                p.sendMessage("If you choose to set a new PWarp, this warp will be overwritten.");
                            } catch (IOException e) {
                                pl.makeLog(e);
                                p.sendMessage(pl.err + "Saving warps file Unsuccessful");
                            }
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
