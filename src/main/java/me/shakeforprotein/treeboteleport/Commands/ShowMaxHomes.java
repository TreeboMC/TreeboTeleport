package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShowMaxHomes implements CommandExecutor {

    private TreeboTeleport pl;

    public ShowMaxHomes(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(pl.err + "Incorrect usage. Correct usage is /showmaxhomes <player name>");
        } else {
                Player p = Bukkit.getOfflinePlayer(args[0]).getPlayer();
                int currentMaxHomes = getHomes(p);
                sender.sendMessage("Player: " + p + " has a maximum of " + currentMaxHomes + "Homes");
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