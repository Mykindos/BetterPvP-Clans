package net.betterpvp.clans.worldevents.types.nms;

import net.betterpvp.core.utility.UtilMath;
import net.minecraft.server.v1_16_R1.*;
import org.bukkit.Location;
import org.bukkit.entity.PolarBear;
import org.bukkit.entity.Snowman;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;


public class BossSnowGolem extends EntitySnowman {

    public BossSnowGolem(World world) {
        super(EntityTypes.SNOW_GOLEM, world);
        this.goalSelector.a(1, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalArrowAttack(this, 1.25D, 20, 10.0F));
        this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, new Class[0]));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this,
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
            EntitySnowball entitysnowball = new EntitySnowball(this.world, this);
            double d0 = entityliving.getHeadY() - 1.100000023841858D;
            double d1 = entityliving.locX() - this.locX();
            double d2 = d0 - entitysnowball.locY();
            double d3 = entityliving.locZ() - this.locZ();
            float f1 = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
            entitysnowball.shoot(d1, d2 + (double) f1, d3, 1.6F, 16.0F);
            this.playSound(SoundEffects.ENTITY_SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.world.addEntity(entitysnowball);
        }else{

            for(int i = 0; i < 10; i++){
                EntitySnowball entitysnowball = new EntitySnowball(this.world, this);
                double d0 = entityliving.getHeadY() - UtilMath.randDouble(0.5, 1.5);
                double d1 = entityliving.locX() - this.locX();
                double d2 = d0 - entitysnowball.locY();
                double d3 = entityliving.locZ() - this.locZ();
                float f1 = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
                entitysnowball.shoot(d1, d2 + (double) f1, d3, 1.6F, 12.0F);
                this.playSound(SoundEffects.ENTITY_SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                this.world.addEntity(entitysnowball);
            }

        }
    }

    @Override
    public boolean hasPumpkin(){
        return false;
    }


}
