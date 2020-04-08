package net.betterpvp.clans.skills.selector.skills.gladiator;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.DamageManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Bloodshed extends Skill {

    public Bloodshed(Clans i) {
        super(i, "Bloodshed", "Gladiator", getAxes, rightClick, 5, true, true);
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{"NECROMANCER SKILL : Sacrifice your blood for a boost in speed"};
    }

    @Override
    public Types getType() {
        return Types.AXE;
    }

    @Override
    public double getRecharge(int level) {
        return 10;
    }

    @Override
    public float getEnergy(int level) {
        return 0;
    }

    @Override
    public void activateSkill(Player p) {
        int level = getLevel(p);
        double healthReduction = 0.50 + (level * 0.05);
        double proposedHealth = p.getHealth() - (20 - (20 * healthReduction));

        p.setHealth(Math.max(0.5, proposedHealth));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 4));
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BAT_DEATH, 2.0f, 0.5f);
        p.playSound(p.getLocation(), Sound.BLOCK_CONDUIT_AMBIENT, 2.0f, 2.0f);
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

        return true;
    }
}
