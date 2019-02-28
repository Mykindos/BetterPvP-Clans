package net.betterpvp.clans.skills.selector.skills.knight;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilTime;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.WeakHashMap;

public class Thorns extends Skill {

    private WeakHashMap<Player, Long> cd = new WeakHashMap<>();

    public Thorns(Clans i) {
        super(i, "Thorns", "Knight", noMaterials, noActions, 3, false, false);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String[] getDescription(int level) {
        // TODO Auto-generated method stub
        return new String[]{
                "Enemies take " + ChatColor.GREEN + (level) + ChatColor.GRAY + " damage when",
                "they hit you using a melee attack."
        };
    }

    @Override
    public Types getType() {
        // TODO Auto-generated method stub
        return Types.PASSIVE_A;
    }

    @Override
    public double getRecharge(int level) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getEnergy(int level) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void activateSkill(Player p) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean usageCheck(Player p) {
        // TODO Auto-generated method stub
        return false;
    }

    @EventHandler
    public void onDamage(CustomDamageEvent e) {
        if (e.getDamagee() instanceof Player) {
            if (e.getCause() == DamageCause.ENTITY_ATTACK) {
                Player p = (Player) e.getDamagee();
                if (hasSkill(p, this)) {
                    if (e.getDamager() instanceof Player) {
                        Player d = (Player) e.getDamager();
                        if (ClanUtilities.canHurt(p, d)) {
                            if (!cd.containsKey(d)) {
                                cd.put(d, System.currentTimeMillis());
                            }

                            if (UtilTime.elapsed(cd.get(d), 2000)) {

                                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(d, p, null, DamageCause.CUSTOM, getLevel(p) * 0.80, false));
                                LogManager.addLog(d, p, "Thorns");
                                cd.put(d, System.currentTimeMillis());
                                return;
                            }
                        } else {
                            return;
                        }

                        return;
                    }
                    LivingEntity ent = (LivingEntity) e.getDamager();
                    LogManager.addLog(ent, p, "Thorns");
                    ent.damage(getLevel(p));
                }

            }
        }

    }

    @Override
    public boolean requiresShield() {
        // TODO Auto-generated method stub
        return false;
    }

}
