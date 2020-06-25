package net.betterpvp.clans.worldevents.types.nms;

import net.minecraft.server.v1_16_R1.*;
import org.bukkit.Location;
import org.bukkit.entity.CaveSpider;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class BossCaveSpider extends EntityCaveSpider {

    public BossCaveSpider(World paramWorld) {
        super(EntityTypes.CAVE_SPIDER, paramWorld);
        this.goalSelector.a(1, new PathfinderGoalFloat(this));
        this.goalSelector.a(3, new PathfinderGoalLeapAtTarget(this, 0.4F));
        this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, 1.0D, false));
        this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 0.8D));
        this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this,
                EntityHuman.class, 8.0F));
        this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, new Class[0]));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this, EntityHuman.class, true));
    }


    public CaveSpider spawnSpider(Location loc) {
        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        getWorld().addEntity(this, SpawnReason.CUSTOM);
        return (CaveSpider) getBukkitEntity();
    }
}
