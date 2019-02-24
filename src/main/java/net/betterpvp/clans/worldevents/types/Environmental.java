package net.betterpvp.clans.worldevents.types;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.events.UpdateEvent;
import net.betterpvp.clans.events.UpdateEvent.UpdateType;
import net.betterpvp.clans.utility.UtilMessage;
import net.betterpvp.clans.utility.UtilTime;
import net.betterpvp.clans.worldevents.WEType;
import net.betterpvp.clans.worldevents.WorldEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;

public abstract class Environmental extends WorldEvent{
	
	private long length, startTime;

	public Environmental(Clans i, String name, WEType type, double lengthInMinutes) {
		super(i, name, type);
		this.length = (long) (lengthInMinutes * 60000);
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
	
	
	@EventHandler
	public void checkTimeup(UpdateEvent e){
		if(e.getType() == UpdateType.SEC){
			if(isActive()){
				if(UtilTime.elapsed(getStartTime(), getLength())){
					UtilMessage.broadcast("World Event", getDisplayName() + ChatColor.GRAY + " has ended!");
					setActive(false);
					
				}
			}
		}
	}
	
	public abstract void subAnnounce();
	
}
