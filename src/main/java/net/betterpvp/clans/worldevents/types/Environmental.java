package net.betterpvp.clans.worldevents.types;

import net.battleau.clans.Clans;
import net.battleau.clans.events.UpdateEvent;
import net.battleau.clans.events.UpdateEvent.UpdateType;
import net.battleau.clans.utility.UtilMessage;
import net.battleau.clans.utility.UtilTime;
import net.battleau.clans.worldevents.WEType;
import net.battleau.clans.worldevents.WorldEvent;
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
