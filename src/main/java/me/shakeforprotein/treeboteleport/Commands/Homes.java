package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.Methods.Guis.OpenHomesMenu;
import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
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

public class Homes implements CommandExecutor {

    private TreeboTeleport pl;
    private OpenHomesMenu openHomesMenu;

    public Homes(TreeboTeleport main) {
        this.pl = main;
        this.openHomesMenu = new OpenHomesMenu(pl);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        File homesYml = new File(pl.getDataFolder() + File.separator + "homes", File.separator + p.getUniqueId() + ".yml");
        if (!homesYml.exists()) {
            pl.createDefaultFile(pl.getDataFolder().toString(), "homes", true);
            sender.sendMessage(pl.err + "Homes file not found.");
            sender.sendMessage("Use '/sethome <home name>' to set a new home");
            try {
                homesYml.createNewFile();
                FileConfiguration homes = YamlConfiguration.loadConfiguration(homesYml);
                try {
                    homes.options().copyDefaults();
                    homes.save(homesYml);
                } catch (FileNotFoundException e) {
                    pl.makeLog(e);
                }
            } catch (IOException e) {
                pl.makeLog(e);
            }
        }

        FileConfiguration homes = YamlConfiguration.loadConfiguration(homesYml);


        if (args.length == 0) {
            openHomesMenu.openHomesMenu(p);
        }
        else{
        }
        return true;
    }
}