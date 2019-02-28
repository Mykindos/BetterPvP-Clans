package net.betterpvp.clans.economy.shops.nms;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;


public class ShopVillager extends EntityVillager {


    public ShopVillager(World world) {
        super(world);
        goalSelector.a(0, new PathfinderGoalFloat(this));
        goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 0.6D));
        goalSelector.a(9, new PathfinderGoalInteract(this, EntityHuman.class, 3.0F, 1.0F));
        goalSelector.a(9, new PathfinderGoalInteract(this, EntityVillager.class, 5.0F, 0.02F));
        goalSelector.a(9, new PathfinderGoalRandomStroll(this, 0.6D));
        goalSelector.a(10, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));


    }


    @Override
    public void onLightningStrike(EntityLightning entitylightning) {
        return;
    }

    @Override
    public void die() {
        return;
    }

    @Override
    public void move(double d0, double d1, double d2) {
        return;
    }

    @Override
    public void collide(Entity entity) {
        return;
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        return false;
    }

    @Override
    public void g(double d0, double d1, double d2) {
        return;
    }

    @Override
    protected String z() {
        return "";
    }

    @Override
    protected String bo() {
        return "";
    }

    @Override
    protected String bp() {
        return "";
    }


    public Villager spawn(Location loc) {

        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        getWorld().addEntity(this, SpawnReason.CUSTOM);
        return (Villager) getBukkitEntity();
    }


}
