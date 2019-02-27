package net.betterpvp.clans.weapon.weapons.legendaries;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilVelocity;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class MagneticBlade extends Weapon {

	public List<String> active = new ArrayList<String>();

	public MagneticBlade(Clans i) {
		super(i, Material.DIAMOND_SWORD, (byte) 0, ChatColor.RED + "Magnetic Blade",
				new String[]{"",
						ChatColor.GRAY + "Damage: " + ChatColor.YELLOW + "7",
						ChatColor.GRAY + "Ability: " + ChatColor.YELLOW + "Magnetic Pull",
						"",
						ChatColor.RESET + "The Magnetic Blade is said to be able",
						ChatColor.RESET + "to pull nearby enemies and objects towards itself",
						ChatColor.RESET + "with unstoppable force.",
		""}, true, 2.0);
	}

	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			if (WeaponManager.isWeapon(player.getItemInHand()) && WeaponManager.getWeapon(player.getItemInHand()).equals(this)) {
				event.getEntity().getVelocity().multiply(-0.4D);
			}
		}
	}

	@EventHandler
	public void onWindRiderUse(PlayerInteractEvent event) {
		Player player = event.getPlayer();


		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

			if(player.getItemInHand() == null) return;
			if(player.getItemInHand().getType() != Material.DIAMOND_SWORD) return;
			if (WeaponManager.isWeapon(player.getItemInHand()) && WeaponManager.getWeapon(player.getItemInHand()).equals(this)) {
				if (player.getLocation().getBlock().isLiquid()) {
					UtilMessage.message(player, getName(), "You cannot use " + ChatColor.LIGHT_PURPLE + getName() + ChatColor.GRAY + " in water.");
					return;
				}

				if(!ClanUtilities.canCast(player)){
					return;
				}

				if (Energy.use(player, getName(), 10.0, true)) {
					active.add(player.getName());
				}
			}
		}
	}

	@EventHandler
	public void Energy(UpdateEvent e) {
		if (e.getType() == UpdateEvent.UpdateType.TICK) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if(active.contains(p.getName())){

					if(p.isBlocking()){
						if(!Energy.use(p, getName(), 3, true)){
							active.remove(p.getName());
						}else{

							p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType());

							for (int i = 0; i <= 5; i++) {
								pull(p, p.getEyeLocation().add(p.getLocation().getDirection().multiply(i)));
							}
						}
					}else {
						active.remove(p.getName());
					}

				}
			}
		}

	}


	public void pull(Player p, Location loc) {

		for(Entity other : loc.getWorld().getEntities()) {
			if(loc.distance(other.getLocation()) > 2) continue;
			if(other instanceof Item || other instanceof LivingEntity) {

				if(other instanceof Player) {
					Player player = (Player) other;
					if (!ClanUtilities.canHurt(p, player)) {
						continue;
					}


				}

				if (UtilMath.offset(p.getLocation(), other.getLocation()) >= 2.0D) {
					UtilVelocity.velocity(other, UtilVelocity.getTrajectory(other, p), 0.3D, false, 0.0D,
							0.0D, 1.0D, true);
				}
			}
		}
		
	}
}
