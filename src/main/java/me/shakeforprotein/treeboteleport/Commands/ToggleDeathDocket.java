package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class ToggleDeathDocket implements CommandExecutor {

    private TreeboTeleport pl;

    public ToggleDeathDocket(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if (pl.getConfig().get("deathDocketToggle." + p.getUniqueId()) == null || pl.getConfig().get("deathDocketToggle." + p.getUniqueId()).equals("false")){
                pl.getConfig().set("deathDocketToggle." + p.getUniqueId(), "true");
            }
            else{
                pl.getConfig().set("deathDocketToggle." + p.getUniqueId(), "false");
            }
        }
        else{
            sender.sendMessage(pl.err + "Only players can run this command");
        }
        return true;
    }
}
