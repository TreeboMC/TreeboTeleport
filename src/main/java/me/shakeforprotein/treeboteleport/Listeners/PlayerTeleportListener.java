package me.shakeforprotein.treeboteleport.Listeners;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
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
        //e.getPlayer().sendMessage(e.getCause().name());
        if (e.getCause().name().equalsIgnoreCase("PLUGIN")) {
            Player p = e.getPlayer();

            pl.lastLocConf.putIfAbsent(p.getUniqueId(), e.getFrom());
            pl.lastLocConf.replace(p.getUniqueId(), e.getFrom());
            if (e.getPlayer().hasPermission("tbteleport.vipplus.back")) {
                p.sendMessage(pl.badge + "Return point set to " + Math.floor(e.getFrom().getX()) + " " + Math.floor(e.getFrom().getY()) + " " + Math.floor(e.getFrom().getZ()));
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