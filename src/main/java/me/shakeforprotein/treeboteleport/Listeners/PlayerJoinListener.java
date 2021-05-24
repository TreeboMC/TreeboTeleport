package me.shakeforprotein.treeboteleport.Listeners;

import me.shakeforprotein.treeboteleport.Bungee.BungeeSend;
import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class PlayerJoinListener implements Listener {

    private TreeboTeleport pl;
    private BungeeSend bungeeSend;

    public PlayerJoinListener(TreeboTeleport main) {
        this.pl = main;
        this.bungeeSend = new BungeeSend(pl);
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
                    Location spawnLoc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
                    e.getPlayer().teleport(spawnLoc);

                }
            }, 40L);
        }
        if (pl.getConfig().getBoolean("isHubServer")) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
                public void run() {
                    Player p = e.getPlayer();
                    Inventory inv = p.getInventory();
                    ItemStack hubItem = pl.getHubItemFromConfig();
                    if (!inv.contains(hubItem)) {
                        inv.addItem(hubItem);
                    }
                }
            }, 30L);
        }

        return true;
}

    @EventHandler
    public boolean onPlayerChangeWorld(PlayerChangedWorldEvent e) {
        if (pl.getConfig().get("tptoggle." + e.getPlayer().getName()) == null) {
            pl.getConfig().set("tptoggle." + e.getPlayer().getName(), 0);
        }
        if (pl.getConfig().isSet("onJoinSpawn." + e.getPlayer().getWorld().getName() + ".world")) {

            //System.out.println("Send to spawn triggered");
            String world = pl.getConfig().getString("onJoinSpawn." + e.getPlayer().getWorld().getName() + ".world");
            double x = pl.getConfig().getDouble("onJoinSpawn." + e.getPlayer().getWorld().getName() + ".x");
            double y = pl.getConfig().getDouble("onJoinSpawn." + e.getPlayer().getWorld().getName() + ".y");
            double z = pl.getConfig().getDouble("onJoinSpawn." + e.getPlayer().getWorld().getName() + ".z");
            float pitch = (float) pl.getConfig().getDouble("onJoinSpawn." + e.getPlayer().getWorld().getName() + ".pitch");
            float yaw = (float) pl.getConfig().getDouble("onJoinSpawn." + e.getPlayer().getWorld().getName() + ".yaw");
            Location spawnLoc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
            e.getPlayer().teleport(spawnLoc);

        } else {
            String world = e.getPlayer().getWorld().getName();
            System.out.println("Tried to send player: " + e.getPlayer().getName() + " : to spawn on world : " + world + " : but no spawn information was found in the config.yml");
        }
        return true;
    }

}
