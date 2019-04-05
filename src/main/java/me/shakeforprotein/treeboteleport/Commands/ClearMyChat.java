package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ClearMyChat implements CommandExecutor {

    private TreeboTeleport pl;

    public ClearMyChat(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        int i;
        for(i=0; i<30; i++) {
            sender.sendMessage("");
        }
        return true;
    }
}
