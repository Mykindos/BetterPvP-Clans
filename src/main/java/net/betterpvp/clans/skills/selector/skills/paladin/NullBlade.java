package net.betterpvp.clans.skills.selector.skills.paladin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class NullBlade extends Skill {

    public NullBlade(Clans i) {
        super(i, "Null Blade", "Paladin", getSwords, noActions, 5, false, false);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Your sword sucks " + ChatColor.GREEN + getEnergy(level) + ChatColor.GRAY + " energy from",
                "opponents with every attack"
        };
    }

    @EventHandler
    public void onDamage(CustomDamageEvent e) {
        if (e.getCause() == DamageCause.ENTITY_ATTACK) {
            if (e.getDamagee() instanceof Player) {
                if (e.getDamager() instanceof Player) {
                    Player dam = (Player) e.getDamager();
                    Player target = (Player) e.getDamagee();

                    if (hasSkill(dam, this)) {
                        double degen;

                        degen = getEnergy(getLevel(dam)) * 0.01;

                        Energy.degenerateEnergy(target, degen);
                        Energy.regenerateEnergy(dam, degen);
                    }
                }

            }
        }
    }


    @Override
    public Types getType() {

        return Types.PASSIVE_A;
    }

    @Override
    public double getRecharge(int level) {

        return 0;
    }

    @Override
    public float getEnergy(int level) {

        return 7 + ((level - 1) * 2);
    }


    @Override
    public boolean usageCheck(Player player) {

        return false;
    }

}
