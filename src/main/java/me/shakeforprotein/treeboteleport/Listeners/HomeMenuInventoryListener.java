package me.shakeforprotein.treeboteleport.Listeners;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.io.File;
import java.util.UUID;


public class HomeMenuInventoryListener implements Listener {

    private TreeboTeleport pl;

    public HomeMenuInventoryListener(TreeboTeleport main) {
        pl = main;
    }


    @EventHandler
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
}