package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TpToggle implements CommandExecutor{

    private TreeboTeleport pl;

    public TpToggle(TreeboTeleport main) {
        this.pl = main;
    }

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(pl.getConfig().get("tptoggle." + pl.getName()) == null){
            pl.getConfig().set("tptoggle." + pl.getName(), 1);
            sender.sendMessage(pl.badge + "Teleport requests have been toggled OFF");
        }
        else if(pl.getConfig().getInt("tptoggle." + sender.getName()) == 1){
            pl.getConfig().set("tptoggle." + sender.getName(), 0);
            sender.sendMessage(pl.badge + "Teleport requests have been toggled ON");
        }
        else if(pl.getConfig().getInt("tptoggle." + sender.getName()) == 0){
            pl.getConfig().set("tptoggle." + sender.getName(), 1);
            sender.sendMessage(pl.badge + "Teleport requests have been toggled OFF");
        }
        return true;

    }
}
