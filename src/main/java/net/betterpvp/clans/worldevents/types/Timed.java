package net.betterpvp.clans.worldevents.types;

import net.battleau.clans.Clans;
import net.battleau.clans.worldevents.WEType;
import net.battleau.clans.worldevents.WorldEvent;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class Timed extends WorldEvent{
	
	private List<WorldEventMinion> minions;
	private long length;
	private long startTime;

	public Timed(Clans i, String name, WEType type, double lengthInMinutes) {
		super(i, name, type);
		minions = new ArrayList<>();
		this.length = (long) (lengthInMinutes * 60000);
	}
	
	protected List<WorldEventMinion> getMinions(){
		return minions;
	}

	protected long getStartTime(){
		return startTime;
	}
	
	protected void setStartTime(long l){
		this.startTime = l;
	}
	
	protected long getLength(){
		return length;
	}
	
	protected boolean isMinion(LivingEntity ent){
		for(WorldEventMinion b : getMinions()){
			if(b.getEntity().equals(ent)){
				return true;
			}
		}
		return false;
	}
	
	public WorldEventMinion getMinion(LivingEntity ent){
		for(WorldEventMinion bm : getMinions()){
			if(bm.getEntity() == ent){
				return bm;
			}
		}
		return null;
	}

}
