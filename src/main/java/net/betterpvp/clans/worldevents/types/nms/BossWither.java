package net.betterpvp.clans.worldevents.types.nms;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.entity.Wither;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class BossWither extends EntityWither{

	public BossWither(World world) {
		super(world);
	
		this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false,
				new Class[0]));
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this,
				EntityHuman.class, true));

	
	}

	
	@Override
	public void E() {
		
	}
	

	
	@Override
	public void a(EntityLiving entityliving, float f) {
		
	}
	
	


	
	
	public  Wither spawn(Location loc){
		setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		getWorld().addEntity(this, SpawnReason.CUSTOM);
		return (Wither) getBukkitEntity();
	}

}
