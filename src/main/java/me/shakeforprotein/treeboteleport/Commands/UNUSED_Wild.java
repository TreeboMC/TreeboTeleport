package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.Random;

public class UNUSED_Wild {

    private TreeboTeleport pl;
    private Random random = new Random();
    private int Y = 0;

    public UNUSED_Wild(TreeboTeleport main) {
        this.pl = main;
    }

    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Randomly teleports a player.");
                    this.setUsage("/wild or /wild <range>- requires tbteleport.player.wild");
                    this.setPermission("tbteleport.player.wild");
                    if (sender.hasPermission(this.getPermission())) {

                        if (args.length == 1) {

                            if (isNumeric(args[0])) {
                                int range = Integer.parseInt(args[0]);

                                if (range > 2499 && range < 25001) {
                                    Player targetPlayer = (Player) sender;

                                    World w = targetPlayer.getWorld();
                                    Location targetLocation = findSafeBlock(w, ((Player) targetPlayer), true, range);
                                    if(targetLocation != null) {
                                        targetPlayer.teleport(targetLocation);
                                    }
                                } else if (range < 2500) {
                                    sender.sendMessage(pl.err + " To prevent abuse, this feature requires a minimum range of 2500 blocks.");
                                } else if (range > 25000) {
                                    sender.sendMessage(pl.err + " To reduce demands on server resources this feature is limited to a maximum 25000 blocks");
                                }
                            } else if (sender.hasPermission("tbteleport.staff.wild.other")) {
                                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);
                                World w = ((Player) targetPlayer).getWorld();
                                if (targetPlayer.isOnline()) {
                                    Location targetLocation = findSafeBlock(w, ((Player) targetPlayer), false, 0);
                                    if(targetLocation != null) {
                                        ((Player) targetPlayer).teleport(targetLocation);
                                    }
                                } else {
                                    sender.sendMessage(pl.err + "Player not found");
                                }
                            } else {
                                sender.sendMessage(pl.err + "Argument must be a number.");
                            }
                        } else if (sender instanceof Player) {
                            if (!pl.getCommandCooldown((Player) sender)) {
                                sender.sendMessage(pl.badge + "Checking for safe location. This may take a moment.");
                                Player player = (Player) sender;
                                World w = player.getWorld();

                                if (args.length == 0) {
                                    Location targetLocation = findSafeBlock(w, ((Player) sender), false, 0);
                                    if(targetLocation != null) {
                                        ((Player) sender).teleport(targetLocation);
                                    }
                                } else {
                                    sender.sendMessage(pl.err + "This command does not support additional arguments.");
                                }
                            } else {
                                sender.sendMessage(pl.err + "This command is on cooldown and can only be run once every " + pl.getConfig().getString("CommandDelay") + " seconds");
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You do not have access to this command. You require permission node " + ChatColor.GOLD + this.getPermission());
                    }
                    return true;
                }
            };
            pl.registerNewCommand(pl.getDescription().getName(), item2);
        }
        return true;
    }


    private int getCoords(int min, int max) {
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
                if (uBlock.getType() == Material.GRASS_BLOCK || uBlock.getType() == Material.END_STONE) {
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
            if (highBlock > 100) {
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

    private Location findSafeBlock(World w, CommandSender sender, boolean override, int range) {
        boolean notSafe = false;
        long minX = pl.getConfig().getInt("wild.minX");
        long minZ = pl.getConfig().getInt("wild.minZ");
        long maxX = pl.getConfig().getInt("wild.maxX");
        long maxZ = pl.getConfig().getInt("wild.maxZ");

        if (override) {
            if (sender instanceof Player) {
                Player s = (Player) sender;
                Location temp = s.getLocation();
                minX = temp.getBlockX() - range;
                minZ = temp.getBlockZ() - range;
                maxX = temp.getBlockX() + range;
                maxZ = temp.getBlockZ() + range;
                s.sendMessage("Got range " + range);
            }
        }

        int X = 1;
        int Z = 1;
        int attempts = 0;
        int maxAttempts = 10;
        while (!isSafe(w, X, Z, attempts)) {
            X = getCoords(1, ((int) maxX - (int) minX)) + (int) minX;
            Z = getCoords(1, ((int) maxZ - (int) minZ)) + (int) minZ;
            if (attempts % 2 == 0) {
                //sender.sendMessage("Attempt " + attempts + " of " + maxAttempts);
            }
            attempts++;
            if (attempts >= maxAttempts) {
                String failMessage = ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("wild.messages.failure")).replace("{COORDS}", X + "," + Y + "," + Z);
                sender.sendMessage(failMessage);
                notSafe = true;
                break;
            }
        }

        Location landOn = w.getBlockAt(X, (Y + 2), Z).getLocation();
        landOn.add(0.5, 0, 0.5);
        if (pl.getConfig().getBoolean("useCooldown")) {
            pl.setCooldown((Player) sender);
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("wild.messages.success")).replace("{COORDS}", X + "," + Y + "," + Z));
        if (pl.getConfig().get("wild.cost") != null) {
            int cost = pl.getConfig().getInt("wild.cost");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + sender.getName() + " " + cost);
        }
        if(notSafe){
            return null;
        }
        else {
            return landOn;
        }
    }


    public static boolean isNumeric(String strNum) {
        return strNum.matches("-?\\d+(\\.\\d+)?");
    }

}
