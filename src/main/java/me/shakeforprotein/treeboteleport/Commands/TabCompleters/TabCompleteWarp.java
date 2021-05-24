package me.shakeforprotein.treeboteleport.Commands.TabCompleters;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TabCompleteWarp implements TabCompleter {
    private TreeboTeleport pl;

    public TabCompleteWarp(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("warp")) {
            if (args.length == 1) {
                ArrayList<String> outputStrings = new ArrayList<>();

                File listFile = new File(pl.getDataFolder(), File.separator + "warps.yml");
                FileConfiguration warpList = YamlConfiguration.loadConfiguration(listFile);
                    for (String item : warpList.getConfigurationSection("warps").getKeys(false)) {
                        if (item.toLowerCase().startsWith(args[0].toLowerCase())) {
                            if(warpList.getConfigurationSection("warps." + item).get("requiredPermission") == null || sender.hasPermission(warpList.getString("warps." + item + ".requiredPermission"))) {
                                outputStrings.add(item);
                            }
                        }
                    }

                return outputStrings;
            }
        }
        return null;
    }
}
