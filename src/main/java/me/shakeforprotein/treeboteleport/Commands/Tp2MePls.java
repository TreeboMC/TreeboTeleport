package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.Bungee.BungeeSend;
import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.Iterator;

public class Tp2MePls {

    private TreeboTeleport pl;
    private BungeeSend bungeeSend;

    public Tp2MePls(TreeboTeleport main) {
        this.pl = main;
        this.bungeeSend = new BungeeSend(pl);
    }

    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Requests a player teleport to your position");
                    this.setUsage("/tpahere - requires tbteleport.player.tp");
                    this.setPermission("tbteleport.player.tp");
                    if (sender.hasPermission(this.getPermission())) {

                        if (args.length == 1) {
                            boolean foundPlayer = false;

                            Iterator iter = Bukkit.getOnlinePlayers().iterator();
                            while (iter.hasNext()) {
                                Player p = (Player) iter.next();
                                if (p.getName().equalsIgnoreCase(args[0])) {
                                    foundPlayer = true;
                                    String command = "";
                                    if (pl.getConfig().get("tpRequest." + sender.getName()) == null || (System.currentTimeMillis() - 30000) > pl.getConfig().getLong("tpRequest." + sender.getName() + ".requestTime")) {

                                        if (pl.getConfig().get("tptoggle." + p.getName()) == null || pl.getConfig().getInt("tptoggle." + p.getName()) == 0) {
                                            sender.sendMessage(pl.badge + "Request to teleport " + p.getName() + " to your location sent.");
                                            p.sendMessage("Player " + ChatColor.GOLD + sender.getName() + ChatColor.RESET + " would like you to teleport " + ChatColor.GOLD + "TO THEM");
                                            command = "tellraw " + p.getName() + " [\"\",{\"text\":\"Please type \"},{\"text\":\"/tpok\",\"color\":\"green\"},{\"text\":\" or click \"},{\"text\":\"[HERE]\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tpok\"}},{\"text\":\" in the next \"},{\"text\":\"30 Seconds\",\"color\":\"green\"},{\"text\":\" to Accept\"}]";
                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                                            pl.getConfig().set("tpRequest." + p.getName() + ".type", "toSender");
                                            pl.getConfig().set("tpRequest." + p.getName() + ".requestTime", System.currentTimeMillis());
                                            pl.getConfig().set("tpRequest." + p.getName() + ".requester", sender.getName());
                                        } else {
                                            sender.sendMessage(pl.err + p.getName() + " has disabled incomming teleport requests");
                                        }
                                    } else {
                                        sender.sendMessage(pl.err + "You already have a pending teleport request.");
                                    }
                                }
                            }
                            if (!foundPlayer) {
                                sender.sendMessage(pl.err + "Player " + ChatColor.GOLD + args[0] + ChatColor.RESET + " was not found on this server. Attempting to locate on other servers");
                                bungeeSend.sendPluginMessage("CrossServerTPAHere", "ALL", "toSender," + sender.getName() + "," + args[0]);
                            }
                        } else {
                            sender.sendMessage(pl.err + "This command requires a single player argument");
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
}
