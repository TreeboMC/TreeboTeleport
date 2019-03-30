package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FixTTHomes implements CommandExecutor {

    private TreeboTeleport pl;

    public FixTTHomes(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        File homesFolder = new File(pl.getDataFolder() + File.separator + "homes");
        for (File userFile : homesFolder.listFiles()) {
            String fileName = userFile.getName();
            sender.sendMessage(fileName);
            File localHomeFile = new File(homesFolder + File.separator + fileName);
            if (!localHomeFile.exists()) {
                try {
                        FileConfiguration playerHomes = YamlConfiguration.loadConfiguration(localHomeFile);
                        for (String home : playerHomes.getConfigurationSection("homes").getKeys(false)) {
                            playerHomes.set("homes." + home + ".x", Integer.parseInt(playerHomes.getString("homes." + home + ".x")));
                            playerHomes.set("homes." + home + ".y", Integer.parseInt(playerHomes.getString("homes." + home + ".y")));
                            playerHomes.set("homes." + home + ".z", Integer.parseInt(playerHomes.getString("homes." + home + ".z")));
                            playerHomes.set("homes." + home + ".x", Integer.parseInt(playerHomes.getString("homes." + home + ".pitch")));
                            playerHomes.set("homes." + home + ".x", Integer.parseInt(playerHomes.getString("homes." + home + ".yaw")));
                        }
                        playerHomes.save(localHomeFile);

                } catch (IOException e) {
                    sender.sendMessage("Repairing (" + fileName + ") failed");
                }
            }


        }
        return true;
    }
}
