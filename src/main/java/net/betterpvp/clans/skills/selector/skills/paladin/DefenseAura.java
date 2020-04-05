package net.betterpvp.clans.skills.selector.skills.paladin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DefenseAura extends Skill {


    public DefenseAura(Clans i) {
        super(i, "Defensive Aura", "Paladin", getAxes,
                rightClick, 5,
                true, true);
    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Right click with Axe to Activate",
                "",
                "Gives you, and all allies within " + ChatColor.GREEN + (6 + level) + ChatColor.GRAY + " blocks",
                "2 bonus hearts",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level),
                "Energy: " + ChatColor.GREEN + getEnergy(level)};
    }

    @Override
    public Types getType() {

        return Types.AXE;
    }

    @Override
    public void activateSkill(Player p) {
        if (usageCheck(p)) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 200, 1));
            int level = getLevel(p);
            for (Player cur : UtilPlayer.getNearby(p.getLocation(), (6 + level))) {
                if (!ClanUtilities.canHurt(p, cur)) {
                    cur.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 200, 0));
                    cur.setHealth(Math.min(cur.getHealth() + 6, cur.getMaxHealth()));
                }
            }

        }

    }

    @Override
    public boolean usageCheck(Player player) {
        if (player.getLocation().getBlock().getType() == Material.WATER) {
            UtilMessage.message(player, "Skill", "You cannot use " + ChatColor.GREEN + getName() + ChatColor.GRAY + " in water.");
            return false;
        }

        return true;
    }

    @Override
    public double getRecharge(int level) {

        return 30 - ((level - 1) * 2);
    }

    @Override
    public float getEnergy(int level) {

        return 30 - ((level - 1) * 2);
    }

}
