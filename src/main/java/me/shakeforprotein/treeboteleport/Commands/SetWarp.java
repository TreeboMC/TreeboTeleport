package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SetWarp implements CommandExecutor {

    private TreeboTeleport pl;

    public SetWarp(TreeboTeleport main){
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
                warps.set("warps." + args[0] + ".name", name);
                warps.set("warps." + args[0] + ".world", world);
                warps.set("warps." + args[0] + ".x", x);
                warps.set("warps." + args[0] + ".y", y);
                warps.set("warps." + args[0] + ".z", z);
                warps.set("warps." + args[0] + ".pitch", pitch);
                warps.set("warps." + args[0] + ".yaw", yaw);
                if(args.length > 1){
                    warps.set("warps." + args[0] + ".requiredPermission", args[1]);
                }

                try {
                    warps.save(warpsYml);
                    p.sendMessage(pl.badge + "Warp with name: " + ChatColor.GOLD + args[0] + ChatColor.RESET + " has been saved.");
                } catch (IOException e) {
                    pl.makeLog(e);
                    p.sendMessage(pl.err + "Saving warps file Unsuccessful");
                }
            }
        return true;
    }
}
