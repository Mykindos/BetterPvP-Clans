package net.betterpvp.clans.skills.selector.skills.gladiator;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ThreateningShout extends Skill implements InteractSkill {

    public ThreateningShout(Clans i) {
        super(i, "Threatening Shout", "Gladiator", getAxes, rightClick, 5, true, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Right click with a axe to activate.",
                "",
                "Release a roar, which frightens all enemies",
                "within " + ChatColor.GREEN + (4 + level) + ChatColor.GRAY + " blocks",
                "and grants them Vulnerability for " + ChatColor.GREEN + (2 + level),
                "seconds.",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level)
        };
    }

    @Override
    public Types getType() {

        return Types.AXE;
    }

    @Override
    public double getRecharge(int level) {

        return 20 - ((level - 1));
    }

    @Override
    public float getEnergy(int level) {

        return 0;
    }


    @Override
    public boolean usageCheck(Player p) {

        if(!UtilBlock.isGrounded(p)){
            UtilMessage.message(p, getClassType(), "You can only use threatening shout while grounded.");
            return false;
        }
        return true;
    }

    @Override
    public void activate(Player p, Gamer gamer) {
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 2.0F, 2.0F);
        int level = getLevel(p);
        for (Player d : UtilPlayer.getInRadius(p.getLocation(), (6 + level))) {
            if (ClanUtilities.canHurt(p, d)) {
                EffectManager.addEffect(d, EffectType.VULNERABILITY, (3 + getLevel(p)) * 1000);
            }
        }
    }
}
