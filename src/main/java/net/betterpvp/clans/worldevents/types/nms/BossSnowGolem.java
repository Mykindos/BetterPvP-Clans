package net.betterpvp.clans.worldevents.types.nms;

import net.betterpvp.core.utility.UtilMath;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalArrowAttack;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomLookaround;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.animal.EntitySnowman;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.projectile.EntitySnowball;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.entity.Snowman;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;


public class BossSnowGolem extends EntitySnowman {

    public BossSnowGolem(World world) {
        super(EntityTypes.aF, world);
        this.bP.a(1, new PathfinderGoalFloat(this));
        this.bP.a(1, new PathfinderGoalArrowAttack(this, 1.25D, 20, 10.0F));
        this.bP.a(6, new PathfinderGoalRandomLookaround(this));
        this.bQ.a(1, new PathfinderGoalHurtByTarget(this, new Class[0]));
        this.bQ.a(2, new PathfinderGoalNearestAttackableTarget<>(this,
                EntityHuman.class, true));
    }

    @Override
    public void initPathfinder(){

    }


    public Snowman spawnGolem(Location loc) {
        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        getWorld().addEntity(this, SpawnReason.CUSTOM);
        return (Snowman) getBukkitEntity();
    }

    @Override
    public void a(EntityLiving entityliving, float f) {
        if(UtilMath.randomInt(10) < 9) {
            // t = world
            EntitySnowball entitysnowball = new EntitySnowball(this.t, this);
            double d0 = entityliving.getHeadY() - 1.100000023841858D;
            double d1 = entityliving.locX() - this.locX();
            double d2 = d0 - entitysnowball.locY();
            double d3 = entityliving.locZ() - this.locZ();
            float f1 = (float) (Math.sqrt(d1 * d1 + d3 * d3) * 0.2F);
            entitysnowball.shoot(d1, d2 + (double) f1, d3, 1.6F, 16.0F);
            this.playSound(SoundEffects.sa, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.t.addEntity(entitysnowball);
        }else{

            for(int i = 0; i < 10; i++){
                EntitySnowball entitysnowball = new EntitySnowball(this.t, this);
                double d0 = entityliving.getHeadY() - UtilMath.randDouble(0.5, 1.5);
                double d1 = entityliving.locX() - this.locX();
                double d2 = d0 - entitysnowball.locY();
                double d3 = entityliving.locZ() - this.locZ();
                float f1 = (float) (Math.sqrt(d1 * d1 + d3 * d3) * 0.2F);
                entitysnowball.shoot(d1, d2 + (double) f1, d3, 1.6F, 12.0F);
                this.playSound(SoundEffects.sa, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                this.t.addEntity(entitysnowball);
            }

        }
    }

    @Override
    public boolean hasPumpkin(){
        return false;
    }


}
