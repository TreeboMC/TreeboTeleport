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

public class OpenWarpsMenu {

    private TreeboTeleport pl;

    public OpenWarpsMenu(TreeboTeleport main) {
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
                } catch (FileNotFoundException e) {
                    pl.makeLog(e);
                }
            } catch (IOException e) {
                pl.makeLog(e);
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

        if(warpsMenu.get("menuRows") != null){
            invSize = warpsMenu.getInt("menuRows") * 9;
        }
        Inventory WarpsMenu = Bukkit.createInventory(null, invSize, menuName);

        for(String item : warpsMenu.getConfigurationSection("warps").getKeys(false)){

            String title = "";
            String icon = "ENDER_PEARL";
            String id = item;

            if(warpsMenu.getString("warps." + item + ".title") != null){
                title = ChatColor.translateAlternateColorCodes('&',warpsMenu.getString("warps." + item + ".title"));
            }
            else{
                title = "Warp " + item;
                warpsMenu.set("warps." + item + ".title", title);
            }
            if(warpsMenu.getString("warps." + item + ".icon") != null){
                icon = warpsMenu.getString("warps." + item + ".icon");
            }
            else{
                warpsMenu.set("warps." + item + ".icon", icon);
            }
            try{warpsMenu.save(menuYml);}
            catch (IOException err){
                pl.makeLog(err);
                System.out.println("Failed to update warps file");
            }

            ItemStack newItem = new ItemStack(Material.getMaterial(icon), 1);
            ItemMeta iMeta = newItem.getItemMeta();
            List<String> iLore = new ArrayList<String>();

            iMeta.setDisplayName(title);
            iLore.add("ID: " + item);

            iMeta.setLore(iLore);

            newItem.setItemMeta(iMeta);

            if(warpsMenu.get("warps." + item + ".requiredPermission") == null || p.hasPermission(warpsMenu.getString("warps." + item + ".permission"))){
                WarpsMenu.addItem(newItem);
            }
        }
        p.openInventory(WarpsMenu);
    }
}
