package net.betterpvp.clans.worldevents.types.bosses.ads;

import net.betterpvp.clans.worldevents.types.WorldEventMinion;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.CaveSpider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Broodling extends WorldEventMinion{

	private long systemTime;
	
	public Broodling(CaveSpider ent, long systemTime) {
		super(ent);
		this.systemTime = systemTime;
		ent.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
	}

	@Override
	public String getDisplayName() {
		
		return ChatColor.RED + "Broodling";
	}
	
	public CaveSpider getSpider(){
		return (CaveSpider) getEntity();
	}

	@Override
	public double getMaxHealth() {
		
		return 5;
	}
	
	public long getSystemTime(){
		return systemTime;
	}
	
	public void setSystemTime(long l){
		this.systemTime = l;
	}

}
