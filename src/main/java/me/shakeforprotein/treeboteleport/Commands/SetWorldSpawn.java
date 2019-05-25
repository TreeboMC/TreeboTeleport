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

public class SetWorldSpawn implements CommandExecutor {

    private TreeboTeleport pl;

    public SetWorldSpawn(TreeboTeleport main){
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
                    sender.sendMessage(ChatColor.GOLD + "[TREEBO TELEPORT] " + ChatColor.RED + "ERROR:" + ChatColor.RESET + "Creating Spawns file failed");
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
            if (args.length == 0) {
                String name = world;
                spawns.set("spawns." + name + ".name", name);
                spawns.set("spawns." + name + ".world", world);
                spawns.set("spawns." + name + ".x", x);
                spawns.set("spawns." + name + ".y", y);
                spawns.set("spawns." + name + ".z", z);
                spawns.set("spawns." + name + ".pitch", pitch);
                spawns.set("spawns." + name + ".yaw", yaw);

                pl.getConfig().set("onJoinSpawn." + p.getWorld().getName() + ".world", world);
                pl.getConfig().set("onJoinSpawn." + p.getWorld().getName() + ".x", x);
                pl.getConfig().set("onJoinSpawn." + p.getWorld().getName() + ".y", y);
                pl.getConfig().set("onJoinSpawn." + p.getWorld().getName() + ".z", z);
                pl.getConfig().set("onJoinSpawn." + p.getWorld().getName() + ".pitch", pitch);
                pl.getConfig().set("onJoinSpawn." + p.getWorld().getName() + ".yaw", yaw);

                try {
                    spawns.save(spawnsYml);
                    p.sendMessage(pl.badge + "World spawn saved for world: " + ChatColor.YELLOW + "[" + ChatColor.GOLD + name + ChatColor.YELLOW + " + ]");
                } catch (IOException e) {
                    pl.makeLog(e);
                    p.sendMessage(pl.err + "Saving spawns file Unsuccessful");
                }
            } else if (args.length > 0) {
                p.sendMessage(pl.err + "Too many arguments");
            }

        return true;
    }
}
