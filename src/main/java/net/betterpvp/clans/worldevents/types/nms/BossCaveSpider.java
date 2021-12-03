package net.betterpvp.clans.worldevents.types.nms;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.monster.EntityCaveSpider;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.entity.CaveSpider;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class BossCaveSpider extends EntityCaveSpider {

    public BossCaveSpider(World paramWorld) {
        super(EntityTypes.k, paramWorld);
        this.bP.a(1, new PathfinderGoalFloat(this));
        this.bP.a(3, new PathfinderGoalLeapAtTarget(this, 0.4F));
        this.bP.a(4, new PathfinderGoalMeleeAttack(this, 1.0D, false));
        this.bP.a(5, new PathfinderGoalRandomStroll(this, 0.8D));
        this.bP.a(6, new PathfinderGoalLookAtPlayer(this,
                EntityHuman.class, 8.0F));
        this.bP.a(6, new PathfinderGoalRandomLookaround(this));
        this.bQ.a(1, new PathfinderGoalHurtByTarget(this, new Class[0]));
        this.bQ.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this, EntityHuman.class, true));
    }


    public CaveSpider spawnSpider(Location loc) {
        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        getWorld().addEntity(this, SpawnReason.CUSTOM);
        return (CaveSpider) getBukkitEntity();
    }
}
