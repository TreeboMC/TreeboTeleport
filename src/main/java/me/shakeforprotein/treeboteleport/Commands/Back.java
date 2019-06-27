package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Back implements CommandExecutor {

    private TreeboTeleport pl;

    public Back(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        String pUUID = "player_" + p.getUniqueId();
        // File lastLocFile = new File(pl.getDataFolder(), "lastLocation.yml");
        // FileConfiguration lastLocConf = YamlConfiguration.loadConfiguration(lastLocFile);

        if(pl.lastLocConf.containsKey(pUUID)){
            p.sendMessage(pl.badge + "Sending you to your previous location");
            p.teleport((Location) pl.lastLocConf.get(pUUID));
        }
        else{
            p.sendMessage(pl.err + "Could not find previous location");
        }
        return true;
    }
}