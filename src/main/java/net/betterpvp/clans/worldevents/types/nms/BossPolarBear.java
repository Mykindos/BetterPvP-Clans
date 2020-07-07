package net.betterpvp.clans.worldevents.types.nms;

import net.minecraft.server.v1_16_R1.*;
import org.bukkit.Location;
import org.bukkit.entity.PolarBear;
import org.bukkit.entity.Spider;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;


public class BossPolarBear extends EntityPolarBear {

    public BossPolarBear(World world) {
        super(EntityTypes.POLAR_BEAR, world);
        this.goalSelector.a(1, new PathfinderGoalFloat(this));

        this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, new Class[0]));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this,
                EntityHuman.class, true));
    }


    public PolarBear spawnPolarBear(Location loc) {
        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        getWorld().addEntity(this, SpawnReason.CUSTOM);
        return (PolarBear) getBukkitEntity();
    }


}
