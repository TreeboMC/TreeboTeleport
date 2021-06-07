package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class SetShop implements CommandExecutor {

    private TreeboTeleport pl;

    public SetShop(TreeboTeleport main) {
        this.pl = main;
    }

    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Saves the location for the /shop command");
                    this.setUsage("/ttelereload - requires tbteleport.admin.setshop");
                    this.setPermission("tbteleport.admin.setshop");
                    if (sender.hasPermission(this.getPermission())) {

                        if (sender instanceof Player) {
                            Player p = (Player) sender;

                            String world = p.getWorld().getName();

                            Location pLoc = p.getLocation();
                            pl.getConfig().set("shop." + p.getWorld().getName() + ".world", world);
                            pl.getConfig().set("shop." + p.getWorld().getName() + ".x", pLoc.getX());
                            pl.getConfig().set("shop." + p.getWorld().getName() + ".y", pLoc.getY());
                            pl.getConfig().set("shop." + p.getWorld().getName() + ".z", pLoc.getZ());
                            pl.getConfig().set("shop." + p.getWorld().getName() + ".pitch", pLoc.getPitch());
                            pl.getConfig().set("shop." + p.getWorld().getName() + ".yaw", pLoc.getYaw());

                            sender.sendMessage(pl.badge + p.getWorld().getName() + "Shop set successfully, don't forget to run /ttelesaveconfig");
                        } else {
                            sender.sendMessage(pl.err + "This command can only be run as a player");
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

            String world = p.getWorld().getName();

            Location pLoc = p.getLocation();
            pl.getConfig().set("shop." + p.getWorld().getName() + ".world", world);
            pl.getConfig().set("shop." + p.getWorld().getName() + ".x", pLoc.getX());
            pl.getConfig().set("shop." + p.getWorld().getName() + ".y", pLoc.getY());
            pl.getConfig().set("shop." + p.getWorld().getName() + ".z", pLoc.getZ());
            pl.getConfig().set("shop." + p.getWorld().getName() + ".pitch", pLoc.getPitch());
            pl.getConfig().set("shop." + p.getWorld().getName() + ".yaw", pLoc.getYaw());

            sender.sendMessage(pl.badge + p.getWorld().getName() + "Shop set successfully, don't forget to run /ttelesaveconfig");
        } else {
            sender.sendMessage(pl.err + "This command can only be run as a player");
        }
        return true;
    }
}
