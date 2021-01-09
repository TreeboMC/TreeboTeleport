package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class Top {

    private TreeboTeleport pl;

    public Top(TreeboTeleport main) {
        this.pl = main;
    }

    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Reloads the TreeboTeleport Config from file");
                    this.setUsage("/top - requires tbteleport.vipplus.top");
                    this.setPermission("tbteleport.vipplus.top");
                    if (sender.hasPermission(this.getPermission())) {

                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            Location bLoc = p.getLocation().getBlock().getLocation();
                            int i = 256;
                            if(p.getWorld().getEnvironment() == World.Environment.NETHER){
                                i = 127;
                            }
                            for (i = i; i > 0; i--) {
                                Block block = bLoc.getWorld().getBlockAt((int) bLoc.getX(), i, (int) bLoc.getZ());
                                Block buu = bLoc.getWorld().getBlockAt((int) bLoc.getX(), i + 2, (int) bLoc.getZ());
                                Block buu2 = bLoc.getWorld().getBlockAt((int) bLoc.getX(), i + 1, (int) bLoc.getZ());
                                Location tempLoc = p.getLocation();
                                if ((!(block.getType() == Material.AIR)
                                        && !(block.getType() == Material.LAVA)
                                        && !(block.getType() == Material.CACTUS)
                                        && !(block.getType() == Material.NETHER_PORTAL)
                                        && !(block.getType() == Material.END_GATEWAY)
                                        && !(block.getType() == Material.END_GATEWAY)
                                ) && buu.getType() == Material.AIR && buu2.getType() == Material.AIR
                                ) {
                                    tempLoc = buu.getLocation();
                                    p.teleport(tempLoc);
                                    break;
                                }
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
}
