package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class DisableTpSafety {

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
                            //If hash contains player id then tp protection is currently disabled. As such, we remove the player id to re enable protection.
                            if (pl.tpSafetyOff.containsKey(p.getUniqueId())) {
                                pl.tpSafetyOff.remove(p.getUniqueId());
                                p.sendMessage(pl.badge + "Teleport protection enabled.");
                            } else {
                                pl.tpSafetyOff.put(p.getUniqueId(), p.getName());
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
}
