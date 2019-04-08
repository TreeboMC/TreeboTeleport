package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Spawn implements CommandExecutor {

    private TreeboTeleport pl;

    public Spawn(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
            Player p = (Player) sender;
            String world = p.getWorld().getName();

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
        return true;
    }
}
