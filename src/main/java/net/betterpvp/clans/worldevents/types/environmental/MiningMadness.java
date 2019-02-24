package net.betterpvp.clans.worldevents.types.environmental;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.worldevents.WEType;
import net.betterpvp.clans.worldevents.types.Environmental;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class MiningMadness extends Environmental{

	public MiningMadness(Clans i) {
		super(i, "MiningMadness", WEType.ENVIRONMENTAL, 30);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void spawn() {
		setStartTime(System.currentTimeMillis());
	}
	
	@Override
	public String getDisplayName() {
		
		return ChatColor.GREEN.toString() + ChatColor.BOLD + "Mining Madness";
	}

	@Override
	public Location[] getTeleportLocations() {
		
		return null;
	}
	
	@Override
	public void subAnnounce() {
		Bukkit.broadcastMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "For the duration of the event, you earn 2x ores from Fields,");
		Bukkit.broadcastMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "and ores regenerate 2x as quick.");
	}


}
