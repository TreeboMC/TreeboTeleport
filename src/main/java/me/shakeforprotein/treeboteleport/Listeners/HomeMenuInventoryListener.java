package me.shakeforprotein.treeboteleport.Listeners;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


public class HomeMenuInventoryListener implements Listener {

    private TreeboTeleport pl;

    public HomeMenuInventoryListener(TreeboTeleport main) {
        pl = main;
    }


/*    @EventHandler
    public void invClickEvent(InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();
        Player p = (Player) e.getWhoClicked();
        InventoryView invView = e.getView();
        String menuName = "Homes - " + p.getName();
        boolean staff = false;
        try {
            String name = invView.getTitle();
            if (name != null && name != "" && name.split(" - ").length > 1 && !(name.split(" - ")[1] == null)) {
                if (p.getUniqueId().equals(Bukkit.getOfflinePlayer(name.split(" - ")[1]).getUniqueId())) {
                    staff = false;
                } else {
                    staff = true;
                    menuName = "Homes - " + Bukkit.getOfflinePlayer(name.split(" - ")[1]).getName();
                }
                int slot = e.getSlot();
                File menuYml = new File(pl.getDataFolder() + File.separator + "homes", File.separator + p.getUniqueId().toString() + ".yml");
                if (staff) {
                    UUID offPUUID = Bukkit.getOfflinePlayer(name.split(" - ")[1]).getUniqueId();
                    menuYml = new File(pl.getDataFolder() + File.separator + "homes", File.separator + offPUUID.toString() + ".yml");
                }
                FileConfiguration homesMenu = YamlConfiguration.loadConfiguration(menuYml);


                if (invView.getTitle().equalsIgnoreCase(menuName)) {
                    try {
                        if (inv.getItem(slot) != null && inv.getItem(slot).hasItemMeta()) {
                            if (ChatColor.stripColor(inv.getItem(slot).getItemMeta().getDisplayName()).equalsIgnoreCase("Venture into the unknown")) {
                                Bukkit.dispatchCommand(p, "wild");
                            } else if (ChatColor.stripColor(inv.getItem(slot).getItemMeta().getDisplayName()).equalsIgnoreCase("Your Bed")) {
                                Bukkit.dispatchCommand(p, "bed");
                            } else {
                                for (String item : homesMenu.getConfigurationSection("homes").getKeys(false)) {
                                    if (ChatColor.stripColor(inv.getItem(slot).getItemMeta().getDisplayName()).equalsIgnoreCase(homesMenu.getString("homes." + item + ".name"))) {
                                        if (staff) {
                                            World world = Bukkit.getWorld(homesMenu.getString("homes." + item + ".world"));
                                            int x = homesMenu.getInt("homes." + item + ".x");
                                            int y = homesMenu.getInt("homes." + item + ".y");;
                                            int z = homesMenu.getInt("homes." + item + ".z");;
                                            float yaw = (float) homesMenu.getDouble("homes." + item + ".yaw");;
                                            float pitch = (float) homesMenu.getDouble("homes." + item + ".pitch");;
                                            String home = inv.getItem(slot).getItemMeta().getDisplayName();
                                            p.sendMessage("Sending you to player home - " + home);
                                            Location loc = new Location(world, x, y, z, yaw, pitch);
                                            p.teleport(loc);

                                        } else {
                                            Bukkit.dispatchCommand(p, "home " + inv.getItem(slot).getItemMeta().getDisplayName());
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception err) {
                        pl.makeLog(err);
                    }
                    e.setCancelled(true);
                }
            }
        } catch (IllegalStateException err) {
            String error = err.getMessage();
        }
    }

 */

