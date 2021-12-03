package net.betterpvp.clans.worldevents.types.nms;

import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.boss.wither.EntityWither;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import org.bukkit.entity.Wither;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class BossWither extends EntityWither {

    public BossWither(World world) {
        super(EntityTypes.aZ, world);

        this.bQ.a(1, new PathfinderGoalHurtByTarget(this,
                new Class[0]));
        this.bQ.a(2, new PathfinderGoalNearestAttackableTarget<>(this,
                EntityHuman.class, true));

        this.bP.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));

    }


    @Override
    public void E() {

    }

    @Override
    protected void initPathfinder(){

    }


    @Override
    public void a(EntityLiving entityliving, float f) {

    }

    public void setMot(Vec3D vec3d){return;}


    @Override
    public void i(double d0, double d1, double d2) { return;}

    @Override
    public void p(double d0, double d1, double d2){return;}


    public Wither spawn(Location loc) {
        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        getWorld().addEntity(this, SpawnReason.CUSTOM);
        return (Wither) getBukkitEntity();
    }

}
