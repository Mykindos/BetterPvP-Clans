package net.betterpvp.clans.worldevents.types.TimedEvents.ads;

import net.betterpvp.clans.worldevents.types.WorldEventMinion;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

public class EventRavager extends WorldEventMinion {

    public EventRavager(LivingEntity ent) {
        super(ent);

        ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getMaxHealth());
        ent.setHealth(getMaxHealth());

    }

    @Override
    public String getDisplayName() {

        return ChatColor.RED + "Ravager";
    }

    @Override
    public double getMaxHealth() {

        return 30;
    }

}
