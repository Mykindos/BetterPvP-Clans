package net.betterpvp.clans.clans.listeners;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.ClanUtilities.ClanRelation;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class DamageListener extends BPVPListener<Clans> {
	
	public DamageListener(Clans i){
		super(i);
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager().getType() == EntityType.FISHING_HOOK){
			if(ClanUtilities.getClan(e.getEntity().getLocation()) != null){
				if(ClanUtilities.getClan(e.getEntity().getLocation()) instanceof AdminClan){
					AdminClan a = (AdminClan) ClanUtilities.getClan(e.getEntity().getLocation());
					if(a.isSafe()){
						e.setCancelled(true);
					}
				}
			}
			return;
		}
		if (e.getEntity() instanceof Player) {
			if (UtilPlayer.getPlayer(e.getDamager()) != null) {
				Player damaged = (Player) e.getEntity();
				Player damager = UtilPlayer.getPlayer(e.getDamager());
				Clan damagedClan = ClanUtilities.getClan(damaged);
				Clan damagerClan = ClanUtilities.getClan(damager);

				if (!ClanUtilities.canHurt(damager, damaged)) {
					UtilMessage.message(damager, "Clan", "You cannot harm "
							+ ClanUtilities.getRelation(damagedClan, damagerClan).getPrimary() + damaged.getName() + ChatColor.GRAY + ".");
					e.setCancelled(true);
				}
			} else if (e.getDamager() instanceof Projectile) {
				Projectile damager = (Projectile) e.getDamager();

				if (damager.getShooter() instanceof Player) {
					Player shooter = (Player) damager.getShooter();
					Player damaged = (Player) e.getEntity();
					Clan damagedClan = ClanUtilities.getClan(damaged);
					Clan damagerClan = ClanUtilities.getClan(shooter);

					if (ClanUtilities.getRelation(damagedClan, damagerClan) == ClanRelation.SELF
							|| ClanUtilities.getRelation(damagedClan, damagerClan) == ClanRelation.ALLY
							|| ClanUtilities.getRelation(damagedClan, damagerClan) == ClanRelation.ALLY_TRUST) {
						UtilMessage.message(shooter, "Clan", "You cannot harm "
								+ ClanUtilities.getRelation(damagedClan, damagerClan).getPrimary() + damaged.getName() + ChatColor.GRAY + ".");
						e.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void onSafeZoneDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {

			Player p = (Player) e.getEntity();

			Clan clan = ClanUtilities.getClan(p.getLocation());
			if (clan instanceof AdminClan) {
				AdminClan admin = (AdminClan) clan;

				if (admin.isSafe() ) {
					if(e.getCause() == DamageCause.DROWNING || e.getCause() == DamageCause.FALL){

						e.setCancelled(true);
					}
				}
			}
		}
	}

}
