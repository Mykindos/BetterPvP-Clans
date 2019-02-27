package net.betterpvp.clans.effects;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class EffectManager extends BPVPListener<Clans> {

	private static List<Effect> effects = new ArrayList<>();

	public EffectManager(Clans i){
		super(i);
	}

	public static List<Effect> getEffects(){
		return effects;
	}

	public static void addEffect(Player p, EffectType type, long length){
		addEffect(p, type, 1, length);
	}

	public static void addEffect(Player p, EffectType type, int level,  long length){
		if(hasEffect(p, type)){
			removeEffect(p, type);
		}
		if(type == EffectType.STRENGTH){
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (int) ((length / 1000) * 20), level -1));
		}
		if(type == EffectType.SILENCE){
			p.getWorld().playSound(p.getLocation(), Sound.ZOMBIE_REMEDY, 1F, 1.5F);
		}
		
		if(type == EffectType.VULNERABILITY) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, (int) ((length / 1000) * 20), 0));
		}
		getEffects().add(new Effect(p.getUniqueId(), type, level, System.currentTimeMillis() + length));
	}

	public static Effect getEffect(Player p, EffectType type){
		return effects.stream().filter(e -> e.getPlayer().equals(p.getUniqueId())
				&& e.getType() == type).findAny().orElse(null);

	}


	public static boolean hasEffect(Player p, EffectType type){
		return getEffect(p, type) != null;
	}

	public static void removeEffect(Player p, EffectType type){
		effects.removeIf(e -> e.getPlayer().equals(p.getUniqueId()) && e.getType() == type);

	}



	public static double getShardBoost(Player p){
		double d = 1.0;
		for(Effect e : effects){
			if(e.getPlayer().equals(p.getUniqueId())){
				if(e.getType() == EffectType.SHARD_50){
					d = 1.5;
				}

				if(e.getType() == EffectType.SHARD_100){
					d = 2.0;
				}
			}

		}

		return d;
	}



	public static boolean hasShardBoost(Effect type){
		return type.getType() == EffectType.SHARD_50 || type.getType() == EffectType.SHARD_100;
	}

	public static boolean hasProtection(Player p){
		return hasEffect(p, EffectType.PROTECTION);
	}

	public static void clearEffect(Player p){
		ListIterator<Effect> iterator = effects.listIterator();
		while(iterator.hasNext()){
			Effect next = iterator.next();
			if(next.getPlayer() == p.getUniqueId() && !hasShardBoost(next)){
				iterator.remove();
			}
		}
	}


	@EventHandler
	public void onFall(EntityDamageEvent e){
		if(e.getCause() == DamageCause.FALL){
			if(e.getEntity() instanceof Player){
				if(hasEffect((Player) e.getEntity(), EffectType.NOFALL)){
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		clearEffect(e.getEntity());
	}

	@EventHandler
	public void showTag(UpdateEvent e){
		if(e.getType() == UpdateEvent.UpdateType.SEC){
			for(final Player p : Bukkit.getOnlinePlayers()){

				Gamer g = GamerManager.getOnlineGamer(p);
				if(g != null){

						if(!UtilTime.elapsed(g.getLastDamaged(), 15000)){
							if(EffectManager.hasEffect(p, EffectType.INVISIBILITY)) continue;
							ParticleEffect.VILLAGER_HAPPY.display(0.1F, 0, 0.1F, (float) 10, 1, p.getLocation().add(0, 4, 0), 30);
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void shockUpdate(UpdateEvent e){
		if(e.getType() == UpdateEvent.UpdateType.TICK){
			for(Effect ef : getEffects()){
				if(ef.getType() == EffectType.SHOCK){
					if(Bukkit.getPlayer(ef.getPlayer()) != null){
						Player p = Bukkit.getPlayer(ef.getPlayer());
						p.playEffect(EntityEffect.HURT);
					}
				}
			}
		}
	}


	@EventHandler
	public void onUpdate(UpdateEvent e){
		if(e.getType() == UpdateEvent.UpdateType.TICK){
			ListIterator<Effect> iterator = effects.listIterator();
			while(iterator.hasNext()){
				Effect next = iterator.next();
				if(next.hasExpired()){
					if(next.getType() == EffectType.VULNERABILITY){
						if(Bukkit.getPlayer(next.getPlayer()) != null){
							UtilMessage.message(Bukkit.getPlayer(next.getPlayer()), "Condition", "Your vulnerability has worn off!");
						}
					}
					iterator.remove();
				}
			}
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e){
		if(e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ() 
				|| e.getFrom().getY() != e.getTo().getY()){
			if(hasEffect(e.getPlayer(), EffectType.STUN)){
				e.setCancelled(true);
			}
		}
	}


	@EventHandler
	public void entityDamageEvent(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player) e.getEntity();
			if(hasProtection(p)){
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void entDamage(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
			Player dam = (Player) e.getDamager();
			Player p = (Player) e.getEntity();
			if(hasEffect(p, EffectType.PROTECTION)){
				UtilMessage.message(dam, "Protected", "This is a new player and has protection!");
				e.setCancelled(true);
			}

			if(hasEffect(dam, EffectType.PROTECTION)){
				UtilMessage.message(dam, "Protected", "You cannot damage other players while you have protection!");
				UtilMessage.message(dam, "Protected", "Type '/protection' to disable this permanentely.");
				e.setCancelled(true);
			}
		}
	}

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onStrengthDamage(CustomDamageEvent e){
		if(e.getCause() != DamageCause.ENTITY_ATTACK) return;
		if(e.getDamager() instanceof Player){
			Player p = (Player) e.getDamager();
			Effect effect = getEffect(p, EffectType.STRENGTH);
			if(effect != null){
				e.setDamage(e.getDamage() + (1.5 * effect.getLevel()));
			}
		}
	}

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onDamage(CustomDamageEvent e){
		if(e.getDamagee() instanceof Player){
			Player p = (Player) e.getDamagee();
			Effect effect = getEffect(p, EffectType.VULNERABILITY);
			if(effect != null){
				if(e.getCause() == DamageCause.LIGHTNING) return;
				e.setDamage((e.getDamage() * (1.0 + (effect.getLevel() * 0.25))));

			}
		}
	}

	@EventHandler (priority = EventPriority.HIGHEST)
	public void resistanceReduction(CustomDamageEvent e){
		if(e.getDamagee() instanceof Player){

			Player p = (Player) e.getDamagee();
			Effect effect = getEffect(p, EffectType.RESISTANCE);
			if(effect != null){
				e.setDamage(e.getDamage() * (1.0 - (effect.getLevel() * 20) * 0.01));

			}
		}
	}



}
