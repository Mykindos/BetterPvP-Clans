package net.betterpvp.clans.cosmetics.types.particles;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.cosmetics.Cosmetic;
import net.betterpvp.clans.cosmetics.CosmeticType;
import net.betterpvp.core.donation.DonationExpiryTimes;
import net.betterpvp.core.particles.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class Flame extends Cosmetic {

    public Flame(Clans instance) {
        super(instance);
    }

    @Override
    public CosmeticType getCosmeticType() {
        return CosmeticType.PARTICLE;
    }

    @Override
    public String getName() {
        return "Flame";
    }

    @Override
    public String getDisplayName() {
        return "Flame";
    }

    @Override
    public long getExpiryTime() {
        return DonationExpiryTimes.NONE;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player player = e.getPlayer();
        if(getActive().contains(player.getUniqueId())){
            if(!canShow(player)) return;
            if(e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()){
                float radius = 0.7f;
                for(float y = 0; y <= 0.5; y+=0.25) {
                    for (int q = 0; q <= 5; q++) {
                        float x = (float) (radius * Math.cos(q) * 3);
                        float z = (float) (radius * Math.sin(q) * 3);
                        ParticleEffect.FLAME.display(player.getLocation(), new Vector(x, y, z).normalize().multiply(0.5), 0.015f, 1, null, Bukkit.getOnlinePlayers());
                    }
                }
            }
        }

    }
}
