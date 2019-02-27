package net.betterpvp.clans.skills.selector.skills.ranger;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.client.ClientUtilities;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.skills.selector.skills.data.HuntersThrillData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Iterator;

public class HuntersThrill extends Skill {

	public HuntersThrill(Clans i) {
		super(i, "Hunters Thrill", "Ranger",
				noMaterials, noActions,5, true, false);
	}

	@Override
	public String[] getDescription(int level) {
		// TODO Auto-generated method stub
		return new String[] {"For each consecutive hit within " + ChatColor.GREEN + (8 + level) + ChatColor.GRAY + " seconds of each other",
		"you gain increased movement speed"};
	}

	@EventHandler
	public void onArrowHit(CustomDamageEvent event) {
		if (event.getProjectile() != null) {
			
			if(event.getProjectile() instanceof Arrow){
				if(event.getDamager() instanceof Player){
					Player player = (Player) event.getDamager();
					if (event.getDamagee() instanceof Player) {

						Player entity = (Player) event.getDamagee();
						if (player == entity) {
							return;
						}

						
							if (Role.getRole(player) != null && Role.getRole(player).getName().equals(getClassType())) {
								if(ClientUtilities.getClient(player) != null){
									if(hasSkill(player, this)){

										if (HuntersThrillData.getHuntersThrillData(player.getUniqueId()) == null) {
											new HuntersThrillData(player.getUniqueId());
										}

										HuntersThrillData data = HuntersThrillData.getHuntersThrillData(player.getUniqueId());

										if (data.getCharge() < 4) {
											data.addCharge();
											data.setLastHit(System.currentTimeMillis());
											player.removePotionEffect(PotionEffectType.SPEED);
											player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, data.getCharge() - 1));
										}
									}
								}
							}
						}
					
				}
			}
		}
	}



	@EventHandler
	public void updateHuntersThrillData(UpdateEvent event) {
		if (event.getType() == UpdateType.TICK) {
			Iterator<HuntersThrillData> iterator = HuntersThrillData.data.iterator();
			while (iterator.hasNext()) {
				HuntersThrillData data = iterator.next();

				Player player = Bukkit.getPlayer(data.getUUID());

				if (player != null) {
					if (System.currentTimeMillis() >= data.getLastHit() + (8000 + (getLevel(player) * 1000))) {
						iterator.remove();
					}
				}
			}
		}
	}

	@Override
	public void activateSkill(Player player) {

	}

	@Override
	public boolean usageCheck(Player player) {
		return true;
	}



	@Override
	public Types getType() {
		// TODO Auto-generated method stub
		return Types.PASSIVE_B;
	}

	@Override
	public double getRecharge(int level) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getEnergy(int level) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean requiresShield() {
		// TODO Auto-generated method stub
		return false;
	}

}
