package net.betterpvp.clans.skills.selector.skills.gladiator;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilTime;
import net.betterpvp.core.utility.UtilVelocity;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.WeakHashMap;

public class Stampede extends Skill {

    private WeakHashMap<Player, Long> sprintTime = new WeakHashMap<>();
    private WeakHashMap<Player, Integer> sprintStr = new WeakHashMap<>();

    public Stampede(Clans i) {
        super(i, "Stampede", "Gladiator", noMaterials, noActions, 5,
                false, false);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "You slowly build up speed as you",
                "sprint. You gain a level of Speed",
                "for every " + ChatColor.GREEN + (7 - level) + ChatColor.GRAY + " seconds, up to a max",
                "of Speed II.",
                "",
                "Attacking during stampede deals",
                "2 bonus damage per speed level."};
    }

    @Override
    public Types getType() {

        return Types.PASSIVE_B;
    }


    @Override
    public boolean usageCheck(Player player) {

        return false;
    }

    @EventHandler
    public void Skill(UpdateEvent event) {
        if (event.getType() != UpdateType.FASTER) {
            return;
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (sprintTime.containsKey(p)) {
                if (hasSkill(p, this)) {
                    if (!p.isSprinting()) {
                        sprintTime.remove(p);
                        sprintStr.remove(p);
                        p.removePotionEffect(PotionEffectType.SPEED);
                    } else {
                        long time = sprintTime.get(p);
                        int str = sprintStr.get(p);
                        if (str > 0) {
                            if (p.hasPotionEffect(PotionEffectType.SPEED)) {
                                p.removePotionEffect(PotionEffectType.SPEED);
                            }

                            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, str - 1));
                        }
                        if (UtilTime.elapsed(time, (8 - getLevel(p)) * 1000)) {
                            sprintTime.put(p, System.currentTimeMillis());
                            if (str < 2) {
                                sprintStr.put(p, str + 1);

                                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_AMBIENT, 2.0F, 0.2F * str + 1.0F);
                            }
                        }
                    }
                }
            } else if (p.isSprinting()) {
                if (!sprintTime.containsKey(p)) {
                    this.sprintTime.put(p, System.currentTimeMillis());
                    this.sprintStr.put(p, 0);
                }
            }
        }


    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(CustomDamageEvent e) {
        if (e.getCause() != DamageCause.ENTITY_ATTACK) return;
        if (e.getDamager() instanceof Player) {

            Player p = (Player) e.getDamager();
            if (!sprintStr.containsKey(p)) {
                return;
            }
            if (sprintStr.get(p) == 0) {
                return;
            }
            sprintTime.remove(p);
            int str = sprintStr.get(p);
            sprintStr.remove(p);
            p.removePotionEffect(PotionEffectType.SPEED);

            if (e.getDamagee() instanceof LivingEntity) {
                e.setKnockback(false);
                UtilVelocity.velocity(e.getDamagee(), UtilVelocity.getTrajectory2d(p, e.getDamagee()), 2.0D, true, 0.0D, 0.4D, 1.0D, true);
                e.setDamage(e.getDamage() + str);
            }
        }
    }

    @Override
    public double getRecharge(int level) {

        return 0;
    }

    @Override
    public float getEnergy(int level) {

        return 0;
    }

}
