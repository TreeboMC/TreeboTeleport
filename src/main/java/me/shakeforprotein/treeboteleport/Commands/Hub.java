package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.Methods.Guis.OpenHubMenu;
import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Hub implements CommandExecutor {

    private TreeboTeleport pl;
    private OpenHubMenu openHubMenu;

    public Hub(TreeboTeleport main){
        this.pl = main;
        this.openHubMenu = new OpenHubMenu(pl);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String w = player.getWorld().getName();
            if (cmd.getName().equalsIgnoreCase("hub")) {
                if (args.length == 0){
                    openHubMenu.openHubMenu((Player) sender);
                }
                else{
                    sender.sendMessage("The HUB command does not support additional arguments");
                }
            }
        }
        return true;
    }
}
