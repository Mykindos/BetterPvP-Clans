package net.betterpvp.clans.worldevents;

import org.bukkit.inventory.ItemStack;

public class Drop {
	
	private ItemStack i;
	private double chance;
	
	public Drop(ItemStack i, double chance){
		this.i = i;
		this.chance = chance;
	}
	
	public ItemStack getItemStack(){
		return i;
	}
	
	public double getChance(){
		return chance;
	}

}
