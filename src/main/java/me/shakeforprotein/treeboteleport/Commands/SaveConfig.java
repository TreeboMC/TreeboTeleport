package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

public class SaveConfig implements CommandExecutor{

    private TreeboTeleport pl;

    public SaveConfig(TreeboTeleport main) {
        this.pl = main;
    }

    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Saves the TreeboTeleport Config to file");
                    this.setUsage("/ttelesaveconfig - requires tbteleport.admin");
                    this.setPermission("tbteleport.admin");
                    if (sender.hasPermission(this.getPermission())) {

                        pl.saveConfig();
                        sender.sendMessage(pl.badge + "Saved Config");
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        pl.saveConfig();
        sender.sendMessage(pl.badge + "Saved Config");
        return true;
    }
}
