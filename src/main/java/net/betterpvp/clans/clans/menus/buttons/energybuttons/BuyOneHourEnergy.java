package net.betterpvp.clans.clans.menus.buttons.energybuttons;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.menus.buttons.ClanMenuButton;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;

public class BuyOneHourEnergy extends ClanMenuButton{
	
	
	public BuyOneHourEnergy(Clan clan) {
		super(clan, 2, new ItemStack(Material.EMERALD), ChatColor.GREEN + "Buy 1 Hour of Energy",
				"",
				ChatColor.GRAY + "Buy " + ChatColor.GREEN + (clan.getTerritory().size() * 25) 
				+ ChatColor.GRAY + " energy for " + ChatColor.YELLOW + "$" 
				+ NumberFormat.getInstance().format(((clan.getTerritory().size() * 25) * Clans.getOptions().getCostPerEnergy()))
				+ ChatColor.YELLOW + " coins.",
				"",
				ChatColor.GREEN + "Left-Click: " + ChatColor.GRAY + "Confirm");
	
	}
	
	
}
