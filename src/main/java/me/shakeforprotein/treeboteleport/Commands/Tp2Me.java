package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Tp2Me implements CommandExecutor {

    private TreeboTeleport pl;

    public Tp2Me(TreeboTeleport main){
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (args.length == 0) {
                sender.sendMessage(pl.err + "This command requires a player argument");
            } else if (args.length == 1) {
                Player targetPlayer = null;
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getName().equalsIgnoreCase(args[0])) {
                        targetPlayer = p;
                        break;
                    }
                }
                if (targetPlayer != null) {
                    targetPlayer.teleport((Player) sender);
                } else {
                    sender.sendMessage(pl.err + "Unable to find player with name " + ChatColor.GREEN + args[0]);
                    sender.sendMessage("Are you sure they are on this server?");
                }
            } else {
                sender.sendMessage(pl.err + "This command only supports one argument");
            }
        return true;
    }
}
