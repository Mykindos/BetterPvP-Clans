package net.betterpvp.clans.skills.events;

import net.betterpvp.clans.skills.skills.Skill;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkillDequipEvent extends Event{
	
	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return handlers;
	}
	
	 public static HandlerList getHandlerList() {
	        return handlers;
	    }

	private Player p;
	private Skill s;
	
	public SkillDequipEvent(Player p, Skill s){
		this.p = p;
		this.s = s;
	}
	
	public Skill getSkill(){
		return s;
	}
	
	public Player getPlayer(){
		return p;
	}

}
