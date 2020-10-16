package net.betterpvp.clans.worldevents.types.TimedEvents.ads;

import net.betterpvp.clans.worldevents.types.WorldEventMinion;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EventVindicator extends WorldEventMinion {

    public EventVindicator(LivingEntity ent) {
        super(ent);

        ent.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
        ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getMaxHealth());
        ent.setHealth(getMaxHealth());

    }

    @Override
    public String getDisplayName() {

        return ChatColor.RED + "Vindicator";
    }

    @Override
    public double getMaxHealth() {

        return 25;
    }

}
