package net.betterpvp.clans.economy.shops.nms;

import net.minecraft.sounds.SoundEffect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.ai.goal.PathfinderGoalInteract;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class ShopZombie extends EntityZombie {


    public ShopZombie(World world) {
        super(world);
        this.bP.a(0, new PathfinderGoalFloat(this));
        this.bP.a(5, new PathfinderGoalMoveTowardsRestriction(this, 0.6D));
        this.bP.a(9, new PathfinderGoalInteract(this, EntityHuman.class, 3.0F, 1.0F));
        this.bP.a(9, new PathfinderGoalInteract(this, EntityVillager.class, 5.0F, 0.02F));
        this.bP.a(10, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
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
    public boolean f(EntityHuman var0) {
        return false;
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        return false;
    }

    @Override
    public void i(double d0, double d1, double d2) { return;}

    @Override
    public void p(double d0, double d1, double d2){return;}

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


    public Zombie spawn(Location loc) {
        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        getWorld().addEntity(this, SpawnReason.CUSTOM);
        return (Zombie) getBukkitEntity();
    }


}
