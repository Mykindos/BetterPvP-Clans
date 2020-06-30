package net.betterpvp.clans.skills.selector.skills.paladin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.skills.selector.skills.ToggleSkill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class Void extends Skill implements ToggleSkill {

    public Void(Clans i) {
        super(i, "Void", "Paladin", getSwordsAndAxes, noActions, 5, false, false);

    }

    private Set<UUID> active = new HashSet<>();

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Drop Axe/Sword to Toggle.",
                "",
                "While in void form, you receive",
                "Slownesss III, and take no Knockback",
                "",
                "Reduces incoming damage by 5, but",
                "burns 20 of your energy",
                "",
                "Energy / Second: " + ChatColor.GREEN + getEnergy(level)
        };
    }


    @EventHandler
    public void audio(UpdateEvent e) {
        if (e.getType() != UpdateType.SEC) {
            return;
        }
        for (UUID uuid : active) {
            if (Bukkit.getPlayer(uuid) != null) {
                Player cur = Bukkit.getPlayer(uuid);

                cur.getWorld().playSound(cur.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 2F, 0.5F);
                cur.getWorld().playSound(cur.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 2F, 0.5F);
            }
        }
    }

    @EventHandler
    public void update(UpdateEvent e) {
        if (e.getType() == UpdateType.TICK) {
            Iterator<UUID> it = active.iterator();
            while (it.hasNext()) {
                UUID uuid = it.next();
                if (Bukkit.getPlayer(uuid) != null) {
                    Player cur = Bukkit.getPlayer(uuid);
                    if (!cur.hasPotionEffect(PotionEffectType.SLOW)) {
                        cur.addPotionEffect(getPot());
                    }
                    if (!hasSkill(cur, this)) {
                        it.remove();
                    } else if (!Energy.use(cur, getName(), getEnergy(getLevel(cur)) / 6, true)) {
                        it.remove();
                    }
                } else {
                    it.remove();
                }
            }

        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(CustomDamageEvent e) {
        if (e.getDamagee() instanceof Player) {
            final Player p = (Player) e.getDamagee();

            Role role = Role.getRole(p);
            if (role != null && role.getName().equals(getClassType())) {
                if (active.contains(p.getUniqueId())) {

                    e.setDamage(e.getDamage() - 5);
                    Energy.degenerateEnergy(p, 0.20);

                    e.setKnockback(false);
                }

            }
        }
    }

    private PotionEffect getPot() {
        return new PotionEffect(PotionEffectType.SLOW, 20, 2);
    }


    @Override
    public Types getType() {

        return Types.PASSIVE_B;
    }

    @Override
    public double getRecharge(int level) {

        return 0;
    }

    @Override
    public float getEnergy(int level) {

        return (float) (11 - ((level - 1) * 0.5));
    }


    @Override
    public boolean usageCheck(Player p) {

        return true;
    }

    @Override
    public void activateToggle(Player player, Gamer gamer) {
        if (active.contains(player.getUniqueId())) {
            active.remove(player.getUniqueId());
            UtilMessage.message(player, getClassType(), "Void: " + ChatColor.RED + "Off");
        } else {
            active.add(player.getUniqueId());
            UtilMessage.message(player, getClassType(), "Void: " + ChatColor.GREEN + "On");
        }
    }
}
