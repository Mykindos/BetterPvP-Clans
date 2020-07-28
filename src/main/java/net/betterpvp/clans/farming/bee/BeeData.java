package net.betterpvp.clans.farming.bee;

import net.betterpvp.core.utility.UtilMath;
import org.bukkit.Location;

public class BeeData {

    private Location loc;
    private long harvestTime;
    private boolean harvestable;

    public BeeData(Location loc) {
        this.loc = loc;
        this.harvestable = false;
        updateHarvestTime();
    }

    public Location getLoc() {
        return loc;
    }

    public void updateHarvestTime(){
        this.harvestTime = System.currentTimeMillis() + UtilMath.randomInt(1800000, 5400000);
    }

    public long getHarvestTime() {
        return harvestTime;
    }

    public boolean isHarvestable() {
        return harvestable;
    }

    public void setHarvestable(boolean harvestable) {
        this.harvestable = harvestable;
    }
}
