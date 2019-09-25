package net.betterpvp.clans.skills.selector.skills.assassin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class FeatherFalling extends Skill {

    public FeatherFalling(Clans i) {
        super(i, "Feather Falling", "Assassin",
                noMaterials,
                noActions, 3, true, false);
    }

    @EventHandler
    public void onFallDamage(CustomDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (event.getDamagee() instanceof Player) {
                Player player = (Player) event.getDamagee();

                Role r = Role.getRole(player);
                if (r != null && r.getName().equals(getClassType())) {
                    event.setCancelled("Feather Falling");
                }

            }
        }
    }

    @Override
    public void activateSkill(Player player) {

    }

    @Override
    public boolean usageCheck(Player player) {
        return true;
    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{"Avoid fall damage",
                "Prevent fall damage by: " + ChatColor.GREEN + (7 + level) + "0%"};
    }

    @Override
    public Types getType() {

        return null;
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
