package net.betterpvp.clans.economy.shops.nms;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.InteractGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;

import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import javax.annotation.Nullable;

public class ShopSkeleton extends Skeleton {


    public ShopSkeleton(Level world) {
        super(EntityType.SKELETON, world);
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 0.0D));
        goalSelector.addGoal(9, new InteractGoal(this, Player.class, 3.0F, 1.0F));
        goalSelector.addGoal(9, new InteractGoal(this, Villager.class, 5.0F, 0.02F));

        goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    @Override
    protected void registerGoals() {

    }

    @Override
    public void push(Entity entity) {
        return;
    }

    @Override
    public boolean isPreventingPlayerRest(Player var0) {
        return false;
    }

    @Override
    public boolean hurt(DamageSource damagesource, float f) {
        return false;
    }


    @Override
    public void knockback(double d0, double d1, double d2) { return;}

    @Override
    public void setDeltaMovement(double d0, double d1, double d2){return;}

    @Override
    public boolean randomTeleport(double d0, double d1, double d2, boolean flag) {
        return false;
    }

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

    @Override
    public boolean isPushable() {
        return false;
    }

    //Combustion
    @Override
    public boolean doHurtTarget(Entity entity) {
        return false;
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultydamagescaler){return;}

    @Override
    public void setSecondsOnFire(int i, boolean callEvent) {
        return;
    }


    @Override
    protected boolean damageEntity0(final DamageSource damagesource, float f) {
        return false;
    }

    public CraftEntity spawn(Location loc) {
        this.absMoveTo(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        this.level.addFreshEntity(this, SpawnReason.CUSTOM);
        return getBukkitEntity();
    }


}
