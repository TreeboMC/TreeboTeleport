package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.Bungee.BungeeSend;
import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.Iterator;


public class TpOk implements CommandExecutor{

    private TreeboTeleport pl;
    private BungeeSend bungeeSend;

    public TpOk(TreeboTeleport main) {
        this.pl = main;
        bungeeSend = new BungeeSend(pl);
    }

    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Accepts a teleport request");
                    this.setUsage("/tpok - requires tbteleport.player.tp");
                    this.setPermission("tbteleport.player.tp");
                    if (sender.hasPermission(this.getPermission())) {

                        if (args.length == 0) {
                            Player targetPlayer = null;

                            if (pl.getConfig().get("tpRequest." + sender.getName()) != null && (System.currentTimeMillis() - 30000) < pl.getConfig().getLong("tpRequest." + sender.getName() + ".requestTime")) {
                                String type = pl.getConfig().getString("tpRequest." + sender.getName() + ".type");
                                String requester = pl.getConfig().getString("tpRequest." + sender.getName() + ".requester");
                                Iterator iter = Bukkit.getOnlinePlayers().iterator();
                                while (iter.hasNext()) {
                                    Player p = (Player) iter.next();
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
                                    sender.sendMessage(pl.err + "Requesting player is not on this server. Attempting to locate.");
                                    sender.sendMessage("You will not receive a fail message.");
                                    if(type.equalsIgnoreCase("toPlayer")){


                                        bungeeSend.sendConnectOther(pl.getConfig().getString("general.serverName"), requester);
                                        Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
                                            @Override
                                            public void run() {
                                                Player telporter = Bukkit.getPlayer(requester);
                                                Player reciever = (Player) sender;

                                                telporter.teleport(reciever);
                                            }
                                        }, 40L);
                                    }
                                    else if (type.equalsIgnoreCase("toSender")){
                                        bungeeSend.sendPluginMessage("CrossServerTeleport", "ALL", sender.getName() + "," + requester);
                                    }
                                }
                            } else {
                                sender.sendMessage(pl.err + "Cannot find teleport request");
                            }
                        } else {
                            sender.sendMessage(pl.err + "Incorrect usage. This command does not accept any arguments");
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
            Player targetPlayer = null;

            if (pl.getConfig().get("tpRequest." + sender.getName()) != null && (System.currentTimeMillis() - 30000) < pl.getConfig().getLong("tpRequest." + sender.getName() + ".requestTime")) {
                String type = pl.getConfig().getString("tpRequest." + sender.getName() + ".type");
                String requester = pl.getConfig().getString("tpRequest." + sender.getName() + ".requester");
                Iterator iter = Bukkit.getOnlinePlayers().iterator();
                while (iter.hasNext()) {
                    Player p = (Player) iter.next();
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
                    sender.sendMessage(pl.err + "Requesting player is not on this server. Attempting to locate.");
                    sender.sendMessage("You will not receive a fail message.");
                    if(type.equalsIgnoreCase("toPlayer")){


                        bungeeSend.sendConnectOther(pl.getConfig().getString("general.serverName"), requester);
                        Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
                            @Override
                            public void run() {
                                Player telporter = Bukkit.getPlayer(requester);
                                Player reciever = (Player) sender;

                                telporter.teleport(reciever);
                            }
                        }, 40L);
                    }
                    else if (type.equalsIgnoreCase("toSender")){
                        bungeeSend.sendPluginMessage("CrossServerTeleport", "ALL", sender.getName() + "," + requester);
                    }
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