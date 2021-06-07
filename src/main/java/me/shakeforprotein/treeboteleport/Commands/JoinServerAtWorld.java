package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.Bungee.BungeeSend;
import me.shakeforprotein.treeboteleport.Methods.Guis.OpenHubMenu;
import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class JoinServerAtWorld implements CommandExecutor{

    private TreeboTeleport pl;
    private BungeeSend bungeeSend;

    public JoinServerAtWorld(TreeboTeleport main) {
        this.pl = main;
        this.bungeeSend = new BungeeSend(pl);
    }

    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Super secret command");
                    this.setUsage("/jsaw - requires tbteleport.player.jsaw");
                    this.setPermission("tbteleport.player.jsaw");
                    if (sender.hasPermission(this.getPermission())) {

                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            if (args.length == 2) {
                                String server = args[0];
                                String world = args[1];
                                if (pl.getConfig().getString("general.serverName") != null && pl.getConfig().getString("general.serverName").equalsIgnoreCase(server)) {
                                    ((Player) sender).teleport(Bukkit.getWorld(world).getSpawnLocation());
                                } else {
                                    bungeeSend.sendConnectOther(server, sender.getName());
                                    bungeeSend.sendPluginMessage("Jsaw", server, world + "," + sender.getName());
                                }
                            } else {
                                sender.sendMessage(pl.badge + ChatColor.RED + "ERROR: " + ChatColor.RESET + "No Help is available for this command");
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
            if (args.length == 2) {
                String server = args[0];
                String world = args[1];
                if (pl.roots.getConfig().getString("General.ServerDetails.ServerName") != null && pl.roots.getConfig().getString("General.ServerDetails.ServerName").equalsIgnoreCase(server)) {
                    ((Player) sender).teleport(Bukkit.getWorld(world).getSpawnLocation());
                } else {
                    bungeeSend.sendConnectOther(server, sender.getName());
                    bungeeSend.sendPluginMessage("Jsaw", server, world + "," + sender.getName());
                }
            } else {
                sender.sendMessage(pl.badge + ChatColor.RED + "ERROR: " + ChatColor.RESET + "No Help is available for this command");
            }
        }
        return true;
    }
}