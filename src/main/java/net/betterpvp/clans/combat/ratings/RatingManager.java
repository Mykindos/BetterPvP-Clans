package net.betterpvp.clans.combat.ratings;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class RatingManager extends BPVPListener<Clans> {

    public RatingManager(Clans instance) {
        super(instance);
    }

    // Decay every 2 hours
    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.MIN_128) {
            new BukkitRunnable(){
                @Override
                public void run(){
                    for (Gamer gamer : GamerManager.getGamers()) {
                        for (Map.Entry<String, Rating> entry : gamer.getRatings().entrySet()) {
                            if (UtilTime.elapsed(entry.getValue().getLastKill(), (86400000 * 2))) {
                                if (entry.getValue().getRating() > 1700) {
                                    entry.getValue().setRating(entry.getValue().getRating() - 1);
                                    RatingRepository.updateRating(gamer, entry.getKey());
                                }
                            }
                        }
                    }
                }
            }.runTaskAsynchronously(getInstance());

        }
    }
}
