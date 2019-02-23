package net.betterpvp.clans.worldevents.types.bosses.ads;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Slime;

public class SlimeQuarter extends SlimeBase{

	public SlimeQuarter(Slime s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDisplayName() {
		
		return ChatColor.RED.toString() + ChatColor.BOLD + "1/4 Slime King";
	}

	@Override
	public double getMaxHealth() {
		
		return 125;
	}

	@Override
	public int getSize() {
		
		return 3;
	}

}
