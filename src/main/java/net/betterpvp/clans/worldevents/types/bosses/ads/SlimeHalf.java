package net.betterpvp.clans.worldevents.types.bosses.ads;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Slime;

public class SlimeHalf extends SlimeBase{

	public SlimeHalf(Slime s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDisplayName() {
		
		return ChatColor.RED + "1/2 Slime King";
	}

	@Override
	public double getMaxHealth() {
		
		return 250;
	}

	@Override
	public int getSize() {
		
		return 9;
	}

}
