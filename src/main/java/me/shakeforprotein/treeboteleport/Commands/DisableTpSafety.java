package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisableTpSafety implements CommandExecutor {

    private TreeboTeleport pl;

    public DisableTpSafety(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;
            //If hash contains player id then tp protection is currently disabled. As such, we remove the player id to re enable protection.
            if(pl.tpSafetyOff.containsKey(p.getUniqueId())){
                pl.tpSafetyOff.remove(p.getUniqueId());
                p.sendMessage(pl.badge + "Teleport protection enabled.");
            }
            else{
                pl.tpSafetyOff.put(p.getUniqueId(), p.getName());
                p.sendMessage(pl.badge + "Teleport Safeties disabled. TreeboMC takes no responsibility for any death as a result of this. Be safe out there.");
            }
        }
        else{
            sender.sendMessage(pl.err + "Only players may disable their teleport protection.");
        }

        return true;
    }
}
