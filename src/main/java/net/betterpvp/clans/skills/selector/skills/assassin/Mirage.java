package net.betterpvp.clans.skills.selector.skills.assassin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.classes.events.RegenerateEnergyEvent;
import net.betterpvp.clans.classes.roles.Assassin;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.events.UpdateEvent;
import net.betterpvp.clans.events.UpdateEvent.UpdateType;
import net.betterpvp.clans.module.PlayerJumpEvent;
import net.betterpvp.clans.module.RechargeManager;
import net.betterpvp.clans.scoreboard.ScoreboardManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.utility.*;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.ai.StuckAction;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.entity.EntityHumanNPC;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;



public class Mirage extends Skill{

	public static HashMap<Player, MirageData> active = new HashMap<>();

	public Mirage(Clans i) {
		super(i, "Mirage", "Assassin", getSwords, rightClick, 5, true, true);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getDescription(int level) {

		return new String[] {
				"Block with Sword to Channel.",
				"",
				"Fool your enemies by creating a clone of yourself",
				"that runs towards your target block.",
				"",
				"Max Duration: " + ChatColor.GREEN + (2 + level) + ChatColor.GRAY + " seconds.",
				"Energy: " + ChatColor.GREEN + (getEnergy(level)) + ChatColor.GRAY + ".",
				"Recharge: " + ChatColor.GREEN + (getRecharge(level)) + ChatColor.GRAY + " seconds.",
				"",
				"THIS IS AN EARLY VERSION, IT MAY BE ADJUSTED."
		};
	}

	@Override
	public Types getType() {

		return Types.SWORD;
	}

	@Override
	public double getRecharge(int level) {

		return 30 - (level * 1.5);
	}

	@Override
	public float getEnergy(int level) {

		return 25 - (level * 2);
	}

	@Override
	public boolean requiresShield() {

		return true;
	}

	@Override
	public void activateSkill(Player p) {
		UtilMessage.message(p, getClassType(), "You used " + ChatColor.GREEN + getName() + " " + getLevel(p) + ChatColor.GRAY + ".");
		spawnFakePlayer(p);


		//EntityLiving el = (EntityLiving) npc;
		//PathEntity path = el.getNavi
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, player.getName());

		npc.spawn(new Location(player.getWorld(), 0, -5, 0));



		new BukkitRunnable() {
			@Override
			public void run() {
				npc.despawn();
				npc.destroy();
			}
		}.runTaskLater(getInstance(), 1);
	}



	@EventHandler
	public void onNpcClick(CustomDamageEvent e) {
		
		
		if(e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if(active.containsKey(p)) {
				destroy(active.get(p));
			}
		}
		Iterator<Entry<Player, MirageData>> it = active.entrySet().iterator();
		while(it.hasNext()) {
			Entry<Player, MirageData> next = it.next();
			if(next.getValue().npc.getEntity() == e.getDamagee()) {

				next.getValue().hitsLeft -= 1;



				if(next.getValue().hitsLeft <= 0) {
					destroy(next.getValue());
					it.remove();	
				}

			}
		}

	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player x = e.getEntity();

		Iterator<Entry<Player, MirageData>> it = active.entrySet().iterator();
		while(it.hasNext()) {
			Entry<Player, MirageData> next = it.next();
			if(x == next.getValue().npc.getEntity()) {
				destroy(next.getValue());
				it.remove();
			}
		}
	}

	public void spawnFakePlayer(Player player) {
		if(active.containsKey(player)) return;

		EffectManager.addEffect(player, EffectType.INVISIBILITY, (3000 + (getLevel(player) * 1000)));
		NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, player.getName());
		active.put(player, new MirageData(npc, System.currentTimeMillis(), player.getTargetBlock((Set<Material>) null, 25).getLocation(), player));
		npc.setProtected(false);
		npc.setFlyable(false);
		npc.getNavigator().setPaused(false);
		npc.data().set(NPC.DEFAULT_PROTECTED_METADATA, false);
		npc.data().set(NPC.TARGETABLE_METADATA, true);
		npc.data().set(NPC.FLYABLE_METADATA, false);




