package net.betterpvp.clans.cosmetics;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.donation.IDonation;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Cosmetic extends BPVPListener<Clans> implements IDonation {

    private List<UUID> activePlayers;

    public Cosmetic(Clans instance){
        super(instance);
        activePlayers = new ArrayList<>();
    }

    public List<UUID> getActive(){
        return activePlayers;
    }

    public abstract CosmeticType getCosmeticType();

    protected boolean canShow(Player player) {

        if(player.isDead()) {
            return false;
        }

        Gamer gamer = GamerManager.getOnlineGamer(player);
        if (gamer != null) {
            if (!UtilTime.elapsed(gamer.getLastDamaged(), 15000)) {
                return false;
            }

            if (EffectManager.hasEffect(player, EffectType.INVISIBILITY)) {
                return false;
            }
        }

        return true;
    }

}
