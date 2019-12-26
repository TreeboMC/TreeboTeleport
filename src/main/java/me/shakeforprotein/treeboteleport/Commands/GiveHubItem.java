package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class GiveHubItem {

    private TreeboTeleport pl;

    public GiveHubItem(TreeboTeleport main) {
        this.pl = main;
    }


    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Issues a new Hub item if the player doesn't have one.");
                    this.setUsage("/GiveHubItem - requires tbteleport.player.givehubitem");
                    this.setPermission("tbteleport.player.givehubitem");
                    if (sender.hasPermission(this.getPermission())) {


                        Player p = (Player) sender;
                        Inventory inv = p.getInventory();
                        ItemStack hubItem = pl.getHubItem();
                        if (!inv.contains(hubItem)) {
                            inv.addItem(hubItem);
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
