package net.betterpvp.clans.economy.shops.nms;

import net.minecraft.server.level.WorldServer;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLightning;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;


public class ShopVillager extends EntityVillager {


    public ShopVillager(World world) {
        super(EntityTypes.aV, world);

       bP.a(10, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 16.0F));


    }


    public void setMot(Vec3D vec3d){return;}

    @Override
    public void onLightningStrike(WorldServer worldserver, EntityLightning entitylightning) {
        return;
    }


    @Override
   protected void initPathfinder() {

        return;
   }

    @Override
    public void setVillagerData(VillagerData villagerdata) {return;}

    @Override
    public void collide(Entity entity) {
        return;
    }

    @Override
    public void i(double d0, double d1, double d2) { return;}

    @Override
    public void p(double d0, double d1, double d2){return;}


    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        return false;
    }


    @Override
    public boolean a(double d0, double d1, double d2, boolean flag) {
        return false;
    }

   // @Override
   // public void a(Entity entity, float f, double d0, double d1){return;}

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
