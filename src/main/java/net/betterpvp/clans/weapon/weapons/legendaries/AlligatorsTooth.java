package net.betterpvp.clans.weapon.weapons.legendaries;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.skills.selector.skills.paladin.Polymorph;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import net.betterpvp.core.utility.UtilVelocity;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;


public class AlligatorsTooth extends Weapon {

	public List<String> active = new ArrayList<>();
	public WeakHashMap<Player, Long> wait = new WeakHashMap<>();

	public AlligatorsTooth(Clans i) {
		super(i, Material.DIAMOND_SWORD, (byte) 0, ChatColor.RED + "Alligators Tooth",
				new String[]{"",
						ChatColor.GRAY + "Damage: " + ChatColor.YELLOW + "7 + 2 in Water",
						ChatColor.GRAY + "Ability: " + ChatColor.YELLOW + "Alliagtor Rush",
						ChatColor.GRAY + "Passive: " + ChatColor.YELLOW + "Water Breathing",
						ChatColor.GRAY + "Knockback: " + ChatColor.YELLOW + "100%",
						"",
						ChatColor.RESET + "A blade forged from hundreds of ",
						ChatColor.RESET + "alligators teeth. It's powers allow ",
						ChatColor.RESET + "its owner to swim with great speed, ",
						ChatColor.RESET + "able to catch any prey.",
		""}, true, 2.0);
	}

	@EventHandler
	public void onAlligatorToothUse(PlayerInteractEvent event) {



		Player player = event.getPlayer();

		if(player.getItemInHand() == null) return;
		if(player.getItemInHand().getType() != Material.DIAMOND_SWORD) return;

		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (isThisWeapon(player)) {
				if (!player.getLocation().getBlock().isLiquid()) {
					UtilMessage.message(player, getName(), "You cannot use " + ChatColor.LIGHT_PURPLE + "Alligator Rush" + ChatColor.GRAY + " out of water.");
					return;
				}

				if(!active.contains(player.getName())){
					if (Energy.use(player, "Alligator Rush", 10.0, true)) {
						active.add(player.getName());
						wait.put(player, System.currentTimeMillis());
					}
				}
			}
		}
	}

	@EventHandler
	public void Update(UpdateEvent event) {
		if (event.getType() == UpdateEvent.UpdateType.TICK) {
			for (Player player : Bukkit.getOnlinePlayers()) {

				if (active.contains(player.getName())) {

					if(player.isBlocking()){
						if (!isThisWeapon(player)) {
							active.remove(player.getName());
						}else if(!Energy.use(player, "Alligator Rush", 0.25, true)) {

							active.remove(player.getName());

						}  else if (!player.getLocation().getBlock().isLiquid()) {
							active.remove(player.getName());
							UtilMessage.message(player, getName(), "You cannot use " + ChatColor.LIGHT_PURPLE + "Alligator Rush" + ChatColor.GRAY + " out of water.");
						} else if(Polymorph.polymorphed.containsKey(player)){
							active.remove(player.getName());
						} else {
							UtilVelocity.velocity(player, 1.0D, 0.11D, 1.0D, true);
							player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, 8);
							player.getWorld().playSound(player.getLocation(), Sound.SWIM, 0.8F, 1.5F);
						}
					}else{
						if(UtilTime.elapsed(wait.get(player), 500)){
							active.remove(player.getName());
							wait.remove(player);
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void entityDamEvent(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			if(e.getCause() == DamageCause.DROWNING){
				Player p = (Player) e.getEntity();
				if(p.getItemInHand() == null) return;
				if(p.getItemInHand().getType() != Material.DIAMOND_SWORD) return;
				if (isThisWeapon(p)) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler (priority = EventPriority.LOW)
	public void onEntityDamage(CustomDamageEvent event) {
		if(event.getCause() != DamageCause.ENTITY_ATTACK) return;
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			if(player.getItemInHand() == null) return;
			if(player.getItemInHand().getType() != Material.DIAMOND_SWORD) return;

			if (isThisWeapon(player)) {
				if(event.getDamager().getLocation().getBlock().isLiquid()){
					event.setDamage(event.getDamage() + 2);
				}
			}
		}
	}
}
