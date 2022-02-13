package me.shakeforprotein.treeboteleport.Listeners;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.io.IOException;

public class BedClickListener implements Listener {

    private TreeboTeleport pl;

    public BedClickListener(TreeboTeleport main) {
        this.pl = main;
    }

    @EventHandler
    private void onBedInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        //File homesYml = new File(pl.getDataFolder() + File.separator + "homes", File.separator + p.getUniqueId().toString() + ".yml");
        File homesYml = new File(pl.getPlayerDataFolder() + File.separator + p.getUniqueId().toString(), File.separator + "homes.yml");
        FileConfiguration homes = YamlConfiguration.loadConfiguration(homesYml);
        if (p.getWorld().getEnvironment() == World.Environment.NORMAL && !p.getWorld().getName().toLowerCase().contains("resource") && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Material b = e.getClickedBlock().getType();

            if (b.toString().contains("_BED")) {
                p.setBedSpawnLocation(e.getClickedBlock().getLocation());
                int x = p.getBedSpawnLocation().getBlockX();
                int y = p.getBedSpawnLocation().getBlockY() + 1;
                int z = p.getBedSpawnLocation().getBlockZ();
                double pitch = p.getBedSpawnLocation().getPitch();
                double yaw = p.getBedSpawnLocation().getYaw();
                String world = p.getBedSpawnLocation().getWorld().getName();
                homes.set("bed.x", x);
                homes.set("bed.y", y);
                homes.set("bed.z", z);
                homes.set("bed.pitch", pitch);
                homes.set("bed.yaw", yaw);
                homes.set("bed.world", world);
                try {
                    homes.save(homesYml);
                    p.sendMessage(pl.badge + "Bed Location Set.");
                }
                catch (IOException err){
                    p.sendMessage(pl.err + "Failed to save home");
                }
            }
        }
    }
}