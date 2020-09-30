package net.betterpvp.clans.worldevents.types.TimedEvents.ads;

import net.betterpvp.clans.worldevents.types.WorldEventMinion;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EventVillager extends WorldEventMinion {

    public EventVillager(LivingEntity ent) {
        super(ent);

        ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getMaxHealth());
        ent.setHealth(getMaxHealth());

    }

    @Override
    public String getDisplayName() {

        return ChatColor.AQUA + "Villager";
    }

    @Override
    public double getMaxHealth() {

        return 10;
    }

}
