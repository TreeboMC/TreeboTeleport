package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetVanillaWorldSpawn implements CommandExecutor {

        private TreeboTeleport pl;

        public SetVanillaWorldSpawn(TreeboTeleport main){
            this.pl = main;
        }

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
                if (sender instanceof Player) {
                    World w = ((Player) sender).getWorld();
                    sender.sendMessage(pl.badge + "Current Vanilla World Spawn: " + w.getSpawnLocation().toString());
                    Location pLoc = ((Player) sender).getLocation();
                    w.setSpawnLocation(pLoc);
                    sender.sendMessage(pl.badge + "Set Vanilla World Spawn to: " + w.getSpawnLocation().toString());


            }
            return true;
        }
}
