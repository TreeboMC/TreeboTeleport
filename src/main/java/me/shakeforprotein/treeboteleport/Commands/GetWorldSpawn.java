package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetWorldSpawn implements CommandExecutor {

    private TreeboTeleport pl;

    public GetWorldSpawn(TreeboTeleport main){
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (sender instanceof Player) {
                World w = ((Player) sender).getWorld();
                System.out.println("Player has requested Spawn Location");
                System.out.println(w.getSpawnLocation().toString());
                sender.sendMessage(w.getSpawnLocation().toString());
            }
        return true;
    }
}
