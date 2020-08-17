package net.betterpvp.clans.donations.cosmetics;

import net.betterpvp.clans.Clans;
import net.betterpvp.core.particles.data.color.RegularColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.awt.*;

public class KnightWings extends CosmeticWings{

    public KnightWings(Clans clans) {
        super(clans);
    }

    @Override
    String getSetting() {
        return "Cosmetics.Knight Wings";
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

        display(loc, loc2, new RegularColor(Color.WHITE), new RegularColor(Color.WHITE), new RegularColor(Color.WHITE));

        loc = player.getLocation().clone();
        loc.setPitch(0.0F);
        loc.add(0.0D, 0.0D, 0.0D);
        loc.add(loc.getDirection().multiply(-0.2D));
        loc.setYaw(loc.getYaw() - 75);
        loc2 = loc.clone();
        display(loc, loc2, new RegularColor(Color.WHITE), new RegularColor(Color.WHITE), new RegularColor(Color.WHITE));
    }

    @Override
    public String getName() {
        return "KnightWings";
    }

    @Override
    public String getDisplayName() {
        return "Knight Wings";
    }

    @Override
    public long getExpiryTime() {
        return 0;
    }
}
