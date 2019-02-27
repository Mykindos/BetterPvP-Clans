package net.betterpvp.clans.skills.selector.skills.ranger;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.gamer.combat.LogManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.events.SkillDequipEvent;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class Volley extends Skill{

	private Set<UUID> volleys = new HashSet<UUID>();
	private Clans i;
	public Volley(Clans i) {
		super(i, "Volley", "Ranger", getBow,
				leftClick, 5,
				true, true);
		this.i = i;
	}

	@Override
	public String[] getDescription(int level) {
		// TODO Auto-generated method stub
		return new String[]{
				"Left click with Bow to Prepare",
				"Your next shot is instant, and shoots",
				"a volley of arrows in the direction you are facing",
				"",
				"Upon hitting a target, the arrow will deal 5 hearts",
				"of damage to a naked player",
				"",
				"Cooldown: " + ChatColor.GREEN +  getRecharge(level),
				"Energy: " + ChatColor.GREEN + getEnergy(level)
		};
	}

	@EventHandler
	public void onDequip(SkillDequipEvent e){
		if(e.getSkill() == this){
			volleys.remove(e.getPlayer().getUniqueId());
		}
	}

	@Override
	public Types getType() {
		// TODO Auto-generated method stub
		return Types.BOW;
	}

	private List<Arrow> arrows = new ArrayList<>();

	@EventHandler
	public void onShoot(final EntityShootBowEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player) e.getEntity();
			if(ClanUtilities.canCast(p)) {
				if(hasSkill(p, this)){
					if(volleys.contains(p.getUniqueId())){
						Location c = p.getLocation();
						new BukkitRunnable(){

							@Override
							public void run() {
								if(e.getProjectile() instanceof Arrow){
									Arrow j = (Arrow) e.getProjectile();
									Longshot.getArrows().remove(j);
									j.remove();
								}
							}


						}.runTaskLater(i, 1);


						Vector v;
						for(int i = 0; i < 10; i+=2){
							Arrow n = p.launchProjectile(Arrow.class);
							n.setShooter(p);
							c.setYaw(c.getYaw() + i);
							v = c.getDirection();
							n.setVelocity(v.multiply(2));
							arrows.add(n);
						}
						c = p.getLocation();
						for(int i = 0; i < 10; i+=2){
							Arrow n = p.launchProjectile(Arrow.class);
							n.setShooter(p);
							c.setYaw(c.getYaw() - i);
							v = c.getDirection();
							n.setVelocity(v.multiply(2));
							arrows.add(n);
						}

						p.getWorld().playSound(p.getLocation(), Sound.PISTON_EXTEND, 3F, 1F);
						volleys.remove(p.getUniqueId());
					}
				}
			}
		}
	}

	@EventHandler
	public void onHit(CustomDamageEvent e){

		if(e.getProjectile() instanceof Arrow){
			Arrow a = (Arrow) e.getProjectile();
			LivingEntity p =  e.getDamagee();

			if(arrows.contains(a)){
				if(a.getShooter() instanceof LivingEntity){
					e.setDamage(8);
					LogManager.addLog(p, (LivingEntity) a.getShooter(), "Volley");
				}
			}
		}

	}

	@Override
	public void activateSkill(Player player) {
		volleys.remove(player.getUniqueId());
		volleys.add(player.getUniqueId());
		UtilMessage.message(player, getClassType(), "You prepared " + getName());

	}

	@Override
	public boolean usageCheck(Player player) {
		if (player.getLocation().getBlock().getType() == Material.WATER || player.getLocation().getBlock().getType() == Material.STATIONARY_WATER) {
			UtilMessage.message(player, "Skill", "You cannot use " + ChatColor.GREEN + getName() + " in water.");
			return false;
		}
		return true;
	}

	@Override
	public double getRecharge(int level) {
		// TODO Auto-generated method stub
		return 15 - ((level -1) * 2);
	}

	@Override
	public float getEnergy(int level) {
		// TODO Auto-generated method stub
		return 30 - ((level -1) * 3);
	}

	@Override
	public boolean requiresShield() {
		// TODO Auto-generated method stub
		return false;
	}

}
