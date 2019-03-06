package net.betterpvp.clans.dailies.perks;

import net.md_5.bungee.api.ChatColor;

public class Swim extends QuestPerk{

	public Swim() {
		super("Swim");

	}

	@Override
	public String[] getDescription(int price) {
		// TODO Auto-generated method stub
		return new String[]{
				ChatColor.YELLOW + "Buy Price: " + ChatColor.GREEN + price,
				"",
				ChatColor.WHITE + "Allows usage of the swim perk",
				ChatColor.WHITE + "With any class, or without one."
		};
	}

}
