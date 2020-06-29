package net.betterpvp.clans.worldevents.types.nms;

import net.minecraft.server.v1_16_R1.*;
import org.bukkit.Location;
import org.bukkit.entity.Wither;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class BossWither extends EntityWither {

    public BossWither(World world) {
        super(EntityTypes.WITHER, world);

        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this,
                new Class[0]));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this,
                EntityHuman.class, true));

        this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));

    }


    @Override
    public void E() {

    }

    @Override
    protected void initPathfinder(){

    }


    @Override
    public void a(EntityLiving entityliving, float f) {

    }

    public void setMot(Vec3D vec3d){return;}


    @Override
    public void f(double d0, double d1, double d2){return;}


    @Override
    public void h(double d0, double d1, double d2) { return;}


    public Wither spawn(Location loc) {
        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        getWorld().addEntity(this, SpawnReason.CUSTOM);
        return (Wither) getBukkitEntity();
    }

}
