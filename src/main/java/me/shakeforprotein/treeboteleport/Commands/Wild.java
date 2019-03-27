package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class Wild implements CommandExecutor{

        private TreeboTeleport pl;
        private Random random = new Random();
        private int Y = 0;

        public Wild(TreeboTeleport main){
            this.pl = main;
        }

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (pl.getCD((Player) sender)) {
                if (sender instanceof Player && sender.hasPermission("tbteleport.wild")) {
                    Player player = (Player) sender;
                    World w = player.getWorld();
                    if (args.length == 0) {


                        ((Player) sender).teleport((findSafeBlock(w, (Player) sender)));

                    } else {
                        sender.sendMessage(pl.err + "This command does not support additional arguments.");
                    }
                }
            }
            return true;
        }


        private int getCoords(int min, int max){
            int coord = random.nextInt(max - min) + min;
            return coord;
        }

        private boolean isSafe(World w, int X, int Z, int attemps) {
            boolean safe = false;
            int minY = pl.getConfig().getInt("wild.minY");
            int maxY = pl.getConfig().getInt("wild.maxY");

            if (attemps == 0) {
                return safe;
            } else if (w.getEnvironment() != World.Environment.NETHER) { //World is NOT Nether
                int highBlock = maxY;
                while (highBlock > minY) {
                    Block block = w.getBlockAt(X, highBlock, Z);
                    Block uBlock = block;
                    Block aBlock = block.getRelative(0, 2, 0);
                    block = block.getRelative(0, 1, 0);
                    //Bukkit.broadcastMessage(X + "," + highBlock + "," + Z);
                    if (uBlock.getType() == Material.GRASS_BLOCK) {
                        if (block.getType() == Material.AIR) {
                            if (aBlock.getType() == Material.AIR) {
                                Y = highBlock;
                                safe = true;
                                break;
                            }
                        }
                    }
                    highBlock--;
                }
            } else { //World is Nether
                int highBlock = maxY;
                if (highBlock > 100){
                    highBlock = 100;
                }
                while (highBlock > minY) {
                    Block block = w.getBlockAt(X, highBlock, Z);
                    Block uBlock = block;
                    Block aBlock = block.getRelative(0, 2, 0);
                    block = block.getRelative(0, 1, 0);
                    //Bukkit.broadcastMessage(X + "," + highBlock + "," + Z);
                    if (uBlock.getType() == Material.NETHERRACK) {
                        if (block.getType() == Material.AIR) {
                            if (aBlock.getType() == Material.AIR) {
                                Y = highBlock;
                                safe = true;
                                break;
                            }
                        }
                    }
                    highBlock--;
                }
            }
            return safe;
        }

        private Location findSafeBlock(World w, CommandSender sender){
            long minX = pl.getConfig().getInt("wild.minX");
            long minZ = pl.getConfig().getInt("wild.minZ");
            long maxX = pl.getConfig().getInt("wild.maxX");
            long maxZ = pl.getConfig().getInt("wild.maxZ");

            int X = 1;
            int Z = 1;
            int attempts = 0;
            int maxAttempts = 100;
            while (!isSafe(w, X, Z, attempts)){
                X = getCoords(1, ((int) maxX - (int) minX)) + (int) minX;
                Z = getCoords(1, ((int) maxZ - (int) minZ)) + (int) minZ;
                attempts++;
                if(attempts >= maxAttempts){
                    String failMessage = ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("wild.messages.failure")).replace("{COORDS}",X + "," + Y + "," + Z);
                    sender.sendMessage(failMessage);
                    break;
                }
            }

            Location landOn = w.getBlockAt(X,(Y+2),Z).getLocation();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("wild.messages.success")).replace("{COORDS}",X + "," + Y + "," + Z));

            return landOn;
        }
}
