package net.betterpvp.clans.skills.selector.skills.assassin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.framework.RechargeManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.skills.selector.skills.data.RecallDataOLD;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.clans.weapon.Weapon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.InventoryView;

import java.util.Arrays;
import java.util.Iterator;

public class RecallOLD extends Skill {

	public RecallOLD(Clans i) {
		super(i, "Recall", "Assassin",
				getSwordsAndAxes,
				noActions, 5, true, false);

	}




	@EventHandler
	public void onRecall(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		InventoryView iv = player.getOpenInventory();


		if(iv.getType() != InventoryType.CRAFTING){
			return;
		}

		if(Weapon.getWeapon(event.getItemDrop().getItemStack()) != null){
			return;
		}
		

		if(!hasSkill(player, this)){
			return;
		}

		event.setCancelled(true);

		if (Arrays.asList(getMaterials()).contains(event.getItemDrop().getItemStack().getType())) {
		
			if (usageCheck(player)) {
			
			
				
				int level = getLevel(player);
			
				if (!RecallDataOLD.containsData(player)) {
				
					if (RechargeManager.getInstance().add(player, getName(), getRecharge(level), showRecharge())) {
						

						new RecallDataOLD(player, player.getLocation());

						UtilMessage.message(player, getClassType(), "You used " + ChatColor.GREEN + getName() + ChatColor.GRAY + ".");
					}
				} else {
					
					RecallDataOLD.recall(player);
				}
			}



		}



	}






	@EventHandler
	public void updateRecallData(UpdateEvent event) {
		if (event.getType() == UpdateType.TICK) {
			Iterator<RecallDataOLD> iterator = RecallDataOLD.recalls.iterator();
			while (iterator.hasNext()) {
				RecallDataOLD data = iterator.next();
				if (System.currentTimeMillis() >= data.getTime()) {
					data.getItem().getLocation().getWorld().playEffect(data.getItem().getLocation(), Effect.STEP_SOUND, Material.EMERALD_BLOCK);
					data.getItem().remove();

					if (Bukkit.getPlayer(data.getPlayer()) != null) {
						UtilMessage.message(Bukkit.getPlayer(data.getPlayer()), getClassType(), "Your Recall posistion was lost.");
					}
					data.getItem().teleport(data.getLocation());
					iterator.remove();
				}


			}
		}
	}

	@EventHandler
	public void onHopperPickup(InventoryPickupItemEvent event) {
		if (event.isCancelled()) {
			return;
		}

		for (RecallDataOLD recall : RecallDataOLD.recalls) {
			if (event.getItem().equals(recall.getItem())) {
				event.setCancelled(true);
			}
		}
	}

	@Override
	public void activateSkill(Player player) {

	}

	@Override
	public boolean usageCheck(Player player) {

		
		if(EffectManager.isSilenced(player)){
			UtilMessage.message(player, getClassType(), "You cannot use " + ChatColor.GREEN + getName(getLevel(player)) + ChatColor.GRAY + " while silenced!");
			return false;
		}

		Clan clan = ClanUtilities.getClan(player.getLocation());
		if (clan != null) {
			if (clan instanceof AdminClan) {
				AdminClan adminClan = (AdminClan) clan;

				if (adminClan.isSafe()) {

					UtilMessage.message(player, getClassType(), "You cannot use " + ChatColor.GREEN + getName() + ChatColor.GRAY + " in Safe Zones.");
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String[] getDescription(int level) {
		// TODO Auto-generated method stub
		return new String[] {"Dropping your sword will create a",
				"saved location in which upon reactivating",
				"you will instantly teleport back to",
				"",
				"Cooldown: " + ChatColor.GREEN + getRecharge(level),
				"Energy: " + ChatColor.GREEN + getEnergy(level)};
	}

	@Override
	public Types getType() {
		// TODO Auto-generated method stub
		return Types.PASSIVE_B;
	}

	@Override
	public double getRecharge(int level) {
		// TODO Auto-generated method stub
		return 30 - ((level-1) *2);
	}

	@Override
	public float getEnergy(int level) {
		// TODO Auto-generated method stub
		return (float)80 - ((level -1) * 5);
	}

	@Override
	public boolean requiresShield() {
		// TODO Auto-generated method stub
		return false;
	}
}
