package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;

public class MergeEssentialsData implements CommandExecutor {

    private TreeboTeleport pl;

    public MergeEssentialsData(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        File essDataFolder = new File("plugins" + File.separator + "Essentials");
        File userDataFoler = new File(essDataFolder + File.separator + "userdata");
        File homesFolder = new File(pl.getDataFolder() + File.separator + "homes");

        for (File userFile : userDataFoler.listFiles()) {
            String fileName = userFile.getName();
            sender.sendMessage(fileName);
            File essUserFile = new File(userDataFoler + File.separator + fileName);
            File localHomeFile = new File(homesFolder + File.separator + fileName);
            if (!localHomeFile.exists()) {
                try {
                    FileConfiguration essHomes = YamlConfiguration.loadConfiguration(essUserFile);
                    if (essHomes.getConfigurationSection("homes") != null) {
                        localHomeFile.createNewFile();
                        FileConfiguration playerHomes = YamlConfiguration.loadConfiguration(localHomeFile);
                        for (String home : essHomes.getConfigurationSection("homes").getKeys(false)) {
                            playerHomes.set("playerName", essHomes.getString("lastAccountName"));
                            playerHomes.set("homes." + home + ".name", home);
                            playerHomes.set("homes." + home + ".world", essHomes.getString("homes." + home + ".world"));
                            playerHomes.set("homes." + home + ".x", essHomes.getInt("homes." + home + ".x"));
                            playerHomes.set("homes." + home + ".y", essHomes.getInt("homes." + home + ".y"));
                            playerHomes.set("homes." + home + ".z", essHomes.getInt("homes." + home + ".z"));
                            playerHomes.set("homes." + home + ".pitch", essHomes.getInt("homes." + home + ".pitch"));
                            playerHomes.set("homes." + home + ".yaw", essHomes.getInt("homes." + home + ".yaw"));
                        }
                        playerHomes.save(localHomeFile);
                    }
                } catch (IOException e) {
                    sender.sendMessage("Creating new file (" + fileName + ") failed");
                }
            }


        }
        return true;
    }
}
