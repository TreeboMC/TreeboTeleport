package me.shakeforprotein.treeboteleport.Listeners;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.io.File;
import java.util.ConcurrentModificationException;


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
        try {
            String name = invView.getTitle();
            int slot = e.getSlot();
            File menuYml = new File(pl.getDataFolder() + File.separator + "homes", File.separator + p.getUniqueId().toString() + ".yml");
            FileConfiguration homesMenu = YamlConfiguration.loadConfiguration(menuYml);

            String menuName = "Homes - " + p.getName();

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
                                    Bukkit.dispatchCommand(p, "home " + inv.getItem(slot).getItemMeta().getDisplayName());
                                }
                            }
                        }
                    }
                } catch (Exception err) {
                    pl.makeLog(err);
                }
                e.setCancelled(true);
            }
        } catch (IllegalStateException err) {
            String error = err.getMessage();
        }
    }
}