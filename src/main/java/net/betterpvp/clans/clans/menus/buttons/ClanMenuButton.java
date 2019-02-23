package net.betterpvp.clans.clans.menus.buttons;

import net.betterpvp.clans.clans.Clan;
import net.betterpvp.core.interfaces.Button;
import org.bukkit.inventory.ItemStack;

public class ClanMenuButton extends Button{
	
	private Clan clan;

	public ClanMenuButton(Clan clan, int slot, ItemStack item, String name,
			String... lore) {
		super(slot, item, name, lore);
		this.clan = clan;
	}
	
	public Clan getClan(){
		return clan;
	}

}
