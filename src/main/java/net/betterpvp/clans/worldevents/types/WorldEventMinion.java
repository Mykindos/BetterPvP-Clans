package net.betterpvp.clans.worldevents.types;

import org.bukkit.entity.LivingEntity;

public abstract class WorldEventMinion {
	
	private LivingEntity ent;

	
	public WorldEventMinion(LivingEntity ent){
		this.ent = ent;
		ent.setMaxHealth(getMaxHealth());
		ent.setHealth(getMaxHealth());
		ent.setCustomName(getDisplayName());
		ent.setCustomNameVisible(true);
		ent.setRemoveWhenFarAway(false);
	}
	
	public LivingEntity getEntity(){
		return ent;
	}
	
	
	public abstract String getDisplayName();
	public abstract double getMaxHealth();
}
