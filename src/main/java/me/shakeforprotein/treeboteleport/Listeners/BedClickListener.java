package me.shakeforprotein.treeboteleport.Listeners;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BedClickListener implements Listener {

    private TreeboTeleport pl;

    public BedClickListener(TreeboTeleport main) {
        this.pl = main;
    }

    @EventHandler
    private void onBedInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Material b = e.getClickedBlock().getType();

            if (b.toString().contains("_BED")) {

                File bedYml = new File(pl.getDataFolder(), File.separator + "beds.yml");
                if (!bedYml.exists()) {
                    try {
                        bedYml.createNewFile();
                        FileConfiguration bed = YamlConfiguration.loadConfiguration(bedYml);
                        try {
                            bed.options().copyDefaults();
                            bed.save(bedYml);
                        } catch (FileNotFoundException err) {
                            pl.makeLog(err);
                        }
                    } catch (IOException err) {
                        pl.makeLog(err);
                    }
                }

                FileConfiguration bed = YamlConfiguration.loadConfiguration(bedYml);
                Location loc = e.getClickedBlock().getLocation();
                String world = loc.getWorld().getName();
                double x = loc.getX();
                double y = loc.getY();
                double z = loc.getZ();

                bed.set("beds.UUID" + p.getUniqueId() + ".owner", p.getUniqueId());
                bed.set("beds.UUID" + p.getUniqueId() + ".name", p.getName());
                bed.set("beds.UUID" + p.getUniqueId() + ".world", world);
                bed.set("beds.UUID" + p.getUniqueId() + ".x", x);
                bed.set("beds.UUID" + p.getUniqueId() + ".y", y);
                bed.set("beds.UUID" + p.getUniqueId() + ".z", z);
                p.setBedSpawnLocation(loc, true);
                try {
                    bed.save(bedYml);
                    p.sendMessage(pl.badge + ChatColor.GREEN + "Bed Point Set");
                } catch (IOException err) {
                    pl.makeLog(err);
                }
            }
        }
    }
}