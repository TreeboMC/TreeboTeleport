package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Back implements CommandExecutor {

    private TreeboTeleport pl;

    public Back(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (pl.lastLocConf.containsKey(p.getUniqueId())) {
                p.sendMessage(pl.badge + "Sending you to your previous location");
                p.teleport((Location) pl.lastLocConf.get(p.getUniqueId()));
            } else {
                p.sendMessage(pl.err + "Could not find previous location");
            }
        } else {
            sender.sendMessage(pl.badge + "This command can only be run by a player");
        }
        return true;
    }
}