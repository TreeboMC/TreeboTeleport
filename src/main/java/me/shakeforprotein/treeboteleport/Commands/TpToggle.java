package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

public class TpToggle implements CommandExecutor{

    private TreeboTeleport pl;

    public TpToggle(TreeboTeleport main) {
        this.pl = main;
    }

    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Enables/Disables incomming teleport requests");
                    this.setUsage("/tptoggle - requires tbteleport.player.tp");
                    this.setPermission("tbteleport.player.tp");
                    if (sender.hasPermission(this.getPermission())) {

                        if (pl.getConfig().get("tptoggle." + pl.getName()) == null) {
                            pl.getConfig().set("tptoggle." + pl.getName(), 1);
                            sender.sendMessage(pl.badge + "Teleport requests have been toggled OFF");
                        } else if (pl.getConfig().getInt("tptoggle." + sender.getName()) == 1) {
                            pl.getConfig().set("tptoggle." + sender.getName(), 0);
                            sender.sendMessage(pl.badge + "Teleport requests have been toggled ON");
                        } else if (pl.getConfig().getInt("tptoggle." + sender.getName()) == 0) {
                            pl.getConfig().set("tptoggle." + sender.getName(), 1);
                            sender.sendMessage(pl.badge + "Teleport requests have been toggled OFF");
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
        if (pl.getConfig().get("tptoggle." + pl.getName()) == null) {
            pl.getConfig().set("tptoggle." + pl.getName(), 1);
            sender.sendMessage(pl.badge + "Teleport requests have been toggled OFF");
        } else if (pl.getConfig().getInt("tptoggle." + sender.getName()) == 1) {
            pl.getConfig().set("tptoggle." + sender.getName(), 0);
            sender.sendMessage(pl.badge + "Teleport requests have been toggled ON");
        } else if (pl.getConfig().getInt("tptoggle." + sender.getName()) == 0) {
            pl.getConfig().set("tptoggle." + sender.getName(), 1);
            sender.sendMessage(pl.badge + "Teleport requests have been toggled OFF");
        }
        return true;
    }
}
