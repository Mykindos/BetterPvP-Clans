package net.betterpvp.clans.skills.selector.skills.gladiator;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.libs.jline.WindowsTerminal;
import org.bukkit.entity.Player;

public class StrengthInNumbers extends Skill implements InteractSkill {

    public StrengthInNumbers(Clans i) {
        super(i, "Strength in Numbers", "Gladiator", getAxes, rightClick, 5, true, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Right click with Axe to Activate.",
                "",
                "Grant all allies within 6 blocks",
                "Strength I for " + ChatColor.GREEN + (2 + level),
                "seconds.",
                "",
                "This does not give you the buff.",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level),
                "Energy: " + ChatColor.GREEN + getEnergy(level)
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

        return 30 - ((level - 1) * 2);
    }

    @Override
    public boolean usageCheck(Player p) {

        return true;
    }

    @Override
    public void activate(Player p, Gamer gamer) {
        UtilMessage.message(p, getClassType(), "You used " + ChatColor.GREEN + getName(getLevel(p)));
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0F, 2.0F);
        EffectManager.addEffect(p, EffectType.STRENGTH, 1, (2 + getLevel(p)) * 1000);

        for (Player d : UtilPlayer.getInRadius(p.getLocation(), 10)) {
            if (d.equals(p)) continue;
            if (!ClanUtilities.canHurt(p, d)) {
                EffectManager.addEffect(d, EffectType.STRENGTH, 1, (2 + getLevel(p)) * 1000);
            }
        }
    }
}
