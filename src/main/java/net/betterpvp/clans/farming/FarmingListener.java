package net.betterpvp.clans.farming;

import net.betterpvp.clans.Clans;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class FarmingListener extends BPVPListener<Clans> {

    public FarmingListener(Clans instance) {
        super(instance);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Block b = e.getBlock();

        if (ClientUtilities.getOnlineClient(e.getPlayer()).isAdministrating()) {
            return;
        }


        if (e.getBlock().getType() == Material.PISTON) {
            if (b.getLocation().getY() > Clans.getOptions().getFarmingMinY() && b.getLocation().getY() < Clans.getOptions().getFarmingMaxY()) {
                UtilMessage.message(e.getPlayer(), "Farming", "You cannot place regular pistons within the farming levels.");
                e.setCancelled(true);
                return;

            }
        }

        if (FarmBlocks.isSeed(b.getType())) {

            if (b.getLocation().getY() > Clans.getOptions().getFarmingMaxY() || b.getLocation().getY() < Clans.getOptions().getFarmingMinY()) {
                UtilMessage.message(e.getPlayer(), "Farming", "You can only cultivate between 50 and 58 Y.");
                e.setCancelled(true);

            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Client c = ClientUtilities.getOnlineClient(e.getPlayer());
            if (c != null) {
                if (c.isAdministrating()) {
                    return;
                }
            }


            if (FarmBlocks.isSeed(e.getPlayer().getInventory().getItemInMainHand().getType())) {
                if (e.getClickedBlock().getLocation().getY() > Clans.getOptions().getFarmingMaxY()
                        || e.getClickedBlock().getLocation().getY() < Clans.getOptions().getFarmingMinY()) {
                    UtilMessage.message(e.getPlayer(), "Farming", "You can only cultivate between 50 and 58 Y");
                    e.setCancelled(true);

                }
            }
        }
    }
}
