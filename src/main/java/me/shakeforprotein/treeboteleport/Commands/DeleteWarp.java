package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DeleteWarp implements CommandExecutor {

    private TreeboTeleport pl;

    public DeleteWarp(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            File warpsYml = new File(pl.getDataFolder(), "warps.yml");
            YamlConfiguration warps = YamlConfiguration.loadConfiguration(warpsYml);

            Player p = (Player) sender;

            if (args.length == 0) {
                p.sendMessage(pl.err + "You must provide the warp name to delete");
            } else if (args.length > 1) {
                p.sendMessage(pl.err + "Too many arguments");
            } else {
                args[0] = args[0].toLowerCase();
                warps.set("warps." + args[0] + ".name", null);
                warps.set("warps." + args[0] + ".world", null);
                warps.set("warps." + args[0] + ".x", null);
                warps.set("warps." + args[0] + ".y", null);
                warps.set("warps." + args[0] + ".z", null);
                warps.set("warps." + args[0] + ".pitch", null);
                warps.set("warps." + args[0] + ".yaw", null);
                warps.set("warps." + args[0], null);
                try {
                    warps.save(warpsYml);
                    p.sendMessage(pl.badge + "If a warp existed with name: " + ChatColor.GOLD + args[0] + ChatColor.RESET + " it has now been deleted.");
                } catch (IOException e) {
                    pl.makeLog(e);
                    p.sendMessage(pl.err + "Saving Warps file unsuccessful");
                }
            }
        return true;
    }
}
