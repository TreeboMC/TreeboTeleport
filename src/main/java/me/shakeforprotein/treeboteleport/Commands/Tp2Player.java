package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class Tp2Player implements CommandExecutor{

    private TreeboTeleport pl;

    public Tp2Player(TreeboTeleport main) {
        this.pl = main;
    }

    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Teleports you to the target player");
                    this.setUsage("/tp <player> - requires tbteleport.staff.tp");
                    this.setPermission("tbteleport.staff.tp");
                    if (sender.hasPermission(this.getPermission())) {

                        if (args.length == 0) {
                            sender.sendMessage(pl.err + "This command requires a player argument");
                        } else if (args.length == 1) {
                            Player targetPlayer = null;
                            Iterator iter = Bukkit.getOnlinePlayers().iterator();
                            while (iter.hasNext()) {
                                Player p = (Player) iter.next();
                                if (p.getName().equalsIgnoreCase(args[0])) {
                                    targetPlayer = p;
                                    break;
                                }
                            }
                            if (targetPlayer != null) {
                                ((Player) sender).teleport(targetPlayer);
                            } else {
                                sender.sendMessage(pl.err + "Unable to find player with name " + ChatColor.GOLD + args[0]);
                                sender.sendMessage("Are you sure they are on this server?");
                            }
                        } else {
                            sender.sendMessage(pl.err + "This command only supports one argument");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You do not have access to this command. You require permission node " + ChatColor.GOLD + this.getPermission());
                    }
                    return true;
                }
            };
            pl.registerNewCommand(pl.getDescription().getName(), item2);
        }
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(pl.err + "This command requires a player argument");
        } else if (args.length == 1) {
            Player targetPlayer = null;
            Iterator iter = Bukkit.getOnlinePlayers().iterator();
            while (iter.hasNext()) {
                Player p = (Player) iter.next();
                if (p.getName().equalsIgnoreCase(args[0])) {
                    targetPlayer = p;
                    break;
                }
            }
            if (targetPlayer != null) {
                ((Player) sender).teleport(targetPlayer);
            } else {
                sender.sendMessage(pl.err + "Unable to find player with name " + ChatColor.GOLD + args[0]);
                sender.sendMessage("Are you sure they are on this server?");
            }
        } else {
            sender.sendMessage(pl.err + "This command only supports one argument");
        }
        return true;
    }
}
