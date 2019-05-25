package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetTTeleCooldown implements CommandExecutor {

    private TreeboTeleport pl;

    public SetTTeleCooldown(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(args.length != 1){
            sender.sendMessage(pl.err + "This command expects a single argument <true|false|integer>");
        }
        else{
            if(args[0].equalsIgnoreCase("true") || args[0].equalsIgnoreCase("false")){
                pl.getConfig().set("useCooldowns", args[0]);
            }
            else if (pl.isInteger(args[0])){
                pl.getConfig().set("CommandDelay", args[0]);
            }
            else{
                sender.sendMessage(pl.err + "Invalid input. Please use /setttelecooldown <true|false|integer>");
            }
        }
        return true;
    }
}
