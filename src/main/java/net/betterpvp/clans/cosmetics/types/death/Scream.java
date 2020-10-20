package net.betterpvp.clans.cosmetics.types.death;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDeathEvent;
import net.betterpvp.clans.cosmetics.Cosmetic;
import net.betterpvp.clans.cosmetics.CosmeticType;
import net.betterpvp.core.donation.DonationExpiryTimes;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

public class Scream extends Cosmetic {

    public Scream(Clans instance) {
        super(instance);
    }

    @Override
    public CosmeticType getCosmeticType() {
        return CosmeticType.DEATHEFFECT;
    }

    @Override
    public String getName() {
        return "Scream";
    }

    @Override
    public String getDisplayName() {
        return "Scream";
    }

    @Override
    public long getExpiryTime() {
        return DonationExpiryTimes.NONE;
    }

    @EventHandler
    public void onKill(CustomDeathEvent e){
        if(getActive().contains(e.getKilled().getUniqueId())){
            e.getKilled().getWorld().playSound(e.getKilled().getLocation(), Sound.ENTITY_WOLF_HOWL, 0.5f, 2f);
        }
    }
}
