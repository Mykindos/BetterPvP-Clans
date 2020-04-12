package net.betterpvp.clans.worldevents.types.nms;

import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftWitherSkeleton;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class BossSkeleton extends EntitySkeletonWither {

    public BossSkeleton(World world) {
        super(EntityTypes.WITHER_SKELETON, world);
        this.goalSelector.a(1, new PathfinderGoalFloat(this));

        this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this,
                EntityHuman.class, 8.0F));
        this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this,
                new Class[0]));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this,
                EntityHuman.class, true));


    }


    public WitherSkeleton spawn(Location loc) {
        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        getWorld().addEntity(this, SpawnReason.CUSTOM);
        return (WitherSkeleton) getBukkitEntity();
    }

}
