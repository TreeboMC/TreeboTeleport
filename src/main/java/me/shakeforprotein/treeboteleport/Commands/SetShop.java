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
            double x = p.getLocation().getX();
            double y = p.getLocation().getY();
            double z = p.getLocation().getZ();
            float pitch = p.getLocation().getPitch();
            float yaw = p.getLocation().getYaw();

            pl.getConfig().set("shop." + p.getWorld().getName() + ".world", world);
            pl.getConfig().set("shop." + p.getWorld().getName() + ".x", x);
            pl.getConfig().set("shop." + p.getWorld().getName() + ".y", y);
            pl.getConfig().set("shop." + p.getWorld().getName() + ".z", z);
            pl.getConfig().set("shop." + p.getWorld().getName() + ".pitch", pitch);
            pl.getConfig().set("shop." + p.getWorld().getName() + ".yaw", yaw);
        }
        else{
            sender.sendMessage(pl.err + "This command can only be run as a player");
        }
        return true;
    }
}
