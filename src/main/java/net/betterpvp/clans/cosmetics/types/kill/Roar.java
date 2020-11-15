package net.betterpvp.clans.cosmetics.types.kill;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDeathEvent;
import net.betterpvp.clans.cosmetics.Cosmetic;
import net.betterpvp.clans.cosmetics.CosmeticType;
import net.betterpvp.core.donation.DonationExpiryTimes;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

public class Roar extends Cosmetic {

    public Roar(Clans instance) {
        super(instance);
    }

    @Override
    public CosmeticType getCosmeticType() {
        return CosmeticType.KILLEFFECT;
    }

    @Override
    public String getName() {
        return "Roar";
    }

    @Override
    public String getDisplayName() {
        return "Roar";
    }

    @Override
    public long getExpiryTime() {
        return DonationExpiryTimes.NONE;
    }

    @EventHandler
    public void onDeath(CustomDeathEvent e){

        if(getActive().contains(e.getKiller().getUniqueId())){
            e.getKiller().getWorld().playSound(e.getKiller().getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 0.8f);
        }

    }
}
