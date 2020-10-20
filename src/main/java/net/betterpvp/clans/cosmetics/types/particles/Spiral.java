package net.betterpvp.clans.cosmetics.types.particles;

import com.google.common.util.concurrent.AtomicDouble;
import net.betterpvp.clans.Clans;
import net.betterpvp.clans.cosmetics.Cosmetic;
import net.betterpvp.clans.cosmetics.CosmeticType;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.core.donation.DonationExpiryTimes;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.particles.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Spiral extends Cosmetic {

    public Spiral(Clans instance) {
        super(instance);
    }

    @Override
    public CosmeticType getCosmeticType() {
        return CosmeticType.PARTICLE;
    }

    @Override
    public String getName() {
        return "Spiral";
    }

    @Override
    public String getDisplayName() {
        return "Spiral";
    }

    @Override
    public long getExpiryTime() {
        return DonationExpiryTimes.NONE;
    }

    @EventHandler
    public void onUpdate(UpdateEvent e){
        if(e.getType() == UpdateEvent.UpdateType.SLOW){
            for(UUID uuid : getActive()){
                final Player player = Bukkit.getPlayer(uuid);
                if(player != null){
                    if(!canShow(player)) continue;

                    int radius = 1;
                    AtomicDouble y = new AtomicDouble(0);
                    for(int q = 0; q <=100; q++){
                        final float x = (float) (radius * Math.cos(q));
                        final float z = (float) (radius * Math.sin(q));

                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                if(!EffectManager.hasEffect(player, EffectType.INVISIBILITY)) {
                                    ParticleEffect.FIREWORKS_SPARK.display(player.getLocation().add(x, y.doubleValue(), z));
                                    y.set(y.doubleValue() + 0.025f);
                                }
                            }
                        }.runTaskLater(getInstance(), q * 1L);
                    }
                }
            }
        }
    }
}
