package net.betterpvp.clans.skills.selector.skills.gladiator;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class CripplingBlow extends Skill {


    public CripplingBlow(Clans i) {
        super(i, "Crippling Blow", "Gladiator", getAxes, noActions, 3, false, false);
    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Your powerflow axe blows give",
                "targets Slow I for " + ChatColor.GREEN + (1 + (level * 0.5)) + ChatColor.GRAY + " seconds,",
                "as well as no knockback."};
    }


    @EventHandler
    public void onDamage(CustomDamageEvent e) {
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();//
            if (Arrays.asList(getMaterials()).contains(p.getItemInHand().getType())) {
                if (hasSkill(p, this)) {

                    LivingEntity ent = (LivingEntity) e.getDamagee();
                    LogManager.addLog(ent, p, "Crippling Blow");
                    ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) ((1 + (getLevel(p) * 0.5)) * 20), 0));

                }

            }
        }
    }

    @EventHandler
    public void onDamageKnockBack(CustomDamageEvent e) {
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();

            if (hasSkill(p, this)) {
                if (Arrays.asList(getMaterials()).contains(p.getItemInHand().getType())) {
                    e.setKnockback(false);
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
    public void activateSkill(Player p) {


    }

    @Override
    public boolean usageCheck(Player p) {

        return false;
    }

}
