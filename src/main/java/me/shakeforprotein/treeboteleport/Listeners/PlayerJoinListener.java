package me.shakeforprotein.treeboteleport.Listeners;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import me.shakeforprotein.treeboteleport.UpdateChecker.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;


public class PlayerJoinListener implements Listener {

    private TreeboTeleport pl;
    private UpdateChecker uc;

    public PlayerJoinListener(TreeboTeleport main) {
        this.pl = main;
        //    this.uc = new UpdateChecker(main);
    }

    @EventHandler
    public boolean onPlayerJoin(PlayerJoinEvent e) {
        if (pl.getConfig().isSet("onJoinSpawn." + e.getPlayer().getWorld().getName() + ".world")) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
                public void run() {
                    System.out.println("Send to spawn triggered");
                    String world = pl.getConfig().getString("onJoinSpawn." + e.getPlayer().getWorld().getName() + ".world");
                    double x = pl.getConfig().getDouble("onJoinSpawn." + e.getPlayer().getWorld().getName() + ".x");
                    double y = pl.getConfig().getDouble("onJoinSpawn." + e.getPlayer().getWorld().getName() + ".y");
                    double z = pl.getConfig().getDouble("onJoinSpawn." + e.getPlayer().getWorld().getName() + ".z");
                    float pitch = (float) pl.getConfig().getDouble("onJoinSpawn." + e.getPlayer().getWorld().getName() + ".pitch");
                    float yaw = (float) pl.getConfig().getDouble("onJoinSpawn." + e.getPlayer().getWorld().getName() + ".yaw");
                    Location spawnLoc = new Location(Bukkit.getWorld(world), x, y, z, pitch, yaw);
                    pl.shakeTP(e.getPlayer(), spawnLoc);

                }
            }, 40L);
        }
        if (pl.getConfig().getBoolean("isHubServer")) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
                public void run() {
                    Player p = e.getPlayer();
                    Inventory inv = p.getInventory();
                    ItemStack hubItem = pl.getHubItem();
                    if (!inv.contains(hubItem)) {
                        inv.addItem(hubItem);
                    }
                }
            }, 30L);
            if (e.getPlayer().hasPermission(uc.requiredPermission)) {
                if (!uc.updateNotified.containsKey(e.getPlayer())) {
                    uc.checkUpdates(e.getPlayer());
                    uc.updateNotified.putIfAbsent(e.getPlayer(), true);
                }
            }
        }


        return true;
    }

    @EventHandler
    public boolean onPlayerChangeWorld(PlayerChangedWorldEvent e) {
        if (pl.getConfig().get("tptoggle." + e.getPlayer().getName()) == null) {
            pl.getConfig().set("tptoggle." + e.getPlayer().getName(), 0);
        }
        if (pl.getConfig().isSet("onJoinSpawn." + e.getPlayer().getWorld().getName() + ".world")) {

            System.out.println("Send to spawn triggered");
            String world = pl.getConfig().getString("onJoinSpawn." + e.getPlayer().getWorld().getName() + ".world");
            double x = pl.getConfig().getDouble("onJoinSpawn." + e.getPlayer().getWorld().getName() + ".x");
            double y = pl.getConfig().getDouble("onJoinSpawn." + e.getPlayer().getWorld().getName() + ".y");
            double z = pl.getConfig().getDouble("onJoinSpawn." + e.getPlayer().getWorld().getName() + ".z");
            float pitch = (float) pl.getConfig().getDouble("onJoinSpawn." + e.getPlayer().getWorld().getName() + ".pitch");
            float yaw = (float) pl.getConfig().getDouble("onJoinSpawn." + e.getPlayer().getWorld().getName() + ".yaw");
            Location spawnLoc = new Location(Bukkit.getWorld(world), x, y, z, pitch, yaw);
            pl.shakeTP(e.getPlayer(), spawnLoc);

        } else {
            String world = e.getPlayer().getWorld().getName();
            System.out.println("Tried to send player: " + e.getPlayer().getName() + " : to spawn on world : " + world + " : but no spawn information was found in the config.yml");
        }
        return true;
    }
}
