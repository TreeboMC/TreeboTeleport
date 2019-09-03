package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class ToggleDeathDocket implements CommandExecutor {

    private TreeboTeleport pl;

    public ToggleDeathDocket(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!pl.getConfig().getBoolean("disabledCommands.toggledeathdocket")) {

            if(sender instanceof Player){
            Player p = (Player) sender;
            if (pl.getConfig().get("deathDocket.toggle." + p.getUniqueId()) == null || pl.getConfig().get("deathDocket.toggle." + p.getUniqueId()).equals("false")){
                pl.getConfig().set("deathDocket.toggle." + p.getUniqueId(), "true");
            }
            else{
                pl.getConfig().set("deathDocket.toggle." + p.getUniqueId(), "false");
            }
        }
        else{
            sender.sendMessage(pl.err + "Only players can run this command");
        }}else {
            sender.sendMessage(pl.err + "The command /" + cmd + " has been disabled on this server");
        }
        return true;
    }
}
