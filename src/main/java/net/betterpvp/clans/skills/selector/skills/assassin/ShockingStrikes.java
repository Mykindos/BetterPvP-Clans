package net.betterpvp.clans.skills.selector.skills.assassin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ShockingStrikes extends Skill {

    public ShockingStrikes(Clans i) {
        super(i, "Shocking Strikes", "Assassin", noMaterials, noActions, 3, false, false);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Your attacks shock targets for",
                ChatColor.GREEN.toString() + (level) + ChatColor.GRAY + " second, giving them Slow I",
                "and Screen-Shake."
        };
    }

    @EventHandler
    public void onDamage(CustomDamageEvent e) {
        if (e.getCause() != DamageCause.ENTITY_ATTACK) return;
        if (e.getDamager() instanceof Player) {
            if (e.getDamagee() instanceof Player) {
                Player dam = (Player) e.getDamager();
                Player ent = (Player) e.getDamagee();
                if (ClanUtilities.canHurt(dam, ent)) {
                    if (Role.getRole(dam) != null && Role.getRole(dam).getName().equals(getClassType())) {
                        if (hasSkill(dam, this)) {
                            EffectManager.addEffect(ent, EffectType.SHOCK, (long) (getLevel(dam)) * 1000);
                            ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 0));
                            e.setReason(getName());
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
