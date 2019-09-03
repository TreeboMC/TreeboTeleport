package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class AddMaxHomes implements CommandExecutor {

    private TreeboTeleport pl;

    public AddMaxHomes(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!pl.getConfig().getBoolean("disabledCommands.addmaxhomes")) {

            if (args.length != 2) {
                sender.sendMessage(pl.err + "Incorrect usage. Correct usage is /addmaxhomes <player name> <amount>");
            } else {
                if (pl.isInteger(args[1])) {
                    Player p = Bukkit.getOfflinePlayer(args[0]).getPlayer();
                    int currentMaxHomes = getHomes(p);
                    int newHomes = currentMaxHomes + Integer.parseInt(args[1]);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + p.getName() + " add tbteleport.maxhomes." + newHomes);
                    sender.sendMessage(pl.badge + "Successfully set " + p.getName() + "'s maximum homes to " + getHomes(p));
                } else {
                    sender.sendMessage(pl.err + "Second argument must be a number");
                }
            }
        }else {
            sender.sendMessage(pl.err + "The command /" + cmd + " has been disabled on this server");
        }
        return true;
    }

    private int getHomes(Player p) {
        int i = 100;
        int maxHomes = 1;
        while (i > 0) {
            System.out.println("Maxhomes " + i);
            i--;
            if (p.hasPermission("tbteleport.maxhomes." + i)) {
                maxHomes = i;
                break;
            }
        }
        return maxHomes;
    }
}
