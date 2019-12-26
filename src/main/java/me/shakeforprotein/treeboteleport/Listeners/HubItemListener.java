package me.shakeforprotein.treeboteleport.Listeners;

import me.shakeforprotein.treeboteleport.Methods.Guis.OpenHubMenu;
import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public class HubItemListener implements Listener {

    private TreeboTeleport pl;
    private OpenHubMenu openHubMenu;

    public HubItemListener(TreeboTeleport main) {
        this.pl = main;
        this.openHubMenu = new OpenHubMenu(pl);
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent e) {
        ItemStack configItem = null;
        if(pl.getConfig().getBoolean("isHubServer")) {
            configItem = pl.getHubItem();
        }
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            if (e.getItem() != null && e.getItem().equals(configItem)) {
                openHubMenu.openHubMenu(p);
            } else if (e.getPlayer().hasPermission("tbteleport.admin.restoreinventory") && e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().contains("Death Docket -")) {
                if (e.getItem().getItemMeta().getLore().get(4) != null) {
                    String cmd = "tellraw " + e.getPlayer().getName() + " [\"\",{\"text\":\"Click \"},{\"text\":\"[HERE]\",\"color\":\"green\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/restoreplayerinventory " + e.getItem().getItemMeta().getLore().get(0).split(" - ")[1] + " " + e.getItem().getItemMeta().getLore().get(4).split(" - ")[1] + "\"}},{\"text\":\" to restore player inventory.\"}]";
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);

                }
            }
        }
    }
}

