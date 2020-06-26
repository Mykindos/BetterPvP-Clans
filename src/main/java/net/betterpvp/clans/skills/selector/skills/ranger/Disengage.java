package net.betterpvp.clans.skills.selector.skills.ranger;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilVelocity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class Disengage extends Skill implements InteractSkill {

    private HashMap<UUID, Long> disengages = new HashMap<>();

    public Disengage(Clans i) {
        super(i, "Disengage", "Ranger", getSwords,
                rightClick, 5,
                true, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Right click with sword to prepare",
                "",
                "If you are attacked",
                "within " + ChatColor.GREEN + (0 + (level * 0.5)) + ChatColor.GRAY + " seconds you successfully disengage",
                "",
                "If successful, you leap backwards",
                "and your attacker receives Slow 4",
                "for " + ChatColor.GREEN + (2 + level) + ChatColor.GRAY + " seconds.",
                "",
                "Recharge: " + ChatColor.GREEN + getRecharge(level)};
    }

    @Override
    public Types getType() {

        return Types.SWORD;
    }

    @EventHandler
    public void onDamage(CustomDamageEvent e) {
        if (e.getCause() == DamageCause.ENTITY_ATTACK) {

            if (e.getDamagee() instanceof Player) {
                Player p = (Player) e.getDamagee();
                Role role = Role.getRole(p);
                if (role != null && role.getName().equals(getClassType())) {
                    if (hasSkill(p, this)) {
                        if (disengages.containsKey(p.getUniqueId())) {
                            LivingEntity ent = e.getDamager();
                            Vector vec = ent.getLocation().getDirection();
                            e.setKnockback(false);
                            e.setDamage(0);
                            UtilVelocity.velocity(p, vec, 3D, true, 0.0D, 0.4D, 1.5D, true);
                            EffectManager.addEffect(p, EffectType.NOFALL, 3000);
                            ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (2 + getLevel(p)) * 20, 3));
                            UtilMessage.message(p, getClassType(), "You successfully disengaged");
                            disengages.remove(p.getUniqueId());
                        }
                    }
                }

            }


        }
    }

    @EventHandler
    public void checkTimers(UpdateEvent e) {
        if (e.getType() == UpdateType.TICK) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (disengages.containsKey(p.getUniqueId())) {
                    if (disengages.get(p.getUniqueId()) - System.currentTimeMillis() <= 0) {
                        disengages.remove(p.getUniqueId());
                    }
                }
            }
        }
    }

    @Override
    public boolean usageCheck(Player player) {
        if (player.getLocation().getBlock().getType() == Material.WATER) {
            UtilMessage.message(player, "Skill", "You cannot use " + ChatColor.GREEN + getName() + ChatColor.GRAY + " in water.");
            return false;
        }
        return true;
    }

    @Override
    public double getRecharge(int level) {

        return 12;
    }

    @Override
    public float getEnergy(int level) {

        return 0;
    }

    @Override
    public void activate(Player player, Gamer gamer) {
        if (!disengages.containsKey(player.getUniqueId())) {
            disengages.put(player.getUniqueId(), (long) (System.currentTimeMillis() + ((0 + (getLevel(player) * 0.5)) * 1000L)));
            UtilMessage.message(player, getClassType(), "You prepared disengage");
        }
    }
}
