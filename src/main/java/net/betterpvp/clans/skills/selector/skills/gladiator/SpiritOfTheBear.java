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
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpiritOfTheBear extends Skill implements InteractSkill {


    public SpiritOfTheBear(Clans i) {
        super(i, "Spirit of the Bear", "Gladiator", getAxes, rightClick, 5, true, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Right click with a axe to activate.",
                "",
                "Call upon the spirit of the bear",
                "granting all allies within " + ChatColor.GREEN + (5 + (level)) + ChatColor.GRAY + " blocks",
                "Resistance II for 5 seconds.",
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

        return 20 - ((level - 1) * 2);
    }

    @Override
    public float getEnergy(int level) {

        return 0;
    }

    @Override
    public boolean usageCheck(Player player) {

        return true;
    }


    @Override
    public void activate(Player player, Gamer gamer) {
        UtilMessage.message(player, getClassType(), "You used " + ChatColor.GREEN + getName() + " " + getLevel(player));
        player.getWorld().playSound(player.getLocation().add(0.0, -1.0, 0.0), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.8F, 2.5F);
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1));
        EffectManager.addEffect(player, EffectType.RESISTANCE, 5000);
        for (Player p : UtilPlayer.getInRadius(player.getLocation(), (5 + getLevel(player)))) {
            if (!ClanUtilities.canHurt(player, p)) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1));
                EffectManager.addEffect(p, EffectType.RESISTANCE, 5000);
                UtilMessage.message(p, getClassType(), "You received the spirit of the bear!");
            }
        }
    }
}
