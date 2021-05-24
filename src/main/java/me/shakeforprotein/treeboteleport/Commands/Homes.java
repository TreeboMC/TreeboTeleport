package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.Methods.Guis.OpenHomesMenu;
import me.shakeforprotein.treeboteleport.TreeboTeleport;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Homes {

    private TreeboTeleport pl;
    private OpenHomesMenu openHomesMenu;

    public Homes(TreeboTeleport main) {
        this.pl = main;
        this.openHomesMenu = new OpenHomesMenu(pl);
    }

/*    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Opens the Homes gui");
                    this.setUsage("/Homes - requires tbteleport.player.homes");
                    this.setPermission("tbteleport.player.homes");
                    if (sender.hasPermission(this.getPermission())) {

                        boolean found = false;
                        Player p = (Player) sender;
                        String tempUUID = "0";
                        String tempName = "";
                        File homesYml = new File(pl.getDataFolder() + File.separator + "homes", File.separator + p.getUniqueId().toString() + ".yml");
                        if (args.length == 1 && !(args[0].equalsIgnoreCase("setDefault")) && sender.hasPermission("tbteleport.staff.homes.others")) {
                            for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                                if (offlinePlayer.getName().equalsIgnoreCase(args[0])) {
                                    found = true;
                                    tempName = offlinePlayer.getName();
                                    tempUUID = offlinePlayer.getUniqueId().toString();
                                    File testFile = new File(pl.getDataFolder() + File.separator + "homes", File.separator + offlinePlayer.getUniqueId().toString() + ".yml");
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
                        } else if (!found && args.length == 1 && !args[0].equalsIgnoreCase("setdefault")) {
                            if (sender.hasPermission("tbteleport.staff.homes.others")) {
                                sender.sendMessage(pl.err + "Invalid usage. Try /homes setDefault <home>");
                            } else {
                                sender.sendMessage(pl.err + "Invalid usage. Try /homes setDefault <home>");
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
 */

    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Opens the Homes gui");
                    this.setUsage("/Homes - requires tbteleport.player.homes");
                    this.setPermission("tbteleport.player.homes");
                    if (sender.hasPermission(this.getPermission()) && sender instanceof Player) {

                        /*****************************************************************************************************/
                        /*ThoughtProcess: This section determines if the sender is staff, and if there is an Player argument */
                        /*ThoughtProcess: If there is a player argument, it attempts to get the players uuid which it uses   */
                        /*ThoughtProcess: to get the appropriate homes file. If there is no player argument, or the sender is*/
                        /*ThoughtProcess: not staff, it will get the senders personal homes file.                            */
                        /*****************************************************************************************************/
                        boolean staff = false;
                        String owner = sender.getName();
                        if (sender.hasPermission("treeboteleport.staff.homes.other")) {
                            staff = true;
                        }
                        //File homesYml = new File(pl.getDataFolder() + File.separator + "homes", File.separator + ((Player) sender).getUniqueId().toString() + ".yml");
                        File homesYml = new File(pl.getPlayerDataFolder() + File.separator + ((Player) sender).getUniqueId().toString(), File.separator + "homes.yml");

                        if (args.length == 1 && staff) {
                            //homesYml = new File(pl.getDataFolder() + File.separator + "homes", File.separator + (Bukkit.getOfflinePlayer(args[0])).getUniqueId().toString() + ".yml");
                            homesYml = new File(pl.getDataFolder() + File.separator + Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString(), File.separator + "homes.yml");
                            owner = args[0].toLowerCase();
                        }

                        if (homesYml.exists()) {
                            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(homesYml);
                            OpenHomesMenu((Player) sender, owner, yamlConfiguration);
                        } else {
                            sender.sendMessage(pl.err + "Homes file not found.");
                        }
                    } else {
                        if (sender instanceof Player) {
                            sender.sendMessage(pl.badge + "Sorry, you require the permission node " + this.getPermission());
                        } else {
                            sender.sendMessage(pl.badge + "Sorry, this command can only be run by players");
                        }
                    }
                    return true;
                }
            };
            pl.registerNewCommand(pl.getDescription().getName(), item2);
        }
        return true;
    }


    private void OpenHomesMenu(Player opener, String owner, YamlConfiguration yamlConfiguration) {
        String menuName = pl.badge + "Homes - " + owner;
        int invSize = 9;
        int totalHomes = yamlConfiguration.getConfigurationSection("homes").getKeys(false).size();
        invSize = (int) Math.floor(totalHomes / 9) + 1;
        if (totalHomes % 9 > 0) {
            invSize = invSize + 1;
        }
        invSize = invSize * 9;

        ItemStack spawnItem = new ItemStack(Material.COMPASS, 1);
        ItemStack wildItem = new ItemStack(Material.CHORUS_FRUIT, 1);
        ItemStack closeItem = new ItemStack(Material.STRUCTURE_VOID, 1);

        ItemMeta spawnItemMeta = spawnItem.getItemMeta();
        ItemMeta wildItemMeta = wildItem.getItemMeta();
        ItemMeta closeItemMeta = closeItem.getItemMeta();

        spawnItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lTravel to Spawn"));
        wildItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&3&lTravel into the wilderness"));
        closeItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lClose Menu"));

        spawnItem.setItemMeta(spawnItemMeta);
        wildItem.setItemMeta(wildItemMeta);
        closeItem.setItemMeta(closeItemMeta);

        Inventory homesMenu = Bukkit.createInventory(null, invSize, menuName);
        homesMenu.setItem(invSize - 7, spawnItem);
        homesMenu.setItem(invSize - 5, wildItem);
        homesMenu.setItem(invSize - 3, closeItem);

        for (String home : yamlConfiguration.getConfigurationSection("homes").getKeys(false)) {

            ItemStack homeItem = new ItemStack(Material.RED_BED, 1);
            if (yamlConfiguration.getString("homes." + home + ".icon") != null) {
                homeItem.setType(Material.valueOf(yamlConfiguration.getString("homes." + home + ".icon")));
            }
            ItemMeta homeItemMeta = homeItem.getItemMeta();
            if (yamlConfiguration.getString("homes." + home + ".name") != null) {
                if (yamlConfiguration.getString("homes." + home + "colour") != null) {
                    homeItemMeta.setDisplayName(ChatColor.valueOf(yamlConfiguration.getString("homes." + home + ".colour")) + yamlConfiguration.getString("homes." + home + ".name"));
                } else {
                    homeItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', yamlConfiguration.getString("homes." + home + ".name")));
                }
            } else {
                homeItemMeta.setDisplayName(home);
            }

            List<String> homeItemLore = new ArrayList<String>();

            homeItemLore.add("world: " + yamlConfiguration.getString("homes." + home + ".world"));
            homeItemLore.add("x: " + yamlConfiguration.getInt("homes." + home + ".x"));
            homeItemLore.add("y: " + yamlConfiguration.getInt("homes." + home + ".y"));
            homeItemLore.add("z: " + yamlConfiguration.getInt("homes." + home + ".z"));
            homeItemLore.add("pitch: " + yamlConfiguration.getInt("homes." + home + ".pitch"));
            homeItemLore.add("yaw: " + yamlConfiguration.getInt("homes." + home + ".yaw"));
            homeItemLore.add("identifier: " + home);
            homeItemMeta.setLore(homeItemLore);
            homeItem.setItemMeta(homeItemMeta);
            homesMenu.addItem(homeItem);
        }
        opener.openInventory(homesMenu);
    }
}