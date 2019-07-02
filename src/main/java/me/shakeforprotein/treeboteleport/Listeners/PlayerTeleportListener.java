package me.shakeforprotein.treeboteleport.Listeners;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
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

            //SAVE LAST LOCATION TO HASHMAP
            if(!(pl.lockMove.containsKey(p.getUniqueId()))) {
                pl.lastLocConf.putIfAbsent(p.getUniqueId(), e.getFrom());
                pl.lastLocConf.replace(p.getUniqueId(), e.getFrom());
                if (e.getPlayer().hasPermission("tbteleport.vipplus.back")) {
                    p.sendMessage(pl.badge + "Return point set to " + Math.floor(e.getFrom().getX()) + " " + Math.floor(e.getFrom().getY()) + " " + Math.floor(e.getFrom().getZ()));
                }
            }

            //LOCK PLAYERS IN PLACE IF TELEPORTED BY PLUGIN
            if (pl.getConfig().get("teleportProtection") != null && pl.getConfig().getInt("teleportProtection") > 0 && !(pl.tpSafetyOff.containsKey(e.getPlayer().getUniqueId()))) {
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
                e.getPlayer().sendMessage(pl.badge + "As a safety feature you have been locked in place for " + pl.getConfig().getInt("teleportProtection") + " seconds.");
                e.getPlayer().sendMessage("You can at your own risk disable this protection with /disabletpsafety");
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