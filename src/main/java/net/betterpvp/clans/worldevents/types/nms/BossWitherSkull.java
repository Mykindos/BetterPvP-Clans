package net.betterpvp.clans.worldevents.types.nms;

import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.projectile.EntityWitherSkull;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.projectiles.ProjectileSource;

public class BossWitherSkull extends EntityWitherSkull {

    public BossWitherSkull(World world, ProjectileSource src) {
        super(EntityTypes.bb, world);
        this.projectileSource = src;

    }

    public BossWitherSkull(World world, EntityLiving entityliving, double d0, double d1, double d2, ProjectileSource src) {
        super(world, entityliving, d0, d1, d2);
        this.projectileSource = src;
        this.setShooter(entityliving);
    }


    public WitherSkull spawn(Location loc) {
        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        getWorld().addEntity(this, SpawnReason.CUSTOM);
        return (WitherSkull) getBukkitEntity();
    }


}
