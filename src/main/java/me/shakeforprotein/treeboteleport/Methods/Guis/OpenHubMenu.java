package me.shakeforprotein.treeboteleport.Methods.Guis;

import me.shakeforprotein.treeboteleport.Bungee.BungeeRecieve;
import me.shakeforprotein.treeboteleport.TreeboTeleport;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class OpenHubMenu {

    private TreeboTeleport pl;
    private BungeeRecieve bungeeRecieve;

    public OpenHubMenu(TreeboTeleport main){
        this.pl = main;
        this.bungeeRecieve = new BungeeRecieve(pl);
    }

    public boolean openHubMenu(Player p){
        File menuYml = new File(pl.getDataFolder(), File.separator + "hubMenu.yml");
        FileConfiguration hubMenu = YamlConfiguration.loadConfiguration(menuYml);
        if(!menuYml.exists()) {
            try {
                menuYml.createNewFile();
                try{
                    hubMenu.options().copyDefaults();
                    hubMenu.save(menuYml);
                }
                catch (FileNotFoundException ex){
                    pl.roots.errorLogger.logError(pl, ex);
                }
            } catch (IOException ex) {
                pl.roots.errorLogger.logError(pl, ex);
            }
        }

        try{
            Set menuItems = hubMenu.getConfigurationSection("hubmenu.menuItems").getKeys(false);
            String[] menuItemStrings = Arrays.copyOf(menuItems.toArray(), menuItems.size(), String[].class);
            String menuName = "HubMenu";
            if(hubMenu.get("menuName") != null){
                menuName = hubMenu.getString("menuName");
                menuName = ChatColor.translateAlternateColorCodes('&', menuName);
            }
            int invSize = hubMenu.getInt("hubmenu.menuRows" + "") * 9;
            Inventory HubMenu = Bukkit.createInventory(null, invSize, menuName);

            for(String item : menuItemStrings){
                Material icon = Material.getMaterial(hubMenu.getString("hubmenu.menuItems." + item + ".icon"));
                int position = hubMenu.getInt("hubmenu.menuItems." + item + ".position");
                String colour = "WHITE";
                if(hubMenu.getString("hubmenu.menuItems." + item + ".colour") != null){
                    colour = hubMenu.getString("hubmenu.menuItems." + item + ".colour");
                }
                String displayName = ChatColor.valueOf(colour)+ hubMenu.getString("hubmenu.menuItems." + item + ".label");
                ItemStack newItem = new ItemStack(icon, 1);
                ItemMeta itemMeta = newItem.getItemMeta();
                itemMeta.setDisplayName(displayName);
                List<String> itemLore = new ArrayList<String>();


                if(hubMenu.getConfigurationSection("hubmenu.menuItems." + item + ".lore") != null){
                    for(String newLore: hubMenu.getConfigurationSection("hubmenu.menuItems." + item + ".lore").getKeys(false)){
                        itemLore.add(ChatColor.translateAlternateColorCodes('&', hubMenu.getString("hubmenu.menuItems." + item + ".lore." + newLore)));
                    }
                }

                for(String srv : pl.getConfig().getStringList("ServerList")){
                    if(ChatColor.stripColor(displayName).equalsIgnoreCase(srv)){
                        itemLore.add(ChatColor.RED + "" + ChatColor.BOLD + "Playing Now: " + ChatColor.GREEN + "" + ChatColor.BOLD + pl.playerCounts.get(srv));
                    }
                }
                itemMeta.setLore(itemLore);
                newItem.setItemMeta(itemMeta);
                HubMenu.setItem(position, newItem);
            }
            p.openInventory(HubMenu);
        }
        catch(NullPointerException ex){
            pl.roots.errorLogger.logError(pl, ex);
        }
        return true;
    }
}
