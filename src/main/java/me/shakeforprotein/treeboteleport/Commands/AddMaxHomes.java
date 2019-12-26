package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;


public class AddMaxHomes {

    private TreeboTeleport pl;

    public AddMaxHomes(TreeboTeleport main) {
        this.pl = main;
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


    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Checks player permissions to determine current maximum homes, and increments by the second argument.");
                    this.setUsage("/AddMaxHomes <Player name> <Amount> - requires tbteleport.setmaxhomes");
                    this.setPermission("tbteleport.setmaxhomes");
                    if(sender.hasPermission(this.getPermission())) {
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
                    }
                    else{
                        sender.sendMessage(ChatColor.RED + "You do not have access to this command. You require permission node " + ChatColor.GOLD + this.getPermission());
                    }
                    return true;
                }
            };
            registerNewCommand(pl.getDescription().getName(), item2);
        }
        return true;
    }

    private void registerNewCommand(String fallback, BukkitCommand command) {
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register(fallback, command);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
    }
}
