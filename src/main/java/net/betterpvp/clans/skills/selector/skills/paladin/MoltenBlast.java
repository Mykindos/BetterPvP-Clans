package net.betterpvp.clans.skills.selector.skills.paladin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.particles.ParticleEffect;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MoltenBlast extends Skill implements InteractSkill {

    public List<LargeFireball> fireballs = new ArrayList<>();


    public MoltenBlast(Clans i) {
        super(i, "Molten Blast", "Paladin",
                getAxes,
                rightClick, 5, true, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Right click with an Axe to Activate",
                "",
                "Shoot a large fireball that deals",
                "area of effect damage, and igniting any players hit",
                "for " + ChatColor.GREEN + ((level * 0.5)) + ChatColor.GRAY + " seconds",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level),
                "Energy: " + ChatColor.GREEN + getEnergy(level)
        };
    }

    @EventHandler
    public void update(UpdateEvent e) {
        if (e.getType() == UpdateType.TICK) {
            Iterator<LargeFireball> it = fireballs.iterator();
            while (it.hasNext()) {
                LargeFireball f = it.next();
                if (f == null || f.isDead()) {
                    it.remove();
                    continue;
                }
                if (f.getLocation().getY() < 255 || !f.isDead()) {
                    ParticleEffect.LAVA.display(f.getLocation());
                } else {
                    it.remove();
                }
            }
        }
    }


    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof LargeFireball) {
            fireballs.remove(e.getEntity());
        }
    }

	/*
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e){

		if(e.getDamager() instanceof Projectile){
			Projectile j = (Projectile) e.getDamager();
			if(j instanceof LargeFireball && j.getShooter() instanceof Player){
				Player player = (Player) j.getShooter();
				if(e.getEntity() instanceof LivingEntity){
					if(e.getEntity() instanceof Player){
						Player target = (Player) e.getEntity();

						if(!ClanUtilities.canHurt(player, target)){
							e.setCancelled(true);
							return;
						}else{
							PvPManager.addLog(target, player.getName(), "Molten Blast");

						}
					}

					e.setDamage(7);
					((LivingEntity)e.getEntity()).setFireTicks((int) (20 * (0 + (getLevel(player) * 0.5))));
				}
			}

		}
	}
	 */

    @EventHandler
    public void onaDamage(CustomDamageEvent e) {

        if (e.getProjectile() != null) {
            Projectile j = e.getProjectile();
            if (j instanceof LargeFireball && j.getShooter() instanceof Player) {
                Player player = (Player) j.getShooter();

                LogManager.addLog(e.getDamagee(), player, "Molten Blast");
                e.setKnockback(true);
                e.setDamage(6);
                new BukkitRunnable(){
                    @Override
                    public void run(){
                        e.getDamagee().setFireTicks((int) (20 * (0 + (getLevel(player) * 0.5))));
                    }
                }.runTaskLater(getInstance(), 2);

            }


        }
    }


    @Override
    public Types getType() {

        return Types.AXE;
    }


    @EventHandler
    public void onExplode(EntityExplodeEvent e){
        if(e.getEntity() instanceof LargeFireball){

            e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 2.0f, 1.0f);
        }
    }


    @Override
    public boolean usageCheck(Player player) {

		/*
		Location start = player.getEyeLocation();
		for(int z = 0; z < 100; z++){
			Vector increase = start.getDirection();
			Location point = start.add(increase);
			point.getWorld().spigot().playEffect(point, Effect.FIREWORKS_SPARK, Effect.FIREWORKS_SPARK.getId(), 0, 0.0F, 0.0F, 0.0F, 0, 1, 100);
			for(Player p : UtilPlayer.getNearby(point, 3)){
				if(p.equals(player)) continue;
				PvPManager.addLog(p, player.getName(), "Fireworks Nigga");
				p.damage(20.0);

			}
			if(point.getBlock().getType() != Material.AIR){
				FireworkEffect fe =  FireworkEffect.builder().with(Type.BALL).withColor(Color.WHITE).build();
				UtilFirework.spawn(point, fe);
				break;

			}

		}
		 */


        if (player.getLocation().getBlock().getType() == Material.WATER) {
            UtilMessage.message(player, "Skill", "You cannot use " + ChatColor.GREEN + getName() + " in water.");
            return false;
        }

        return ClanUtilities.canCast(player);
    }

    /*
     * Stops players from deflecting fireballs (Molten blast)
     */
    @EventHandler
    public void onDeflect(EntityDamageByEntityEvent e) {

        if (e.getEntity() instanceof Projectile) {
            Projectile p = (Projectile) e.getEntity();
            if (p.getShooter() instanceof Player) {
                if (p instanceof LargeFireball) {
                    if (e.getDamager() instanceof Player || e.getDamager() instanceof Projectile) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }


    @Override
    public double getRecharge(int level) {

        return 25 - ((level - 1) * 2);
    }

    @Override
    public float getEnergy(int level) {

        return 50 - ((level - 1) * 3);
    }

    @Override
    public void activate(Player p, Gamer gamer) {
        LargeFireball f = p.launchProjectile(LargeFireball.class);
        f.setYield(2.0F);
        f.setVelocity(f.getVelocity().multiply(5));
        f.setIsIncendiary(false);

        fireballs.add(f);
        UtilMessage.message(p, getClassType(), "You used " + ChatColor.GREEN + "Molten Blast" + ChatColor.GRAY + ".");

    }
}
