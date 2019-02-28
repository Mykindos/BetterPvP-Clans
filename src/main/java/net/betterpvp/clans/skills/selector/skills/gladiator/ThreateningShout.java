package net.betterpvp.clans.skills.selector.skills.gladiator;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ThreateningShout extends Skill {

    public ThreateningShout(Clans i) {
        super(i, "Threatening Shout", "Gladiator", getAxes, rightClick, 5, true, true);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String[] getDescription(int level) {
        // TODO Auto-generated method stub
        return new String[]{
                "Right click with Axe to Activate.",
                "",
                "Release a roar, which frightens all enemies",
                "within " + ChatColor.GREEN + (4 + level) + ChatColor.GRAY + " blocks",
                "and grants them Vulnerability for " + ChatColor.GREEN + (2 + level),
                "seconds.",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level),
                "Energy: " + ChatColor.GREEN + getEnergy(level)
        };
    }

    @Override
    public Types getType() {
        // TODO Auto-generated method stub
        return Types.AXE;
    }

    @Override
    public double getRecharge(int level) {
        // TODO Auto-generated method stub
        return 19 - ((level - 1));
    }

    @Override
    public float getEnergy(int level) {
        // TODO Auto-generated method stub
        return 40 - ((level - 1) * 2);
    }

    @Override
    public void activateSkill(Player p) {
        p.getWorld().playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 2.0F, 2.0F);
        int level = getLevel(p);
        for (Player d : UtilPlayer.getInRadius(p.getLocation(), (8 + level))) {
            if (ClanUtilities.canHurt(p, d)) {
                EffectManager.addEffect(d, EffectType.VULNERABILITY, (3 + getLevel(p)) * 1000);
            }
        }

    }

    @Override
    public boolean usageCheck(Player p) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean requiresShield() {
        // TODO Auto-generated method stub
        return false;
    }

}
