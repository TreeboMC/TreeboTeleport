package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NameIt implements CommandExecutor {

    private TreeboTeleport pl;

    public NameIt(TreeboTeleport main){
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!pl.getConfig().getBoolean("disabledCommands.nameit")) {

            StringBuilder fullText = new StringBuilder();
            int i;

            for (i = 0; i < args.length; i++) {
                fullText.append(args[i] + " ");
            }

            String theText = fullText.toString().trim();
            theText = ChatColor.translateAlternateColorCodes('&', theText);

            ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(theText);
            item.setItemMeta(meta);
            ((Player) sender).getInventory().setItemInMainHand(item);
        }else {
            sender.sendMessage(pl.err + "The command /" + cmd + " has been disabled on this server");
        }
        return true;
    }

}
