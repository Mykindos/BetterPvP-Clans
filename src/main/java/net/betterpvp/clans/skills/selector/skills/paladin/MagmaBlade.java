package net.betterpvp.clans.skills.selector.skills.paladin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.Arrays;

public class MagmaBlade extends Skill {

    public MagmaBlade(Clans i) {
        super(i, "Magma Blade", "Paladin", getSwords,
                noActions, 3, false, false);
    }


    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Your sword scorches opponents,",
                "dealing an additional " + ChatColor.GREEN + ((level * 1.0)) + ChatColor.GRAY + " damage",
                "to players who are on fire."};
    }

    @Override
    public Types getType() {

        return Types.PASSIVE_A;
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(CustomDamageEvent e) {
        if (e.getCause() != DamageCause.ENTITY_ATTACK) return;
        if (e.getDamager() instanceof Player) {

            Player p = (Player) e.getDamager();
            LivingEntity ent = (LivingEntity) e.getDamagee();
            if (Arrays.asList(getMaterials()).contains(p.getItemInHand().getType())) {
                if (Role.getRole(p) != null && Role.getRole(p).getName().equals(getClassType())) {
                    if (hasSkill(p, this)) {
                        if (ent.getFireTicks() > 0) {
                            e.setDamage(e.getDamage() + ((getLevel(p) * 1.0)));
                        }
                    }
                }
            }

        }

    }

    @Override
    public void activateSkill(Player player) {


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

    @Override
    public boolean requiresShield() {

        return false;
    }
}

