package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;


public class ToggleDeathDocket {

    private TreeboTeleport pl;

    public ToggleDeathDocket(TreeboTeleport main) {
        this.pl = main;
    }

    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Disables receiving of Death Docket");
                    this.setUsage("/ToggleDeathDocket");
                    //this.setPermission("tbteleport.admin");
                    if (sender.hasPermission(this.getPermission())) {

                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            if (pl.getConfig().get("deathDocket.toggle." + p.getUniqueId()) == null || pl.getConfig().get("deathDocket.toggle." + p.getUniqueId()).equals("false")) {
                                pl.getConfig().set("deathDocket.toggle." + p.getUniqueId(), "true");
                            } else {
                                pl.getConfig().set("deathDocket.toggle." + p.getUniqueId(), "false");
                            }
                        } else {
                            sender.sendMessage(pl.err + "Only players can run this command");
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
