package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;

public class Tp2MePls implements CommandExecutor {

    private TreeboTeleport pl;

    public Tp2MePls(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            boolean foundPlayer = false;
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().equalsIgnoreCase(args[0])) {
                    foundPlayer = true;
                    if (pl.getConfig().get("tptoggle." + p.getName()) == null || pl.getConfig().getInt("tptoggle." + p.getName()) == 0) {
                        sender.sendMessage(pl.badge + "Request to teleport " + p.getName() + " to your location sent.");
                        p.sendMessage("Player " + ChatColor.GOLD + sender.getName() + ChatColor.RESET + " would like you to teleport " + ChatColor.GOLD + "TO THEM");
                        //p.sendMessage("Please type " + ChatColor.GREEN + "/tpok " + ChatColor.RESET + "in the next " + ChatColor.GREEN + " 30 Seconds" + ChatColor.RESET + " to Accept" );
                        String command = "tellraw " + p.getName() + " [\"\",{\"text\":\"Please type \"},{\"text\":\"/tpok\",\"color\":\"green\"},{\"text\":\" or click \"},{\"text\":\"[HERE]\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tpok\"}},{\"text\":\" in the next \"},{\"text\":\"30 Seconds\",\"color\":\"green\"},{\"text\":\" to Accept\"}]";
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                        pl.getConfig().set("tpRequest." + p.getName() + ".type", "toSender");
                        pl.getConfig().set("tpRequest." + p.getName() + ".requestTime", System.currentTimeMillis());
                        pl.getConfig().set("tpRequest." + p.getName() + ".requester", sender.getName());
                    } else {
                        sender.sendMessage(pl.err + p.getName() + " has disabled incomming teleport requests");
                    }
                }
            }
            if (!foundPlayer) {
                sender.sendMessage(pl.err + "Player " + ChatColor.GOLD + args[0] + ChatColor.RESET + " is not online on this server. Please check spelling");
            }
        } else {
            sender.sendMessage(pl.err + "This command requires a single player argument");
        }
        return true;
    }
}
