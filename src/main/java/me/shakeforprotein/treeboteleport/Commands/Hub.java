package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.Methods.Guis.OpenHubMenu;
import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;


public class Hub implements CommandExecutor{

    private TreeboTeleport pl;
    private OpenHubMenu openHubMenu;

    public Hub(TreeboTeleport main) {
        this.pl = main;
        this.openHubMenu = new OpenHubMenu(pl);
    }

    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Opens the hub gui");
                    this.setUsage("/Hub - requires tbteleport.player.hub");
                    this.setPermission("tbteleport.player.hub");
                    if (sender.hasPermission(this.getPermission())) {

                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            String w = player.getWorld().getName();
                            if (args.length == 0) {
                                openHubMenu.openHubMenu((Player) sender);
                            } else {
                                sender.sendMessage(pl.err + "The HUB command does not support additional arguments");
                            }
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
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String w = player.getWorld().getName();
            if (args.length == 0) {
                openHubMenu.openHubMenu((Player) sender);
            } else {
                sender.sendMessage(pl.err + "The HUB command does not support additional arguments");
            }
        }
        return true;
    }
}
