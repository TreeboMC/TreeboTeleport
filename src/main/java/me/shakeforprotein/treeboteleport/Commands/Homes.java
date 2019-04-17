package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.Methods.Guis.OpenHomesMenu;
import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
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

public class Homes implements CommandExecutor {

    private TreeboTeleport pl;
    private OpenHomesMenu openHomesMenu;

    public Homes(TreeboTeleport main) {
        this.pl = main;
        this.openHomesMenu = new OpenHomesMenu(pl);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean found = false;
        Player p = (Player) sender;
        String tempUUID = "0";
        String tempName = "";
        File homesYml = new File(pl.getDataFolder() + File.separator + "homes", File.separator + p.getUniqueId() + ".yml");
        if (args.length == 1 && !(args[0].equalsIgnoreCase("setDefault")) && sender.hasPermission("tbteleport.staff.homes.others")) {
            for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                if (offlinePlayer.getName().equalsIgnoreCase(args[0])) {
                    found = true;
                    tempName = offlinePlayer.getName();
                    tempUUID = offlinePlayer.getUniqueId().toString();
                    File testFile = new File(pl.getDataFolder() + File.separator + "homes", File.separator + offlinePlayer.getUniqueId() + ".yml");
                    if (testFile.exists()) {
                        homesYml = testFile;
                    }
                }
            }
            if (!found) {
                sender.sendMessage(pl.err + "Player either doesn't exist, or has no homes file");
            }
        }
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
        } else if (args.length == 2 && args[0].equalsIgnoreCase("setDefault")) {
            found = false;
            for (String home : homes.getConfigurationSection("homes").getKeys(false)) {
                if (args[1].equalsIgnoreCase(home)) {
                    found = true;
                    homes.set("defaultHome", home);
                    try {
                        homes.save(homesYml);
                        sender.sendMessage(pl.badge + "Default home successfully set to " + home);
                    } catch (IOException err) {
                        pl.makeLog(err);
                        sender.sendMessage(pl.err + "Failed to save default home.");
                    }
                }
            }
            if (!found) {
                sender.sendMessage(pl.err + "Could not find home with that name");
            }
        } else if (args.length == 1 && sender.hasPermission("tbteleport.staff.homes.others")) {
            openHomesMenu.openOthersHomes(p, tempUUID, tempName);
        }
        else if (!found && args.length == 1 && !args[0].equalsIgnoreCase("setdefault")){
            if(sender.hasPermission("tbteleport.staff.homes.others")){
                sender.sendMessage(pl.err + "Invalid usage. Try /homes setDefault <home>");
            }
            else{
                sender.sendMessage(pl.err + "Invalid usage. Try /homes setDefault <home>");
            }
        }
        return true;
    }
}