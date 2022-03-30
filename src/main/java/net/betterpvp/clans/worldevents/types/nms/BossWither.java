package net.betterpvp.clans.worldevents.types.nms;

import net.minecraft.world.entity.EntityType;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.entity.Wither;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class BossWither extends WitherBoss {

    public BossWither(Level world) {
        super(EntityType.WITHER, world);

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this,
                new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this,
                Player.class, true));

        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));

    }


    @Override
    public void processPortalCooldown() {

    }

    @Override
    protected void registerGoals(){

    }


    @Override
    public void performRangedAttack(LivingEntity entityliving, float f) {

    }

    @Override
    public void setDeltaMovement(Vec3 vec3d){return;}


    @Override
    public void setDeltaMovement(double d0, double d1, double d2) { return;}

    @Override
    public void knockback(double d0, double d1, double d2){return;}


    public CraftEntity spawn(Location loc) {
        this.absMoveTo(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        this.level.addFreshEntity(this, SpawnReason.CUSTOM);
        return getBukkitEntity();
    }

}
