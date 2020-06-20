package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Wild2 {

    private TreeboTeleport pl;
    private Random random = new Random();
    private int Y = 0;
    private int limit = 10;
    private int minY;
    private int maxY;
    private int minX;
    private int maxX;
    private int minZ;
    private int maxZ;


    public Wild2(TreeboTeleport main) {
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
                        minX = (pl.getConfig().getString("wild.minX") != null) ? pl.getConfig().getInt("wild.minX") : -50000;
                        maxX = (pl.getConfig().getString("wild.maxX") != null) ? pl.getConfig().getInt("wild.maxX") : 50000;
                        minZ = (pl.getConfig().getString("wild.minZ") != null) ? pl.getConfig().getInt("wild.minZ") : -50000;
                        maxZ = (pl.getConfig().getString("wild.maxZ") != null) ? pl.getConfig().getInt("wild.maxZ") : 50000;


                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            if (player.getWorld().getName().toLowerCase().contains("grid")) {
                                limit = 10;
                            }
                            if (args.length == 0) {
                                doWild(player);
                            } else if (args.length == 1 && !isNumeric(args[0])) {
                                doStaffWild(player, args[0]);
                            } else if (args.length == 1 && isNumeric(args[0])) {
                                doWildRanged(player, Integer.parseInt(args[0]));
                            } else if (args.length == 2 && isNumeric(args[1])) {
                                doStaffWildRanged(player, args[0], Integer.parseInt(args[1]));
                            } else {
                                sender.sendMessage(pl.badge + pl.err + this.getUsage());
                            }
                        }
                    }
                    return true;
                }
            };
            pl.registerNewCommand(pl.getDescription().getName(), item2);
        }
        return true;
    }


    public void doWild(Player targetPlayer) {
        World w = targetPlayer.getWorld();
        int limit = 0;
        int randX = ThreadLocalRandom.current().nextInt(0, (maxX - minX)) + maxX;
        int randZ = ThreadLocalRandom.current().nextInt(0, (maxZ - minZ)) + maxZ;
        Chunk chunk = w.getBlockAt(randX, 128, randZ).getChunk();
        String sending = "Was unable to find a safe block.";
        for (int a = 0; a < 16; a++) {
            for (int b = 0; b < 16; b++) {
                for (int c = 150; c > 50; c--) {
                    if(targetPlayer.getWorld().getEnvironment() == World.Environment.NETHER){
                        if(c > 126){
                            c=110;
                        }
                    }
                    Block block = chunk.getBlock(a, c, b).getLocation().subtract(0, 1, 0).getBlock();
                    if (block.getType().isSolid() && block.getLocation().add(0, 1, 0).getBlock().getType() == Material.AIR && block.getLocation().add(0, 2, 0).getBlock().getType() == Material.AIR) {
                        if (!isDangerous(block.getType())) {
                            targetPlayer.teleport(chunk.getBlock(a, c, b).getLocation().add(0.5, 0.5, 0.5));
                            targetPlayer.setVelocity(new Vector(0, 0, 0));
                            targetPlayer.setFallDistance(0);
                            sending = "Safe block located at " + block.getX() + " " + block.getY() + " " + block.getZ();
                            if (limit < 1) {
                                targetPlayer.sendMessage(pl.badge + sending);
                                limit++;
                            }
                            break;
                        }
                    }
                }
            }
        }
        targetPlayer.sendMessage(pl.badge + sending);
    }


    public void doWildRanged(Player targetPlayer, int range) {
        if (range > 999 && range < 25001) {
            World w = targetPlayer.getWorld();
            int limit = 0;
            int randX = (ThreadLocalRandom.current().nextInt(0, (range * 2)) - range) + targetPlayer.getLocation().getBlockX();
            int randZ = (ThreadLocalRandom.current().nextInt(0, (range * 2)) - range) + targetPlayer.getLocation().getBlockZ();
            Chunk chunk = w.getBlockAt(randX, 128, randZ).getChunk();
            String sending = "Was unable to find a safe block.";
            boolean foundBlock = false;
            for (int a = 0; a < 16; a++) {
                for (int b = 0; b < 16; b++) {
                    for (int c = 150; c > 50; c--) {
                        if(targetPlayer.getWorld().getEnvironment() == World.Environment.NETHER){
                            if(c > 126){
                                c=110;
                            }
                        }
                        Block block = chunk.getBlock(a, c, b).getLocation().subtract(0, 1, 0).getBlock();
                        if (block.getType().isSolid() && block.getLocation().add(0, 1, 0).getBlock().getType() == Material.AIR && block.getLocation().add(0, 2, 0).getBlock().getType() == Material.AIR) {
                            if (!isDangerous(block.getType())) {
                                targetPlayer.teleport(chunk.getBlock(a, c, b).getLocation().add(0.5, 0.5, 0.5));
                                targetPlayer.setVelocity(new Vector(0, 0, 0));
                                targetPlayer.setFallDistance(0);
                                sending = "Safe block located at " + block.getX() + " " + block.getY() + " " + block.getZ();
                                foundBlock = true;
                                if (limit < 1) {
                                    targetPlayer.sendMessage(pl.badge + sending);
                                    limit++;
                                }
                                break;
                            }
                        }
                    }
                }
            }
            if(!foundBlock) {
                targetPlayer.sendMessage(pl.badge + "was unable to find a safe block.");
            }
        } else if (range > 1000) {
            targetPlayer.sendMessage(pl.badge + pl.err + "For performance reasons, maximum range is capped at 25000.");
        } else if (range < 1000) {
            targetPlayer.sendMessage(pl.badge + pl.err + "For player security reasons, minimum range is capped at 1000.");
        }
    }

    public void doStaffWild(Player sendingPlayer, String targetPlayer) {
        if (sendingPlayer.hasPermission("tbteleport.staff.wild.other")) {
            if (Bukkit.getPlayer(targetPlayer) != null) {
                Player targetPlayerp = Bukkit.getPlayer(targetPlayer);
                World w = targetPlayerp.getWorld();
                int limit = 0;
                int randX = ThreadLocalRandom.current().nextInt(0, (maxX - minX)) + maxX;
                int randZ = ThreadLocalRandom.current().nextInt(0, (maxZ - minZ)) + maxZ;
                Chunk chunk = w.getBlockAt(randX, 128, randZ).getChunk();
                String sending = "Was unable to find a safe block.";
                for (int a = 0; a < 16; a++) {
                    for (int b = 0; b < 16; b++) {
                        for (int c = 150; c > 50; c--) {
                            if(Bukkit.getPlayer(targetPlayer).getWorld().getEnvironment() == World.Environment.NETHER){
                                if(c > 120){
                                    c=110;
                                }
                            }
                            Block block = chunk.getBlock(a, c, b).getLocation().subtract(0, 1, 0).getBlock();
                            if (block.getType().isSolid() && block.getLocation().add(0, 1, 0).getBlock().getType() == Material.AIR && block.getLocation().add(0, 2, 0).getBlock().getType() == Material.AIR) {
                                if (!isDangerous(block.getType())) {
                                    targetPlayerp.teleport(chunk.getBlock(a, c, b).getLocation().add(0.5, 0.5, 0.5));
                                    targetPlayerp.setVelocity(new Vector(0, 0, 0));
                                    targetPlayerp.setFallDistance(0);
                                    sending = "Safe block located at " + block.getX() + " " + block.getY() + " " + block.getZ() + ". Moving the problem element now";
                                    if (limit < 1) {
                                        sendingPlayer.sendMessage(pl.badge + sending);
                                        limit++;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                sendingPlayer.sendMessage(pl.badge + sending);
            }
        }
    }

    public void doStaffWildRanged(Player sendingPlayer, String tPlayer, int range) {
        if (sendingPlayer.hasPermission("tbteleport.staff.wild.other")) {
            if (range < 1000000) {
                if (Bukkit.getPlayer(tPlayer) != null) {
                    Player targetPlayer = Bukkit.getPlayer(tPlayer);
                    World w = targetPlayer.getWorld();
                    int limit = 0;
                    int randX = (ThreadLocalRandom.current().nextInt(0, (range * 2)) - range) + targetPlayer.getLocation().getBlockX();
                    int randZ = (ThreadLocalRandom.current().nextInt(0, (range * 2)) - range) + targetPlayer.getLocation().getBlockZ();
                    Chunk chunk = w.getBlockAt(randX, 128, randZ).getChunk();
                    String sending = "Was unable to find a safe block.";
                    boolean foundBlock = false;
                    for (int a = 0; a < 16; a++) {
                        for (int b = 0; b < 16; b++) {
                            for (int c = 150; c > 50; c--) {
                                if(targetPlayer.getWorld().getEnvironment() == World.Environment.NETHER){
                                    if(c > 126){
                                        c=110;
                                    }
                                }
                                Block block = chunk.getBlock(a, c, b).getLocation().subtract(0, 1, 0).getBlock();
                                if (block.getType().isSolid() && block.getLocation().add(0, 1, 0).getBlock().getType() == Material.AIR && block.getLocation().add(0, 2, 0).getBlock().getType() == Material.AIR) {
                                    if (!isDangerous(block.getType())) {
                                        targetPlayer.teleport(chunk.getBlock(a, c, b).getLocation().add(0.5, 0.5, 0.5));
                                        targetPlayer.setVelocity(new Vector(0, 0, 0));
                                        targetPlayer.setFallDistance(0);
                                        sending = "Safe block located at " + block.getX() + " " + block.getY() + " " + block.getZ() + ". Moving the problem element now.";
                                        foundBlock = true;
                                        if (limit < 1) {
                                            targetPlayer.sendMessage(pl.badge + sending);
                                            limit++;
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if(!foundBlock) {
                        sendingPlayer.sendMessage(pl.badge + sending);
                    }
                } else {
                    sendingPlayer.sendMessage(pl.badge + pl.err + " Unknown player: " + tPlayer);
                }
            }
            sendingPlayer.sendMessage(pl.badge + pl.err + " Come on, isn't that a little much.");
        }
    }

    private int getCoords(int min, int max) {
        int coord = random.nextInt(max - min) + min;
        return coord;
    }

    private int getMod4Coords(int min, int max) {
        int coord = (random.nextInt(((int) max / 4)) - ((int) (min / 4)) + min / 4) * 4;
        return coord + 1;
    }


    public static boolean isNumeric(String strNum) {
        return strNum.matches("-?\\d+(\\.\\d+)?");
    }


    public boolean isDangerous(Material material) {
        ArrayList<Material> dangerous = new ArrayList<>();
        dangerous.add(Material.CACTUS);
        dangerous.add(Material.FIRE);
        dangerous.add(Material.LAVA);
        dangerous.add(Material.MAGMA_BLOCK);
        dangerous.add(Material.CAMPFIRE);
        dangerous.add(Material.ICE);
        dangerous.add(Material.TRIPWIRE);
        dangerous.add(Material.FROSTED_ICE);
        dangerous.add(Material.DISPENSER);
        dangerous.add(Material.ACACIA_PRESSURE_PLATE);
        dangerous.add(Material.BIRCH_PRESSURE_PLATE);
        dangerous.add(Material.DARK_OAK_PRESSURE_PLATE);
        dangerous.add(Material.OAK_PRESSURE_PLATE);
        dangerous.add(Material.SPRUCE_PRESSURE_PLATE);
        dangerous.add(Material.JUNGLE_PRESSURE_PLATE);
        dangerous.add(Material.STONE_PRESSURE_PLATE);
        dangerous.add(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
        dangerous.add(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
        dangerous.add(Material.VINE);
        dangerous.add(Material.LADDER);
        dangerous.add(Material.NETHERRACK);
        dangerous.add(Material.GLASS_PANE);
        dangerous.add(Material.RED_STAINED_GLASS_PANE);
        dangerous.add(Material.BLACK_STAINED_GLASS_PANE);
        dangerous.add(Material.BLUE_STAINED_GLASS_PANE);
        dangerous.add(Material.BROWN_STAINED_GLASS_PANE);
        dangerous.add(Material.CYAN_STAINED_GLASS_PANE);
        dangerous.add(Material.GRAY_STAINED_GLASS_PANE);
        dangerous.add(Material.GREEN_STAINED_GLASS_PANE);
        dangerous.add(Material.LIME_STAINED_GLASS_PANE);
        dangerous.add(Material.MAGENTA_STAINED_GLASS_PANE);
        dangerous.add(Material.ORANGE_STAINED_GLASS_PANE);
        dangerous.add(Material.PINK_STAINED_GLASS_PANE);
        dangerous.add(Material.PURPLE_STAINED_GLASS_PANE);
        dangerous.add(Material.WHITE_STAINED_GLASS_PANE);
        dangerous.add(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        dangerous.add(Material.YELLOW_STAINED_GLASS_PANE);
        dangerous.add(Material.LIGHT_GRAY_STAINED_GLASS);
        dangerous.add(Material.ACACIA_FENCE);
        dangerous.add(Material.BIRCH_FENCE);
        dangerous.add(Material.DARK_OAK_FENCE);
        dangerous.add(Material.OAK_FENCE);
        dangerous.add(Material.SPRUCE_FENCE);
        dangerous.add(Material.JUNGLE_FENCE);
        dangerous.add(Material.STONE_BRICK_WALL);
        dangerous.add(Material.END_STONE_BRICK_WALL);
        dangerous.add(Material.COBBLESTONE_WALL);
        dangerous.add(Material.ANDESITE_WALL);
        dangerous.add(Material.BRICK_WALL);
        dangerous.add(Material.DIORITE_WALL);
        dangerous.add(Material.GRANITE_WALL);
        dangerous.add(Material.MOSSY_COBBLESTONE_WALL);
        dangerous.add(Material.MOSSY_STONE_BRICK_WALL);
        dangerous.add(Material.NETHER_BRICK_WALL);
        dangerous.add(Material.PRISMARINE_WALL);
        dangerous.add(Material.RED_NETHER_BRICK_WALL);
        dangerous.add(Material.ACACIA_FENCE_GATE);
        dangerous.add(Material.BIRCH_FENCE_GATE);
        dangerous.add(Material.DARK_OAK_FENCE_GATE);
        dangerous.add(Material.OAK_FENCE_GATE);
        dangerous.add(Material.SPRUCE_FENCE_GATE);
        dangerous.add(Material.JUNGLE_FENCE_GATE);

        for (Material danger : dangerous) {
            if (material == danger) {
                return true;
            }
        }
        return false;
    }

}

