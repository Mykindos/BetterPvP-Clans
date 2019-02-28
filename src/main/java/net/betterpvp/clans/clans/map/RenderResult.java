package net.betterpvp.clans.clans.map;

import net.betterpvp.clans.clans.map.NMS.MaterialMapColorInterface;

public class RenderResult {
    private MaterialMapColorInterface color;
    private short avgY;

    public RenderResult(MaterialMapColorInterface color, short avgY) {
        this.color = color;
        this.avgY = avgY;
    }

    public MaterialMapColorInterface getColor() {
        return this.color;
    }

    public short getAverageY() {
        return this.avgY;
    }
}
