package net.betterpvp.clans.economy.shops.nms;


import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;

import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.player.Player;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;

import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;


public class ShopVillager extends Villager {


    public ShopVillager(Level world) {
        super(EntityType.VILLAGER, world);

       goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 16.0F));


    }

    @Override
    public void setDeltaMovement(Vec3 vec3d){return;}

    @Override
    public void thunderHit(ServerLevel worldserver, LightningBolt entitylightning) {
        return;
    }


    @Override
    protected void registerGoals(){

    }

    @Override
    public void setVillagerData(VillagerData villagerdata) {return;}

    @Override
    public void push(Entity entity) {
        return;
    }

    @Override
    public void knockback(double d0, double d1, double d2) { return;}

    @Override
    public void setDeltaMovement(double d0, double d1, double d2){return;}


    @Override
    public boolean hurt(DamageSource damagesource, float f) {
        return false;
    }


    @Override
    public boolean randomTeleport(double d0, double d1, double d2, boolean flag) {
        return false;
    }

   // @Override
   // public void a(Entity entity, float f, double d0, double d1){return;}

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }


    public CraftEntity spawn(Location loc) {
        this.absMoveTo(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        this.level.addFreshEntity(this, SpawnReason.CUSTOM);
        return getBukkitEntity();
    }


}
