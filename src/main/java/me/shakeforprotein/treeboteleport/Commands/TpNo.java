package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;


public class TpNo {

    private TreeboTeleport pl;

    public TpNo(TreeboTeleport main) {
        this.pl = main;
    }

    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Denies a teleport request");
                    this.setUsage("/tpno - requires tbteleport.player.tp");
                    this.setPermission("tbteleport.player.tp");
                    if (sender.hasPermission(this.getPermission())) {

                        Player p = (Player) sender;
                        pl.getConfig().set("tpRequest." + p.getName() + ".type", "toSender");
                        pl.getConfig().set("tpRequest." + p.getName() + ".requestTime", 0);
                        pl.getConfig().set("tpRequest." + p.getName() + ".requester", sender.getName());
                        sender.sendMessage("Teleport request has been denied.");

                        for (OfflinePlayer offPlayer : Bukkit.getOfflinePlayers()) {
                            if (offPlayer.getName().equalsIgnoreCase(pl.getConfig().getString("tpRequest." + p.getName() + ".requester"))) {
                                if (offPlayer instanceof Player) {
                                    ((Player) offPlayer).sendMessage(pl.badge + p.getName() + "has denied your teleport request.");
                                }
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
}
