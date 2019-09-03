package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FixSkyGridHomes implements CommandExecutor {

    private TreeboTeleport pl;

    public FixSkyGridHomes(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!pl.getConfig().getBoolean("disabledCommands.fixskygridhomes")) {

            if (args.length == 0) {
                sender.sendMessage(pl.badge + "I STRONGLY recommend you make a backup of your player homes");
                sender.sendMessage("type '/fixskygridhomes go' to continue");
            } else if (args[0].equalsIgnoreCase("go")) {
                File homesFolder = new File(pl.getDataFolder() + File.separator + "homes");
                for (File userFile : homesFolder.listFiles()) {
                    String fileName = userFile.getName();
                    sender.sendMessage(fileName);
                    File localHomeFile = new File(homesFolder + File.separator + fileName);
                    if (!localHomeFile.exists()) {
                        try {
                            FileConfiguration playerHomes = YamlConfiguration.loadConfiguration(localHomeFile);
                            for (String home : playerHomes.getConfigurationSection("homes").getKeys(false)) {
                                String world = playerHomes.getString("homes." + home + ".world");
                                if (world.contains("SkyGrid-world")) {
                                    world = world.replace("SkyGrid-world", "skygrid_world");
                                    playerHomes.set("homes." + home + ".world", world);
                                }
                            }
                            playerHomes.save(localHomeFile);

                        } catch (IOException e) {
                            sender.sendMessage("Repairing (" + fileName + ") failed");
                        }
                    }


                }
            }
        }else {
            sender.sendMessage(pl.err + "The command /" + cmd + " has been disabled on this server");
        }
        return true;
    }
}
