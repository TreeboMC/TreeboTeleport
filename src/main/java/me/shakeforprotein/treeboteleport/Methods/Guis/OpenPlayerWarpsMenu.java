package me.shakeforprotein.treeboteleport.Methods.Guis;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import java.util.List;
import java.util.Set;

public class OpenPlayerWarpsMenu {

    private TreeboTeleport pl;

    public OpenPlayerWarpsMenu(TreeboTeleport main) {
        this.pl = main;
    }

    public void openWarpsMenu(Player p) {
        File menuYml = new File(pl.getDataFolder(), "warps.yml");
        FileConfiguration warpsMenu = YamlConfiguration.loadConfiguration(menuYml);
        if (!menuYml.exists()) {
            try {
                menuYml.createNewFile();
                try {
                    warpsMenu.options().copyDefaults();
                    warpsMenu.save(menuYml);
                } catch (FileNotFoundException ex) {
                    pl.roots.errorLogger.logError(pl, ex);
                }
            } catch (IOException ex) {
                pl.roots.errorLogger.logError(pl, ex);
            }
        }

        String menuName = "Warps Interface";
        if(warpsMenu.get("menuName") != null){
            menuName = ChatColor.translateAlternateColorCodes('&',warpsMenu.getString("menuName"));
        }
        else{
            warpsMenu.set("menuName", "Warps Interface");
        }

        int invSize = 18;

        Set keys = warpsMenu.getConfigurationSection("playerWarps").getKeys(false);

        if(keys.size() > 9){
            invSize = 27;
        }

        if(keys.size() > 18){
            invSize = 36;
        }

        if(keys.size() > 27){
            invSize = 45;
        }

        if(keys.size() > 36){
            invSize = 54;
        }

        Inventory WarpsMenu = Bukkit.createInventory(null, invSize, menuName);

        for(String item : warpsMenu.getConfigurationSection("playerWarps").getKeys(false)){

            String title = "";
            String icon = "ENDER_PEARL";
            String id = item;

            if(warpsMenu.getString("warps." + item + ".title") != null){
                title = ChatColor.translateAlternateColorCodes('&',warpsMenu.getString("warps." + item + ".title"));
            }
            else{
                title = "Warp " + item;
                warpsMenu.set("playerWarps." + item + ".title", title);
            }
            if(warpsMenu.getString("playerWarps." + item + ".icon") != null){
                icon = warpsMenu.getString("playerWarps." + item + ".icon");
            }
            else{
                warpsMenu.set("playerWarps." + item + ".icon", icon);
            }
            try{warpsMenu.save(menuYml);}
            catch (IOException ex){
                pl.roots.errorLogger.logError(pl, ex);
                System.out.println("Failed to update warps file");
            }

            ItemStack newItem = new ItemStack(Material.getMaterial(icon), 1);
            ItemMeta iMeta = newItem.getItemMeta();
            List<String> iLore = new ArrayList<String>();

            iMeta.setDisplayName(title);
            iLore.add("ID: " + item);

            if(warpsMenu.getStringList("playerWarps." + item + ".lore").size() > 0){
                iLore.add("");
                for(String loreString : warpsMenu.getStringList("playerWarps." + item + ".lore")){
                    iLore.add(ChatColor.translateAlternateColorCodes('&', loreString));
                }
            }

            iMeta.setLore(iLore);

            newItem.setItemMeta(iMeta);

            if(warpsMenu.get("playerWarps." + item + ".requiredPermission") == null){
                WarpsMenu.addItem(newItem);
            }

            else if (p.hasPermission(warpsMenu.getString("playerWarps." + item + ".requiredPermission"))){
                WarpsMenu.addItem(newItem);
            }
        }
        p.openInventory(WarpsMenu);
    }
}
