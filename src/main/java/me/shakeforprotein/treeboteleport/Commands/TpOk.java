package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class TpOk implements CommandExecutor {

    private TreeboTeleport pl;

    public TpOk(TreeboTeleport main){
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

            if (args.length == 0) {
                Player targetPlayer = null;

                if (pl.getConfig().get("tpRequest." + sender.getName()) != null && (System.currentTimeMillis() - 30000) < pl.getConfig().getLong("tpRequest." + sender.getName() + ".requestTime")) {
                    String type = pl.getConfig().getString("tpRequest." + sender.getName() + ".type");
                    String requester = pl.getConfig().getString("tpRequest." + sender.getName() + ".requester");
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getName().equalsIgnoreCase(requester)) {
                            targetPlayer = p;
                        }
                    }
                    if (targetPlayer != null) {
                        if (type.equalsIgnoreCase("toPlayer")) {
                            targetPlayer.teleport((Player) sender);
                            pl.getConfig().set("tpRequest." + sender.getName() + ".requestTime", 0);
                        } else if (type.equalsIgnoreCase("toSender")) {
                            ((Player) sender).teleport(targetPlayer);
                            pl.getConfig().set("tpRequest." + sender.getName() + ".requestTime", 0);
                        } else {
                            sender.sendMessage(pl.err + "Invalid teleport Type");
                        }
                    } else {
                        sender.sendMessage(pl.err + "Requesting player is not online");
                    }
                } else {
                    sender.sendMessage(pl.err + "Cannot find teleport request");
                }
            } else {
                sender.sendMessage(pl.err + "Incorrect usage. This command does not accept any arguments");
            }

        return true;
    }
}
