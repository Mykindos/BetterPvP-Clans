package net.betterpvp.clans.weapon.weapons.legendaries;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.morphs.MorphUtilities;
import net.betterpvp.clans.weapon.Weapon;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class HyperAxe extends Weapon {


	public HyperAxe(Clans i) {
		super(i, Material.RECORD_7, (byte) 0, ChatColor.RED + "Hyper Axe",
				new String[] {"", ChatColor.GRAY + "Damage: " + ChatColor.YELLOW + "4",
						ChatColor.GRAY + "Ability: " + ChatColor.YELLOW + "Hyper Speed",
						ChatColor.GRAY + "Passive: " + ChatColor.YELLOW + "Hyper Attack",
						ChatColor.GRAY + "Knockback: " + ChatColor.YELLOW + "None", "",
						ChatColor.WHITE + "Rumoured to attack foes 500% faster",
						ChatColor.WHITE + "than any other weapon known to man.", "" }, true, 2);


	}




	@EventHandler (priority = EventPriority.LOW)
	public void onEntityDamage(CustomDamageEvent event) {
		if(event.getCause() != DamageCause.ENTITY_ATTACK) return;
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			
			if(player.getItemInHand() == null) return;
			if(player.getItemInHand().getType() != Material.RECORD_7) return;
			if(MorphUtilities.isMorphed(player)){
				return;
			}
			if (isWeapon(player.getItemInHand()) && getWeapon(player.getItemInHand()).equals(this)) {

				event.setDamage(4);
				event.setKnockback(false);
				event.setDamageDelay(50);

			}


		}
	}
}
