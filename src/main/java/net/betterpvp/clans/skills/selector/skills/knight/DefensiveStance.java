package net.betterpvp.clans.skills.selector.skills.knight;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.DamageManager;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.ChannelSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import net.betterpvp.core.utility.UtilVelocity;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

public class DefensiveStance extends ChannelSkill {

    private Set<UUID> active = new HashSet<>();
    private WeakHashMap<Player, Long> gap = new WeakHashMap<>();

    public DefensiveStance(Clans i) {
        super(i, "Defensive Stance", "Knight", getSwords
                , rightClick, 5, false, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Hold Block with Sword to Channel.",
                "",
                "While active, you are immune to all",
                "melee damage from attacks infront of you.",
                "",
                "Players who attack you receive damage,",
                "and get knocked back.",
                "",
                "Energy / Second: " + ChatColor.GREEN + getEnergy(level)};
    }

    @Override
    public Types getType() {

        return Types.SWORD;
    }

    @Override
    public void activateSkill(Player p) {
        if (hasSkill(p, this)) {
            if (!active.contains(p.getUniqueId())) {
                if (Energy.use(p, getName(), 5, true)) {
                    active.add(p.getUniqueId());
                    gap.put(p, System.currentTimeMillis());
                }
            }
        }

    }

    @Override
    public boolean usageCheck(Player player) {
        if (UtilBlock.isInLiquid(player)) {
            UtilMessage.message(player, getClassType(), "You cannot use " + ChatColor.GREEN + getName() + ChatColor.GRAY + " in water!");
            return false;
        }

        return true;
    }

    @EventHandler
    public void onDamage(CustomDamageEvent e) {
        if (e.getCause() == DamageCause.ENTITY_ATTACK) {
            if (e.getDamagee() instanceof Player) {
                Player p = (Player) e.getDamagee();
                if (active.contains(p.getUniqueId())) {
                    if (Role.getRole(p) != null && Role.getRole(p).getName().equals(getClassType())) {
                        if (p.isHandRaised()) {
                            if (hasSkill(p, this)) {
                                Vector look = p.getLocation().getDirection();
                                look.setY(0);
                                look.normalize();

                                Vector from = UtilVelocity.getTrajectory(p, e.getDamager());
                                from.normalize();
                                if (p.getLocation().getDirection().subtract(from).length() > 0.6D) {
                                    return;
                                }

                                if (!DamageManager.hasDamageData(e.getDamager().getUniqueId().toString(), DamageCause.ENTITY_ATTACK)) {
                                    e.getDamager().setVelocity(e.getDamagee().getEyeLocation().getDirection().add(new Vector(0, 0.5, 0)).multiply(1));


                                    CustomDamageEvent ev = new CustomDamageEvent(e.getDamager(), e.getDamagee(), null, DamageCause.ENTITY_ATTACK, 2, false);
                                    ev.setIgnoreArmour(false);
                                    Bukkit.getPluginManager().callEvent(
                                            ev);
                                }
                                e.setCancelled("Skill Defensive Stance");

                                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1.0F, 2.0F);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void Energy(UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (active.contains(p.getUniqueId())) {

                if (p.isHandRaised()) {

                    if (!Energy.use(p, getName(), getEnergy(getLevel(p)) / 2, true)) {
                        active.remove(p.getUniqueId());
                    } else if (!hasSkill(p, this)) {
                        active.remove(p.getUniqueId());
                    } else if (!hasSwordInMainHand(p)) {
                        active.remove(p.getUniqueId());
                    } else {
                        p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, 20);
                    }

                } else {
                    if (gap.containsKey(p)) {
                        if (UtilTime.elapsed(gap.get(p), 250)) {
                            active.remove(p.getUniqueId());
                            gap.remove(p);
                        }
                    }
                }

            }
        }
    }

    @Override
    public double getRecharge(int level) {

        return 0;
    }

    @Override
    public float getEnergy(int level) {

        return (float) 7 - ((level - 1));
    }

}
