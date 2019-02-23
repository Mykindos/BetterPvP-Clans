package net.betterpvp.clans.worldevents.types.bosses.ads;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Slime;

public class SlimeFull extends SlimeBase{

	public SlimeFull(Slime s) {
		super(s);
		
	}

	@Override
	public String getDisplayName() {
		
		return ChatColor.RED.toString() + ChatColor.BOLD + "Slime King";
	}

	@Override
	public double getMaxHealth() {
		
		return 500;
	}

	@Override
	public int getSize() {
		
		return 11;
	}



}
