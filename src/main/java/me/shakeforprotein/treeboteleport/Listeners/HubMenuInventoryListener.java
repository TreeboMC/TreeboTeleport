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
import java.util.Arrays;
import java.util.Set;

public class HubMenuInventoryListener implements Listener {

    private TreeboTeleport pl;

    public HubMenuInventoryListener(TreeboTeleport main) {
        pl = main;
    }


    @EventHandler
    public void invClickEvent(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        InventoryView inventoryView = e.getView();
        Player p = (Player) e.getWhoClicked();
        try {
            String name = inventoryView.getTitle();
            String clickedButton = "";
            int slot = e.getSlot();

            File menuYml = new File(pl.getDataFolder(), File.separator + "hubMenu.yml");
            FileConfiguration hubMenu = YamlConfiguration.loadConfiguration(menuYml);

            String menuName = "HubMenu";
            if (hubMenu.get("menuName") != null) {
                menuName = hubMenu.getString("menuName");
                menuName = ChatColor.translateAlternateColorCodes('&', menuName);
            }

            if (name.equalsIgnoreCase(menuName)) {

                try {
                    Set menuItems = hubMenu.getConfigurationSection("hubmenu.menuItems").getKeys(false);
                    String[] menuItemStrings = Arrays.copyOf(menuItems.toArray(), menuItems.size(), String[].class);

                    for (String item : menuItemStrings) {
                        int position = hubMenu.getInt("hubmenu.menuItems." + item + ".position");
                        if (slot == position) {
                            clickedButton = item;
                            break;
                        }
                    }

                    if (!clickedButton.equals("")) {
                        String command;
                        String executor;
                        command = hubMenu.getString("hubmenu.menuItems." + clickedButton + ".command");
                        executor = hubMenu.getString("hubmenu.menuItems." + clickedButton + ".executor");
                        if (command.startsWith("SERVER")) {
                            pl.bungeeApi.connectOther(e.getWhoClicked().getName(), command.split(" ")[1]);
                        } else if (command.startsWith("WORLD")) {
                            ((Player) e.getWhoClicked()).teleport(Bukkit.getWorld(command.split(" ")[1]).getSpawnLocation());
                        } else if (executor.equals("console")) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                        } else {
                            Bukkit.dispatchCommand(e.getWhoClicked(), command);
                        }
                    }
                } catch (Exception ex) {
                    pl.roots.errorLogger.logError(pl, ex);
                }
                e.setCancelled(true);
            }
        } catch (IllegalStateException ex) {
            pl.roots.errorLogger.logError(pl, ex);
        }
    }
}