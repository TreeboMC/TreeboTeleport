package me.shakeforprotein.treeboteleport.Listeners;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RespawnListener implements Listener {

    private TreeboTeleport pl;

    public RespawnListener(TreeboTeleport main){
        this.pl = main;
    }

    @EventHandler
    public boolean onRespawnEvent(PlayerRespawnEvent e){
        Player p = e.getPlayer();
        if(p.getBedSpawnLocation() != null){
            p.sendMessage(pl.badge + "Sending you to your bed");
            pl.shakeTP(p, p.getBedSpawnLocation());
        }
        else{
            File spawnsYml = new File(pl.getDataFolder(), File.separator + "spawns.yml");
            if (!spawnsYml.exists()) {
                try {
                    spawnsYml.createNewFile();
                    FileConfiguration spawns = YamlConfiguration.loadConfiguration(spawnsYml);
                    try {
                        spawns.options().copyDefaults();
                        spawns.save(spawnsYml);
                    } catch (FileNotFoundException err) {
                        pl.makeLog(err);
                    }
                } catch (IOException err) {
                    pl.makeLog(err);
                }
            }
            FileConfiguration spawns = YamlConfiguration.loadConfiguration(spawnsYml);
            String world = p.getWorld().getName();

            if(spawns.get("spawns." + world + ".x") != null){
                String confWorld = spawns.getString("spawns." + world + ".world");
                double x = spawns.getDouble("spawns." + world + ".x");
                double y = spawns.getDouble("spawns." + world + ".y");
                double z = spawns.getDouble("spawns." + world + ".z");
                float pitch = (float) spawns.getDouble("spawns." + world + ".pitch");
                float yaw = (float) spawns.getDouble("spawns." + world + ".yaw");
                Location loc = new Location(Bukkit.getWorld(confWorld),x,y,z,yaw,pitch);
                p.sendMessage(pl.badge + "Returning you to Spawn");
                pl.shakeTP(p,loc);
            }

        }
        return true;
    }

}
