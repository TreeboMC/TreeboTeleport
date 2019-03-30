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

public class Home implements CommandExecutor {

    private TreeboTeleport pl;

    public Home(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (pl.getCD((Player) sender)) {
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
                    //   sender.sendMessage("Creating Homes file failed");
                }
            }
            FileConfiguration homes = YamlConfiguration.loadConfiguration(homesYml);

            if (args.length == 0) {
                if (homes.get("defaultHome") != null) {
                    String command = "home " + homes.getString("defaultHome");
                    Bukkit.dispatchCommand(sender, command);
                } else {
                    p.sendMessage(ChatColor.GOLD + "The following Homes are available for travel:");
                    for (String item : homes.getConfigurationSection("homes").getKeys(false)) {
                        String command = "tellraw " + p.getName() + " {\"text\":\"[" + homes.getString("homes." + item + ".name") + "]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/home " + item + "\"}}";
                        pl.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                    }
                    if (p.getBedSpawnLocation() != null) {
                        String command = "tellraw " + p.getName() + " {\"text\":\"[BED]\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/home bed\"}}";
                    }
                    p.sendMessage(ChatColor.RED + "[End of List]");

                }
            } else {
                args[0] = args[0].toLowerCase();

                if (args[0].equalsIgnoreCase("bed")) {
                    String command = "bed";
                    Bukkit.dispatchCommand(sender, command);
                } else if (homes.get("homes." + args[0] + ".x") != null) {
                    String world = homes.getString("homes." + args[0] + ".world");
                    double x = homes.getDouble("homes." + args[0] + ".x");
                    double y = homes.getDouble("homes." + args[0] + ".y");
                    double z = homes.getDouble("homes." + args[0] + ".z");
                    float pitch = (float) homes.getDouble("homes." + args[0] + ".pitch");
                    float yaw = (float) homes.getDouble("homes." + args[0] + ".yaw");
                    Location loc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
                    p.sendMessage(pl.badge + "Returning you to: " + ChatColor.GOLD + args[0]);
                    p.teleport(loc);
                } else {
                    p.sendMessage(pl.err + "No Home found with that name");
                }
            }
        }
        return true;
    }
}
