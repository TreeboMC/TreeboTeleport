package me.shakeforprotein.treeboteleport.Commands;

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

    public Homes(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        File homesYml = new File(pl.getDataFolder() + File.separator + "homes", File.separator + p.getUniqueId() + ".yml");
        if (!homesYml.exists()) {
            //sender.sendMessage("Homes file not found");
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

            p.sendMessage(ChatColor.GOLD + "The following Homes are available for travel:");
            for (String item : homes.getConfigurationSection("homes").getKeys(false)) {
                String command = "tellraw " + p.getName() + " {\"text\":\"[" + homes.getString("homes." + item + ".name") + "]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/home " + item + "\"}}";
                pl.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
            }
            if (p.getBedSpawnLocation() != null) {
                String command = "tellraw " + p.getName() + " {\"text\":\"[BED]\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/bed\"}}";
                pl.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
            }
            p.sendMessage(ChatColor.RED + "[End of List]");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("setdefault")) {
            if (homes.get("homes." + args[1].toLowerCase()) != null) {
                homes.set("defaultHome", args[1].toLowerCase());
            }
        } else if (args.length == 1 && p.hasPermission("tbteleport.otherhomes") && Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore()) {

            String uuid = Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString();
            File playerYml = new File(pl.getDataFolder() + File.separator + "homes", File.separator + uuid + ".yml");
            if (!homesYml.exists()) {
                sender.sendMessage("Homes file not found");
            } else {
                FileConfiguration playerHomes = YamlConfiguration.loadConfiguration(playerYml);

                p.sendMessage(ChatColor.GOLD + args[0] + " has following Homes are available for travel:");
                for (String item : homes.getConfigurationSection("homes").getKeys(false)) {
                    String world = playerHomes.getString("homes." + item + ".world");
                    int x = playerHomes.getInt("homes." + item + "x");
                    int y = playerHomes.getInt("homes." + item + "y");
                    int z = playerHomes.getInt("homes." + item + "z");
                    String commandString = "tp2worldat " + world + " " + x + " " + y + " " + z;

                    String command = "tellraw " + p.getName() + " {\"text\":\"[" + playerHomes.getString("homes." + item + ".name") + "]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + commandString + "\"}}";
                    pl.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                }

                if (Bukkit.getOfflinePlayer(uuid).getBedSpawnLocation() != null) {
                    Location bedLocation = Bukkit.getOfflinePlayer(uuid).getBedSpawnLocation();
                    double bedX = bedLocation.getX();
                    double bedY = bedLocation.getX();
                    double bedZ = bedLocation.getX();
                    String bedWorld = bedLocation.getWorld().getName();
                    String commandString = "tp2worldat " + bedWorld + " " + bedX + " " + bedY + " " + bedZ;


                    String command = "tellraw " + p.getName() + " {\"text\":\"[" + "Bed" + "]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + commandString + "\"}}";
                }
            }
        }
        else{
            sender.sendMessage(pl.err + " Invalid usage. Valid arguments are '/homes setdefault <home name>'");
        }
        return true;
    }
}