package net.betterpvp.clans.worldevents.types.bosses.ads;

import net.betterpvp.clans.worldevents.types.WorldEventMinion;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SkeletonMinion extends WorldEventMinion{


	public SkeletonMinion(Skeleton ent) {
		super(ent);
	
		ent.setSkeletonType(SkeletonType.WITHER);
		ent.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
		ent.setMaxHealth(getMaxHealth());
		ent.setHealth(getMaxHealth());
		ent.setCustomName(getDisplayName());
		ent.setCustomNameVisible(true);
		ent.setRemoveWhenFarAway(false);
		ent.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDisplayName() {
		
		return ChatColor.BLUE + "Skeleton King";
	}

	@Override
	public double getMaxHealth() {
		
		return 200;
	}

}
