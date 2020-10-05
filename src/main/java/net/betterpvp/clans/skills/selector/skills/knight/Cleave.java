package net.betterpvp.clans.skills.selector.skills.knight;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.Arrays;

public class Cleave extends Skill {

    public Cleave(Clans i) {
        super(i, "Cleave", "Knight", getAxes, noActions, 3, false, false);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Your attacks hit all opponents",
                "within " + ChatColor.GREEN + (2 + level) + ChatColor.GRAY + " blocks of your target.",
                "",
                "Only applies to axes."
        };
    }


    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {

        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();

            if (hasSkill(p, this)) {
                if (Arrays.asList(getMaterials()).contains(p.getInventory().getItemInMainHand().getType())) {
                    if (ClanUtilities.canCast(p)) {

                        if (e.getEntity() instanceof Player) {
                            Player dam = (Player) e.getEntity();

                            if (!ClanUtilities.canHurt(p, dam)) {
                                return;
                            }
                        }

                        for (LivingEntity ent : UtilPlayer.getAllInRadius(p.getLocation(), 2 + getLevel(p))) {
                            if (ent.equals(p)) continue;
                            if (ent.equals(e.getEntity())) continue;


                            LogManager.addLog(ent, p, "Cleave");
                            Bukkit.getPluginManager().callEvent(new CustomDamageEvent(ent, p, null, DamageCause.ENTITY_ATTACK, (4 + getLevel(p)), true));
                        }
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

        return 0;
    }

    @Override
    public boolean usageCheck(Player player) {

        return false;
    }

}
