package net.betterpvp.clans.skills.selector.skills.knight;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;


public class Fury extends Skill {
    public Fury(Clans i) {
        super(i, "Fury", "Knight", noMaterials, noActions, 3,
                false, false);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Your attacks deal a bonus " + ChatColor.GREEN + (level * 0.5) + ChatColor.GRAY + " damage"
        };
    }

    @Override
    public Types getType() {

        return Types.PASSIVE_A;
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

            if (e.getDamagee() instanceof LivingEntity) {
                Player p = (Player) e.getDamager();
                if (Role.getRole(p) != null && Role.getRole(p).getName().equals(getClassType())) {
                    if (hasSkill(p, this)) {
                        e.setDamage(e.getDamage() + (getLevel(p) * 0.5));
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

        return 0;
    }

    @Override
    public boolean requiresShield() {

        return false;
    }
}

