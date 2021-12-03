package net.betterpvp.clans.worldevents.types.nms;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomLookaround;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.animal.EntityPolarBear;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.entity.PolarBear;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;


public class BossPolarBear extends EntityPolarBear {

    public BossPolarBear(World world) {
        super(EntityTypes.ar, world);
        this.bP.a(1, new PathfinderGoalFloat(this));

        this.bP.a(6, new PathfinderGoalRandomLookaround(this));
        this.bQ.a(1, new PathfinderGoalHurtByTarget(this, new Class[0]));
        this.bQ.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this,
                EntityHuman.class, true));
    }


    public PolarBear spawnPolarBear(Location loc) {
        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        getWorld().addEntity(this, SpawnReason.CUSTOM);
        return (PolarBear) getBukkitEntity();
    }


}
