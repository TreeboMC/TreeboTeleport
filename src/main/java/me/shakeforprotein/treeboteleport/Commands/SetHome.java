package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SetHome implements CommandExecutor{

    private TreeboTeleport pl;

    public SetHome(TreeboTeleport main) {
        this.pl = main;
    }

    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Creates a home for easy return.");
                    this.setUsage("/sethome <homename> - requires tbteleport.player.sethome");
                    this.setPermission("tbteleport.player.sethome");
                    if (sender.hasPermission(this.getPermission())) {

                        Player p = (Player) sender;
                        //File homesYml = new File(pl.getDataFolder() + File.separator + "homes", File.separator + p.getUniqueId().toString() + ".yml");
                        File homesYml = new File(pl.getPlayerDataFolder() + File.separator + p.getUniqueId().toString(), File.separator + "homes.yml");

                        if (!homesYml.exists()) {
                            try {
                                homesYml.createNewFile();
                                FileConfiguration homes = YamlConfiguration.loadConfiguration(homesYml);
                                try {
                                    homes.options().copyDefaults();
                                    homes.save(homesYml);
                                } catch (FileNotFoundException ex) {
                                    pl.roots.errorLogger.logError(pl, ex);
                                }
                            } catch (IOException ex) {
                                pl.roots.errorLogger.logError(pl, ex);
                            }
                        }
                        FileConfiguration homes = YamlConfiguration.loadConfiguration(homesYml);
                        Location loc = p.getLocation();
                        String world = loc.getWorld().getName();
                        double x = loc.getX();
                        double y = loc.getY();
                        double z = loc.getZ();
                        float pitch = loc.getPitch();
                        float yaw = loc.getYaw();
                        Vector direction = loc.getDirection();
                        if (args.length == 0) {
                            p.sendMessage(pl.err + "You must provide a name for your new Home");
                        } else if (args.length > 1) {
                            p.sendMessage(pl.err + "Too many arguments");
                        } else {
                            if (args[0].equalsIgnoreCase("bed")) {
                                p.sendMessage(pl.err + ChatColor.GOLD + "Bed" + ChatColor.RESET + " is a disallowed warp name");
                            } else {
                                int maxHomes = getHomes(p);
                                int existingHomes = 0;
                                if (homes.getConfigurationSection("homes") != null && homes.getConfigurationSection("homes").getKeys(false) != null) {
                                    existingHomes = homes.getConfigurationSection("homes").getKeys(false).toArray().length;
                                }
                                if (maxHomes > existingHomes) {
                                    String name = args[0];
                                    args[0] = args[0].toLowerCase();
                                    homes.set("homes." + args[0] + ".name", name);
                                    homes.set("homes." + args[0] + ".world", world);
                                    homes.set("homes." + args[0] + ".x", x);
                                    homes.set("homes." + args[0] + ".y", y);
                                    homes.set("homes." + args[0] + ".z", z);
                                    homes.set("homes." + args[0] + ".pitch", pitch);
                                    homes.set("homes." + args[0] + ".yaw", yaw);

                                    try {
                                        homes.save(homesYml);
                                        p.sendMessage(pl.badge + "Home with name: " + ChatColor.GOLD + args[0] + ChatColor.RESET + " has been saved.");
                                    } catch (IOException ex) {
                                        pl.roots.errorLogger.logError(pl, ex);
                                        p.sendMessage(pl.err + "Saving Homes file Unsuccessful");
                                    }
                                } else {
                                    p.sendMessage(pl.err + "You do not have any additional homes available. You may purchase more at " + ChatColor.BLUE + "http://store.treebomc.com " + ChatColor.RESET + "or consider " + ChatColor.RED + " deleting" + ChatColor.RESET + "one of your existing homes with " + ChatColor.RED + "/delhome");
                                }
                            }
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


    private int getHomes(Player p) {
        int i = 100;
        int maxHomes = 1;
        while (i > 0) {
            i--;
            if (p.hasPermission("tbteleport.maxhomes." + i)) {
                maxHomes = i;
                break;
            }
        }
        return maxHomes;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        //File homesYml = new File(pl.getDataFolder() + File.separator + "homes", File.separator + p.getUniqueId().toString() + ".yml");
        File homesYml = new File(pl.getPlayerDataFolder() + File.separator + p.getUniqueId().toString(), File.separator + "homes.yml");

        if (!homesYml.exists()) {
            try {
                homesYml.createNewFile();
                FileConfiguration homes = YamlConfiguration.loadConfiguration(homesYml);
                try {
                    homes.options().copyDefaults();
                    homes.save(homesYml);
                } catch (FileNotFoundException ex) {
                    pl.roots.errorLogger.logError(pl, ex);
                }
            } catch (IOException ex) {
                pl.roots.errorLogger.logError(pl, ex);
            }
        }
        FileConfiguration homes = YamlConfiguration.loadConfiguration(homesYml);
        Location loc = p.getLocation();
        String world = loc.getWorld().getName();
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        float pitch = loc.getPitch();
        float yaw = loc.getYaw();
        Vector direction = loc.getDirection();
        if (args.length == 0) {
            p.sendMessage(pl.err + "You must provide a name for your new Home");
        } else if (args.length > 1) {
            p.sendMessage(pl.err + "Too many arguments");
        } else {
            if (args[0].equalsIgnoreCase("bed")) {
                p.sendMessage(pl.err + ChatColor.GOLD + "Bed" + ChatColor.RESET + " is a disallowed warp name");
            } else {
                int maxHomes = getHomes(p);
                int existingHomes = 0;
                if (homes.getConfigurationSection("homes") != null && homes.getConfigurationSection("homes").getKeys(false) != null) {
                    existingHomes = homes.getConfigurationSection("homes").getKeys(false).toArray().length;
                }
                if (maxHomes > existingHomes) {
                    String name = args[0];
                    args[0] = args[0].toLowerCase();
                    homes.set("homes." + args[0] + ".name", name);
                    homes.set("homes." + args[0] + ".world", world);
                    homes.set("homes." + args[0] + ".x", x);
                    homes.set("homes." + args[0] + ".y", y);
                    homes.set("homes." + args[0] + ".z", z);
                    homes.set("homes." + args[0] + ".pitch", pitch);
                    homes.set("homes." + args[0] + ".yaw", yaw);

                    try {
                        homes.save(homesYml);
                        p.sendMessage(pl.badge + "Home with name: " + ChatColor.GOLD + args[0] + ChatColor.RESET + " has been saved.");
                    } catch (IOException ex) {
                        pl.roots.errorLogger.logError(pl, ex);
                        p.sendMessage(pl.err + "Saving Homes file Unsuccessful");
                    }
                } else {
                    p.sendMessage(pl.err + "You do not have any additional homes available. You may purchase more at " + ChatColor.BLUE + "http://store.treebomc.com " + ChatColor.RESET + "or consider " + ChatColor.RED + " deleting" + ChatColor.RESET + "one of your existing homes with " + ChatColor.RED + "/delhome");
                }
            }
        }
        return true;
    }
}
