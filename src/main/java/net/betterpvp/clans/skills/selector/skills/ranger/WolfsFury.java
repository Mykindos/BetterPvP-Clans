package net.betterpvp.clans.skills.selector.skills.ranger;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.WeakHashMap;

public class WolfsFury extends Skill implements InteractSkill {

    public WolfsFury(Clans i) {
        super(i, "Wolfs Fury", "Ranger", getAxes, rightClick, 5, true, true);

    }

    private WeakHashMap<Player, Long> active = new WeakHashMap<>();

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Right click with Axe to Activate.",
                "",
                "Summon the power of the wolf, gaining",
                "Strength 1 for " + ChatColor.GREEN + (3 + level) + ChatColor.GRAY + " seconds, and giving",
                "no knockback on your attacks.",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level)
        };
    }

    @Override
    public Types getType() {

        return Types.AXE;
    }

    @Override
    public double getRecharge(int level) {

        return 25 - ((level - 1) * 1.5);
    }

    @Override
    public float getEnergy(int level) {

        return 0;
    }

    @EventHandler
    public void onDamage(CustomDamageEvent e) {
        if (e.getCause() == DamageCause.ENTITY_ATTACK) {

            if (e.getDamager() instanceof Player) {

                if (active.containsKey((Player) e.getDamager())) {
                    if (hasSkill((Player) e.getDamager(), this)) {
                        LogManager.addLog(e.getDamagee(), e.getDamager(), "Wolfs Fury");
                    }
                    e.setKnockback(false);
                }
            }
            if (e.getDamager() instanceof Player) {
                if (active.containsKey((Player) e.getDamager())) {

                }
            }

        }
    }


    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateType.FAST) {
            Iterator<Entry<Player, Long>> it = active.entrySet().iterator();
            while (it.hasNext()) {
                Entry<Player, Long> next = it.next();
                if (!hasSkill(next.getKey(), this)) {
                    it.remove();
                    continue;
                }
                if (UtilTime.elapsed(next.getValue(), (3 + getLevel(next.getKey()) * 1000))) {
                    it.remove();
                }
            }
        }
    }

    @Override
    public boolean usageCheck(Player p) {

        return true;
    }

    @Override
    public void activate(Player p, Gamer gamer) {
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WOLF_HOWL, 1.0f, 1.0f);
        UtilMessage.message(p, getClassType(), "You activated " + ChatColor.GREEN + getName(getLevel(p)));
        active.put(p, System.currentTimeMillis());
        EffectManager.addEffect(p, EffectType.STRENGTH, 1, ((3 + getLevel(p)) * 1000));
    }
}
