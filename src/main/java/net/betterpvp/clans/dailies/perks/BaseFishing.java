package net.betterpvp.clans.dailies.perks;

import net.md_5.bungee.api.ChatColor;

public class BaseFishing extends QuestPerk{

	public BaseFishing() {
		super("Base Fishing");

	}

	@Override
	public String[] getDescription(int price) {
		// TODO Auto-generated method stub
		return new String[]{
				ChatColor.YELLOW + "Buy Price: " + ChatColor.GREEN + price,
				"",
				ChatColor.WHITE + "Allows a player to fish from their base."
		};
	}

}
