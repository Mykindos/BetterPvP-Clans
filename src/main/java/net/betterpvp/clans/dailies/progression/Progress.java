package net.betterpvp.clans.dailies.progression;

import java.util.UUID;

import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;

import net.md_5.bungee.api.ChatColor;

public class Progress {

	private UUID uuid;
	private String questName;
	private boolean complete;

	public Progress(UUID uuid, String questName){
		this.uuid = uuid;
		this.questName = questName;
	}


	public UUID getUUID(){
		return uuid;
	}

	public String getQuestName(){
		return questName;
	}

	public boolean isComplete(){
		return complete;
	}

	public void onComplete(UUID player){
		if(Bukkit.getPlayer(player) != null){
			double fragments = 5;
			UtilMessage.message(Bukkit.getPlayer(player), "Daily", "You have completed " + ChatColor.YELLOW + getQuestName() + ChatColor.GRAY + " and received "
					+ ChatColor.GREEN + fragments + ChatColor.GRAY + " fragments.");
			GamerManager.getOnlineGamer(player).addFragments(fragments);
		}

		setComplete(true);
	}

	public void setComplete(boolean b){
		this.complete = b;
	}

}
