package net.betterpvp.clans.worldevents.types.bosses.ads;

import net.betterpvp.clans.worldevents.types.WorldEventMinion;
import net.betterpvp.clans.worldevents.types.nms.BossPolarBear;
import net.minecraft.server.v1_16_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.PolarBear;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class PolarBearCub extends WorldEventMinion {

    public PolarBearCub(PolarBear bear) {
        super(bear);

        bear.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getMaxHealth());
        bear.setHealth(getMaxHealth());
        bear.setCustomName(getDisplayName());
        bear.setCustomNameVisible(true);
        bear.setRemoveWhenFarAway(false);
        bear.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        bear.setBaby();

    }



    @Override
    public String getDisplayName() {
        return ChatColor.BLUE.toString() + ChatColor.BOLD + "Polar Bear cub";
    }

    @Override
    public double getMaxHealth() {
        return 75;
    }
}
