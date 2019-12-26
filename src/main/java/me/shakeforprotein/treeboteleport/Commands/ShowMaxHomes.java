package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class ShowMaxHomes {

    private TreeboTeleport pl;

    public ShowMaxHomes(TreeboTeleport main) {
        this.pl = main;
    }


    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Checks permissions to determine the number of homes a player is permitted");
                    this.setUsage("/ShowMaxHomes <player name> - requires tbteleport.setmaxhomes or tbteleport.getmaxhomes");
                    this.setPermission("tbteleport.getmaxhomes");
                    if (sender.hasPermission(this.getPermission()) || sender.hasPermission("tbteleport.setmaxhomes")) {

                        if (args.length != 1) {
                            sender.sendMessage(pl.err + "Incorrect usage. Correct usage is /showmaxhomes <player name>");
                        } else {
                            Player p = Bukkit.getOfflinePlayer(args[0]).getPlayer();
                            int currentMaxHomes = getHomes(p);
                            sender.sendMessage("Player: " + p + " has a maximum of " + currentMaxHomes + "Homes");
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

    private int getHomes(Player p) {
        int i = 100;
        int maxHomes = 1;
        while (i > 0) {
            //System.out.println("Maxhomes " + i);
            i--;
            if (p.hasPermission("tbteleport.maxhomes." + i)) {
                maxHomes = i;
                break;
            }
        }
        return maxHomes;
    }
}