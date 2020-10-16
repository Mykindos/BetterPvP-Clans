package net.betterpvp.clans.skills.selector.skills.data;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;


public class RecallData {

    public List<TempData> locs = new ArrayList<>();
    private long time;


    public RecallData() {
        this.time = System.currentTimeMillis();
    }


    public double getHealth() {
        return locs.get(0).getHealth();
    }

    public void addLocation(Location l, double h, double max) {
        TempData loc = new TempData(l, h);
        locs.add(loc);
        if (locs.size() > max) {
            locs.remove(0);
        }
    }


    public Location getLocation() {
        return locs.get(0).getLocation();

    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    private class TempData {
        private Location l;
        private double h;

        public TempData(Location l, double h) {
            this.l = l;
            this.h = h;
        }

        public Location getLocation() {
            return l;
        }

        public double getHealth() {
            return h;
        }


    }

}
