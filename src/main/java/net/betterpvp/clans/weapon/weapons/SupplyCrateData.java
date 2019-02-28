package net.betterpvp.clans.weapon.weapons;

import org.bukkit.Location;

public class SupplyCrateData {


    private Location loc;
    private int count = 60;

    public SupplyCrateData(Location loc) {
        this.loc = loc;
    }

    public SupplyCrateData(Location loc, int count) {
        this.loc = loc;
        this.count = count;
    }

    public Location getLocation() {
        return loc;
    }

    public int getCount() {
        return count;
    }

    public void takeCount() {
        this.count--;
    }

}
