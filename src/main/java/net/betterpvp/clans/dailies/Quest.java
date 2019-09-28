package net.betterpvp.clans.dailies;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.betterpvp.core.framework.BPVPListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.dailies.progression.Progress;

public class Quest extends BPVPListener<Clans> {
	
	private Clans instance;
	private boolean active;
	private String questName;
	private String[] description;
	private List<Progress> progression;
	
	public Quest(Clans i, String questName, String[] description){
		super(i);
		this.instance = i;
		this.questName = questName;
		this.description = description;
		this.progression = new ArrayList<>();
	}
	
	public List<Progress> getProgression(){
		return progression;
	}
	
	public Progress getQuestProgression(UUID player, String questName){
		for(Progress p: getProgression()){
			if(p.getUUID().equals(player) && p.getQuestName().equals(questName)){
				return p;
			}
		}
		return null;
	}
	
	public Clans getInstance(){
		return instance;
	}

	public boolean isActive(){
		return active;
	}
	
	public String getName(){
		return questName;
	}
	
	public String[] getDescription(){
		return description;
	}
	

	
	public void setActive(boolean active){
		this.active = active;
	}

}
