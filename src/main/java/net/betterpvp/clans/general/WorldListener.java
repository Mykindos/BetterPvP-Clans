package net.betterpvp.clans.general;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.skills.selector.page.ClassSelectionPage;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldListener extends BPVPListener<Clans> {
    public WorldListener(Clans instance) {
        super(instance);
    }


    /**
     * Speeds up the night time
     * @param e
     */
    @EventHandler
    public void onTimeUpdate(UpdateEvent e) {
        if(e.getType() == UpdateEvent.UpdateType.TICK_2){
            World world = Bukkit.getWorld("world");
            if(world.getTime() > 13000){
                world.setTime(world.getTime() + 20);
            }
        }
    }

    /**
     * Opens the Build Management menu
     * @param e
     */
    @EventHandler
    public void onOpenBuildManager(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block b = e.getClickedBlock();
            if (b.getType() == Material.ENCHANTMENT_TABLE) {

                e.getPlayer().openInventory(new ClassSelectionPage(e.getPlayer()).getInventory());
                e.setCancelled(true);
            }
        }
    }

    /**
     * Stops it from raining
     * @param e
     */
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        if (!e.getWorld().hasStorm() && !e.getWorld().isThundering()) {
            e.setCancelled(true);
        }
    }
}