		npc.getNavigator().getDefaultParameters().stuckAction(new StuckAction() {
			@Override
			public
			boolean run(NPC npc, Navigator navigator) {


				if(npc.getEntity().getLocation().getY() > npc.getNavigator().getTargetAsLocation().getY()) {
					npc.getNavigator().setPaused(true);

					npc.getEntity().setVelocity(npc.getEntity().getLocation().getDirection().multiply(1).add(new Vector(0, 0.5, 0)));



				}else if(npc.getEntity().getLocation().getY() <= npc.getNavigator().getTargetAsLocation().getY()) {
					if(npc.getNavigator().getTargetAsLocation().getY() - npc.getEntity().getLocation().getY() < 2) {
						npc.getNavigator().setPaused(true);
						Vector vel = UtilVelocity.getTrajectory(npc.getEntity().getLocation(), npc.getNavigator().getTargetAsLocation());
						double mult = vel.length() / 3;
						UtilVelocity.velocity(npc.getEntity(), vel, 
								0.5D + mult, false, 0.4D, 0.8D * mult, 1.5D * mult, true);
					}
				}

				new BukkitRunnable() {
					@Override
					public void run() {
						if(!npc.getNavigator().isPaused() || npc.getEntity() == null) {
							this.cancel();
						}else {
							if(UtilBlock.isGrounded(npc.getEntity())) {
								npc.getNavigator().setPaused(false);
								this.cancel();
							}
						}
					}
				}.runTaskTimer(getInstance(), 20, 10);

				return true;
			}
		});

