package net.betterpvp.clans.skills.selector.skills.necromancer;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.DamageManager;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Bloodshed extends Skill implements InteractSkill {

    public Bloodshed(Clans i) {
        super(i, "Bloodshed", "Necromancer", getAxes, rightClick, 5, true, true);
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                "Right click with Axe to activate.",
                "",
                "Sacrifice " +ChatColor.GREEN +  UtilMath.round( 100 - ((0.50 + (level * 0.05)) * 100), 2) + "%" + ChatColor.GRAY + " of your health to grant",
                "yourself Speed V for 4 seconds.",
                "",
                "Recharge: " + ChatColor.GREEN + getRecharge(level)
        };
    }

    @Override
    public Types getType() {
        return Types.AXE;
    }

    @Override
    public double getRecharge(int level) {
        return 12 - (level);
    }

    @Override
    public float getEnergy(int level) {
        return 0;
    }

    @Override
    public boolean usageCheck(Player p) {
        int level = getLevel(p);
        double healthReduction = 0.50 + (level * 0.05);
        double proposedHealth = p.getHealth() - (20 - (20 * healthReduction));

        if(proposedHealth <= 0.5){
            UtilMessage.message(p, "Skill", "You do not have enough health to use " + ChatColor.GREEN + getName(level) + ChatColor.GRAY + ".");
            return false;
        }

        if(UtilBlock.isInLiquid(p)){
            UtilMessage.message(p, "Skill", "You cannot use " + net.md_5.bungee.api.ChatColor.GREEN + getName() + net.md_5.bungee.api.ChatColor.GRAY + " in water.");
            return false;
        }

        return true;
    }

    @Override
    public void activate(Player p, Gamer gamer) {
        int level = getLevel(p);
        double healthReduction = 0.50 + (level * 0.05);
        double proposedHealth = p.getHealth() - (20 - (20 * healthReduction));

        p.setHealth(Math.max(0.5, proposedHealth));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 4));
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_RAVAGER_ROAR, 2.0f, 0.3f);
        p.playSound(p.getLocation(), Sound.BLOCK_CONDUIT_AMBIENT, 2.0f, 2.0f);
    }
}
