package net.betterpvp.clans.clans.map;

import net.betterpvp.clans.Clans;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;

public class SendTask extends BukkitRunnable
{
	public void run()
	{
		if(Clans.getOptions().isMapEnabled()) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.getItemInHand().getType() == Material.MAP){

					if(p.getItemInHand().getDurability() != MinimapRenderer.usedMaps.get(p).current) {
						p.getItemInHand().setDurability(MinimapRenderer.usedMaps.get(p).current);


					}

					MapView map = Bukkit.getMap(MinimapRenderer.usedMaps.get(p).current);
					if (map != null) {

						p.sendMap(map);
					}

				}
			}
		}


	}
}
