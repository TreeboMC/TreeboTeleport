package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class Tp2Me implements CommandExecutor {

    private TreeboTeleport pl;

    public Tp2Me(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage(pl.err + "This command requires a player argument");
            } else if (args.length == 1) {
                Player targetPlayer = null;
                Iterator iter = Bukkit.getOnlinePlayers().iterator();
                while (iter.hasNext()) {
                    Player it = (Player) iter.next();
                    if (it.getName().equalsIgnoreCase(args[0])) {
                        targetPlayer = it;
                    }
                }
                if (targetPlayer != null) {
                    targetPlayer.teleport(player);
                } else {
                    sender.sendMessage(pl.err + "Unable to find player with name " + ChatColor.GREEN + args[0]);
                    sender.sendMessage("Are you sure they are on this server?");
                }
            } else {
                sender.sendMessage(pl.err + "This command only supports one argument");
            }
        }
        return true;
    }
}
