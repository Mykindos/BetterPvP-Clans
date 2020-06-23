package net.betterpvp.clans.economy.shops.nms;

import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import javax.annotation.OverridingMethodsMustInvokeSuper;


public class ShopVillager extends EntityVillager {


    public ShopVillager(World world) {
        super(EntityTypes.VILLAGER, world);

       goalSelector.a(10, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 16.0F));


    }


    public void setMot(Vec3D vec3d){return;}

    @Override
    public void onLightningStrike(EntityLightning entitylightning) {
        return;
    }


    @Override
   protected void initPathfinder() {

        return;
   }

   // @Override
 //   public boolean a(BlockPosition blockposition){return false;}

   // @Override
   // public void movementTick(){return;}


    @Override
    public void a(NBTTagCompound nbttagcompound){return;};

    @Override
    public void setVillagerData(VillagerData villagerdata) {return;}

    @Override
    public void collide(Entity entity) {
        return;
    }

    @Override
    public void h(double d0, double d1, double d2) { return;}


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
    public void a(Entity entity, float f, double d0, double d1){return;}

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


    public Villager spawn(Location loc) {

        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        getWorld().addEntity(this, SpawnReason.CUSTOM);
        return (Villager) getBukkitEntity();
    }


}
