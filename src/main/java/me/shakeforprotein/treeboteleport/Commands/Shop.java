package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Shop implements CommandExecutor{

    private TreeboTeleport pl;

    public Shop(TreeboTeleport main) {
        this.pl = main;
    }

    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Teleports player to the Shop");
                    this.setUsage("/shop - requires tbteleport.player.shop");
                    this.setPermission("tbteleport.player.shop");
                    if (sender.hasPermission(this.getPermission())) {

                        if (sender instanceof Player) {
                            Player p = (Player) sender;

                            if (pl.getConfig().isSet("shop." + p.getWorld().getName() + ".world")) {
                                String world = pl.getConfig().getString("shop." + p.getWorld().getName() + ".world");
                                double x = pl.getConfig().getDouble("shop." + p.getWorld().getName() + ".x");
                                double y = pl.getConfig().getDouble("shop." + p.getWorld().getName() + ".y");
                                double z = pl.getConfig().getDouble("shop." + p.getWorld().getName() + ".z");
                                float pitch = (float) pl.getConfig().getDouble("shop." + p.getWorld().getName() + ".pitch");
                                float yaw = (float) pl.getConfig().getDouble("shop." + p.getWorld().getName() + ".yaw");
                                Location shopLoc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
                                p.teleport(shopLoc);
                            }
                        } else {
                            sender.sendMessage(pl.err + "This command can only be run by a player.");
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

            if (pl.getConfig().isSet("shop." + p.getWorld().getName() + ".world")) {
                String world = pl.getConfig().getString("shop." + p.getWorld().getName() + ".world");
                double x = pl.getConfig().getDouble("shop." + p.getWorld().getName() + ".x");
                double y = pl.getConfig().getDouble("shop." + p.getWorld().getName() + ".y");
                double z = pl.getConfig().getDouble("shop." + p.getWorld().getName() + ".z");
                float pitch = (float) pl.getConfig().getDouble("shop." + p.getWorld().getName() + ".pitch");
                float yaw = (float) pl.getConfig().getDouble("shop." + p.getWorld().getName() + ".yaw");
                Location shopLoc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
                p.teleport(shopLoc);
            }
        } else {
            sender.sendMessage(pl.err + "This command can only be run by a player.");
        }
        return true;
    }
}
