package net.betterpvp.clans.skills.selector.skills.ranger;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.particles.ParticleEffect;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilMath;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Iterator;

public class Longshot extends Skill{

	private Clans i;

	public Longshot(Clans i) {
		super(i, "Longshot", "Ranger",
				noMaterials, noActions,5, true, false);
		this.i = i;
	}

	@Override
	public String[] getDescription(int level) {
		// TODO Auto-generated method stub
		return new String[] {
				"Shoot an arrow that gains additional", 
				"damage the further the target hit is",
				"Caps out at " + ChatColor.GREEN + (13 + level) + ChatColor.GRAY + " damage",
		"Cannot be used in own territory"};
	}

	private static HashMap<Arrow, Location> arrows = new HashMap<>();

	@EventHandler
	public void update(UpdateEvent e){
		if(e.getType() == UpdateType.TICK){
			Iterator<Arrow> it = arrows.keySet().iterator();
			while(it.hasNext()){
				Arrow next = it.next();
				if(next == null){
					it.remove();
				}else if(next.isDead()){
					it.remove();
				}else{
					ParticleEffect.FIREWORKS_SPARK.display(new Vector(0, 0.0, 0), 15, next.getLocation().add(new Vector(0,0.25, 0)), 20);
				}
			}
		}
	}

	@EventHandler
	public void onShoot(EntityShootBowEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player) e.getEntity();
			if(ClanUtilities.canCast(p)){
				Role r = Role.getRole(p);
				if (r != null && r.getName().equals(getClassType())) {

						if(hasSkill(p, this)){
							if(e.getProjectile() instanceof Arrow){
								Projectile j = (Projectile) e.getProjectile();
								if(j.getShooter() instanceof Player){
									Player shooter = (Player) j.getShooter();
									Clan c = ClanUtilities.getClan(shooter);
									Clan d = ClanUtilities.getClan(shooter.getLocation());
									if(c == null || c != d && !c.isAllied(d)){
										Arrow a = (Arrow) e.getProjectile();
										arrows.put(a, e.getProjectile().getLocation());
									}
								}

							}

						}


				}
			}
		}

	}

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onArrowHit(final ProjectileHitEvent e){
		new BukkitRunnable(){

			@Override
			public void run() {
				if(e.getEntity() instanceof Arrow){
					Arrow a = (Arrow) e.getEntity();
					arrows.remove(a);
				}

			}


		}.runTaskLater(i, 2L);

	}


	@EventHandler (priority = EventPriority.HIGHEST)
	public void onDamage(CustomDamageEvent e){
		if(e.getProjectile() != null){

			if(e.getProjectile() instanceof Arrow){
				if(e.getDamager() instanceof Player){
					Player shooter = (Player) e.getDamager();

					Arrow arrow = (Arrow) e.getProjectile();
					if(arrow.getShooter() instanceof Player){
						if(arrows.containsKey(arrow)){
							Location loc = arrows.get(arrow);
							double length = UtilMath.offset(loc, e.getDamagee().getLocation());

							double damage = Math.min((16 + getLevel((Player) arrow.getShooter())), length / 3.0 - 4);


							e.setDamage(e.getDamage() + (damage));
							arrows.remove(arrow);

							if(e.getDamagee() instanceof Player){

								LogManager.addLog( e.getDamagee(), shooter, "Longshot");

							}
						}

					}
				}
			}
		}
	}

	@Override
	public void activateSkill(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean usageCheck(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	public static HashMap<Arrow, Location> getArrows(){
		return arrows;
	}



	@Override
	public Types getType() {
		// TODO Auto-generated method stub
		return Types.PASSIVE_A;
	}

	@Override
	public double getRecharge(int level) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getEnergy(int level) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean requiresShield() {
		// TODO Auto-generated method stub
		return false;
	}

}
