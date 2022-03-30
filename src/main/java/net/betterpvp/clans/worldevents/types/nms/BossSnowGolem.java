package net.betterpvp.clans.worldevents.types.nms;

import net.betterpvp.core.utility.UtilMath;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.entity.Snowman;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;


public class BossSnowGolem extends SnowGolem {

    public BossSnowGolem(Level world) {
        super(EntityType.SNOW_GOLEM, world);
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RangedAttackGoal(this, 1.25D, 20, 10.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this,
                Player.class, true));
    }

    @Override
    public void registerGoals(){

    }


    public CraftEntity spawn(Location loc) {
        this.absMoveTo(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        this.level.addFreshEntity(this, SpawnReason.CUSTOM);
        return getBukkitEntity();
    }

    @Override
    public void performRangedAttack(LivingEntity entityliving, float f) {
        if(UtilMath.randomInt(10) < 9) {
            // t = world
            Snowball entitysnowball = new Snowball(this.level, this);
            double d0 = entityliving.getYHeadRot() - 1.100000023841858D;
            double d1 = entityliving.getX() - this.getX();
            double d2 = d0 - entitysnowball.getY();
            double d3 = entityliving.getZ() - this.getZ();
            float f1 = (float) (Math.sqrt(d1 * d1 + d3 * d3) * 0.2F);
            entitysnowball.shoot(d1, d2 + (double) f1, d3, 1.6F, 16.0F);
            this.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level.addFreshEntity(entitysnowball);
        }else{

            for(int i = 0; i < 10; i++){
                Snowball entitysnowball = new Snowball(this.level, this);
                double d0 = entityliving.getYHeadRot() - UtilMath.randDouble(0.5, 1.5);
                double d1 = entityliving.getX() - this.getX();
                double d2 = d0 - entitysnowball.getY();
                double d3 = entityliving.getZ() - this.getZ();
                float f1 = (float) (Math.sqrt(d1 * d1 + d3 * d3) * 0.2F);
                entitysnowball.shoot(d1, d2 + (double) f1, d3, 1.6F, 12.0F);
                this.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                this.level.addFreshEntity(entitysnowball);
            }

        }
    }

    @Override
    public boolean hasPumpkin(){
        return false;
    }


}
