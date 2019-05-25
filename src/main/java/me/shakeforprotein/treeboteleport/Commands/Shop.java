package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Shop implements CommandExecutor {

    private TreeboTeleport pl;

    public Shop(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (pl.getConfig().isSet("shop." + p.getWorld().getName() + ".world")) {
                String world = pl.getConfig().getString("shop." + p.getWorld().getName() + ".world");
                int x = Math.toIntExact(pl.getConfig().getInt("shop." + p.getWorld().getName() + ".x"));
                int y = Math.toIntExact(pl.getConfig().getInt("shop." + p.getWorld().getName() + ".y"));
                int z = Math.toIntExact(pl.getConfig().getInt("shop." + p.getWorld().getName() + ".z"));
                float pitch = Math.toIntExact(pl.getConfig().getInt("shop." + p.getWorld().getName() + ".pitch"));
                float yaw = Math.toIntExact(pl.getConfig().getInt("shop." + p.getWorld().getName() + ".yaw"));
                Location shopLoc = new Location(Bukkit.getWorld(world), x, y, z, pitch, yaw);
                p.teleport(shopLoc);
            }
        } else {
            sender.sendMessage(pl.err + "This command can only be run by a player.");
        }
        return true;
    }
}
