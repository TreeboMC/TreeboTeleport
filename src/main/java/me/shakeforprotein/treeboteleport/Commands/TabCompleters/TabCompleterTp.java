package me.shakeforprotein.treeboteleport.Commands.TabCompleters;

import me.shakeforprotein.treeboteleport.Bungee.BungeeSend;
import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabCompleterTp implements TabCompleter {

    private TreeboTeleport pl;
    private BungeeSend bungeeSend;

  /*  public TabCompleteTp(TreeboTeleport main) {
        this.pl = main;
        bungeeSend = new BungeeSend(pl);
    }*/

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            ArrayList<String> outputStrings = new ArrayList<>();

            for(String player : pl.fullPlayerList){
                if(player.toLowerCase().startsWith(args[0].toLowerCase())){
                    outputStrings.add(player);
                }
            }
            return outputStrings;
        }

        else if (args.length == 2){
            ArrayList<String> outputStrings = new ArrayList<>();

            for(String player : pl.fullPlayerList){
                if(player.toLowerCase().startsWith(args[1].toLowerCase())){
                    outputStrings.add(player);
                }
            }
            return outputStrings;

        }
        return null;
    }
}