    @EventHandler
    public void onHomesMenuClick(InventoryClickEvent e) {
        if (e.getView().getTitle().startsWith(pl.badge + "Homes - ")) {
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            if (!(e.getClickedInventory().getItem(e.getSlot()).getType() == Material.AIR)) {
                ItemStack clickedItem = e.getClickedInventory().getItem(e.getSlot());
                ItemMeta clickedMeta = clickedItem.getItemMeta();
                String owner = e.getView().getTitle().split("Homes - ")[1];
                if (e.getClick().isLeftClick()) {
                    if (clickedItem.hasItemMeta()) {
                        if (clickedMeta.hasDisplayName() && clickedMeta.getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&e&lTravel to Spawn"))) {
                            Bukkit.dispatchCommand(p, "spawn");
                        } else if (clickedMeta.hasDisplayName() && clickedMeta.getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&3&lTravel into the wilderness"))) {
                            Bukkit.dispatchCommand(p, "wild");
                        } else if (clickedMeta.hasDisplayName() && clickedMeta.getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&c&lClose Menu"))) {
                            p.closeInventory();
                        } else {
                            p.teleport(new Location(Bukkit.getWorld(clickedMeta.getLore().get(0).split(": ")[1]),
                                    Integer.parseInt(clickedMeta.getLore().get(1).split(": ")[1]),
                                    Integer.parseInt(clickedMeta.getLore().get(2).split(": ")[1]),
                                    Integer.parseInt(clickedMeta.getLore().get(3).split(": ")[1]),
                                    Integer.parseInt(clickedMeta.getLore().get(4).split(": ")[1]), 0));
                            //            Integer.parseInt(clickedMeta.getLore().get(5).split(": ")[1])));
                        }
                    } else {
                        p.sendMessage(pl.err + " Item has no metadata");
                    }
                } else if (e.getClick().isRightClick()) {
                    if (clickedMeta.hasDisplayName() &&
                            !(clickedMeta.getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&e&lTravel to Spawn")))
                            || clickedMeta.getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&3&lTravel into the wilderness"))
                            || clickedMeta.getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&c&lClose Menu"))) {

                        Inventory configMenu = Bukkit.createInventory(null, 9, pl.badge + "Configure Home - " + Bukkit.getOfflinePlayer(owner).getUniqueId().toString() + ":" + clickedMeta.getLore().get(6).split(": ")[1]);

                        ItemStack nameTag = new ItemStack(Material.NAME_TAG, 1);
                        ItemStack icon = new ItemStack(Material.FILLED_MAP, 1);
                        ItemStack back = new ItemStack(Material.STRUCTURE_VOID, 1);
                        ItemMeta tagMeta = nameTag.getItemMeta();
                        ItemMeta iconMeta = icon.getItemMeta();
                        ItemMeta backMeta = back.getItemMeta();
                        tagMeta.setDisplayName("Rename Home " + clickedMeta.getDisplayName());
                        iconMeta.setDisplayName("Set icon for Home " + clickedMeta.getDisplayName());
                        backMeta.setDisplayName("Return to previous menu");
                        nameTag.setItemMeta(tagMeta);
                        icon.setItemMeta(iconMeta);
                        back.setItemMeta(backMeta);
                        configMenu.setItem(0, nameTag);
                        configMenu.setItem(1, icon);
                        configMenu.setItem(8, back);

                        e.getWhoClicked().openInventory(configMenu);
                    }
                }
            }
        } else if (e.getView().getTitle().startsWith(pl.badge + "Configure Home - ")) {
            if (e.getSlot() == -999) {
                e.getWhoClicked().closeInventory();
            } else {
                e.setCancelled(true);
                if (!(e.getClickedInventory() instanceof PlayerInventory)) {
                    String owner = e.getView().getTitle().split(" - ")[1].split(":")[0];
                    e.getWhoClicked().sendMessage(owner);
                    String identifier = e.getView().getTitle().split(" - ")[1].split(":")[1];
                    e.getWhoClicked().sendMessage(identifier);

                    File homesYml = new File(pl.getDataFolder() + File.separator + "homes", File.separator + owner + ".yml");
                    YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(homesYml);
                    Inventory inv = e.getInventory();
                    if (e.getInventory().getItem(e.getSlot()) != null && e.getInventory().getItem(e.getSlot()).getType() != Material.AIR) {
                        if (e.getInventory().getItem(e.getSlot()).getType() == Material.NAME_TAG) {
                            e.getWhoClicked().closeInventory();
                            e.getWhoClicked().sendMessage(pl.badge + org.bukkit.ChatColor.GOLD + " Enter the new name for this home");
                            Bukkit.getScheduler().runTaskLaterAsynchronously(pl, new Runnable() {
                                @Override
                                public void run() {
                                    if (pl.getConfig().getBoolean("ListeningTo." + e.getWhoClicked().getUniqueId().toString())) {
                                        pl.getConfig().set("ListeningTo." + e.getWhoClicked().getUniqueId().toString(), false);
                                        e.getWhoClicked().sendMessage(pl.badge + org.bukkit.ChatColor.RED + " Timeout: " + org.bukkit.ChatColor.GOLD + "No longer monitoring user input for rename.");
                                    }
                                }
                            }, 300);
                            pl.getConfig().set("ListeningTo." + e.getWhoClicked().getUniqueId().toString(), true);
                            pl.getConfig().set("ValuesFor." + e.getWhoClicked().getUniqueId().toString() + ".owner", owner);
                            pl.getConfig().set("ValuesFor." + e.getWhoClicked().getUniqueId().toString() + ".identifier", identifier);
                        } else if (e.getInventory().getItem(e.getSlot()).getType() == Material.FILLED_MAP) {
                            ItemStack item = e.getWhoClicked().getInventory().getItemInMainHand();
                            if (item.getType() != Material.AIR) {
                                yamlConfiguration.set("homes." + identifier + ".icon", item.getType().name().toUpperCase());
                                try {
                                    yamlConfiguration.save(homesYml);
                                } catch (IOException err) {
                                    err.printStackTrace();
                                }
                                e.getWhoClicked().sendMessage(pl.badge + " Icon set for " + identifier);
                                if (e.getWhoClicked().getUniqueId().toString().equalsIgnoreCase(owner)) {
                                    Bukkit.dispatchCommand(e.getWhoClicked(), "homes");
                                } else {
                                    for (Player target : Bukkit.getOnlinePlayers()) {
                                        if (target.getUniqueId().toString().equalsIgnoreCase(owner)) {
                                            Bukkit.dispatchCommand(e.getWhoClicked(), "homes " + target.getName());
                                            break;
                                        }
                                    }
                                    Bukkit.dispatchCommand(e.getWhoClicked(), "homes " + Bukkit.getOfflinePlayer(owner).getName());
                                }
                            } else {
                                e.getWhoClicked().sendMessage(pl.badge + " You must hold an item to use as an icon in your main hand");
                            }
                        } else if (e.getInventory().getItem(e.getSlot()).getType() == Material.STRUCTURE_VOID) {
                            for (Player target : Bukkit.getOnlinePlayers()) {
                                if (target.getUniqueId().toString().equalsIgnoreCase(owner)) {
                                    Bukkit.dispatchCommand(e.getWhoClicked(), "homes " + target.getName());
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void chatEvent(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String world = p.getWorld().getName().split("_")[0];
        if (pl.getConfig().getBoolean("ListeningTo." + p.getUniqueId().toString())) {
            e.setCancelled(true);
            String owner = pl.getConfig().getString("ValuesFor." + p.getUniqueId().toString() + ".owner").toLowerCase();
            String identifier = pl.getConfig().getString("ValuesFor." + p.getUniqueId().toString() + ".identifier");
            File homesYml = new File(pl.getDataFolder() + File.separator + "homes", File.separator + owner + ".yml");
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(homesYml);
            yamlConfiguration.set("homes." + identifier + ".name", e.getMessage().replace(" ", "_").replace("'", "").replace("/", "").replace("\"", ""));
            pl.getConfig().set("ListeningTo." + p.getUniqueId().toString(), null);
            pl.getConfig().set("ValuesFor." + p.getUniqueId().toString(), null);
            try {
                yamlConfiguration.save(homesYml);
            } catch (IOException err) {
                err.printStackTrace();
            }
            p.sendMessage(pl.badge + " Name set for " + owner + " - " + identifier);
            Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
                @Override
                public void run() {
                    if (p.getUniqueId().toString().equalsIgnoreCase(owner)) {
                        Bukkit.dispatchCommand(p, "homes");
                    }
                    else {
                        for (Player target : Bukkit.getOnlinePlayers()) {
                            if (target.getUniqueId().toString().equalsIgnoreCase(owner)) {
                                Bukkit.dispatchCommand(p, "homes " + target.getName());
                                break;
                            }
                        }
                        Bukkit.dispatchCommand(p, "homes " + Bukkit.getOfflinePlayer(owner).getName());
                    }
                }
            }, 1);

        }
    }
}