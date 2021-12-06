package net.betterpvp.clans.worldevents.types.nms;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.projectiles.ProjectileSource;

public class BossWitherSkull extends WitherSkull {

    public BossWitherSkull(Level world, ProjectileSource src) {
        super(EntityType.WITHER_SKULL, world);
        this.projectileSource = src;

    }

    public BossWitherSkull(Level world, LivingEntity entityliving, double d0, double d1, double d2, ProjectileSource src) {
        super(world, entityliving, d0, d1, d2);
        this.projectileSource = src;
        this.setOwner(entityliving);
    }


    public CraftEntity spawn(Location loc) {
        this.absMoveTo(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        this.level.addFreshEntity(this, SpawnReason.CUSTOM);
        return getBukkitEntity();
    }


}
