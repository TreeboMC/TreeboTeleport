package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class DisableTpSafety implements CommandExecutor{

    private TreeboTeleport pl;

    public DisableTpSafety(TreeboTeleport main) {
        this.pl = main;
    }


    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Disables teleport safety protocols.");
                    this.setUsage("/DisableTpSafety - requires tbteleport.player.tptoggle");
                    this.setPermission("tbteleport.player.tptoggle");
                    if (sender.hasPermission(this.getPermission())) {

                        if (sender instanceof Player) {
                            Player p = (Player) sender;

                            if (pl.getConfig().getInt("tpSafetyToggle." + p.getName()) == 0) {
                                pl.getConfig().set("tpSafetyToggle." + p.getName(), true);
                                p.sendMessage(pl.badge + "Teleport Safeties disabled. TreeboMC takes no responsibility for any death as a result of this. Be safe out there.");
                            }
                            else if(pl.getConfig().getBoolean("tpSafetyToggle." + p.getName())) {
                                pl.getConfig().set("tpSafetyToggle." + p.getName(), false);
                                p.sendMessage(pl.badge + "Teleport protection disabled.");
                            }
                            else{
                                pl.getConfig().set("tpSafetyToggle." + p.getName(), true);
                                p.sendMessage(pl.badge + "Teleport Safeties disabled. TreeboMC takes no responsibility for any death as a result of this. Be safe out there.");
                            }
                        } else {
                            sender.sendMessage(pl.err + "Only players may disable their teleport protection.");
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

            if (pl.getConfig().getInt("tpSafetyToggle." + p.getName()) == 0) {
                pl.getConfig().set("tpSafetyToggle." + p.getName(), true);
                p.sendMessage(pl.badge + "Teleport Safeties disabled. TreeboMC takes no responsibility for any death as a result of this. Be safe out there.");
            }
            else if(pl.getConfig().getBoolean("tpSafetyToggle." + p.getName())) {
                pl.getConfig().set("tpSafetyToggle." + p.getName(), false);
                p.sendMessage(pl.badge + "Teleport protection disabled.");
            }
            else{
                pl.getConfig().set("tpSafetyToggle." + p.getName(), true);
                p.sendMessage(pl.badge + "Teleport Safeties disabled. TreeboMC takes no responsibility for any death as a result of this. Be safe out there.");
            }
        } else {
            sender.sendMessage(pl.err + "Only players may disable their teleport protection.");
        }
        return true;
    }
}