		Location loc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(),
				player.getLocation().getYaw(), player.getLocation().getPitch());
		npc.spawn(loc);


		Player p = (Player) npc.getEntity();
		p.setHealth(player.getHealth());
		p.setHealth(p.getHealth() + 0);
		ScoreboardManager.addPlayer(p, ClanUtilities.getClan(player));

		CraftPlayer g = (CraftPlayer) p;
		EntityPlayer q = g.getHandle();


		q.invulnerableTicks = 0;


		p.setSprinting(true);
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1));
		p.getEquipment().setArmorContents(player.getEquipment().getArmorContents());
		p.setItemInHand(player.getItemInHand());


		org.bukkit.scoreboard.Scoreboard s = p.getScoreboard();
		if(s.getObjective("health") != null) {
			s.getObjective("health").unregister();
		}
		Objective ob = s.registerNewObjective("health", "health");
		ob.setDisplayName(ChatColor.RED + "❤");
		ob.setDisplaySlot(DisplaySlot.BELOW_NAME);

		for(Player x : Bukkit.getOnlinePlayers()) {
			if(x == player || x == p) continue;

			x.hidePlayer(player);
		}



	}

	@EventHandler
	public void onJump(PlayerJumpEvent e) {
		if(active.containsKey(e.getPlayer())) {
			MirageData md = active.get(e.getPlayer());

			EntityPlayer ep = ((CraftPlayer) md.npc.getEntity()).getHandle();
			EntityHuman eh = ep;
			EntityHumanNPC aNpc = (EntityHumanNPC) eh;
			aNpc.setShouldJump();


			/*
			if(UtilBlock.isGrounded(md.npc.getEntity())) {
				UtilVelocity.velocity(md.npc.getEntity(), e.getPlayer().getLocation().getDirection(), 0.5, true, 1, 0.15, 1, false);

				new BukkitRunnable() {
					@Override
					public void run() {
						md.npc.getEntity().setVelocity(new Vector(0, -0.5, 0));
					}
				}.runTaskLater(getInstance(), 10);
			}
			 */
		}
	}

	@EventHandler
	public void onUpdate(UpdateEvent e) {
		if(e.getType() == UpdateType.FASTEST) {
			Iterator<Entry<Player, MirageData>> it = active.entrySet().iterator();
			while(it.hasNext()) {
				Entry<Player, MirageData> entry = it.next();
				Player p = entry.getKey();
				MirageData d = entry.getValue();

				if(p == null) {
					destroy(d);
					it.remove();
					continue;
				}
				
				Role r = Role.getRole(p);
				if(r == null || !(r instanceof Assassin)) {
					destroy(d);
					it.remove();
					continue;
				}

				if(p.isBlocking()) {
					
					if(!UtilItem.isSword(p.getItemInHand().getType())) {
						destroy(d);
						it.remove();


						continue;
					}
					if(UtilTime.elapsed(d.start, 3000 + (getLevel(p) * 1000))) {

						destroy(d);
						it.remove();


						continue;
					}
					Location newLoc = p.getTargetBlock((Set<Material>) null, 50).getLocation();
					if(newLoc.getBlock().getType() != Material.AIR && newLoc.getBlock().getType() != Material.BARRIER) {


						d.loc = newLoc;



					}

					if(d.npc.getEntity() != null) {
						if(UtilBlock.isGrounded(d.npc.getEntity())) {
							d.npc.getNavigator().setTarget(d.loc);
						}
					}

				}else {

					if(!UtilTime.elapsed(d.start, 250)) continue;

					destroy(d);
					it.remove();
				}
			}
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if(active.containsKey(p)) {
			destroy(active.get(p));
		}
	}
	
	@EventHandler
	public void onInvClose(InventoryCloseEvent e) {
		if(active.containsKey(e.getPlayer())) {
			MirageData d = active.get(e.getPlayer());
			destroy(d);
			
		}
	}

	public void destroy(MirageData d) {
		if(d.p != null) {
			UtilMessage.message(d.p, getClassType(), "Your " + ChatColor.GREEN + getName() + ChatColor.GRAY + " has been destroyed.");
			EffectManager.removeEffect(d.p, EffectType.INVISIBILITY);
			for(Player x : Bukkit.getOnlinePlayers()) {
				if(x == d.p) continue;

				if(!x.canSee(d.p)) {
					x.showPlayer(d.p);
				}
			}


			if(d.npc.getEntity() != null && !d.npc.getEntity().isDead()) {
				for(Player p : UtilPlayer.getInRadius(d.npc.getEntity().getLocation(), 3)) {
					if(ClanUtilities.canHurt(d.p, p)) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 90, 1));
					}
				}
			}

			if(RechargeManager.getInstance().isCooling(d.p.getName(), getName())) {
				RechargeManager.getInstance().removeCooldown(d.p.getName(), getName(), true);
			}

			RechargeManager.getInstance().add(d.p, getName(), getRecharge(getLevel(d.p)), true);
		}

		if(d.npc.getEntity() != null && !d.npc.getEntity().isDead()) {
			d.npc.getEntity().getWorld().playSound(d.npc.getEntity().getLocation(), Sound.PIG_DEATH, 0.5f, 1.5f);
			d.npc.getEntity().getWorld().playEffect(d.npc.getEntity().getLocation(), Effect.LARGE_SMOKE, 1);
			d.npc.getEntity().getWorld().playEffect(d.npc.getEntity().getLocation(), Effect.LARGE_SMOKE, 1);
			d.npc.getEntity().getWorld().playEffect(d.npc.getEntity().getLocation(), Effect.LARGE_SMOKE, 1);
		}

		d.npc.despawn();
		d.npc.destroy();




	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if(active.containsKey(e.getPlayer())) {
			for(Player x : Bukkit.getOnlinePlayers()) {
				if(x == e.getPlayer()) continue;

				if(!x.canSee(e.getPlayer())) {
					x.showPlayer(e.getPlayer());
				}
			}

			MirageData md = active.get(e.getPlayer());

			destroy(md);
			active.remove(e.getPlayer());

		}
	}
	
	@EventHandler
	public void onRegen(RegenerateEnergyEvent e) {
		
		if(active.containsKey(e.getPlayer())) {
			
			e.setEnergy(0.0008D);
		}
	}


	@Override
	public boolean usageCheck(Player p) {

		return true;
	}

	public class MirageData {

		public NPC npc;
		public long start;
		public Player p;
		public Location loc;
		public int hitsLeft;
		public MirageData(NPC npc, long start, Location lastLoc, Player p) {
			this.npc = npc;
			this.start = start;
			this.loc = lastLoc;
			this.p = p;
			this.hitsLeft = 3;
		}
	}

}
