package net.betterpvp.clans.clans.map;

import io.github.bananapuncher714.cartographer.core.Cartographer;
import io.github.bananapuncher714.cartographer.core.api.SimpleImage;
import io.github.bananapuncher714.cartographer.core.api.events.minimap.MinimapLoadEvent;
import io.github.bananapuncher714.cartographer.core.api.events.player.MapViewerCreateEvent;
import io.github.bananapuncher714.cartographer.core.api.map.MapCursorProvider;
import io.github.bananapuncher714.cartographer.core.api.map.WorldCursorProvider;
import net.betterpvp.clans.Clans;
import net.betterpvp.clans.utilities.UtilClans;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;

public class MapListener extends BPVPListener<Clans> {

    public MapListener(Clans instance) {
        super(instance);
    }

    @EventHandler
    private void onEvent(MinimapLoadEvent event) {

        for (MapCursorProvider prov : event.getMinimap().getMapCursorProviders()) {
            event.getMinimap().unregisterProvider(prov);
        }
        for (WorldCursorProvider prov : event.getMinimap().getWorldCursorProviders()) {
            event.getMinimap().unregisterProvider(prov);
        }

        event.getMinimap().registerProvider(new ClanCursorProvider());
        event.getMinimap().registerProvider(new ClanPixelProvider());

    }

    @EventHandler
    public void onCreate(MapViewerCreateEvent e) {
        e.getViewer().setBackground(null);
        e.getViewer().setOverlay(null);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

        if(!Clans.getOptions().isAdvancedMap()) return;
        Iterator<ItemStack> iterator = e.getDrops().iterator();
        while (iterator.hasNext()) {
            ItemStack next = iterator.next();
            if (next.getType() == Material.FILLED_MAP) {
                iterator.remove();
            }
        }


    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDrop(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack() != null) {
            if (e.getItemDrop().getItemStack().getType() == Material.FILLED_MAP) {
                e.getItemDrop().remove();
            }
        }
    }


    @EventHandler
    public void checkMap(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.SEC) {
            if(!Clans.getOptions().isAdvancedMap()) return;
            ItemStack map = UtilClans.updateNames(Cartographer.getInstance().getMapManager().getItemFor(Cartographer.getInstance().getMapManager().getMinimaps().get("Clans")));

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getInventory().getItemInMainHand().getType() == Material.FILLED_MAP) {

                    if (!Cartographer.getInstance().getMapManager().isMinimapItem(player.getInventory().getItemInMainHand())) {
                        player.getInventory().setItemInMainHand(map);
                    }


                }
            }
        }
    }

}
