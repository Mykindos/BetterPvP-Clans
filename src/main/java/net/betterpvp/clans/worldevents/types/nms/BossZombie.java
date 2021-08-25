package net.betterpvp.clans.worldevents.types.nms;

import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class BossZombie extends EntityZombie {

    public BossZombie(World world) {
        super(world);
        this.bP.a(0, new PathfinderGoalFloat(this));
        this.bP.a(2, new PathfinderGoalMeleeAttack(this, 1.0D, false));
        this.bP
                .a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
        this.bP.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
        this.bP.a(8, new PathfinderGoalLookAtPlayer(this,
                EntityHuman.class, 8.0F));
        this.bP.a(8, new PathfinderGoalRandomLookaround(this));
        this.bQ.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this,
                EntityHuman.class, true));
    }


    public Zombie spawnZombie(Location loc) {
        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        getWorld().addEntity(this, SpawnReason.CUSTOM);
        return (Zombie) getBukkitEntity();
    }


}
