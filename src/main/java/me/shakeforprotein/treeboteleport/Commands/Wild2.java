package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
                        minY = (pl.getConfig().getString("wild.minY") != null) ? pl.getConfig().getInt("wild.minY") : -30;
                        maxY = (pl.getConfig().getString("wild.maxY") != null) ? pl.getConfig().getInt("wild.maxY") : 120;
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
        boolean success = false;
        int i = 0;
        while (!success && i < limit) {
            int X = getCoords(minX, maxX);
            int Z = getCoords(minX, maxX);
            World world = targetPlayer.getWorld();
            Block block;
            if (world.getName().toLowerCase().contains("grid")) {
                limit = 10;
                int Y = getCoords(4, 32) * 4;
                X = getMod4Coords(minX, maxX);
                Z = getMod4Coords(minZ, maxZ);
                block = world.getBlockAt(X, Y, Z);
                Block parseBlock = block;

                world.getChunkAt(block).load(true);
                world.getChunkAt(block).setForceLoaded(true);
                Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
                    @Override
                    public void run() {
                        List<Block> nearbyBlocks = getNearbyBlocks(parseBlock);
                        if (nearbyBlocks.size() > 0) {
                            for (Block block : nearbyBlocks)
                                if (blockIsSafe(block)) {
                                    teleportPlayer(targetPlayer, block);
                                    world.getChunkAt(parseBlock).setForceLoaded(false);
                                    break;
                                }
                        } else {
                            targetPlayer.sendMessage(pl.badge + "Failed to find safe block");
                            world.getChunkAt(parseBlock).setForceLoaded(false);
                        }
                    }
                }, 40L);
            } else {
                block = world.getHighestBlockAt(X, Z).getRelative(BlockFace.DOWN);
                if (targetPlayer.getWorld().getName().toLowerCase().contains("nether")) {
                    maxY = 90;
                }
                if (block.getY() > minY && block.getY() < maxY) {
                    for (Material material : getSafeMaterialList()) {
                        if (block.getType() == material) {
                            if (block.getRelative(0, 1, 0).getType() == Material.AIR && block.getRelative(0, 2, 0).getType() == Material.AIR) {
                                targetPlayer.teleport(block.getRelative(BlockFace.UP).getLocation().add(0.5, 0, 0.5));
                                success = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (i % 2 == 0 && world.getName().toLowerCase().contains("grid")) {
                targetPlayer.sendMessage(pl.badge + "Attempt " + i + " of " + limit);
            }
            i++;
        }
    }

    public void doWildRanged(Player targetPlayer, int range) {
        if (range > 999 && range < 25001) {
            boolean success = false;
            int i = 0;
            while (!success && i < limit) {
                int X = getCoords(1, range * 2) - range + targetPlayer.getLocation().getBlockX();
                int Z = getCoords(1, range * 2) - range + targetPlayer.getLocation().getBlockZ();
                World world = targetPlayer.getWorld();
                Block block = targetPlayer.getLocation().getBlock();
                if (world.getName().toLowerCase().contains("grid")) {
                    X = getMod4Coords(minX, maxX);
                    Z = getMod4Coords(minZ, maxZ);
                    int Y = getCoords(4, 32) * 4;
                    block = world.getBlockAt(X, Y, Z);
                } else {
                    block = world.getHighestBlockAt(X, Z).getRelative(BlockFace.DOWN);
                }
                if (targetPlayer.getWorld().getName().toLowerCase().contains("nether")) {
                    maxY = 90;
                }
                if (block.getY() > minY && block.getY() < maxY) {
                    for (Material material : getSafeMaterialList()) {
                        if (block.getType() == material) {
                            if (block.getRelative(0, 1, 0).getType() == Material.AIR && block.getRelative(0, 2, 0).getType() == Material.AIR) {
                                targetPlayer.teleport(block.getRelative(BlockFace.UP).getLocation().add(0.5, 0, 0.5));
                                success = true;
                                break;
                            }
                        }
                    }
                }
                if (i % 2 == 0) {
                    targetPlayer.sendMessage(pl.badge + "Attempt " + i + " of " + limit);
                }
                i++;
            }
        } else if (range > 1000) {
            targetPlayer.sendMessage(pl.badge + pl.err + "For performance reasons, maximum range is capped at 25000.");
        } else if (range < 1000) {
            targetPlayer.sendMessage(pl.badge + pl.err + "For player security reasons, minimum range is capped at 1000.");
        }
    }

    public void doStaffWild(Player sendingPlayer, String targetPlayer) {
        if (sendingPlayer.hasPermission("tbteleport.staff.wild.other")) {

            boolean success = false;
            int i = 0;
            while (!success && i < limit) {
                int X = getCoords(1, ((int) maxX - (int) minX)) + (int) minX;
                int Z = getCoords(1, ((int) maxZ - (int) minZ)) + (int) minZ;
                World world = Bukkit.getPlayer(targetPlayer).getWorld();

                Block block = Bukkit.getPlayer(targetPlayer).getLocation().getBlock();
                if (world.getName().toLowerCase().contains("grid")) {
                    limit = 1;
                    int Y = getCoords(4, 32) * 4;
                    X = getMod4Coords(minX, maxX);
                    Z = getMod4Coords(minZ, maxZ);
                    block = world.getBlockAt(X, Y, Z);
                    Block parseBlock = block;

                    world.getChunkAt(block).load(true);
                    world.getChunkAt(block).setForceLoaded(true);
                    Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
                        @Override
                        public void run() {
                            List<Block> nearbyBlocks = getNearbyBlocks(parseBlock);
                            if (nearbyBlocks.size() > 0) {
                                for (Block block : nearbyBlocks)
                                    if (blockIsSafe(block)) {
                                        teleportPlayer(Bukkit.getPlayer(targetPlayer), block);
                                        world.getChunkAt(parseBlock).setForceLoaded(false);
                                        break;
                                    }
                            } else {
                                Bukkit.getPlayer(targetPlayer).sendMessage(pl.badge + "Failed to find safe block");
                                world.getChunkAt(parseBlock).setForceLoaded(false);
                            }
                        }
                    }, 40L);
                } else {
                    block = world.getHighestBlockAt(X, Z).getRelative(BlockFace.DOWN);

                    if (Bukkit.getPlayer(targetPlayer).getWorld().getName().toLowerCase().contains("nether")) {
                        maxY = 90;
                    }
                    if (block.getY() > minY && block.getY() < maxY) {
                        for (Material material : getSafeMaterialList()) {
                            if (block.getType() == material) {
                                if (block.getRelative(0, 1, 0).getType() == Material.AIR && block.getRelative(0, 2, 0).getType() == Material.AIR) {
                                    Bukkit.getPlayer(targetPlayer).teleport(block.getRelative(BlockFace.UP).getLocation().add(0.5, 0, 0.5));
                                    success = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (i % 2 == 0) {
                    sendingPlayer.sendMessage(pl.badge + "Attempt " + i + " of " + limit);
                }
                i++;
            }
        }
    }

    public void doStaffWildRanged(Player sendingPlayer, String tPlayer, int range) {
        if (sendingPlayer.hasPermission("tbteleport.staff.wild.other")) {
            if (range < 1000000) {
                Player targetPlayer = Bukkit.getPlayer(tPlayer);
                boolean success = false;
                int i = 0;
                while (!success && i < limit) {
                    int X = getCoords(1, range * 2) - range + targetPlayer.getLocation().getBlockX();
                    int Z = getCoords(1, range * 2) - range + targetPlayer.getLocation().getBlockZ();
                    World world = targetPlayer.getWorld();
                    Block block = Bukkit.getPlayer(tPlayer).getLocation().getBlock();

                    if (world.getName().toLowerCase().contains("grid")) {
                        limit = 1;
                        int Y = getCoords(4, 32) * 4;
                        X = getMod4Coords(minX, maxX);
                        Z = getMod4Coords(minZ, maxZ);
                        block = world.getBlockAt(X, Y, Z);
                        Block parseBlock = block;

                        world.getChunkAt(block).load(true);
                        world.getChunkAt(block).setForceLoaded(true);
                        Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
                            @Override
                            public void run() {
                                List<Block> nearbyBlocks = getNearbyBlocks(parseBlock);
                                if (nearbyBlocks.size() > 0) {
                                    for (Block block : nearbyBlocks)
                                        if (blockIsSafe(block)) {
                                            teleportPlayer(targetPlayer, block);
                                            world.getChunkAt(parseBlock).setForceLoaded(false);
                                            break;
                                        }
                                } else {
                                    targetPlayer.sendMessage(pl.badge + "Failed to find safe block");
                                    world.getChunkAt(parseBlock).setForceLoaded(false);
                                }
                            }
                        }, 40L);
                    } else {
                        block = world.getHighestBlockAt(X, Z).getRelative(BlockFace.DOWN);


                        if (targetPlayer.getWorld().getName().toLowerCase().contains("nether")) {
                            maxY = 90;
                        }
                        if (block.getY() > minY && block.getY() < maxY) {
                            for (Material material : getSafeMaterialList()) {
                                if (block.getType() == material) {
                                    if (block.getRelative(0, 1, 0).getType() == Material.AIR && block.getRelative(0, 2, 0).getType() == Material.AIR) {
                                        targetPlayer.teleport(block.getRelative(BlockFace.UP).getLocation().add(0.5, 0, 0.5));
                                        success = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (i % 2 == 0) {
                        sendingPlayer.sendMessage(pl.badge + "Attempt " + i + " of " + limit);
                    }
                    i++;
                }
            }
        } else {
            sendingPlayer.sendMessage(pl.badge + pl.err + "Be reasonable, more than a million blocks?");
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


    public List<Block> getNearbyBlocks(Block block) {
        List<Block> list = new ArrayList<>();

        int X = block.getX();
        int Y = block.getY();
        int Z = block.getZ();
        int i, j, k = 0;
        for (Material material : getSafeMaterialList()) {
            for (i = 0; i < 10; i++) {
                for (j = 0; j < 6; j++) {
                    for (k = 0; k < 10; k++) {
                        if (block.getWorld().getBlockAt(X + i - 5, Y + j, Z + k - 5).getType().name() == material.name()) {
                            list.add(block.getWorld().getBlockAt(X, Y, Z));
                        }
                    }
                }
            }
        }
        return list;
    }

    public Boolean blockIsSafe(Block block) {
        if (block.getRelative(0, 1, 0).getType() == Material.AIR && block.getRelative(0, 2, 0).getType() == Material.AIR) {
            System.out.println("Block not safe");
            return true;
        }
        return false;
    }

    public void teleportPlayer(Player player, Block block) {
        player.teleport(block.getLocation().add(0.5, 1, 0.5));
    }

    private List<Material> getSafeMaterialList() {
        List<Material> sML = new ArrayList<Material>();
        sML.clear();
        sML.add(Material.GRASS_BLOCK);
        sML.add(Material.GRASS_PATH);
        sML.add(Material.DIRT);
        sML.add(Material.COARSE_DIRT);
        sML.add(Material.SAND);
        sML.add(Material.SANDSTONE);
        sML.add(Material.RED_SANDSTONE);
        sML.add(Material.RED_SAND);
        sML.add(Material.END_STONE);
        sML.add(Material.END_STONE_BRICKS);
        sML.add(Material.NETHERRACK);
        sML.add(Material.NETHER_BRICK);
        sML.add(Material.STONE);
        sML.add(Material.COBBLESTONE);
        sML.add(Material.DIORITE);
        sML.add(Material.ANDESITE);
        sML.add(Material.GRANITE);
        sML.add(Material.SOUL_SAND);
        return sML;
    }

}

