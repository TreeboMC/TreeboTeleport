package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class SetVanillaWorldSpawn {

    private TreeboTeleport pl;

    public SetVanillaWorldSpawn(TreeboTeleport main) {
        this.pl = main;
    }

    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Sets the Vanilla world spawn.");
                    this.setUsage("/sws - requires tbteleport.admin");
                    this.setPermission("tbteleport.admin");
                    if (sender.hasPermission(this.getPermission())) {

                        if (sender instanceof Player) {
                            World w = ((Player) sender).getWorld();
                            sender.sendMessage(pl.badge + "Current Vanilla World Spawn: " + w.getSpawnLocation().toString());
                            Location pLoc = ((Player) sender).getLocation();
                            w.setSpawnLocation(pLoc);
                            sender.sendMessage(pl.badge + "Set Vanilla World Spawn to: " + w.getSpawnLocation().toString());
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
