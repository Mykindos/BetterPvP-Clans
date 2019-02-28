package net.betterpvp.clans.worldevents.types.nms;

import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityWitherSkull;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.projectiles.ProjectileSource;

public class BossWitherSkull extends EntityWitherSkull {

    public BossWitherSkull(World world, ProjectileSource src) {
        super(world);
        this.projectileSource = src;
        // TODO Auto-generated constructor stub
    }

    public BossWitherSkull(World world, EntityLiving entityliving, double d0, double d1, double d2, ProjectileSource src) {
        super(world, entityliving, d0, d1, d2);
        this.projectileSource = src;
        this.shooter = entityliving;
    }


    public WitherSkull spawn(Location loc) {
        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        getWorld().addEntity(this, SpawnReason.CUSTOM);
        return (WitherSkull) getBukkitEntity();
    }


}
