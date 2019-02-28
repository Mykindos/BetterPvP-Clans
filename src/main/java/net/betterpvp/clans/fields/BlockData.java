package net.betterpvp.clans.fields;

import org.bukkit.Location;
import org.bukkit.Material;

public class BlockData {

    private Location loc;
    private Material type;

    public BlockData(Location loc, Material type) {
        this.loc = loc;
        this.type = type;
    }

    public Location getLoc() {
        return loc;
    }

    public Material getType() {
        return type;
    }

}
