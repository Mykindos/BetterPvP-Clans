package net.betterpvp.clans.skills.selector.skills.paladin;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.Map.Entry;

public class Polymorph extends Skill {

    private List<UUID> active = new ArrayList<>();
    public static WeakHashMap<LivingEntity, Long> polymorphed = new WeakHashMap<>();

    public Polymorph(Clans i) {
        super(i, "Polymorph", "Paladin", getSwords, rightClick, 3, true, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Right click with Sword to prepare.",
                "",
                "The next player you hit, is polymorphed",
                "into a sheep for 8 seconds.",
                "",
                "While a player is polymorphed, they cannot deal",
                "or take any damage.",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level),
                "Energy: " + ChatColor.GREEN + getEnergy(level)
        };
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(CustomDamageEvent e) {
        if (e.getCause() != DamageCause.ENTITY_ATTACK) return;
        if (e.getDamager() instanceof Player) {

            Player p = (Player) e.getDamager();
            LivingEntity ent = e.getDamagee();
            if (hasSkill(p, this)) {

                if (active.contains(p.getUniqueId())) {
                    e.setCancelled("Polymorph start");
                    DisguiseAPI.disguiseToAll(ent, new MobDisguise(DisguiseType.SHEEP));
                    ent.getWorld().playSound(ent.getLocation(), Sound.ENTITY_SHEEP_AMBIENT, 2.0f, 1.0f);
                    polymorphed.put(ent, System.currentTimeMillis());

                    ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                    UtilMessage.message(p, getName(), "You polymorphed " + ChatColor.GREEN + ent.getName());
                    if (ent instanceof Player) {
                        EffectManager.addEffect((Player) ent, EffectType.SILENCE, 5000);
                        UtilMessage.message((Player) ent, getName(), ChatColor.GREEN + p.getName() + ChatColor.GRAY + " polymorphed you.");
                    }
                    active.remove(p.getUniqueId());
                }

            }
        }


    }


    @EventHandler
    public void updatePoly(UpdateEvent e) {
        if (e.getType() == UpdateType.FASTER) {
            Iterator<Entry<LivingEntity, Long>> it = polymorphed.entrySet().iterator();
            while (it.hasNext()) {
                Entry<LivingEntity, Long> next = it.next();
                if (UtilTime.elapsed(next.getValue(), 8000)) {
                    UtilMessage.message(next.getKey(), getName(), "You are no longer polymorphed.");
                    DisguiseAPI.undisguiseToAll(next.getKey());
                    it.remove();
                }
            }
        }
    }

    @EventHandler
    public void onPolyDamage(CustomDamageEvent e) {

        if (polymorphed.containsKey(e.getDamager())) {
            e.setCancelled("Polymorphed");
        } else if (polymorphed.containsKey(e.getDamagee())) {
            e.setCancelled("Polymorphed");
        }

    }

    @Override
    public Types getType() {

        return Types.SWORD;
    }

    @Override
    public double getRecharge(int level) {

        return 45 - ((level - 1) * 7);
    }

    @Override
    public float getEnergy(int level) {

        return 40 - ((level - 1) * 3);
    }

    @Override
    public void activateSkill(Player p) {
        UtilMessage.message(p, getClassType(), "You prepared " + ChatColor.GREEN + getName(getLevel(p)));
        active.add(p.getUniqueId());

    }

    @Override
    public boolean usageCheck(Player p) {
        if (active.contains(p.getUniqueId())) {
            UtilMessage.message(p, getClassType(), ChatColor.GREEN + getName() + ChatColor.GRAY + " is already active.");
            return false;
        }

        return true;
    }

}
