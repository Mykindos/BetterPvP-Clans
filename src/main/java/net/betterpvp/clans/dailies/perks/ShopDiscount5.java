package net.betterpvp.clans.dailies.perks;

import net.md_5.bungee.api.ChatColor;

public class ShopDiscount5 extends QuestPerk{

	public ShopDiscount5() {
		super("5% Shop Discount");

	}

	@Override
	public String[] getDescription(int price) {
		// TODO Auto-generated method stub
		return new String[]{
				ChatColor.YELLOW + "Buy Price: " + ChatColor.GREEN + price,
				"",

				ChatColor.WHITE + "Gives players a 5% map long discount."
		};
	}

}
