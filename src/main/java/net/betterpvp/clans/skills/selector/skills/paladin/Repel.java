package net.betterpvp.clans.skills.selector.skills.paladin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.events.UpdateEvent;
import net.betterpvp.clans.events.UpdateEvent.UpdateType;
import net.betterpvp.clans.gamer.combat.LogManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.utility.UtilMessage;
import net.betterpvp.clans.utility.UtilTime;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Bat;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.WeakHashMap;

public class Repel extends Skill{

	public Repel(Clans i) {
		super(i, "Repel", "Paladin", getAxes, rightClick, 5, true, true);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getDescription(int level) {
		// TODO Auto-generated method stub
		return new String[]{
				"Right click with Axe to Activate.",
				"",
				"Release a pack of " + ChatColor.GREEN + (5 + level) + ChatColor.GRAY + " bats",
				"which damage, and knockback",
				"any enemies they come in contact with",
				"",
				"Cooldown: " + ChatColor.GREEN + getRecharge(level),
				"Energy: " + ChatColor.GREEN + getEnergy(level)
		};
	}

	@Override
	public Types getType() {
		// TODO Auto-generated method stub
		return Types.AXE;
	}

	@Override
	public double getRecharge(int level) {
		// TODO Auto-generated method stub
		return 20 - ((level -1) * 2);
	}

	@Override
	public float getEnergy(int level) {
		// TODO Auto-generated method stub
		return 50 - ((level -1) * 3);
	}

	private WeakHashMap<Player, Long> active = new WeakHashMap<>();
	private WeakHashMap<Player, Location> playerVelocity = new WeakHashMap<>();
	private WeakHashMap<Player, ArrayList<Bat>> bats = new WeakHashMap<>();

	@Override
	public void activateSkill(Player p) {
		UtilMessage.message(p, getClassType(), "You used " + ChatColor.GREEN + getName() + " " + getLevel(p));
		active.put(p, System.currentTimeMillis());
		playerVelocity.put(p, p.getEyeLocation());
		bats.put(p, new ArrayList<Bat>());

		for(int i = 0; i < (5 + getLevel(p)); i++){
			bats.get(p).add(p.getWorld().spawn(p.getEyeLocation(), Bat.class));
		}

	}

	public boolean hitPlayer(Location loc, LivingEntity player) {
		if (loc.add(0, -loc.getY(), 0).toVector().subtract(player.getLocation().add(0, -player.getLocation().getY(), 0).toVector()).length() < 0.8D) {
			return true;
		}
		if (loc.add(0, -loc.getY(), 0).toVector().subtract(player.getLocation().add(0, -player.getLocation().getY(), 0).toVector()).length() < 1.2) {
			return (loc.getY() > player.getLocation().getY()) && (loc.getY() < player.getEyeLocation().getY());
		}
		return false;
	}
	
	private WeakHashMap<Player, Long> batCD = new WeakHashMap<>();

	@EventHandler
	public void batHit(UpdateEvent e){
		if(e.getType() == UpdateType.TICK){
			for(Player p : active.keySet()){
				for(Bat bat : bats.get(p)){
					Vector rand = new Vector((Math.random() - 0.5D) / 3.0D, (Math.random() - 0.5D) / 3.0D, (Math.random() - 0.5D) / 3.0D);
					bat.setVelocity(playerVelocity.get(p).getDirection().clone().multiply(0.5D).add(rand));

					for (LivingEntity other : p.getWorld().getLivingEntities()){
						if(other instanceof Bat) continue;
						if (hitPlayer(bat.getLocation(), other)) {
							
							
							LogManager.addLog(other, p, "Repel");
							if(other instanceof Player){
								
								if(other.equals(p)) continue;
								if(batCD.containsKey(other)){
									if(UtilTime.elapsed(batCD.get(other), 500)){
										batCD.remove(other);
									}else{
										continue;
									}
									
								}
								if(ClanUtilities.canHurt(p, (Player) other)){
									
									batCD.put((Player) other, System.currentTimeMillis());
								}else{
									continue;
								}
							}
							
							


							Vector v = bat.getLocation().getDirection();
							v.normalize();
							v.multiply(.4d);
							v.setY(v.getY() + 0.2d);

							if (v.getY() > 7.5)
								v.setY(7.5);

							if (other.isOnGround())
								v.setY(v.getY() + 0.4d);

							other.setFallDistance(0);
							Bukkit.getPluginManager().callEvent(new CustomDamageEvent(other, p, null, DamageCause.CUSTOM, 2, false));
							other.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 0));
							if(other instanceof Player){
								EffectManager.addEffect((Player) other, EffectType.SHOCK, 1000L);
							}
							
							other.setVelocity(bat.getLocation().getDirection().add(new Vector(0, .4F, 0)));


							bat.getWorld().playSound(bat.getLocation(), Sound.BAT_HURT, 0.3F, 0.7F);

							bat.remove();

						}
					}

				}
			}
		}
	}



	@EventHandler
	public void onUpdate(UpdateEvent e){
		if(e.getType() == UpdateType.FAST){
			Iterator<Entry<Player, Long>> iterator = active.entrySet().iterator();
			while(iterator.hasNext()){
				Entry<Player, Long> next = iterator.next();
				if(UtilTime.elapsed(next.getValue(), 1500)){
					if(bats.containsKey(next.getKey())){
						for(Bat b : bats.get(next.getKey())){
							b.remove();
						}

						bats.remove(next.getValue());
					}

					iterator.remove();
				}
			}
		}
	}

	@Override
	public boolean usageCheck(Player p) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean requiresShield() {
		// TODO Auto-generated method stub
		return false;
	}

}
