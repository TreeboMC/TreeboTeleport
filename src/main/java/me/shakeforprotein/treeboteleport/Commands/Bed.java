package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Bed implements CommandExecutor {

    private TreeboTeleport pl;

    public Bed(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
            if (p.getBedSpawnLocation() != null) {
                p.sendMessage(pl.badge + "Sending you to your bed");
                pl.shakeTP(p, p.getBedSpawnLocation());
            } else {
                p.sendMessage(pl.err + "Bed Missing");
            }
        return true;
    }
}
