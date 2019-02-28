package net.betterpvp.clans.classes;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.classes.events.RoleChangeEvent;
import net.betterpvp.clans.classes.roles.*;
import net.betterpvp.clans.classes.roles.mysql.StatRepository;

import net.betterpvp.core.client.ClientUtilities;

import net.betterpvp.core.framework.BPVPListener;

import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;

import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RoleManager extends BPVPListener<Clans> {

	public RoleManager(Clans i){
		super(i);
		new Gladiator();
		new Knight();
		new Paladin();
		new Ranger();
		new Assassin();
	}

	@EventHandler
	public void setRoles(UpdateEvent event) {
		if (event.getType() == UpdateType.FAST) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				Role.doEquip(player);
			}
		}
	}

	@EventHandler
	public void messagePlayer(RoleChangeEvent event) {
		Player player = event.getPlayer();
		Role role = event.getRole();

		if (role == null) {
			UtilMessage.message(player, "Class", "Armor Class: " + ChatColor.GREEN + "None");
			for (PotionEffect effect : player.getActivePotionEffects()) {

				if(effect.getType().getName().equals("POISON")
						|| effect.getType().getName().equals("CONFUSION")
						|| effect.getType().getName().equals("BLINDNESS")){

					continue;	
				}
				player.removePotionEffect(effect.getType());

			}
		} else {
			
			for (PotionEffect effect : player.getActivePotionEffects()) {

				// Prevent player from removing negative effects by re-equipping a set
				if(effect.getType().getName().equals("POISON")
						|| effect.getType().getName().equals("CONFUSION")
						|| effect.getType().getName().equals("BLINDNESS")){

					continue;	
				}
				player.removePotionEffect(effect.getType());

			}
			
			if(player.getWorld().getName().equals("tutorial")) return;
			UtilMessage.message(player, "Class", "Armor Class: " + ChatColor.GREEN + role.getName());
			UtilMessage.message(player, "Skills", "Listing " + role.getName() + " Skills: ");
			
			UtilMessage.message(player, role.equipMessage(player));
			StatRepository.addClassStat(role.getName());
			
		}

		player.getWorld().playSound(player.getLocation(), Sound.HORSE_ARMOR, 2.0F, 1.09F);
	}

	@EventHandler(ignoreCancelled = false)
	public void onDamageSound(CustomDamageEvent event) {
		if (event.getDamagee() instanceof Player) {
			if (event.getDamager() instanceof Player) {
				Player damaged = (Player) event.getDamagee();
				Player damager = (Player) event.getDamager();

				if(damaged.getLocation().distance(damager.getLocation()) <= 4.0){
					if (Role.getRole(damaged) != null) {
						if (RechargeManager.getInstance().add(damager, "Damage", 0.7, false)) {
							Role role = Role.getRole(damaged);

							if (role instanceof Knight) {
								damaged.getWorld().playSound(damaged.getLocation(), Sound.BLAZE_HIT, 1.0F, 0.7F);
							} else if (role instanceof Assassin) {
								damaged.getWorld().playSound(damaged.getLocation(), Sound.SHOOT_ARROW, 1.0F, 2.0F);
							} else if (role instanceof Gladiator) {
								damaged.getWorld().playSound(damaged.getLocation(), Sound.BLAZE_HIT, 1.0F, 0.9F);
							} else if (role instanceof Ranger) {
								damaged.getWorld().playSound(damaged.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.4F);
							} else if (role instanceof Paladin) {
								damaged.getWorld().playSound(damaged.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.8F);
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void updateSkillPotions(UpdateEvent event) {
		if (event.getType() == UpdateType.FAST) {
			for (Player player : Bukkit.getOnlinePlayers()) {

				Role role = Role.getRole(player);
				if (role != null) {
					if (role instanceof Assassin) {
						player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
					}
				}
			}
		}
	}

}
