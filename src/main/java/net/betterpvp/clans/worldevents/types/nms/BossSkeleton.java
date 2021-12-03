package net.betterpvp.clans.worldevents.types.nms;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomLookaround;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomStroll;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.monster.EntitySkeletonWither;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class BossSkeleton extends EntitySkeletonWither {

    public BossSkeleton(World world) {
        super(EntityTypes.ba, world);
        this.bP.a(1, new PathfinderGoalFloat(this));

        this.bP.a(5, new PathfinderGoalRandomStroll(this, 1.0D));
        this.bP.a(6, new PathfinderGoalLookAtPlayer(this,
                EntityHuman.class, 8.0F));
        this.bP.a(6, new PathfinderGoalRandomLookaround(this));
        this.bQ.a(1, new PathfinderGoalHurtByTarget(this,
                new Class[0]));
        this.bQ.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this,
                EntityHuman.class, true));


    }


    public WitherSkeleton spawn(Location loc) {
        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        getWorld().addEntity(this, SpawnReason.CUSTOM);
        return (WitherSkeleton) getBukkitEntity();
    }

}
