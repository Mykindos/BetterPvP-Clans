package net.betterpvp.clans.cosmetics.types.kill;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDeathEvent;
import net.betterpvp.clans.cosmetics.Cosmetic;
import net.betterpvp.clans.cosmetics.CosmeticType;
import net.betterpvp.core.donation.DonationExpiryTimes;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

public class EvilLaugh extends Cosmetic {

    public EvilLaugh(Clans instance) {
        super(instance);
    }

    @Override
    public CosmeticType getCosmeticType() {
        return CosmeticType.KILLEFFECT;
    }

    @Override
    public String getName() {
        return "EvilLaugh";
    }

    @Override
    public String getDisplayName() {
        return "Evil Laugh";
    }

    @Override
    public long getExpiryTime() {
        return DonationExpiryTimes.NONE;
    }

    @EventHandler
    public void onKill(CustomDeathEvent e){
        if(getActive().contains(e.getKiller().getUniqueId())){
            e.getKiller().getWorld().playSound(e.getKiller().getLocation(), Sound.ENTITY_WITCH_CELEBRATE, 2, 1f);
        }
    }
}
