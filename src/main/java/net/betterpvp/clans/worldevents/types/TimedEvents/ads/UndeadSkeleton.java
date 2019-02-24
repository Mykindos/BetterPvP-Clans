package net.betterpvp.clans.worldevents.types.TimedEvents.ads;

import net.betterpvp.clans.worldevents.types.WorldEventMinion;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;

public class UndeadSkeleton extends WorldEventMinion{

	public UndeadSkeleton(Skeleton ent) {
		super(ent);
		ent.getEquipment().setItemInHand(new ItemStack(Material.BOW));
		ent.getEquipment().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
		ent.getEquipment().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
		ent.getEquipment().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
		ent.getEquipment().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
	}

	@Override
	public String getDisplayName() {
		
		return ChatColor.RED + "Undead Archer";
	}

	@Override
	public double getMaxHealth() {
		
		return 30;
	}

}
