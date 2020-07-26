package net.betterpvp.clans.clans.map;

import io.github.bananapuncher714.cartographer.core.Cartographer;
import io.github.bananapuncher714.cartographer.core.api.SimpleImage;
import io.github.bananapuncher714.cartographer.core.api.events.minimap.MinimapLoadEvent;
import io.github.bananapuncher714.cartographer.core.api.events.player.MapViewerCreateEvent;
import io.github.bananapuncher714.cartographer.core.api.map.MapCursorProvider;
import io.github.bananapuncher714.cartographer.core.api.map.WorldCursorProvider;
import io.github.bananapuncher714.cartographer.core.map.Minimap;
import io.github.bananapuncher714.cartographer.nbteditor.NBTEditor;
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
import org.bukkit.map.MapView;

import java.util.Iterator;

public class MapListener extends BPVPListener<Clans> {

    public static ItemStack map;
    private static final Object[] MAP_ID = { "io", "github", "bananapuncher714", "cartographer", "map-id" };

    public MapListener(Clans instance) {
        super(instance);
        this.map = UtilClans.updateNames(getItemFor(Cartographer.getInstance().getMapManager().getMinimaps().get("Clans"), 0));
    }

    public ItemStack getItemFor(Minimap map, int mapId) {
        MapView view = Cartographer.getInstance().getHandler().getUtil().getMap(mapId);
        ItemStack mapItem = Cartographer.getInstance().getHandler().getUtil().getMapItem(mapId);

        Cartographer.getInstance().getMapManager().convert(view, map);

        String id = map == null ? "MISSING MAP" : map.getId();
        mapItem = NBTEditor.set(mapItem, id, MAP_ID);

        return mapItem;
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
        event.getMinimap().registerProvider(new ClanPixelProvider(getInstance()));

    }


    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

        if (!Clans.getOptions().isAdvancedMap()) return;
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
            if (!Clans.getOptions().isAdvancedMap()) return;

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getInventory().getItemInMainHand().getType() == Material.FILLED_MAP) {

                    if (!Cartographer.getInstance().getMapManager().isMinimapItem(player.getInventory().getItemInMainHand())) {
                        player.getInventory().setItemInMainHand(map.clone());
                    }


                }
            }
        }
    }

}
