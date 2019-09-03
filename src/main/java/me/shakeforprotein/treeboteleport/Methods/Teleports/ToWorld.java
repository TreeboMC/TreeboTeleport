package me.shakeforprotein.treeboteleport.Methods.Teleports;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ToWorld {

    private TreeboTeleport pl;

    public ToWorld(TreeboTeleport main) {
        this.pl = main;
    }

    public void toWorld(String server, String toWorld, Player p) {
        String world = p.getWorld().getName();
        String thisServer = pl.getConfig().getString("general.serverName");

        if (server.equalsIgnoreCase(thisServer)) {
            if (world.equalsIgnoreCase(toWorld)) { //If world player is on has different name to world player wants
                if (Bukkit.getWorld(toWorld).getSpawnLocation() != null) {
                    p.teleport(Bukkit.getWorld(toWorld).getSpawnLocation());
                    p.sendMessage(pl.badge + "Sending you to world '" + toWorld + "' on server '" + server + "'.");
                } else {
                    p.sendMessage(pl.badge + "Unable to locate spawn coordinates for world '" + toWorld + "'. Please report this issue via /ticket");
                }
            } else {
                p.sendMessage(pl.badge + "You are already connected to this server and gamemode.");
            }
        } else {
            p.sendMessage(pl.badge + "Wrong server detected. Relocating you to the correct server.");
            pl.bungeeApi.connect(p, server);
        }
    }
}

