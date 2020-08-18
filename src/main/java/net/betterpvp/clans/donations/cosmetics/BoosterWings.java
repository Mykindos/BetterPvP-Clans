package net.betterpvp.clans.donations.cosmetics;

import net.betterpvp.clans.Clans;
import net.betterpvp.core.particles.ParticleEffect;
import net.betterpvp.core.particles.data.color.RegularColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BoosterWings extends CosmeticWings{

    public BoosterWings(Clans clans) {
        super(clans);
    }

    @Override
    String getSetting() {
        return "Cosmetics.Booster Wings";
    }

    @Override
    void run(Player player) {
        if (!canShow(player)) return;

        Location loc = player.getLocation().clone();
        loc.setPitch(0.0F);
        loc.add(0.0D, 0.0D, 0.0D);
        loc.add(loc.getDirection().multiply(-0.2D));
        loc.setYaw(loc.getYaw() + 75);
        Location loc2 = loc.clone();

        display(loc, loc2, new RegularColor(255,105,180), new RegularColor(255,105,180), new RegularColor(255,105,180));

        loc = player.getLocation().clone();
        loc.setPitch(0.0F);
        loc.add(0.0D, 0.0D, 0.0D);
        loc.add(loc.getDirection().multiply(-0.2D));
        loc.setYaw(loc.getYaw() - 75);
        loc2 = loc.clone();
        display(loc, loc2, new RegularColor(255,105,180), new RegularColor(255,105,180), new RegularColor(255,105,180));
    }

    @Override
    public String getName() {
        return "BoosterWings";
    }

    @Override
    public String getDisplayName() {
        return "Booster Wings";
    }

    @Override
    public long getExpiryTime() {
        return 0;
    }
}
