package net.betterpvp.clans.worldevents;

import net.betterpvp.clans.Clans;
import net.betterpvp.core.framework.BPVPListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;


public abstract class WorldEvent extends BPVPListener<Clans> {

    private String name;
    private WEType type;

    private boolean active;

    public WorldEvent(Clans i, String name, WEType type) {
        super(i);
        this.name = name;
        this.type = type;

    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean b) {
        active = b;
    }

    public String getName() {
        return name;
    }

    public WEType getType() {
        return type;
    }

    public Location getSpawn() {
        return new Location(Bukkit.getWorld("World"), -4.5, 14, -59.5);
    }

    public abstract String getDisplayName();

    public abstract void spawn();

    public abstract Location[] getTeleportLocations();
}
