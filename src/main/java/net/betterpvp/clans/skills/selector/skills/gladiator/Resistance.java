package net.betterpvp.clans.skills.selector.skills.gladiator;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class Resistance extends Skill {

    public Resistance(Clans i) {
        super(i, "Resistance", "Gladiator", noMaterials, noActions, 3,
                false, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "You take " + ChatColor.GREEN + (level * 15) + ChatColor.GRAY + "% less damage",
                "but you deal " + ChatColor.GREEN + (level * 15) + ChatColor.GRAY + "% less as well"
        };
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(CustomDamageEvent e) {
        if (e.getDamagee() instanceof Player) {
            final Player p = (Player) e.getDamagee();
            if (hasSkill(p, this)) {
                double modifier = ((getLevel(p) * 15));
                double modifier2 = modifier >= 10 ? 0.01 : 0.1;

                e.setDamage(e.getDamage() * (1.0 - (modifier * modifier2)));
            }
        }


        if (e.getDamager() instanceof Player) {
            final Player p = (Player) e.getDamager();
            if (Role.getRole(p) != null && Role.getRole(p).getName().equals(getClassType())) {
                if (hasSkill(p, this)) {
                    double modifier = ((getLevel(p) * 15));
                    double modifier2 = modifier >= 10 ? 0.01 : 0.1;

                    e.setDamage(e.getDamage() * (1.0 - (modifier * modifier2)));
                }
            }
        }
    }


    @Override
    public Types getType() {

        return Types.PASSIVE_A;
    }

    @Override
    public boolean usageCheck(Player player) {

        return false;
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
