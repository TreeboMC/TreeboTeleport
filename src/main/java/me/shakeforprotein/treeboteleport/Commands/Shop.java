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
                Location shopLoc = (Location) pl.getConfig().get("shop." + p.getWorld().getName() + ".location");
                pl.shakeTP(p, shopLoc);
            }
        } else {
            sender.sendMessage(pl.err + "This command can only be run by a player.");
        }
        return true;
    }
}
