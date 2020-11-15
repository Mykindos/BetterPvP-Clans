package net.betterpvp.clans.cosmetics.types.particles;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.cosmetics.Cosmetic;
import net.betterpvp.clans.cosmetics.CosmeticType;
import net.betterpvp.core.donation.DonationExpiryTimes;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.particles.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.UUID;

public class Hearts extends Cosmetic {

    public Hearts(Clans instance) {
        super(instance);
    }

    @Override
    public CosmeticType getCosmeticType() {
        return CosmeticType.PARTICLE;
    }

    @Override
    public String getName() {
        return "Hearts";
    }

    @Override
    public String getDisplayName() {
        return "Hearts";
    }

    @Override
    public long getExpiryTime() {
        return DonationExpiryTimes.NONE;
    }

    @EventHandler
    public void onUpdate(UpdateEvent e){
        if(e.getType() == UpdateEvent.UpdateType.SEC){
            for(UUID uuid : getActive()){
                final Player player = Bukkit.getPlayer(uuid);
                if(player != null){
                    if(!canShow(player)) continue;
                    List<Player> playerList = getClosePlayers(player);
                    int radius = 1;
                    for(int q = 0; q <= 5; q++){
                        final float x = (float) (radius * Math.cos(q));
                        final float z = (float) (radius * Math.sin(q));

                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                ParticleEffect.HEART.display(player.getLocation().add(x, 2, z), playerList);
                            }
                        }.runTaskLater(getInstance(), q * 5L);
                    }
                }
            }
        }
    }
}
