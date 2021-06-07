package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class Bed implements CommandExecutor {

    private TreeboTeleport pl;

    public Bed(TreeboTeleport main) {
        this.pl = main;
    }

    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Returns player to the last bed they interacted with if it still exists");
                    this.setUsage("/bed - requires tbteleport.player.bed");
                    this.setPermission("tbteleport.player.bed");

                    if (sender.hasPermission(this.getPermission())) {

                        if (sender instanceof Player) {
                            Player p = (Player) sender;

                            if (p.getBedSpawnLocation() != null) {
                                p.sendMessage(pl.badge + "Sending you to your bed");
                                p.teleport(p.getBedSpawnLocation());
                            } else {
                                p.sendMessage(pl.err + "Bed Missing");
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
            Player p = (Player) sender;

            if (p.getBedSpawnLocation() != null) {
                p.sendMessage(pl.badge + "Sending you to your bed");
                p.teleport(p.getBedSpawnLocation());
            } else {
                p.sendMessage(pl.err + "Bed Missing");
            }
        }
        return true;
    }
}
