package net.betterpvp.clans.worldevents.types.bosses.ads;

import net.betterpvp.clans.worldevents.types.WorldEventMinion;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.PolarBear;
import org.bukkit.entity.Snowman;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class SnowGolemMinion extends WorldEventMinion {

    public SnowGolemMinion(Snowman golem) {
        super(golem);

        golem.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getMaxHealth());
        golem.setHealth(getMaxHealth());
        golem.setCustomName(getDisplayName());
        golem.setCustomNameVisible(true);
        golem.setRemoveWhenFarAway(false);
        golem.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));

    }

    @Override
    public String getDisplayName() {
        return ChatColor.BLUE.toString() + ChatColor.BOLD + "Boris' Experiment";
    }

    @Override
    public double getMaxHealth() {
        return 25;
    }
}
