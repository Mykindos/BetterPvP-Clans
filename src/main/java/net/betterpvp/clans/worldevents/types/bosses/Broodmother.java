package net.betterpvp.clans.worldevents.types.bosses;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.clans.client.PlayerStat;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.core.framework.BlockRestoreData;
import net.betterpvp.core.utility.*;
import net.betterpvp.clans.worldevents.WEType;
import net.betterpvp.clans.worldevents.types.Boss;
import net.betterpvp.clans.worldevents.types.WorldEventMinion;
import net.betterpvp.clans.worldevents.types.bosses.ads.Broodling;
import net.betterpvp.clans.worldevents.types.nms.BossCaveSpider;
import net.betterpvp.clans.worldevents.types.nms.BossSpider;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Iterator;

public class Broodmother extends Boss{

	private Spider spider;
	private Location[] locs;

	public Broodmother(Clans i) {
		super(i, "Broodmother", WEType.BOSS);
		World w = Bukkit.getWorld("bossworld2");
		locs = new Location[] { 
				new Location(w, 702.5, 135, 192.5),
				new Location(w, 699.5, 135, 123.5),
				new Location(w, 615.5, 135, 126.5),
				new Location(w, 618.5, 135, 199.5),
				new Location(w, 655.5, 135, 199.5)
				};
				
		
	}
	


	@Override
	public double getBaseDamage() {
		// TODO Auto-generated method stub
		return 5;
	}
	
	@Override
	public String getDisplayName() {
		
		return ChatColor.RED.toString() + ChatColor.BOLD + "Broodmother";
	}


	@Override
	public String getBossName() {
		// TODO Auto-generated method stub
		return ChatColor.RED.toString() + ChatColor.BOLD + "Broodmother";
	}

	@Override
	public EntityType getEntityType() {
		// TODO Auto-generated method stub
		return EntityType.SPIDER;
	}

	@Override
	public double getMaxHealth() {
		// TODO Auto-generated method stub
		return 1500;
	}

	@Override
	public LivingEntity getBoss() {
		// TODO Auto-generated method stub
		return spider;
	}


