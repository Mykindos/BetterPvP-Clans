package net.betterpvp.clans.skills.selector.skills.ranger;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.events.SkillDequipEvent;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.particles.ParticleEffect;
import net.betterpvp.core.particles.data.color.RegularColor;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class LevitatingShot extends Skill implements InteractSkill {

    public static List<UUID> active = new ArrayList<UUID>();
    private List<Arrow> levitatingArrows = new ArrayList<>();

    public LevitatingShot(Clans i) {
        super(i, "Levitating Shot", "Ranger",
                getBow, leftClick
                , 5, true, true);
    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Left click to activate.",
                "",
                "Your next arrow is tipped with mysterious magic,",
                "causing the next target you hit to receive Levitation for " + ChatColor.GREEN + (3.5 + (level * .5)) + ChatColor.GRAY + " seconds.",
                "",
                "Players with levitation are unable to use abilities.",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level)
        };
    }

    @EventHandler
    public void onDequip(SkillDequipEvent e) {
        if (e.getSkill() == this) {
            if (active.contains(e.getPlayer().getUniqueId())) {
                active.remove(e.getPlayer().getUniqueId());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(CustomDamageEvent e) {
        if (e.getProjectile() != null) {

            if (e.getDamager() instanceof Player) {
                Player p = (Player) e.getDamager();
                if (hasSkill(p, this)) {
                    if (e.getProjectile() instanceof Arrow) {
                        Arrow a = (Arrow) e.getProjectile();
                        if (levitatingArrows.contains(a)) {
                            e.setReason("Levitating Shot");

                            if (e.getDamagee() instanceof Player) {
                                EffectManager.addEffect((Player) e.getDamagee(), EffectType.LEVITATION, 1, (int) (2.5 + (getLevel(p) * 0.5)) * 1000);
                            } else {
                                e.getDamagee().addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, (int) (2.5 + (getLevel(p) * 0.5)) * 20, 1));
                            }

                            e.setReason(getName());
                            levitatingArrows.remove(a);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void updateParticle(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.TICK) {
            Iterator<Arrow> it = levitatingArrows.iterator();
            while (it.hasNext()) {
                Arrow next = it.next();
                if (next == null) {
                    it.remove();
                } else if (next.isDead()) {
                    it.remove();
                } else {
                    Location loc = next.getLocation().add(new Vector(0, 0.25, 0));
                    ParticleEffect.REDSTONE.display(loc, new RegularColor(128, 0, 128));
                    ParticleEffect.REDSTONE.display(loc, new RegularColor(128, 0, 128));
                    ParticleEffect.REDSTONE.display(loc, new RegularColor(128, 0, 128));

                }
            }
        }
    }

    @EventHandler
    public void ShootBow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (!(event.getProjectile() instanceof Arrow)) {
            return;
        }

        Player player = (Player) event.getEntity();
        if (!active.contains(player.getUniqueId())) {
            return;
        }

        UtilMessage.message(player, getClassType(), "You fired " + ChatColor.GREEN + getName() + ChatColor.GRAY + ".");
        //event.getProjectile().setFireTicks(Integer.MAX_VALUE);
        active.remove(player.getUniqueId());
        levitatingArrows.add((Arrow) event.getProjectile());
    }


    @Override
    public boolean usageCheck(Player player) {
        if (UtilBlock.isInLiquid(player)) {
            UtilMessage.message(player, "Skill", "You cannot use " + ChatColor.GREEN + getName() + ChatColor.GRAY + " in water.");
            return false;
        }
        return true;
    }


    @Override
    public Types getType() {

        return Types.BOW;
    }

    @Override
    public double getRecharge(int level) {

        return 14 - ((level - 1));
    }

    @Override
    public float getEnergy(int level) {

        return 0;
    }

    @Override
    public void activate(Player player, Gamer gamer) {
        active.remove(player.getUniqueId());

        active.add(player.getUniqueId());
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 2.5F, 2.0F);
        UtilMessage.message(player, getClassType(), "You have prepared " + ChatColor.GREEN + getName(getLevel(player)) + ChatColor.GRAY + ".");
    }
}
