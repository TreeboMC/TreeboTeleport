package me.shakeforprotein.treeboteleport.Listeners;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


public class RespawnListener implements Listener {

    private TreeboTeleport pl;

    private HashMap<String, ItemStack> docketMap = new HashMap<>();

    public RespawnListener(TreeboTeleport main) {
        this.pl = main;
    }

    @EventHandler
    public boolean onPlayerDeath(PlayerDeathEvent e) {
        if (!e.getDrops().isEmpty()) {
            String key1 = UUID.randomUUID().toString();
            String key2 = UUID.randomUUID().toString();
            String key3 = key1 + "-" + key2;

            //File deathFile = new File(pl.getDataFolder() + File.separator + "deaths", File.separator + e.getEntity().getUniqueId().toString() + "_" + key3 + ".yml");
            File deathFile = new File(pl.getPlayerDataFolder() + File.separator + e.getEntity().getUniqueId().toString() +File.separator + "deaths", File.separator + e.getEntity().getUniqueId().toString() + "_" + key3 + ".yml");
            FileConfiguration deathYaml = YamlConfiguration.loadConfiguration(deathFile);


            Player p = e.getEntity();
            Location loc = e.getEntity().getLocation();
            int droppedXP = p.getTotalExperience();
            String deathMessage = e.getDeathMessage();

            List<ItemStack> itemList = e.getDrops();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());

            ItemStack docket = new ItemStack(Material.PAPER, 1);
            ItemMeta docketMeta = docket.getItemMeta();
            docketMeta.setDisplayName(pl.badge + "Death Docket - " + date);
            deathYaml.set("date", date);

            List<String> docketLore = new ArrayList<String>();
            docketLore.add("Player - " + p.getName());
            deathYaml.set("player", p.getName());
            deathYaml.set("uuid", p.getUniqueId().toString());
            docketLore.add("Location - " + loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ());
            deathYaml.set("world", loc.getWorld().getName());
            deathYaml.set("xyz", loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ());
            docketLore.add("XP - " + droppedXP);
            deathYaml.set("experience", droppedXP);
            docketLore.add("Death Message - " + deathMessage);
            deathYaml.set("deathMessage", deathMessage);
            docketLore.add("SecretKey - " + key3);
            deathYaml.set("secretKey", key3);
            int i = 0;
            for (ItemStack item : itemList) {
                deathYaml.set("inventory.slot_" + i, item);
                i++;
                docketLore.add(item.getAmount() + " X " + item.getType().name());
                if (item.hasItemMeta() && item.getItemMeta().hasEnchants()) {
                    for (Enchantment enchantment : item.getItemMeta().getEnchants().keySet()) {
                        docketLore.add(" - " + enchantment.getKey() + " LvL " + item.getItemMeta().getEnchants().get(enchantment));
                    }
                } else if (item.hasItemMeta() && item.getItemMeta() instanceof PotionMeta) {
                    docketLore.add(" - " + ((PotionMeta) item.getItemMeta()).getBasePotionData().getType() + " Ext:" + ((PotionMeta) item.getItemMeta()).getBasePotionData().isExtended() + " Upg:" + ((PotionMeta) item.getItemMeta()).getBasePotionData().isUpgraded());
                } else if (item.hasItemMeta() && item.getItemMeta().hasAttributeModifiers()) {
                    for (Attribute attrib : item.getItemMeta().getAttributeModifiers().keySet()) {
                        docketLore.add(" - " + attrib.name() + " LvL " + item.getItemMeta().getAttributeModifiers().get(attrib));
                    }
                }
            }
            docketMeta.setLore(docketLore);
            docket.setItemMeta(docketMeta);
            docketMap.put(p.getUniqueId().toString(), docket);
            deathYaml.set("used", "false");
            try {
                deathYaml.save(deathFile);
            } catch (IOException ex) {
                pl.roots.errorLogger.logError(pl, ex);
            }
        }
        return true;
    }

    @EventHandler
    public boolean onRespawnEvent(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
       /* if(pl.getConfig().get("deathDocket.disable") == null || !pl.getConfig().getString("deathDocket.disable").equalsIgnoreCase("true")){
            p.sendMessage("Issuing docket");
            if (docketMap.containsKey(p.getUniqueId().toString())) {
                if (pl.getConfig().get("deathDocket.toggle." + p.getUniqueId()) == null || pl.getConfig().get("deathDocket.toggle." + p.getUniqueId().toString()).equals("false")) {

                    p.getInventory().setItemInOffHand(docketMap.get(p.getUniqueId().toString()));
                    p.sendMessage(pl.badge + "You been issued a death docket. You can disable this feature at any time with /toggledeathdocket, but in doing so, staff will be unable to assist with item recovery");
                } else {
                    p.sendMessage(pl.badge + "You have Death Dockets disabled. Re-enable with /toggledeathdocket");
                }
            }
        }
        */

        if (p.getBedSpawnLocation() != null) {
            //p.sendMessage(pl.badge + "Sending you to your bed");
            p.teleport(p.getBedSpawnLocation());
        } else {
            File spawnsYml = new File(pl.getDataFolder(), File.separator + "spawns.yml");
            if (!spawnsYml.exists()) {
                try {
                    spawnsYml.createNewFile();
                    FileConfiguration spawns = YamlConfiguration.loadConfiguration(spawnsYml);
                    try {
                        spawns.options().copyDefaults();
                        spawns.save(spawnsYml);
                    } catch (FileNotFoundException ex) {
                        pl.roots.errorLogger.logError(pl, ex);
                    }
                } catch (IOException ex) {
                    pl.roots.errorLogger.logError(pl, ex);
                }
            }
            FileConfiguration spawns = YamlConfiguration.loadConfiguration(spawnsYml);
            String world = p.getWorld().getName();

            if (spawns.get("spawns." + world + ".x") != null) {
                String confWorld = spawns.getString("spawns." + world + ".world");
                double x = spawns.getDouble("spawns." + world + ".x");
                double y = spawns.getDouble("spawns." + world + ".y");
                double z = spawns.getDouble("spawns." + world + ".z");
                float pitch = (float) spawns.getDouble("spawns." + world + ".pitch");
                float yaw = (float) spawns.getDouble("spawns." + world + ".yaw");
                Location loc = new Location(Bukkit.getWorld(confWorld), x, y, z, yaw, pitch);
                p.sendMessage(pl.badge + "Returning you to Spawn");
                p.teleport(loc);
            }

        }
        return true;
    }

    /*@EventHandler
    public void onEntitySpawn(EntitySpawnEvent e){
        if(MinecraftServer.getServer().recentTps[0] < 12 && e.getEntity().getType() != EntityType.DROPPED_ITEM && e.getEntity().getType() != EntityType.FALLING_BLOCK){
            //Bukkit.broadcastMessage("Cancelling spawn for entity: " +e.getEntity().getType());
            e.setCancelled(true);
        }
    }*/

}
