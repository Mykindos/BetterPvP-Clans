package net.betterpvp.clans.clans.commands;

import org.bukkit.entity.Player;

public interface IClanCommand {

	void run(Player p, String[] args);
	
	String getName();

}
