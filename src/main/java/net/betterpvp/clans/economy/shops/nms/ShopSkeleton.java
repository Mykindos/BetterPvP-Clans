package net.betterpvp.clans.economy.shops.nms;

import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Location;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import javax.annotation.Nullable;

public class ShopSkeleton extends EntitySkeleton {


    public ShopSkeleton(World world) {
        super(EntityTypes.SKELETON, world);
        goalSelector.a(0, new PathfinderGoalFloat(this));
        goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 0.0D));
        goalSelector.a(9, new PathfinderGoalInteract(this, EntityHuman.class, 3.0F, 1.0F));
        goalSelector.a(9, new PathfinderGoalInteract(this, EntityVillager.class, 5.0F, 0.02F));

        goalSelector.a(10, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
    }

    @Override
    protected void initPathfinder() {

        return;
    }

    @Override
    public void collide(Entity entity) {
        return;
    }

    @Override
    public boolean e(EntityHuman var0) {
        return false;
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        return false;
    }

    @Override
    public void f(double d0, double d1, double d2){return;}

    @Override
    public boolean a(double d0, double d1, double d2, boolean flag) {
        return false;
    }



    @Override
    protected SoundEffect getSoundAmbient() {
        return null;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return null;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return null;
    }

    @Override
    public void h(double d0, double d1, double d2) { return;}

    //Combustion
    @Override
    public boolean B(Entity entity) {
        return false;
    }

    @Override
    protected void b(DifficultyDamageScaler difficultydamagescaler){return;}

    @Override
    public void setOnFire(int i, boolean callEvent) {
        return;
    }

    @Override
    public double A(@Nullable Entity entity){
        return 0;
    }

    @Override
    protected boolean damageEntity0(final DamageSource damagesource, float f) {
        return false;
    }

    public Skeleton spawn(Location loc) {

        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        getWorld().addEntity(this, SpawnReason.CUSTOM);
        return (Skeleton) getBukkitEntity();
    }


}
