package net.betterpvp.clans.skills.selector.skills.data;

import java.util.UUID;

public class SilencingStrikesData {
	
	private UUID player;
	private UUID entity;
	private int count;
	private long lastHit;
	
	public SilencingStrikesData(UUID player, UUID entity, int count){
		this.player = player;
		this.entity = entity;
		this.count = count;
		this.lastHit = System.currentTimeMillis();
	}
	
	public UUID getPlayer(){
		return player;
	}
	
	public UUID getEntity(){
		return entity;
	}
	
	public int getCount(){
		return count;
	}
	
	public long getLastHit(){
		return lastHit;
	}
	
	public void setLastHit(long time){
		this.lastHit = time;
	}
	
	public void addCount(){
		count++;
	}
	
	

}
