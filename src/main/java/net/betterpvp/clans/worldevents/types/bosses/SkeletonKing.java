package net.betterpvp.clans.worldevents.types.bosses;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.client.ClientUtilities;
import net.betterpvp.clans.client.PlayerStat;
import net.betterpvp.clans.combat.throwables.ThrowableManager;
import net.betterpvp.clans.combat.throwables.events.ThrowableCollideEntityEvent;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.clans.gamer.combat.LogManager;
import net.betterpvp.core.utility.*;
import net.betterpvp.clans.worldevents.WEType;
import net.betterpvp.clans.worldevents.types.Boss;
import net.betterpvp.clans.worldevents.types.WorldEventMinion;
import net.betterpvp.clans.worldevents.types.bosses.ads.SkeletonMinion;
import net.betterpvp.clans.worldevents.types.nms.BossSkeleton;
import net.betterpvp.mah.utility.UtilTime;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.Map.Entry;

public class SkeletonKing extends Boss{

	private Skeleton skeleton;
	private WeakHashMap<Item, LivingEntity> targets = new WeakHashMap<>();
	private boolean boneShield;
	private List<Item> items = new ArrayList<>();
	private Location[] locs;
	
	private boolean stage1, stage2, stage3;
	public SkeletonKing(Clans i){
		super(i, "SkeletonKing", WEType.BOSS);

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
	public String getDisplayName() {

		return ChatColor.RED.toString() + ChatColor.BOLD + "Skeleton King";
	}


	@Override
	public Location getSpawn() {
		return new Location(Bukkit.getWorld("bossworld2"), 660.5, 135, 162.5);
	}
	@Override
	public double getBaseDamage() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public String getBossName() {
		// TODO Auto-generated method stub
		return ChatColor.RED.toString() + ChatColor.BOLD + "Skeleton King";
	}

	@Override
	public EntityType getEntityType() {
		// TODO Auto-generated method stub
		return EntityType.SKELETON;
	}

	@Override
	public double getMaxHealth() {
		// TODO Auto-generated method stub
		return 2000;
	}

	@Override
	public LivingEntity getBoss() {
		// TODO Auto-generated method stub
		return skeleton;
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

	@Override
	public void spawn() {

		skeleton = null;
		if(getBoss() == null){
			if(!getSpawn().getChunk().isLoaded()) {
				getSpawn().getChunk().load();
			}
			Location loc = getSpawn();
			BossSkeleton bs = new BossSkeleton(((CraftWorld) loc.getWorld()).getHandle());
			skeleton = bs.spawn(loc);


			skeleton.setSkeletonType(SkeletonType.WITHER);
			skeleton.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
			skeleton.setMaxHealth(getMaxHealth());
			skeleton.setHealth(getMaxHealth());
			skeleton.setCustomName(getBossName());
			skeleton.setCustomNameVisible(true);
			skeleton.setRemoveWhenFarAway(false);
			skeleton.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
			stage1 = false;
			stage2 = false;
			stage3 = false;

		}

	}

	@EventHandler
	public void onLaunchedBones(UpdateEvent e) {
		if(e.getType() == UpdateType.TICK) {
			if(targets.isEmpty()) return;
			for(Entry<Item, LivingEntity> entry : targets.entrySet()) {
				UtilVelocity.velocity(entry.getKey(), UtilVelocity.getTrajectory(entry.getKey(), entry.getValue()), 0.6, false, 0, 0.2, 1, true);
			}
		}
	}

	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		if(isActive()){
			if (e.getEntity() == getBoss()) {

				announceDeath(e);
				skeleton = null;
				Iterator<WorldEventMinion> it = getMinions().iterator();
				while(it.hasNext()) {
					WorldEventMinion next = it.next();
					if(next.getEntity() != null) {
						next.getEntity().remove();
					}
					
					it.remove();
				}
			}
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

						if(EffectManager.hasProtection(p)) {
							e.setCancelled("PVP Protection");
							UtilMessage.message(p, "World Event", "You must disable your protection to damage bosses. (/protection)");
							return;
						}
						PlayerStat stat = ClientUtilities.getOnlineClient(p).getStats();
						int kills = stat.getSkeletonKingKills();
						double modifier = kills * 2;
						double modifier2 = modifier >= 10 ? 0.01 : 0.1;

						e.setDamage(e.getDamage() * (1 + (modifier * modifier2)));
					}
				}
			}
		}
	}

	@EventHandler
	public void onCollide(ThrowableCollideEntityEvent e) {
		if(e.getThrowable().getSkillName().equalsIgnoreCase("SkeletonKingBone")) {
			e.getThrowable().getItem().remove();
			UtilVelocity.velocity(e.getCollision(), UtilVelocity.getTrajectory(e.getThrowable().getThrower(),  e.getCollision()), 3.0, false, 1, 3, 1, false);
		}
	}

	@EventHandler
	public void entDamage(CustomDamageEvent e){
		if(isActive()){
			if(e.getDamagee() == null || e.getDamager() == null) return;
			if(e.getDamagee() instanceof Skeleton){
				if(e.getDamagee() == getBoss()){
					if(!getMinions().isEmpty()) {
						
						e.setCancelled("Must kill minions");
						return;
					}
					
					skeleton.setCustomName(getBossName() + "  " +  ChatColor.GREEN + (int) skeleton.getHealth() + ChatColor.YELLOW + "/" + ChatColor.GREEN + (int) skeleton.getMaxHealth()); 
					skeleton.setTarget(e.getDamager());

					if(boneShield) {
						if(items.size() > 0) {
							if(e.getCause() == DamageCause.ENTITY_ATTACK) {
								CustomDamageEvent ev = new CustomDamageEvent(e.getDamager(), e.getDamagee(), null, DamageCause.ENTITY_ATTACK, (e.getDamage() / 2.5), false);
								items.get(0).remove();
								items.remove(0);
								Bukkit.getPluginManager().callEvent(ev);
							}else if(e.getCause() == DamageCause.PROJECTILE){
								items.get(0).teleport(e.getDamagee().getEyeLocation());
								ThrowableManager.addThrowable(items.get(0), e.getDamagee(), "SkeletonKingBone", 5000);
								targets.put(items.get(0), e.getDamager());
								items.remove(0);
							}


							if(items.isEmpty()) {
								if(UtilTime.elapsed(shieldDeactivateCooldown, 2000)) {
									boneShield = false;
								}
							}
						}else {
							if(UtilTime.elapsed(shieldDeactivateCooldown, 2000)) {
								boneShield = false;
							}
						}
					}

				}else if(isMinion(e.getDamagee())){
					SkeletonMinion skele = (SkeletonMinion) getMinion(e.getDamagee());

					skele.getEntity().setCustomName(skele.getDisplayName() + "  " +  ChatColor.GREEN + (int) skele.getEntity().getHealth() + ChatColor.YELLOW + "/" + ChatColor.GREEN + (int)  skele.getEntity().getMaxHealth()); 


				}
			}
			if(e.getDamagee() instanceof Player){
				if(e.getDamager() instanceof Skeleton){
					if(getBoss() == e.getDamager()){
						if(!getMinions().isEmpty()) {
							e.setCancelled("Minions available");
							return;
						}
						LogManager.addLog(e.getDamagee(), e.getDamager(), getBossName(), "");
					}else if(isMinion(e.getDamager())){
						SkeletonMinion skele = (SkeletonMinion) getMinion(e.getDamager());

						LogManager.addLog(e.getDamagee(), 
								e.getDamager(), 
								skele.getDisplayName(), "");

					}

				}
			}
		}
	}
	
	@EventHandler
	public void handleMinions(UpdateEvent e) {
		if(isActive()) {
			if(e.getType() == UpdateType.SEC) {
				if(!getMinions().isEmpty()) {
					if(skeleton != null && !skeleton.isDead()) {
						skeleton.setTarget(null);
					}
					
					Iterator<WorldEventMinion> it = getMinions().iterator();
					while(it.hasNext()) {
						WorldEventMinion next = it.next();
						if(next.getEntity() == null) {
							it.remove();
						}else if(next.getEntity().isDead()) {
							it.remove();
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onStageCheck(CustomDamageEvent e) {
		if(isActive()){
			if(e.getDamagee() == null || e.getDamager() == null) return;
			if(e.getDamagee() instanceof Skeleton){
				if(e.getDamagee() == getBoss()){
					if(e.getDamagee().getHealth() < 500) {
						if(!stage3) {
							spawnClones(4);
							stage3 = true;
						}
					} else if(e.getDamagee().getHealth() < 1000) {
						if(!stage2) {
							spawnClones(3);
							stage2 = true;
						}
					}else if(e.getDamagee().getHealth() < 1500) {
						if(!stage1) {
							
							spawnClones(2);
							stage1 = true;
						}
					}
				
					
				}
			}
		}
	}
	
	
	private void spawnClones(int amount) {
		for(int i = 0; i < amount; i++) {
			Skeleton s = (Skeleton) getBoss().getWorld().spawnEntity(getBoss().getLocation(), EntityType.SKELETON);
			SkeletonMinion sm = new SkeletonMinion(s);
			s.setTarget(skeleton.getTarget());
			getMinions().add(sm);
		}
	}

	@EventHandler
	public void kingCombust(EntityCombustEvent e){
		if(isActive()){
			if(e.getEntity() instanceof LivingEntity){
				LivingEntity ent = (LivingEntity) e.getEntity();
				if(isMinion(ent) || ent == getBoss()){
					e.setCancelled(true);
				}
			}
		}
	}


	@EventHandler
	public void onDamage(CustomDamageEvent e){
		if(isActive()){
			if(getBoss() != null && !getBoss().isDead()){
				if(e.getDamager() == getBoss() || isMinion(e.getDamager())){
					e.setDamage(getBaseDamage());

				}
			}
		}
	}
	
	
	
	@EventHandler
	public void onKingEvade(CustomDamageEvent e) {
		if(isActive()){
			if(getBoss() != null && !getBoss().isDead()){
				if(e.getDamagee() == getBoss() || isMinion(e.getDamagee())){
					e.setKnockback(false);
					if(UtilMath.randDouble(0, 100) > 95) {
						Location loc = UtilLocation.findLocationBehind(e.getDamager(), e.getDamagee());
						if(loc == null) return;
						if(!UtilBlock.airFoliage(loc.getBlock()) || !UtilBlock.airFoliage(loc.add(0, 1, 0).getBlock())) return;
						e.getDamagee().teleport(loc);
						Skeleton s = (Skeleton) e.getDamagee();
						s.setTarget(e.getDamager());
						e.getDamagee().getWorld().playSound(getBoss().getLocation(), Sound.ENDERMAN_TELEPORT, 0.5F, 1F);
					}
				}
			}
		}
	}

	private long shieldDeactivateCooldown;

	@EventHandler
	public void onActivateShield(CustomDamageEvent e) {
		if(isActive()) {
			if(getBoss() != null && !getBoss().isDead()){
				if(e.getDamagee() == getBoss()) {
					if(!boneShield) {
						if(UtilMath.randomInt(100) > 97) {
							boneShield = true;
							shieldDeactivateCooldown = System.currentTimeMillis();
							for(int i = 0; i < 50; i++) {
								new BukkitRunnable() {
									@Override
									public void run() {
										Item item = getBoss().getWorld().dropItem(getBoss().getLocation(), new ItemStack(Material.BONE));
										item.setPickupDelay(Integer.MAX_VALUE);
										items.add(item);
									}
								}.runTaskLater(getInstance(), i * 5);
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void updateShield(UpdateEvent e) {
		if(e.getType() == UpdateType.TICK) {
			if(isActive()) {
				if(boneShield) {
					ListIterator<Item> it = items.listIterator();

					double oY = 0.01;
					while(it.hasNext()){
						Item s = it.next();


						double oX = Math.sin(s.getTicksLived()/10) * 2;

						double oZ = Math.cos(s.getTicksLived()/10) * 2;
						//s.teleport(getBoss().getLocation().add(oX, oY, oZ));

						UtilVelocity.velocity(s, UtilVelocity.getTrajectory(s.getLocation(),  getBoss().getLocation().add(oX, oY, oZ)), 0.3, false, 0, 0.1, 1.5, true);
						oY += 0.05;

						if(s.isDead() || s == null) {
							it.remove();

						}
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
