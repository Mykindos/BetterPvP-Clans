package net.betterpvp.clans.skills.selector.skills.knight;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class Sacrifice extends Skill {
    public Sacrifice(Clans i) {
        super(i, "Sacrifice", "Knight", noMaterials, noActions, 3,
                false, false);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{"Deal an extra " + ChatColor.GREEN + level + 0 + "%" + ChatColor.GRAY + " damage",
                "But you now also take",
                ChatColor.GREEN.toString() + (1 + level) + "0%" + ChatColor.GRAY + " extra damage from melee attacks"
        };
    }

    @Override
    public Types getType() {

        return Types.PASSIVE_B;
    }

    @Override
    public void activateSkill(Player player) {


    }

    @Override
    public boolean usageCheck(Player player) {

        return false;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHit(CustomDamageEvent e) {
        if (e.getCause() != DamageCause.ENTITY_ATTACK) return;
        if (e.getDamager() instanceof Player) {

            Player p = (Player) e.getDamager();

            if (hasSkill(p, this)) {
                int level = getLevel(p);
                e.setDamage(Math.ceil(e.getDamage() * (1.0 + (level * 0.1))));
            }

        }


        if (e.getDamagee() instanceof Player) {

            Player p = (Player) e.getDamagee();

            if (hasSkill(p, this))

                e.setDamage(Math.ceil(e.getDamage() * (1.1 + (getLevel(p) * 0.1))));
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

    @Override
    public boolean requiresShield() {

        return false;
    }


}