	@Override
	public void spawn() {
		
		spider = null;
		if(getBoss() == null){
			
			if(!getSpawn().getChunk().isLoaded()) {
				getSpawn().getChunk().load();
			}
			BossSpider bs = new BossSpider(((CraftWorld) Bukkit.getWorld("bossworld2")).getHandle());
			spider = bs.spawnSpider(getSpawn());

			spider.setMaxHealth(getMaxHealth());
			spider.setHealth(getMaxHealth());
			spider.setCustomName(getBossName());
			spider.setCustomNameVisible(true);
			spider.setRemoveWhenFarAway(false);
			spider.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));

		}

	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void bonusDamage(CustomDamageEvent e){
		if(isActive()){
			if(e.getDamagee() == null || e.getDamager() == null) return;
			if(e.getDamager() != null){
				if(e.getDamager() instanceof Player){
					Player p = (Player) e.getDamager();
					
					if(e.getDamagee() == getBoss() || isMinion(e.getDamagee())){
						PlayerStat stat = ClientUtilities.getOnlineClient(p).getStats();
						int kills = stat.getBroodmotherKills();
						double modifier = kills * 2;
						double modifier2 = modifier >= 10 ? 0.01 : 0.1;
						
						e.setDamage(e.getDamage() * (1 + (modifier * modifier2)));
					}
				}
			}
		}
	}


	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		if(isActive()){
			if (getBoss() != null && e.getEntity() == getBoss()) {

				Iterator<WorldEventMinion> it = getMinions().iterator();
				while(it.hasNext()){
					Broodling cs = (Broodling) it.next();
					cs.getEntity().remove();
					it.remove();
				}



				announceDeath(e);
				spider = null;

			} else if (e.getEntity() instanceof CaveSpider) {
				Iterator<WorldEventMinion> it = getMinions().iterator();
				while(it.hasNext()){
					Broodling broodling = (Broodling) it.next();
					if(broodling.getSpider().equals(e.getEntity())){
						it.remove();
					}
				}

			}
		}
	}

	@EventHandler
	public void onDamage(CustomDamageEvent e) {
		if(isActive()){
			if(getBoss() != null && !getBoss().isDead()){
				if (e.getDamager() == getBoss()) {
					e.setDamage(getBaseDamage());

				} else if (e.getDamagee() == getBoss()) {
					if (e.getDamager() instanceof Player) {
						Player p = (Player) e.getDamager();
						if(EffectManager.hasProtection(p)) {
							e.setCancelled("PVP Protection");
							UtilMessage.message(p, "World Event", "You must disable your protection to damage bosses. (/protection)");
							return;
						}
						spider.setTarget(e.getDamager());
						for(WorldEventMinion b : getMinions()){
							Broodling bb = (Broodling) b;
							bb.getSpider().setTarget(e.getDamager());
						}

					}
				}
			}
		}
	}




	@EventHandler
	public void onEnvironmentDamage(CustomDamageEvent e) {
		if(isActive()){
			if (e.getDamagee() == getBoss() || isMinion(e.getDamagee())) {
				if (e.getCause() == DamageCause.FALL) {
					e.setCancelled("Broodmother - Immune to Fall");
				} else if (e.getCause() == DamageCause.SUFFOCATION) {
					e.setCancelled("Broodmother - Immune to Suffocation");
				}
			}
		}
	}

	@EventHandler
	public void broodDamage(CustomDamageEvent e){
		if(isActive()){
			if(getBoss() != null){
				if(e.getDamagee() == getBoss()){
					getBoss().setCustomName(getBossName() + "  " + ChatColor.GREEN + (int) getBoss().getHealth() 
							+ ChatColor.YELLOW + "/" + ChatColor.GREEN + (int) getBoss().getMaxHealth());
				}
				if(e.getDamager() == getBoss()){

					LogManager.addLog(e.getDamagee(),
							e.getDamager(), 
							getBossName(), "");
				}
			}
		}
	}

	@EventHandler
	public void trap(final CustomDamageEvent e){
		if(isActive()){
			if(e.getDamagee() == getBoss()){
				if(e.getDamager() instanceof Player){
					int number = UtilMath.randomInt(150);
					if(number > 50 && number < 55){
						for(Location loc : UtilMath.sphere(e.getDamagee().getLocation(), 6, true)){
							if(loc.getBlock().getType() == Material.AIR){
								new BlockRestoreData(loc.getBlock(), 30, (byte) 0, 10000L);
								loc.getBlock().setType(Material.WEB);
							}
						}
						spawnSpiders(e.getDamager());
					}

				}
			}else if(e.getDamager() == getBoss() || isMinion(e.getDamager())){
				if(e.getDamagee().getLocation().getBlock().getType() == Material.WEB){
					e.setDamage(e.getDamage() * 3);
				}
			}
		}
	}
	
	@Override
	public Location getSpawn() {
		return new Location(Bukkit.getWorld("bossworld2"), 660.5, 135, 162.5);
	}

	@EventHandler
	public void summonBrood(final CustomDamageEvent e) {
		if(isActive()){
			if (e.getDamagee() == getBoss()) {
				if (e.getDamager() instanceof Player) {
					if (getBoss().getHealth() < getMaxHealth() / 2) {
						if (UtilMath.randomInt(100) > 95) {
							for (Player p : UtilPlayer.getInRadius(getBoss().getLocation(), 50)) {
								UtilMessage.message(p, "Broodmother", "The broodmother has released her broodlings!");
								p.playSound(p.getLocation(), Sound.ZOMBIE_INFECT, 1F, 1F);
							}

							spawnSpiders(e.getDamager());
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void broodlingLeap(UpdateEvent e){
		if(e.getType() == UpdateType.FAST){
			if(isActive()){
				for(WorldEventMinion b : getMinions()){
					if(b instanceof Broodling){
						Broodling broodling = (Broodling) b;
						if(UtilTime.elapsed(broodling.getSystemTime(), 5000)){
							broodling.setSystemTime(System.currentTimeMillis());
							if(broodling.getSpider().getTarget() == null) continue;
							LivingEntity target = broodling.getSpider().getTarget();
							if(target instanceof Player){
								if (UtilBlock.isGrounded(broodling.getSpider()) &&  
										UtilMath.offset(broodling.getSpider().getLocation(), target.getEyeLocation()) > 2) {
									Vector vec = UtilVelocity.getTrajectory(broodling.getSpider().getLocation(), target.getEyeLocation());
									UtilVelocity.velocity(broodling.getSpider(), vec, 2, false, 0, 0.5, 0.3, true);
								}
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void checkBoss(UpdateEvent e){
		if(isActive()){
			if(e.getType() == UpdateType.MIN_01){
				if(getBoss() == null || getBoss().isDead()){
					setActive(false);
				}
			}
		}
	}

	@EventHandler
	public void inWater(UpdateEvent e){
		if(e.getType() == UpdateType.SEC){
			if(isActive()){
				if(getBoss() != null){
					if(getBoss().getLocation().getBlock().isLiquid()){
						heal(5);

					}
				}
			}
		}
	}

	public void spawnSpiders(final LivingEntity target){
		for (int z = 0; z < 15; z++) {
			new BukkitRunnable() {
				@Override
				public void run() {
					if (getBoss() != null) {
						BossCaveSpider bcs = new BossCaveSpider(((CraftWorld) getBoss().getWorld()).getHandle());
						CaveSpider broodling = (CaveSpider) bcs.spawnSpider(getBoss()
								.getLocation().add(UtilMath.randomInt(2), 0, UtilMath.randomInt(2)));


						broodling.setTarget(target);
						getMinions().add(new Broodling(broodling, System.currentTimeMillis()));

					}

				}
			}.runTaskLater(getInstance(), z * 10);
		}
	}


	@EventHandler
	public void fang(CustomDamageEvent e) {
		if(isActive()){
			if (e.getDamagee() == getBoss()) {
				LivingEntity ent = (LivingEntity) e.getDamagee();
				if (UtilMath.randomInt(100) > 95) {

					if (e.getDamager() instanceof Player) {
						Player p = (Player) e.getDamager();
						LogManager.addLog(p, ent, getBossName(), "Poisonous Fang");
						Bukkit.getPluginManager().callEvent(new CustomDamageEvent(p, ent, null, DamageCause.ENTITY_ATTACK, 4, false));

						p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 300, 0));
						p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 300, 0));
						UtilMessage.message(p, "Broodmother", "You have been bitten by the broodmother!");
					}
				}
			}
		}
	}
	
	@Override
	public Location[] getTeleportLocations() {
		return locs;
	}


}
