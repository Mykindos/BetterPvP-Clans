package net.betterpvp.clans.donations.cosmetics;

import net.betterpvp.clans.Clans;
import net.betterpvp.core.particles.data.color.RegularColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PaladinWings extends CosmeticWings{

    public PaladinWings(Clans clans) {
        super(clans);
    }

    @Override
    String getSetting() {
        return "Cosmetics.Paladin Wings";
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

        display(loc, loc2, new RegularColor(249, 251, 117), new RegularColor(249, 251, 117), new RegularColor(249, 251, 117));

        loc = player.getLocation().clone();
        loc.setPitch(0.0F);
        loc.add(0.0D, 0.0D, 0.0D);
        loc.add(loc.getDirection().multiply(-0.2D));
        loc.setYaw(loc.getYaw() - 75);
        loc2 = loc.clone();
        display(loc, loc2, new RegularColor(249, 251, 117), new RegularColor(249, 251, 117), new RegularColor(249, 251, 117));
    }

    @Override
    public String getName() {
        return "PaladinWings";
    }

    @Override
    public String getDisplayName() {
        return "Paladin Wings";
    }

    @Override
    public long getExpiryTime() {
        return 0;
    }
}
