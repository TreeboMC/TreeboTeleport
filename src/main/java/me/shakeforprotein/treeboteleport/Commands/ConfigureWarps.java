package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class ConfigureWarps implements CommandExecutor {

    private TreeboTeleport pl;

    public ConfigureWarps(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!pl.getConfig().getBoolean("disabledCommands.configurewarps")) {
            File warpsFile = new File(pl.getDataFolder(), "warps.yml");
            YamlConfiguration warpsYaml = YamlConfiguration.loadConfiguration(warpsFile);
            boolean found = false;


            if (args.length < 4) {
                sender.sendMessage(pl.err + "Insufficient Arguments");
                doHelp(sender);
            } else if (args[0].equalsIgnoreCase("set")) {
                for (String item : warpsYaml.getConfigurationSection("warps").getKeys(false)) {
                    if (args[2].equalsIgnoreCase(item)) {
                        found = true;
                        if (args[1].equalsIgnoreCase("icon")) {
                            if (Material.getMaterial(args[3].toUpperCase()) != null) {
                                warpsYaml.set("warps." + item + ".icon", args[3].toUpperCase());
                                try {
                                    warpsYaml.save(warpsFile);
                                    sender.sendMessage("Warps file saved");
                                } catch (IOException err) {
                                    pl.makeLog(err);
                                    sender.sendMessage(pl.err + "Failed to save warps file");
                                }
                            } else {
                                sender.sendMessage(pl.err + "Unknown Item: '" + args[3].toUpperCase() + "'");
                            }

                        } else if (args[1].equalsIgnoreCase("title")) {
                            StringBuilder fullText = new StringBuilder();
                            int i;
                            for (i = 3; i < args.length; i++) {
                                fullText.append(args[i] + " ");
                            }
                            warpsYaml.set("warps." + item + ".title", fullText.toString().trim());
                            try {
                                warpsYaml.save(warpsFile);
                                sender.sendMessage("Warps file saved");
                            } catch (IOException err) {
                                pl.makeLog(err);
                                sender.sendMessage(pl.err + "Failed to save warps file");
                            }
                        } else {
                            sender.sendMessage(pl.err + "Unknown setting '" + args[1] + "'");
                        }
                    }
                }
                if (!found) {
                    sender.sendMessage(pl.err + "Could not find warp with that id");
                }
            } else {
                sender.sendMessage(pl.err + "Incorrect usage");
                doHelp(sender);
            }
        }else {
            sender.sendMessage(pl.err + "The command /" + cmd + " has been disabled on this server");
        }
        return true;
    }

    private void doHelp(CommandSender s) {
        s.sendMessage(pl.badge + "Correct usage is as follows");
        s.sendMessage("/configurewarps set <icon | title> <warp id> <new value>");
    }
}