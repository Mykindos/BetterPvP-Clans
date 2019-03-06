package net.betterpvp.clans.skills.selector.skills.paladin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class MoltenShield extends Skill {

    public MoltenShield(Clans i) {
        super(i, "Molten Shield", "Paladin", noMaterials, noActions, 1, false, false);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "You are immune to lava and fire damage."
        };
    }

    @Override
    public Types getType() {

        return Types.PASSIVE_A;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(CustomDamageEvent e) {
        if (e.getDamagee() instanceof Player) {
            Player p = (Player) e.getDamagee();
            if (e.getCause() == DamageCause.LAVA || e.getCause() == DamageCause.FIRE || e.getCause() == DamageCause.FIRE_TICK) {
                if (hasSkill(p, this)) {
                    e.setCancelled("Skill Molten Shield");
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

        return 0;
    }

    @Override
    public boolean requiresShield() {

        return false;
    }

    @Override
    public void activateSkill(Player p) {


    }

    @Override
    public boolean usageCheck(Player p) {

        return false;
    }

}
