package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.Methods.Guis.OpenWarpsMenu;
import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class WarpTo implements CommandExecutor {

    private TreeboTeleport pl;
    private OpenWarpsMenu openWarpsMenu;

    public WarpTo(TreeboTeleport main) {
        this.pl = main;
        openWarpsMenu = new OpenWarpsMenu(pl);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        File warpsYml = new File(pl.getDataFolder(), "warps.yml");
        if (!warpsYml.exists()) {
            System.out.println(pl.err + "Warps data not found. Attempting to recover");
            try {
                warpsYml.createNewFile();
                FileConfiguration warps = YamlConfiguration.loadConfiguration(warpsYml);
                try {
                    warps.options().copyDefaults();
                    warps.save(warpsYml);
                } catch (FileNotFoundException e) {
                    pl.makeLog(e);
                }
            } catch (IOException e) {
                pl.makeLog(e);
                System.out.println(pl.err + "Creating warps file failed");
            }
        }
        FileConfiguration warps = YamlConfiguration.loadConfiguration(warpsYml);


        Player p = (Player) sender;
        if (args.length == 0) {
            openWarpsMenu.openWarpsMenu(p);

        } else if (args.length > 1) {
            p.sendMessage(pl.err + "Too many arguments");
        } else {
            args[0] = args[0].toLowerCase();
            if (warps.get("warps." + args[0] + ".x") != null) {
                boolean okWarp = false;
                if (warps.get("warps." + args[0] + ".requiredPermission") == null) {
                    okWarp = true;
                } else if (sender.hasPermission(warps.getString("warps." + args[0] + ".requiredPermission"))) {
                    okWarp = true;
                }

                String world = warps.getString("warps." + args[0] + ".world");
                double x = warps.getDouble("warps." + args[0] + ".x");
                double y = warps.getDouble("warps." + args[0] + ".y");
                double z = warps.getDouble("warps." + args[0] + ".z");
                float pitch = (float) warps.getDouble("warps." + args[0] + ".pitch");
                float yaw = (float) warps.getDouble("warps." + args[0] + ".yaw");
                Location loc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
                p.sendMessage(pl.badge + "Warping you to: " + ChatColor.GOLD + args[0]);
                if (okWarp) {
                    pl.shakeTP(p, loc);
                } else {
                    p.sendMessage(pl.err + "You lack the permissions required to use that warp");
                }
            } else {
                p.sendMessage(pl.err + "No warp found with that name");
            }
        }
        return true;
    }
}
