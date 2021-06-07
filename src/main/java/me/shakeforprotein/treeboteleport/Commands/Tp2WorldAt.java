package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class Tp2WorldAt implements CommandExecutor{

    private TreeboTeleport pl;

    public Tp2WorldAt(TreeboTeleport main) {
        this.pl = main;
    }

    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Does a thing");
                    this.setUsage("/tp2worldat - requires tbteleport.admin");
                    this.setPermission("tbteleport.admin");
                    if (sender.hasPermission(this.getPermission())) {

                        try {
                            if (args.length == 2) {
                                if (args[1].split(",").length == 3) {
                                    String a1 = args[1].split(",")[0];
                                    String a2 = args[1].split(",")[1];
                                    String a3 = args[1].split(",")[2];
                                    if (pl.isInteger(a1) && pl.isInteger(a2) && pl.isInteger(a3)) {
                                        int i1 = Integer.parseInt(a1);
                                        int i2 = Integer.parseInt(a2);
                                        int i3 = Integer.parseInt(a3);
                                        Location tpLoc = Bukkit.getWorld(args[0]).getBlockAt(i1, i2, i3).getLocation().add(0, 1, 0);
                                        ((Player) sender).teleport(tpLoc);
                                    } else {
                                        sender.sendMessage(pl.err + "Invalid coordinate format or world name");
                                    }
                                } else if (args[1].split(",").length == 2) {
                                    String a1 = args[1].split(",")[0];
                                    String a3 = args[1].split(",")[1];
                                    if (pl.isInteger(a1) && pl.isInteger(a3)) {
                                        int i1 = Integer.parseInt(a1);
                                        int i3 = Integer.parseInt(a3);
                                        int i2 = Bukkit.getWorld(args[0]).getHighestBlockYAt(i1, i3);
                                        Location tpLoc = Bukkit.getWorld(args[0]).getBlockAt(i1, i2, i3).getLocation().add(0, 1, 0);
                                        ((Player) sender).teleport(tpLoc);
                                    } else {
                                        sender.sendMessage(pl.err + "Invalid coordinate format or world name");
                                    }
                                }
                            } else if (args.length == 3) {
                                if (pl.isInteger(args[1]) && pl.isInteger(args[2])) {
                                    int i1 = Integer.parseInt(args[1]);
                                    int i3 = Integer.parseInt(args[2]);
                                    int i2 = Bukkit.getWorld(args[0]).getHighestBlockYAt(i1, i3);
                                    Location tpLoc = Bukkit.getWorld(args[0]).getBlockAt(i1, i2, i3).getLocation().add(0, 1, 0);
                                    ((Player) sender).teleport(tpLoc);
                                } else {
                                    sender.sendMessage(pl.err + " Invalid Coordinate format or world name");
                                }
                            } else if (args.length == 4) {
                                if (pl.isInteger(args[3]) && pl.isInteger(args[1]) && pl.isInteger(args[2])) {
                                    int i1 = Integer.parseInt(args[1]);
                                    int i2 = Integer.parseInt(args[2]);
                                    int i3 = Integer.parseInt(args[3]);
                                    Location tpLoc = Bukkit.getWorld(args[0]).getBlockAt(i1, i2, i3).getLocation().add(0, 1, 0);
                                    ((Player) sender).teleport(tpLoc);
                                } else {
                                    sender.sendMessage(pl.err + " Invalid coordinate format or world name");
                                }
                            }
                        } catch (NullPointerException ex) {
                            pl.roots.errorLogger.logError(pl, ex);
                            sender.sendMessage(pl.err + "Could not achieve teleport. Is the world loaded?");
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
        try {
            if (args.length == 2) {
                if (args[1].split(",").length == 3) {
                    String a1 = args[1].split(",")[0];
                    String a2 = args[1].split(",")[1];
                    String a3 = args[1].split(",")[2];
                    if (pl.isInteger(a1) && pl.isInteger(a2) && pl.isInteger(a3)) {
                        int i1 = Integer.parseInt(a1);
                        int i2 = Integer.parseInt(a2);
                        int i3 = Integer.parseInt(a3);
                        Location tpLoc = Bukkit.getWorld(args[0]).getBlockAt(i1, i2, i3).getLocation().add(0, 1, 0);
                        ((Player) sender).teleport(tpLoc);
                    } else {
                        sender.sendMessage(pl.err + "Invalid coordinate format or world name");
                    }
                } else if (args[1].split(",").length == 2) {
                    String a1 = args[1].split(",")[0];
                    String a3 = args[1].split(",")[1];
                    if (pl.isInteger(a1) && pl.isInteger(a3)) {
                        int i1 = Integer.parseInt(a1);
                        int i3 = Integer.parseInt(a3);
                        int i2 = Bukkit.getWorld(args[0]).getHighestBlockYAt(i1, i3);
                        Location tpLoc = Bukkit.getWorld(args[0]).getBlockAt(i1, i2, i3).getLocation().add(0, 1, 0);
                        ((Player) sender).teleport(tpLoc);
                    } else {
                        sender.sendMessage(pl.err + "Invalid coordinate format or world name");
                    }
                }
            } else if (args.length == 3) {
                if (pl.isInteger(args[1]) && pl.isInteger(args[2])) {
                    int i1 = Integer.parseInt(args[1]);
                    int i3 = Integer.parseInt(args[2]);
                    int i2 = Bukkit.getWorld(args[0]).getHighestBlockYAt(i1, i3);
                    Location tpLoc = Bukkit.getWorld(args[0]).getBlockAt(i1, i2, i3).getLocation().add(0, 1, 0);
                    ((Player) sender).teleport(tpLoc);
                } else {
                    sender.sendMessage(pl.err + " Invalid Coordinate format or world name");
                }
            } else if (args.length == 4) {
                if (pl.isInteger(args[3]) && pl.isInteger(args[1]) && pl.isInteger(args[2])) {
                    int i1 = Integer.parseInt(args[1]);
                    int i2 = Integer.parseInt(args[2]);
                    int i3 = Integer.parseInt(args[3]);
                    Location tpLoc = Bukkit.getWorld(args[0]).getBlockAt(i1, i2, i3).getLocation().add(0, 1, 0);
                    ((Player) sender).teleport(tpLoc);
                } else {
                    sender.sendMessage(pl.err + " Invalid coordinate format or world name");
                }
            }
        } catch (NullPointerException ex) {
            pl.roots.errorLogger.logError(pl, ex);
            sender.sendMessage(pl.err + "Could not achieve teleport. Is the world loaded?");
        }
        return true;
    }
}
