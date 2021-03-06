package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.Bungee.BungeeRecieve;
import me.shakeforprotein.treeboteleport.Bungee.BungeeSend;
import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Tp implements CommandExecutor, TabCompleter {

    private TreeboTeleport pl;
    private BungeeSend bungeeSend;
    private BungeeRecieve bungeeRecieve;

    public Tp(TreeboTeleport main) {
        this.pl = main;
        this.bungeeSend = new BungeeSend(pl);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 3 && (pl.isInteger(args[0]) && pl.isInteger(args[1]) && pl.isInteger(args[2]))) {

            String a1 = args[0];
            String a2 = args[1];
            String a3 = args[2];
            int i1 = Integer.parseInt(a1);
            int i2 = Integer.parseInt(a2);
            int i3 = Integer.parseInt(a3);
            Location tpLoc = ((Player) sender).getWorld().getBlockAt(i1, i2, i3).getLocation().add(0, 1, 0);
            ((Player) sender).teleport(tpLoc);

        } else if (args.length == 4 && (pl.isInteger(args[1]) && pl.isInteger(args[2]) && pl.isInteger(args[3]))) {
            if (Bukkit.getWorld(args[0]) != null) {
                String a1 = args[1];
                String a2 = args[2];
                String a3 = args[3];
                int i1 = Integer.parseInt(a1);
                int i2 = Integer.parseInt(a2);
                int i3 = Integer.parseInt(a3);
                Location tpLoc = ((Player) sender).getWorld().getBlockAt(i1, i2, i3).getLocation().add(0, 1, 0);
                ((Player) sender).teleport(tpLoc);
            } else {
                sender.sendMessage(pl.err + "No world found with name '" + args[0] + "'");
            }
        } else if (args.length == 2) {
            Player p1 = null;
            Player p2 = null;
            boolean found1 = false;
            boolean found2 = false;

            for (Player bukkitPlayer : Bukkit.getOnlinePlayers()) {
                if (bukkitPlayer.getName().equalsIgnoreCase(args[0])) {
                    found1 = true;
                    p1 = bukkitPlayer;
                }
                if (bukkitPlayer.getName().equalsIgnoreCase(args[1])) {
                    found2 = true;
                    p2 = bukkitPlayer;
                }
            }
            if (found1) {
                if (found2) {
                    if (!p1.hasPermission("tbteleport.admin.blocktp") || (p1.hasPermission("tbteleport.admin.blocktp") && sender.hasPermission("tbteleport.admin.blocktp"))) {
                        sender.sendMessage(pl.badge + "Sending " + p1.getName() + " to " + p2.getName());
                        p1.sendMessage(pl.badge + "Summoned to " + p2.getName() + " by " + sender.getName());
                        p2.sendMessage(pl.badge + p1.getName() + " has been summoned to your location by " + sender.getName());
                        p1.teleport(p2);
                    } else {
                        sender.sendMessage(pl.err + "You lack the authority to teleport this player.");
                    }
                } else {
                    sender.sendMessage(pl.err + "Player '" + args[1] + "' not found on this server.");
                }
            } else {
                sender.sendMessage(pl.err + "Player '" + args[0] + "' not found on this server.");
            }
        } else if (args.length == 1) {
            Player target = (Player) sender;
            boolean found = false;

            for (Player bukkitPlayer : Bukkit.getOnlinePlayers()) {
                if (bukkitPlayer.getName().equalsIgnoreCase(args[0])) {
                    found = true;
                    target = bukkitPlayer;
                }
            }
            if (found) {
                sender.sendMessage(pl.badge + "Sending you to " + target.getName());
                ((Player) sender).teleport(target.getLocation());
            } else {
                sender.sendMessage(pl.err + "Could not find player '" + args[0] + "' on this server, trying other servers.");
                bungeeSend.sendPluginMessage("CrossServerTeleport", "ALL", sender.getName() + "," + args[0]);
                sender.sendMessage("You will not receive a fail message for this.");
            }
        }
        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 0) {
            ArrayList<String> outputStrings = new ArrayList<>();

            for (String player : pl.fullPlayerList) {
                if (player.toLowerCase().startsWith(args[0].toLowerCase())) {
                    outputStrings.add(player);
                }
            }
            return outputStrings;
        }
        return null;
    }
}
