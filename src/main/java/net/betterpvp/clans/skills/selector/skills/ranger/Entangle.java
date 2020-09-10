package net.betterpvp.clans.skills.selector.skills.ranger;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Entangle extends Skill {

    public Entangle(Clans i) {
        super(i, "Entangle", "Ranger", noMaterials, noActions, 3,
                false, false);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{"Your arrows apply Slowness 2",
                "to any damageable target for " + ChatColor.GREEN + (2 + (level * 0.5)) + ChatColor.GRAY + " seconds"};
    }

    @Override
    public Types getType() {

        return Types.PASSIVE_A;
    }


    @Override
    public boolean usageCheck(Player player) {

        return false;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSlow(CustomDamageEvent e) {
        if (e.isCancelled()) {
            return;
        }

        if (e.getProjectile() != null) {

            LivingEntity ent = e.getDamagee();

            if (e.getDamager() instanceof Player) {
                Player p = (Player) e.getDamager();
                Role r = Role.getRole(p);
                if (r != null && r.getName().equals(getClassType())) {
                    if (hasSkill(p, this)) {

                        LogManager.addLog(ent, p, "Entangle");

                        ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) ((2 +(getLevel(p) * 0.5)) * 20), 1));
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

}
