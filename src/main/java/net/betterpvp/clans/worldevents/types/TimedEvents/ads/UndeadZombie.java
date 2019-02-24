package net.betterpvp.clans.worldevents.types.TimedEvents.ads;

import net.betterpvp.clans.worldevents.types.WorldEventMinion;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class UndeadZombie extends WorldEventMinion{

	public UndeadZombie(LivingEntity ent) {
		super(ent);
		ent.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		ent.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
		ent.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
		ent.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
		ent.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
		ent.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1));
		ent.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDisplayName() {
		
		return ChatColor.RED + "Undead Warrior";
	}

	@Override
	public double getMaxHealth() {
		
		return 30;
	}

}
