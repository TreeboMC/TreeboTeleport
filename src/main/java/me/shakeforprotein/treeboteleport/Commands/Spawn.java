package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class Spawn implements CommandExecutor {

    private TreeboTeleport pl;
    private HashMap spawnsHash = new HashMap<String, Location>();

    public Spawn(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (spawnsHash.isEmpty()) {
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

            for(String key : spawns.getConfigurationSection("spawns").getKeys(false)){
                String world = spawns.getString("spawns." + key + ".world");
                int x = spawns.getInt("spawns." + key + ".x");
                int y = spawns.getInt("spawns." + key + ".y");
                int z = spawns.getInt("spawns." + key + ".z");
                float yaw = (float) spawns.getDouble("spawns." + key + ".yaw");
                float pitch = (float) spawns.getDouble("spawns." + key + ".pitch");
                Location worldSpawn = new Location(Bukkit.getWorld(world), x,y,z,yaw,pitch);
                spawnsHash.putIfAbsent(key, worldSpawn);
            }
        }
        Player p = (Player) sender;


        if (spawnsHash.containsKey(p.getWorld().getName())) {
            Location loc = (Location) spawnsHash.get(p.getWorld().getName());
            p.sendMessage(pl.badge + "Returning you to Spawn");
            p.teleport(loc);
        } else {
            p.sendMessage(pl.err + "No spawn found for this world");
        }
        return true;
    }
}
