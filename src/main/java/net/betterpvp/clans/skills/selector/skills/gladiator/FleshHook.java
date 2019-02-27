package net.betterpvp.clans.skills.selector.skills.gladiator;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.throwables.ThrowableManager;
import net.betterpvp.clans.combat.throwables.Throwables;
import net.betterpvp.clans.combat.throwables.events.ThrowableCollideEntityEvent;
import net.betterpvp.clans.combat.throwables.events.ThrowableHitGroundEvent;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.core.framework.RechargeManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.skills.selector.skills.data.ChargeData;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import net.betterpvp.core.utility.UtilVelocity;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

public class FleshHook extends Skill {

	public FleshHook(Clans i) {
		super(i, "Flesh Hook", "Gladiator", getSwords, rightClick, 5, true, true);

		immune = new WeakHashMap<>();
	}
	
	private WeakHashMap<Player, List<LivingEntity>> immune;

	public HashMap<UUID, Item> hooks = new HashMap<UUID, Item>();
	public WeakHashMap<Player, Long> delay = new WeakHashMap<>();
	

	@Override
	public String[] getDescription(int level) {
		// TODO Auto-generated method stub
		return new String[] {
				"Hold Block with Sword to Channel.",
				"",
				"Fire a hook at an enemy, pulling them towards you",
				"Higher Charge time = faster hook",
				"",
				"Cooldown: " + ChatColor.GREEN + getRecharge(level),
		};
	}

	@Override
	public void activateSkill(Player player) {

		new ChargeData(player.getUniqueId(), 25, 100);
		delay.put(player, System.currentTimeMillis());

	}

	@Override
	public boolean usageCheck(Player player) {
		if (player.getLocation().getBlock().getType() == Material.WATER || player.getLocation().getBlock().getType() == Material.STATIONARY_WATER) {
			UtilMessage.message(player, "Skill", "You cannot use " + ChatColor.GREEN + getName() + ChatColor.GRAY + " in water.");
			return false;
		}

		return true;
	}


	@EventHandler
	public synchronized void updateFleshHook(UpdateEvent event) {
		if (event.getType() == UpdateEvent.UpdateType.TICK) {
			ListIterator<ChargeData> iterator = ChargeData.data.listIterator();
			while (iterator.hasNext()) {
				ChargeData data = iterator.next();
				Player player = Bukkit.getPlayer(data.getUUID());
				if (player != null) {
					if(ClanUtilities.canCast(player)){
						if (Role.getRole(player) != null && Role.getRole(player).getName().equals(getClassType())) {
							if(delay.containsKey(player)){
								if(!UtilTime.elapsed(delay.get(player), 250)){
									continue;
								}
							}
							if(player.isBlocking()){
								if (!Arrays.asList(getMaterials()).contains(player.getItemInHand().getType())) {
									iterator.remove();
									continue;
								}

								if(!hasSwordInMainHand(player)){
									iterator.remove();
								}

								if(!hasSkill(player, this)){
									iterator.remove();
								}

								if (UtilTime.elapsed(data.getLastCharge(), 400L)) {
									if (data.getCharge() < data.getMaxCharge()) {
										data.addCharge();
										data.setLastCharge(System.currentTimeMillis());
										UtilMessage.message(player, getClassType(), getName() + ": " + ChatColor.YELLOW + "+ " + data.getCharge() + "% Strength");
										player.playSound(player.getLocation(), Sound.CLICK, 0.4F, 1.0F + 0.05F * data.getCharge());
									}
								}
							} else {
								if (Arrays.asList(getMaterials()).contains(player.getItemInHand().getType())) {
									double base = 0.8D;
									Location loc = player.getLocation();
									Location loc2 = loc.clone();

									for(int x = -20; x <= 20; x+=5){
										Item item = player.getWorld().dropItem(player.getEyeLocation().add(player.getLocation().getDirection()), new ItemStack(Material.TRIPWIRE_HOOK));
										Throwables throwable = new Throwables(item, player, getName(), 10000L);
										throwable.setCheckHead(true);
										ThrowableManager.getThrowables().add(throwable);
										

										

										loc2.setYaw(loc.getYaw() + x);
										Vector v = loc2.getDirection();

										UtilVelocity.velocity(item, v,
												base + (data.getCharge() / 20) * (0.25D * base), false, 0.0D, 0.2D, 20.0D, false);




									}


									UtilMessage.message(player, getClassType(), "You used " + ChatColor.GREEN + getName() + ChatColor.GRAY + ".");
									player.getWorld().playSound(player.getLocation(), Sound.IRONGOLEM_THROW, 2.0F, 0.8F);


									iterator.remove();

									RechargeManager.getInstance().removeCooldown(player.getName(), getName(), true);
									if (RechargeManager.getInstance().add(player, getName(), getRecharge(getLevel(player)), showRecharge())) {

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
	public void onLand(ThrowableHitGroundEvent e){
		if(e.getThrowable().getSkillName().equalsIgnoreCase(getName())){
			e.getThrowable().getItem().remove();
		}

	}



	@EventHandler
	public void onCollide(ThrowableCollideEntityEvent e){
		if(e.getThrowable().getSkillName().equalsIgnoreCase(getName())){
			LivingEntity collide = e.getCollision();
			if(collide instanceof Player){
				Player player = (Player) collide;
				if(!ClanUtilities.canHurt((Player) e.getThrowable().getThrower(), player)){
					return;
				}

				
			}

			UtilVelocity.velocity(collide, UtilVelocity.getTrajectory(collide.getLocation(), e.getThrowable().getThrower().getLocation()), 2.0D, false, 0.0D, 0.8D, 1.0D, true);
			e.getThrowable().getItem().remove();

			LogManager.addLog(collide, e.getThrowable().getThrower(), "Flesh Hook");
			CustomDamageEvent ev = new CustomDamageEvent(collide, e.getThrowable().getThrower(), null, DamageCause.CUSTOM, 2, false);
			ev.setIgnoreArmour(false);
			
			Bukkit.getPluginManager().callEvent(ev);


		}
	}




	@Override
	public Types getType() {
		// TODO Auto-generated method stub
		return Types.SWORD;
	}

	@Override
	public double getRecharge(int level) {
		// TODO Auto-generated method stub
		return 18 - ((level -1) * 2);
	}

	@Override
	public float getEnergy(int level) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean requiresShield() {
		// TODO Auto-generated method stub
		return true;
	}
}
