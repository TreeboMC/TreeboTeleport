package me.shakeforprotein.treeboteleport.Listeners;

import me.shakeforprotein.treeboteleport.Methods.Guis.OpenHubMenu;
import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BlockIterator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HubItemListener implements Listener {

    private TreeboTeleport pl;
    private OpenHubMenu openHubMenu;

    public HubItemListener(TreeboTeleport main) {
        this.pl = main;
        this.openHubMenu = new OpenHubMenu(pl);
    }


    private ItemStack getHubItem() {
        File menuYml = new File(pl.getDataFolder(), File.separator + "hubMenu.yml");
        FileConfiguration hubMenu = YamlConfiguration.loadConfiguration(menuYml);

        ItemStack configItem = new ItemStack(Material.getMaterial(hubMenu.getString("hubItem.item").trim()), 1);
        ItemMeta confMeta = configItem.getItemMeta();
        confMeta.setDisplayName(hubMenu.getString("hubItem.name"));
        List<String> confItemLore = new ArrayList<String>();
        confItemLore.add(hubMenu.getString("hubItem.lore"));
        confMeta.setLore(confItemLore);
        configItem.setItemMeta(confMeta);
        return configItem;
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent e) {
        ItemStack configItem = getHubItem();
        if (e.getAction() == Action.RIGHT_CLICK_AIR) {
            Player p = e.getPlayer();


            if (e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasLore()) {
                if (e.getItem().getType().equals(configItem.getType()) && ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()).equalsIgnoreCase(configItem.getItemMeta().getDisplayName()) && e.getItem().getItemMeta().getLore().get(0).equalsIgnoreCase(configItem.getItemMeta().getLore().get(0))) {
                    openHubMenu.openHubMenu(p);
                }
            }
        } else if (e.getAction() == Action.LEFT_CLICK_AIR) {
            if (e.getPlayer().hasPermission("tbteleport.fasttravel")) {
                if (e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasLore()) {
                    if (e.getItem().getType().equals(configItem.getType()) && ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(configItem.getItemMeta().getDisplayName())) && e.getItem().getItemMeta().getLore().get(0).equalsIgnoreCase(configItem.getItemMeta().getLore().get(0))) {
                        if (getTargetBlock(e.getPlayer(), 32) != null) {
                            Block target = getTargetBlock(e.getPlayer(), 32);
                            e.getPlayer().teleport(target.getLocation().add(0, 1, 0));
                        }
                    }
                }
            }
        }
    }


    public final Block getTargetBlock(Player player, int range) {
        try {
            BlockIterator iter = new BlockIterator(player, range);
            Block lastBlock = iter.next();
            while (iter.hasNext()) {
                lastBlock = iter.next();
                if (lastBlock.getType() == Material.AIR) {
                    continue;
                }
                break;
            }
            return lastBlock;
        } catch (IllegalStateException e) {
            return null;
        }
    }
}

