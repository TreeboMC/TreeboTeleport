package me.shakeforprotein.treeboteleport.Listeners;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;

public class KillZombies implements Listener {

    private TreeboTeleport pl;

    public KillZombies(TreeboTeleport main) {
        this.pl = main;
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent e) {
        if (pl.getConfig().getBoolean("KillZombies")) {
            if ((e.getEntity() instanceof Zombie || e.getEntity() instanceof Drowned || e.getEntity() instanceof ZombieVillager) && !(e.getEntity() instanceof PigZombie)) {
                if (e.getEntity() instanceof Drowned) {
                    ((Drowned) e.getEntity()).setAI(false);
                } else if (e.getEntity() instanceof ZombieVillager) {
                    ((ZombieVillager) e.getEntity()).setAI(false);
                } else if (e.getEntity() instanceof Zombie) {
                    ((Zombie) e.getEntity()).setAI(false);
                }
            }
        }
        if (pl.getConfig().getBoolean("ReplacePhantomsWithPissedOffWolves")) {
            if (e.getEntity() instanceof Phantom) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {
        for (Entity ent : e.getChunk().getEntities()) {
            if ((ent instanceof Zombie) || (ent instanceof Spider) || (ent instanceof Bat) || (ent instanceof Skeleton) || (ent instanceof Enderman) || (ent instanceof Illager) || ((ent instanceof Fish) && !(ent instanceof PufferFish)) || (ent instanceof Silverfish) || (ent instanceof Slime) || (ent instanceof Blaze) || (ent instanceof Guardian) || (ent instanceof Ghast) || (ent instanceof Vex) || (ent instanceof Phantom) || (ent instanceof SkeletonHorse) || (ent instanceof Rabbit) || (ent instanceof Squid)){
                ent.remove();
            }
        }
    }
}
