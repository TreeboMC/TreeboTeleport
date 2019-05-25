package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetShop implements CommandExecutor {

    private TreeboTeleport pl;

    public SetShop(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            String world = p.getWorld().getName();

            pl.getConfig().set("shop." + p.getWorld().getName() + ".world", world);
            pl.getConfig().set("shop." + p.getWorld().getName() + ".location", p.getLocation());

            sender.sendMessage(pl.badge + p.getWorld().getName() + "Shop set successfully, don't forget to run /ttelesaveconfig");
        }
        else{
            sender.sendMessage(pl.err + "This command can only be run as a player");
        }
        return true;
    }
}
