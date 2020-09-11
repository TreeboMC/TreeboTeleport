package me.shakeforprotein.treeboteleport.Listeners;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;


public class PlayerTeleportListener implements Listener {

    private TreeboTeleport pl;

    public PlayerTeleportListener(TreeboTeleport main) {
        this.pl = main;
    }

    @EventHandler
    public void playerTeleport(PlayerTeleportEvent e) {


        if (e.getCause().name().equalsIgnoreCase("PLUGIN")) {

            Player p = e.getPlayer();
            if(p.isInsideVehicle()){
                Entity v = p.getVehicle();
                v.eject();
                Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
                    @Override
                    public void run() {
                        v.teleport(p.getLocation());
                        v.addPassenger(e.getPlayer());
                    }
                },20L);
            }

            //SAVE LAST LOCATION TO HASHMAP
            if (!(pl.lockMove.containsKey(p.getUniqueId()))) {
                pl.lastLocConf.putIfAbsent(p.getUniqueId(), e.getFrom());
                pl.lastLocConf.replace(p.getUniqueId(), e.getFrom());
                if (e.getPlayer().hasPermission("tbteleport.vipplus.back")) {
                    p.sendMessage(pl.badge + "Return point set to " + Math.floor(e.getFrom().getX()) + " " + Math.floor(e.getFrom().getY()) + " " + Math.floor(e.getFrom().getZ()));
                }
            }

            //LOCK PLAYERS IN PLACE IF TELEPORTED BY PLUGIN
            if (!pl.getConfig().getBoolean("general.disableTPSafety")) {
                if (pl.getConfig().getConfigurationSection("tpSafetyToggle") == null
                        || !pl.getConfig().getConfigurationSection("tpSafetyToggle").getKeys(false).contains(p.getName())
                        || !pl.getConfig().getConfigurationSection("tpSafetyToggle").getBoolean(p.getName())) {
                    if (pl.getConfig().get("teleportProtection") != null && pl.getConfig().getInt("teleportProtection") > 0) {
                        int tpProtection = pl.getConfig().getInt("teleportProtection") * 20;
                        pl.lockMove.putIfAbsent(e.getPlayer().getUniqueId(), e.getPlayer().getName());
                        e.getPlayer().setInvulnerable(true);
                        Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
                            public void run() {
                                e.getPlayer().setInvulnerable(false);
                                if (pl.lockMove.containsKey(e.getPlayer().getUniqueId())) {
                                    pl.lockMove.remove(e.getPlayer().getUniqueId());
                                }
                            }
                        }, tpProtection);
                        e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(pl.badge + ChatColor.BOLD + "As a safety feature you have been locked in place for " + pl.getConfig().getInt("teleportProtection") + " seconds. You can disable this with /disabletpsafety").create());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        Location from = e.getEntity().getLocation();
        Player p = e.getEntity();
        p.sendMessage(pl.badge + "Death Location: " + Math.floor(from.getX()) + " " + Math.floor(from.getY()) + " " + Math.floor(from.getZ()));

        pl.lastLocConf.putIfAbsent(p.getUniqueId(), from);
        pl.lastLocConf.replace(p.getUniqueId(), from);

    }

}