package me.shakeforprotein.treeboteleport.Listeners;

import me.shakeforprotein.treeboteleport.Bungee.BungeeSend;
import me.shakeforprotein.treeboteleport.TreeboTeleport;
import net.minecraft.server.v1_16_R2.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class PlayerMoveListener implements Listener {

    public TreeboTeleport pl;
    private HashMap<Player, Long> afkHash = new HashMap<>();
    private HashMap<Player, Location> locationHash = new HashMap<>();
    private BungeeSend bungeeSend;

    public PlayerMoveListener(TreeboTeleport main){
        this.pl = main;
        bungeeSend = new BungeeSend(pl);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        if(pl.lockMove.containsKey(e.getPlayer().getUniqueId())){
            e.setTo(e.getFrom());
        }

        if(pl.getConfig().getBoolean("doAfkKick")){
            if(afkHash == null || !afkHash.containsKey(e.getPlayer())){
                afkHash.put(e.getPlayer(), System.currentTimeMillis());
                locationHash.put(e.getPlayer(), e.getTo());
            } else if(e.getPlayer().getWorld() != locationHash.get(e.getPlayer()).getWorld()){
                afkHash.replace(e.getPlayer(), System.currentTimeMillis());
                locationHash.replace(e.getPlayer(), e.getTo());
            }
            else{
                Double x, z, x1, x2, z1, z2;
                x1 = Math.floor(locationHash.get(e.getPlayer()).getX());
                x2 = Math.floor(e.getTo().getX());
                z1 = Math.floor(locationHash.get(e.getPlayer()).getZ());
                z2 = Math.floor(e.getTo().getZ());
                x = x2 - x1;
                z = z1 - z2;
                if(Math.abs(x*z) > 1000){
                    afkHash.replace(e.getPlayer(), System.currentTimeMillis());
                    locationHash.replace(e.getPlayer(), e.getTo());
                }
            }
        }
    }

    @EventHandler
    public void playerDisconnect(PlayerQuitEvent e){
        if(afkHash.containsKey(e.getPlayer())){afkHash.remove(e.getPlayer());
        locationHash.remove(e.getPlayer());}
    }

    @EventHandler
    public void playerChangeWorld(PlayerChangedWorldEvent e){
        afkHash.put(e.getPlayer(), System.currentTimeMillis());
        locationHash.put(e.getPlayer(), e.getPlayer().getLocation());
    }
}
