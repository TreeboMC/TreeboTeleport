package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class Concept implements CommandExecutor {

    private TreeboTeleport pl;

    public Concept(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player && sender.hasPermission("tbteleport.concept")) {
            if (pl.getCD((Player) sender)) {

                if (args.length != 2) {
                    sender.sendMessage(ChatColor.RED + "Incorrect usage. This command only accepts two arguments.");
                } else {
                    if (pl.getServer().getName().equalsIgnoreCase(args[0])) {

                    } else {
                        try {
                            pl.connection.createStatement().executeUpdate("INSERT INTO `" + pl.table + "` ('IGNAME', '') VALUES ('" + sender.getName() + "','')");
                        } catch (SQLException err) {
                            pl.makeLog(err);
                        }
                    }
                }
            }
        }
        return true;
    }
}
