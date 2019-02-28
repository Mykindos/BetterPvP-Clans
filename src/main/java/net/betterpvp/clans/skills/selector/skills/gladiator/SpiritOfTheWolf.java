package net.betterpvp.clans.skills.selector.skills.gladiator;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class SpiritOfTheWolf extends Skill {


    public SpiritOfTheWolf(Clans i) {
        super(i, "Spirit of the Wolf", "Gladiator", getAxes, rightClick, 5, true, true);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String[] getDescription(int level) {
        // TODO Auto-generated method stub
        return new String[]{
                "Right click with Axe to Activate.",
                "",
                "Call upon the spirit of the wolf",
                "granting all allies within " + ChatColor.GREEN + (5 + (level)) + ChatColor.GRAY + " blocks",
                "Speed II for 9 seconds.",
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
        return 15 - ((level - 1));
    }

    @Override
    public float getEnergy(int level) {
        // TODO Auto-generated method stub
        return 50 - ((level - 1) * 3);
    }

    @Override
    public void activateSkill(Player player) {
        UtilMessage.message(player, getClassType(), "You used " + ChatColor.GREEN + getName() + " " + getLevel(player));
        player.getWorld().playSound(player.getLocation().add(0.0, -1.0, 0.0), Sound.WOLF_HOWL, 0.5F, 1.0F);
        UtilPlayer.addPotionEffect(player, PotionEffectType.SPEED, 1, 140);

        for (Player p : UtilPlayer.getInRadius(player.getLocation(), (5 + getLevel(player)))) {
            if (!ClanUtilities.canHurt(player, p)) {
                UtilPlayer.addPotionEffect(p, PotionEffectType.SPEED, 1, 140);
                UtilMessage.message(p, getClassType(), "You received the spirit of the wolf!");
            }
        }

    }

    @Override
    public boolean usageCheck(Player player) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean requiresShield() {
        // TODO Auto-generated method stub
        return false;
    }


}
