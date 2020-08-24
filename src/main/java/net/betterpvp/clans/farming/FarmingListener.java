package net.betterpvp.clans.farming;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class FarmingListener extends BPVPListener<Clans> {

    public FarmingListener(Clans instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent e) {
        Block b = e.getBlock();

        if (ClientUtilities.getOnlineClient(e.getPlayer()).isAdministrating()) {
            return;
        }


        Clan clan = ClanUtilities.getClan(b.getLocation());
        if (clan != null) {
            int bonusLevels = 4 + ((clan.getLevel() - 1) * 4);
            int minY = Clans.getOptions().getFarmingMaxY() - bonusLevels;
            if (FarmBlocks.isSeed(b.getType())) {
                if (b.getLocation().getY() > Clans.getOptions().getFarmingMaxY() || Math.ceil(b.getLocation().getY()) < minY) {
                    UtilMessage.message(e.getPlayer(), "Farming", "You can only cultivate between "
                            + ChatColor.GREEN + Clans.getOptions().getFarmingMaxY() + ChatColor.GRAY + " and " + ChatColor.GREEN + minY + ChatColor.GRAY + " Y.");
                    e.setCancelled(true);
                }
            }
        } else {
            if (FarmBlocks.isSeed(b.getType()) || FarmBlocks.isCultivation(b.getType())) {
                UtilMessage.message(e.getPlayer(), "Cultivation", "You cannot cultivate in the wilderness.");
                e.setCancelled(true);
            }
        }


    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent e) {
        if (e.getHand() == EquipmentSlot.OFF_HAND) return;

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Client c = ClientUtilities.getOnlineClient(e.getPlayer());
            if (c != null) {
                if (c.isAdministrating()) {
                    return;
                }
            }

            if (e.getClickedBlock().getType() != Material.FARMLAND) {
                return;
            }

            Clan clan = ClanUtilities.getClan(e.getClickedBlock().getLocation());
            if (clan != null) {
                Block b = e.getClickedBlock();
                int bonusLevels = 4 + ((clan.getLevel() - 1) * 4);
                int minY = Clans.getOptions().getFarmingMaxY() - bonusLevels;
                if (FarmBlocks.isSeed(e.getPlayer().getInventory().getItemInMainHand().getType())) {
                    if (b.getLocation().getY() > Clans.getOptions().getFarmingMaxY() || Math.ceil(b.getLocation().getY()) < minY) {
                        UtilMessage.message(e.getPlayer(), "Farming", "You can only cultivate between "
                                + ChatColor.GREEN + Clans.getOptions().getFarmingMaxY() + ChatColor.GRAY + " and " + ChatColor.GREEN + minY + ChatColor.GRAY + " Y.");
                        e.setCancelled(true);
                    }
                }
            }

        }
    }

    @EventHandler
    public void onGrow(BlockGrowEvent e) {

        Material type = e.getNewState().getType();
        if (type == Material.SUGAR_CANE || type.name().contains("MELON") || type.name().contains("PUMPKIN")) {
            Clan clan = ClanUtilities.getClan(e.getBlock().getChunk());
            if (clan != null) {
                if (!clan.isOnline()) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
