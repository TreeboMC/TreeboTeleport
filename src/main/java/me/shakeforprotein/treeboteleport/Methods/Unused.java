package me.shakeforprotein.treeboteleport.Methods;

import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class Unused {

    public List<Block> getNearbyBlocks(Block block) {
        List<Block> list = new ArrayList<>();

        int X = block.getX();
        int Y = block.getY();
        int Z = block.getZ();
        int i, j, k = 0;

        for (i = 0; i < 10; i++) {
            for (j = 0; j < 6; j++) {
                for (k = 0; k < 10; k++) {
                    list.add(block.getWorld().getBlockAt(X, Y, Z));
                }
            }
        }
        return list;
    }
}
