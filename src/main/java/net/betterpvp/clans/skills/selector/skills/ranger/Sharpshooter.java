package net.betterpvp.clans.skills.selector.skills.ranger;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.client.ClientUtilities;
import net.betterpvp.clans.events.UpdateEvent;
import net.betterpvp.clans.events.UpdateEvent.UpdateType;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.skills.selector.skills.data.SharpshooterData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.Iterator;

public class Sharpshooter extends Skill {

	public Sharpshooter(Clans i) {
		super(i, "Sharpshooter", "Ranger",
				noMaterials,
				noActions, 3, true, false);
	}

	@Override
	public String[] getDescription(int level) {
		// TODO Auto-generated method stub
		return new String[] {"For each consecutive hit,", 
				ChatColor.GREEN.toString() + (level * 0.75) + ChatColor.GRAY + " additional damage per charge"};
	}

	@EventHandler
	public void onArrowHit(CustomDamageEvent event) {
		if (event.getProjectile() instanceof Arrow) {
			if (event.getDamager() instanceof Player) {
				Player player = (Player) event.getDamager();
				if (event.getDamagee() instanceof Player) {

					Player entity = (Player) event.getDamagee();
					if (player == entity) {
						return;
					}


					if (Role.getRole(player) != null && Role.getRole(player).getName().equals(getClassType())) {
						if(ClientUtilities.getClient(player) != null){
							if(hasSkill(player, this)){
								if (SharpshooterData.getSharpshooterData(player.getUniqueId()) == null) {
									new SharpshooterData(player.getUniqueId());
								}

								SharpshooterData data = SharpshooterData.getSharpshooterData(player.getUniqueId());

								if (data.getCharge() < 4) {
									data.addCharge();
									data.setLastHit(System.currentTimeMillis());
									event.setDamage(event.getDamage() +  (data.getCharge() * (getLevel(player) * 0.75)));
								}
							}
						}

					}
				}
			}
		}


	}

	@EventHandler
	public void updateSharpshooterData(UpdateEvent event) {
		if (event.getType() == UpdateType.TICK) {
			Iterator<SharpshooterData> iterator = SharpshooterData.data.iterator();
			while (iterator.hasNext()) {
				SharpshooterData data = iterator.next();

				Player player = Bukkit.getPlayer(data.getUUID());

				if (player != null) {
					if (System.currentTimeMillis() >= data.getLastHit() + 5000L) {
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
