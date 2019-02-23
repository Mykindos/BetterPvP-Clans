package net.betterpvp.clans.clans.map;

import net.betterpvp.clans.Clans;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapView;

public class OneHandedRenderer extends MinimapRenderer
{
	public OneHandedRenderer(int scale, int cpr, Clans plugin)
	{
		super(scale, cpr, plugin);
	}

	public void render(MapView map, MapCanvas canvas, Player player)
	{
		if ((player.getItemInHand().getType() != Material.MAP)) {
			return;
		}
		super.render(map, canvas, player);
	}
}
