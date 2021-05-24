package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NameIt {

    private TreeboTeleport pl;

    public NameIt(TreeboTeleport main) {
        this.pl = main;
    }

    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Allows a player to rename their held item without a nametag");
                    this.setUsage("/nameit <item name> - requires tbteleport.vipplus.nameit");
                    this.setPermission("tbteleport.vipplus.nameit");
                    if (sender.hasPermission(this.getPermission())) {

                        StringBuilder fullText = new StringBuilder();
                        int i;

                        for (i = 0; i < args.length; i++) {
                            fullText.append(args[i] + " ");
                        }

                        String theText = fullText.toString().trim();
                        theText = ChatColor.translateAlternateColorCodes('&', theText);

                        ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
                        if(item.getType() == Material.VILLAGER_SPAWN_EGG){
                            sender.sendMessage(pl.badge + "Sorry, you cannot rename villager spawn eggs.");
                        }
                        else {
                            ItemMeta meta = item.getItemMeta();
                            meta.setDisplayName(theText);
                            item.setItemMeta(meta);
                            ((Player) sender).getInventory().setItemInMainHand(item);
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You do not have access to this command. You require permission node " + ChatColor.GOLD + this.getPermission());
                    }
                    return true;
                }
            };
            pl.registerNewCommand(pl.getDescription().getName(), item2);
        }
        return true;
    }
}
