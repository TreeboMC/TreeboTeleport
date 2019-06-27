package me.shakeforprotein.treeboteleport.Listeners;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    public TreeboTeleport pl;

    public PlayerMoveListener(TreeboTeleport main){
        this.pl = main;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        if(pl.lockMove.containsKey(e.getPlayer().getUniqueId())){
            e.setTo(e.getFrom());
        }
    }
}
