package net.betterpvp.clans.classes;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class DamageData {
	
	private String uuid;
	private long timeOfDamage;
	private long damageDelay;
	private DamageCause cause;
	
	public DamageData(String uuid, DamageCause cause, long b){
		this.uuid = uuid;
		this.timeOfDamage = System.currentTimeMillis();
		this.damageDelay = b;
		this.cause = cause;
	}
	
	public long getTimeOfDamage(){
		return timeOfDamage;
	}
	
	public long getDamageDelay(){
		return damageDelay;
		
	}
	
	public DamageCause getCause(){
		return cause;
	}
	
	public String getUUID(){
		return uuid;
	}

}
