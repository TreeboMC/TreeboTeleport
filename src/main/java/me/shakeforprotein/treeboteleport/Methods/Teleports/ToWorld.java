package me.shakeforprotein.treeboteleport.Methods.Teleports;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ToWorld {

    private TreeboTeleport pl;

    public ToWorld(TreeboTeleport main) {
        this.pl = main;
    }

    public void toWorld(String server, String toWorld, Player p) {
        if (!server.equalsIgnoreCase(Bukkit.getServer().getName())) {
            p.sendMessage("As you were on the wrong server, you will need to repeat the command.");
            p.sendMessage("Changing your server for you now.");
            pl.bungeeApi.connectOther(p.getName(), server);
        }
        else {
            if (Bukkit.getWorld(toWorld) != null) {
                if (Bukkit.getWorld(toWorld).getSpawnLocation() != null) {
                    pl.shakeTP(p, Bukkit.getWorld(toWorld).getSpawnLocation());
                }
            }
        }
    }
}

