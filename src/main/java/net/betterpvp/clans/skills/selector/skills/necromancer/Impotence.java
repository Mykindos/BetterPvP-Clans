package net.betterpvp.clans.skills.selector.skills.necromancer;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.classes.roles.Necromancer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilEntity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;

import java.util.Collections;
import java.util.stream.Collectors;

public class Impotence extends Skill {

    public Impotence(Clans i) {
        super(i, "Impotence", "Necromancer", noMaterials, noActions, 3, false, false);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(CustomDamageEvent e) {
        if (e.getDamagee() instanceof Player) {
            Player player = (Player) e.getDamagee();
            Role role = Role.getRole(player);
            if (role != null && role instanceof Necromancer) {
                if (hasSkill(player, this)) {
                    int level = getLevel(player);
                    int nearby = UtilEntity.getAllInRadius(player.getLocation(), 3 + level).stream().filter(ent -> {
                        if (ent instanceof Player) {
                            if (!ClanUtilities.canHurt(player, (Player) ent)) {
                                return false;
                            }
                        }

                        return true;
                    }).collect(Collectors.toList()).size();


                    e.setDamage(e.getDamage() * (1 - ((15 + (Math.min(nearby, 3) * 5)) * 0.01)));

                }
            }
        }
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                "For each enemy within " + ChatColor.GREEN + (3 + level) + ChatColor.GRAY + "  blocks",
                "you take reduced damage from all sources, at a maximum of 3 players.",
                "",
                "Damage Reduction:",
                "1 nearby enemy = 20%",
                "3 nearby enemies = 30%"
        };
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
        return 0;
    }

    @Override
    public boolean usageCheck(Player p) {
        return false;
    }
}
