package net.betterpvp.clans.skills.selector.skills.knight;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HoldPosition extends Skill implements InteractSkill {


    public HoldPosition(Clans i) {
        super(i, "Hold Position", "Knight", getAxes,
                rightClick, 5,
                true, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Hold your position, gaining",
                "Protection II, Slow III and no",
                "knockback for " + ChatColor.GREEN + (5 + ((level - 1) * 0.5)) + ChatColor.GRAY + " seconds.",
                "",
                "Recharge: " + ChatColor.GREEN + getRecharge(level)
        };
    }

    @Override
    public Types getType() {

        return Types.AXE;
    }

    @EventHandler
    public void onDamage(CustomDamageEvent e) {
        if (e.getDamagee() instanceof Player) {
            Player p = (Player) e.getDamagee();
            if (hasSkill(p, this)) {

                if (p.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                    e.setKnockback(false);
                }
            }
        }
    }

    @Override
    public boolean usageCheck(Player player) {
        if(UtilBlock.isInLiquid(player)){
            UtilMessage.message(player, "Skill", "You cannot use " + net.md_5.bungee.api.ChatColor.GREEN + getName() + net.md_5.bungee.api.ChatColor.GRAY + " in water.");
            return false;
        }

        return true;
    }

    @Override
    public double getRecharge(int level) {
        return 25 - ((level - 1) * 2);
    }

    @Override
    public float getEnergy(int level) {
        return 0;
    }


    @Override
    public void activate(Player player, Gamer gamer) {
        UtilMessage.message(player, getClassType(), "You used " + ChatColor.GREEN + getName(getLevel(player)) + ChatColor.GRAY + ".");
        int level = getLevel(player);
        EffectManager.addEffect(player, EffectType.RESISTANCE, 1, (long) (5 + ((level - 1) * 0.5)) * 1000);
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int) (100 + (((level - 1) * 0.5) * 20)), 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) (100 + (((level - 1) * 0.5) * 20)), 3));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1F, 0.5F);
    }
}
