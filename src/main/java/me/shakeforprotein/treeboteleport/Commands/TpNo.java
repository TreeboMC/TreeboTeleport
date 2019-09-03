package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class TpNo implements CommandExecutor {

    private TreeboTeleport pl;

    public TpNo(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if ((label.equalsIgnoreCase("tpno") && !pl.getConfig().getBoolean("disabledCommands.tpno")) ||
                (label.equalsIgnoreCase("tpdeny") && !pl.getConfig().getBoolean("disabledCommands.tpdeny")) ||
                (label.equalsIgnoreCase("ypcancel") && !pl.getConfig().getBoolean("disabledCommands.tpcancel"))
        ) {


            Player p = (Player) sender;
            pl.getConfig().set("tpRequest." + p.getName() + ".type", "toSender");
            pl.getConfig().set("tpRequest." + p.getName() + ".requestTime", 0);
            pl.getConfig().set("tpRequest." + p.getName() + ".requester", sender.getName());
            sender.sendMessage("Teleport request has been denied.");

            for (OfflinePlayer offPlayer : Bukkit.getOfflinePlayers()) {
                if (offPlayer.getName().equalsIgnoreCase(pl.getConfig().getString("tpRequest." + p.getName() + ".requester"))) {
                    if (offPlayer instanceof Player) {
                        ((Player) offPlayer).sendMessage(pl.badge + p.getName() + "has denied your teleport request.");
                    }
                }
            }
        }else {
            sender.sendMessage(pl.err + "The command /" + cmd + " has been disabled on this server");
        }
            return true;
        }
    }
