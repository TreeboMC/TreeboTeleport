package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SaveConfig implements CommandExecutor {

    private TreeboTeleport pl;

    public SaveConfig(TreeboTeleport main){
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!pl.getConfig().getBoolean("disabledCommands.ttelesaveconfig")) {
            pl.saveConfig();
            sender.sendMessage(pl.badge + "Saved Config");
        }else {
            sender.sendMessage(pl.err + "The command /" + cmd + " has been disabled on this server");
        }
        return true;
    }
}
