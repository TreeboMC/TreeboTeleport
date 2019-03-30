package me.shakeforprotein.treeboteleport.Listeners;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import me.shakeforprotein.treeboteleport.UpdateChecker.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class PlayerJoinListener implements Listener {

    private TreeboTeleport pl;
    private UpdateChecker uc;

    public PlayerJoinListener(TreeboTeleport main) {
        this.pl = main;
        this.uc = new UpdateChecker(main);
    }

    @EventHandler
    public boolean onPlayerJoin(PlayerJoinEvent e) {
        if (pl.getConfig().getBoolean("isHubServer")) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
                public void run() {
                    Player p = e.getPlayer();
                    Inventory inv = p.getInventory();
                    ItemStack hubItem = pl.getHubItem();
                    if (!inv.contains(hubItem)) {
                        inv.addItem(hubItem);
                    }
                }
            }, 30L);

            if (e.getPlayer().hasPermission(uc.requiredPermission)) {
                uc.getCheckDownloadURL(e.getPlayer());
                pl.getConfig().set(e.getPlayer().getName(), "true");
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
                    public void run() {
                        pl.getConfig().set(e.getPlayer().getName(), "false");
                    }
                }, 75L);
            }
        }


        return true;
    }
}
