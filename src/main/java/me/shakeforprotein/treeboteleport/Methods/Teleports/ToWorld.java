package me.shakeforprotein.treeboteleport.Methods.Teleports;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ToWorld {

    private TreeboTeleport pl;

    public ToWorld(TreeboTeleport main) {
        this.pl = main;
    }


    public void toWorld(String server, String toWorld, Player p) {
        if (p.hasPermission("tbteleport.server." + server)) {

            if (!server.equalsIgnoreCase(pl.getConfig().getString("serverName"))) {
                p.sendMessage("As you were on the wrong server, you will need to repeat the command.");
                p.sendMessage("Changing your server for you now.");
                pl.bungeeApi.connectOther(p.getName(), server);
            } else {
                p.teleport(Bukkit.getWorld(toWorld).getSpawnLocation());
            }
        } else {
            p.sendMessage(ChatColor.RED + "You do not have the required permission node ");
        }
    }
}
