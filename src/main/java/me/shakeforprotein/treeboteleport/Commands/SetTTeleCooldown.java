package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

public class SetTTeleCooldown implements CommandExecutor{

    private TreeboTeleport pl;

    public SetTTeleCooldown(TreeboTeleport main) {
        this.pl = main;
    }


    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Sets the cooldown for /wild");
                    this.setUsage("/setttelecooldown - requires tbteleport.admin.cooldowns");
                    this.setPermission("tbteleport.admin.cooldowns");
                    if (sender.hasPermission(this.getPermission())) {

                        if (args.length != 1) {
                            sender.sendMessage(pl.err + "This command expects a single argument <true|false|integer>");
                        } else {
                            if (args[0].equalsIgnoreCase("true") || args[0].equalsIgnoreCase("false")) {
                                pl.getConfig().set("useCooldowns", args[0]);
                                if (args[0].equalsIgnoreCase("true")) {
                                    sender.sendMessage(pl.badge + "useCooldowns enabled successfully, don't forget to run /ttelesaveconfig");
                                } else if (args[0].equalsIgnoreCase("false")) {
                                    sender.sendMessage(pl.badge + "useCooldowns disabled successfully, don't forget to run /ttelesaveconfig");
                                }
                            } else if (pl.isInteger(args[0])) {
                                pl.getConfig().set("CommandDelay", args[0]);
                                sender.sendMessage(pl.badge + "CommandDelay set to " + args[0] + " successfully, don't forget to run /ttelesaveconfig");
                            } else {
                                sender.sendMessage(pl.err + "Invalid input. Please use /setttelecooldown <true|false|integer>");
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
        if (args.length != 1) {
            sender.sendMessage(pl.err + "This command expects a single argument <true|false|integer>");
        } else {
            if (args[0].equalsIgnoreCase("true") || args[0].equalsIgnoreCase("false")) {
                pl.getConfig().set("useCooldowns", args[0]);
                if (args[0].equalsIgnoreCase("true")) {
                    sender.sendMessage(pl.badge + "useCooldowns enabled successfully, don't forget to run /ttelesaveconfig");
                } else if (args[0].equalsIgnoreCase("false")) {
                    sender.sendMessage(pl.badge + "useCooldowns disabled successfully, don't forget to run /ttelesaveconfig");
                }
            } else if (pl.isInteger(args[0])) {
                pl.getConfig().set("CommandDelay", args[0]);
                sender.sendMessage(pl.badge + "CommandDelay set to " + args[0] + " successfully, don't forget to run /ttelesaveconfig");
            } else {
                sender.sendMessage(pl.err + "Invalid input. Please use /setttelecooldown <true|false|integer>");
            }
        }
        return true;
    }
}
