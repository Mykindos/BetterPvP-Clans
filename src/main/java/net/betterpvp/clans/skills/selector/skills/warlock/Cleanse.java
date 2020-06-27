package net.betterpvp.clans.skills.selector.skills.warlock;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Cleanse extends Skill implements InteractSkill {

    public Cleanse(Clans i) {
        super(i, "Cleanse", "Warlock", getAxes, rightClick, 5, true, true);
    }

    @Override
    public void activate(Player player, Gamer gamer) {
        int level = getLevel(player);
        double healthReduction = 0.50 + (level * 0.05);
        double proposedHealth = player.getHealth() - (20 - (20 * healthReduction));

        player.setHealth(Math.max(0.5, proposedHealth));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 1.0f, 0.9f);
        EffectManager.addEffect(player, EffectType.IMMUNETOEFFECTS, (2 + (level / 2)) * 1000);
        UtilMessage.message(player, getClassType(), "You used " + ChatColor.GREEN + getName(level) + ChatColor.GRAY + ".");

        for(Player p : UtilPlayer.getNearby(player.getLocation(), (5 + level))){
            if(p.equals(player)) continue;
            if(!ClanUtilities.canHurt(player, p)){
                EffectManager.addEffect(p, EffectType.IMMUNETOEFFECTS, (2 + (level / 2)) * 1000);
                UtilMessage.message(p, "Cleanse", "You were cleansed of negative by " + ChatColor.GREEN + player.getName());
            }
        }
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                "Right click with a axe to activate.",
                "",
                "Sacrifice " + ChatColor.GREEN + UtilMath.round(100 - ((0.50 + (level * 0.05)) * 100), 2) + "%" + ChatColor.GRAY + " of your health to purge",
                "all negative effects from yourself and allies within " + ChatColor.GREEN + (5 + level) + ChatColor.GRAY + " blocks.",
                "",
                "You and your allies also receive an immunity against",
                "negative effects for " + ChatColor.GREEN + (2 + (level / 2)) + ChatColor.GRAY + " seconds.",
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
        return 15 - level;
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


        return true;
    }
}
