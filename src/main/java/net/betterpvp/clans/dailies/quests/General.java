package net.betterpvp.clans.dailies.quests;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.dailies.Quest;
import net.betterpvp.clans.dailies.progression.Progress;
import net.betterpvp.clans.dailies.progression.types.GeneralProgression;

public abstract class General extends Quest{

	public General(Clans i, String questName, String[] description) {
		super(i, questName, description);

	}

	public abstract int getRequiredAmount();
	
	
	

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		if(isActive()){
			addProgression(e.getPlayer().getUniqueId());
		}
	}
	
	protected boolean addProgression(UUID uuid){
		Progress p = getQuestProgression(uuid, getName());

		if(p == null){
			getProgression().add(new GeneralProgression(uuid, getName(), getRequiredAmount()));
			return true;
		}
		
		return false;
	}

}
