package net.betterpvp.clans.clans.map.NMS;

import net.betterpvp.clans.clans.Clan;
import org.bukkit.block.Block;

public interface INMSHandler {
    MaterialMapColorInterface getColorNeutral();

    MaterialMapColorInterface getBlockColor(Clan c, boolean outline, Block paramBlock);


}
