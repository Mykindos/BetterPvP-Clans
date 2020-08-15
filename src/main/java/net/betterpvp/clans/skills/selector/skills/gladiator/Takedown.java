package net.betterpvp.clans.skills.selector.skills.gladiator;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.WeakHashMap;

public class Takedown extends Skill implements InteractSkill {

    private WeakHashMap<Player, Long> active = new WeakHashMap<>();

    public Takedown(Clans i) {
        super(i, "Takedown", "Gladiator", getSwords, rightClick, 5, true, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Right click with a sword to activate.",
                "",
                "Hurl yourself towards an opponent.",
                "If you collide with them, you " + ChatColor.WHITE + "both",
                "take damage and receive Slow 4",
                "for " + ChatColor.GREEN + (1 + level) + ChatColor.GRAY + " seconds.",
                "",
                "Cannot be used while grounded.",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level),
                "Energy: " + ChatColor.GREEN + getEnergy(level)
        };
    }

    @Override
    public Types getType() {

        return Types.SWORD;
    }

    @Override
    public double getRecharge(int level) {

        return 25 - ((level - 1) * 2);
    }

    @Override
    public float getEnergy(int level) {

        return 40 - ((level - 1) * 3);
    }


    @EventHandler
    public void end(UpdateEvent event) {
        if (event.getType() == UpdateEvent.UpdateType.TICK) {

            Iterator<Entry<Player, Long>> it = active.entrySet().iterator();
            while (it.hasNext()) {

                Entry<Player, Long> next = it.next();
                Player p = next.getKey();
                if (p.isDead()) {
                    it.remove();
                    continue;
                }

                for (Player other : Bukkit.getOnlinePlayers()) {
                    if (other.equals(p)) continue;
                    if (other.isDead()) continue;

                    if (UtilMath.offset(p, other) < 2.5) {

                        doTakeDown(next.getKey(), other);
                        it.remove();
                        return;


                    }
                }


                if (UtilBlock.isGrounded(p)) {
                    if (!p.hasPotionEffect(PotionEffectType.SLOW)) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 50, 2));
                    }
                    if (UtilTime.elapsed(next.getValue(), 750L)) {
                        it.remove();
                        continue;
                    }
                }
            }

        }

    }

    public void doTakeDown(Player p, Player d) {

        if (ClanUtilities.canHurt(p, d)) {

            LogManager.addLog(p, d, "Takedown Recoil");
            LogManager.addLog(d, p, "Takedown");
            UtilMessage.message(p, getClassType(), "You hit " + ChatColor.GREEN + d.getName() + ChatColor.GRAY + " with " + ChatColor.GREEN + getName());

            Bukkit.getPluginManager().callEvent(new CustomDamageEvent(d, p, null, DamageCause.CUSTOM, 10, false));


            UtilMessage.message(d, getClassType(), ChatColor.GREEN + p.getName() + ChatColor.GRAY + " hit you with " + ChatColor.GREEN + getName(getLevel(p)));
            Bukkit.getPluginManager().callEvent(new CustomDamageEvent(p, d, null, DamageCause.CUSTOM, 10, false));

            PotionEffect pot = new PotionEffect(PotionEffectType.SLOW, (int) (1 + (getLevel(p) * 0.5)) * 20, 2);
            p.addPotionEffect(pot);
            d.addPotionEffect(pot);
        }
    }

    @Override
    public boolean usageCheck(Player p) {
        if (p.getLocation().getBlock().isLiquid()) {
            UtilMessage.message(p, getClassType(), "You cannot use " + ChatColor.GREEN + getName() + ChatColor.GRAY + " in water.");
            return false;
        }

        if (UtilBlock.isGrounded(p)) {
            UtilMessage.message(p, getClassType(), "You cannot use " + ChatColor.GREEN + getName() + ChatColor.GRAY + " while grounded.");
            return false;
        }

        return true;
    }

    @Override
    public void activate(Player p, Gamer gamer) {
        Vector vec = p.getLocation().getDirection();

        UtilVelocity.velocity(p, vec, 1.8D, false, 0.0D, 0.4D, 0.6D, false);

        active.put(p, System.currentTimeMillis());
        UtilMessage.message(p, getClassType(), "You used " + ChatColor.GREEN + getName() + " " + getLevel(p));
    }
}
