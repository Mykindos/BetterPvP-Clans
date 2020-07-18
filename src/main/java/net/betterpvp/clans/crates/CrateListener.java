package net.betterpvp.clans.crates;

import net.betterpvp.clans.Clans;
import net.betterpvp.core.framework.BPVPListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CrateListener extends BPVPListener<Clans> {


    public CrateListener(Clans i) {
        super(i);
    }

    @EventHandler
    public void onCrateUse(PlayerInteractEvent e) {
        if(e.getHand() == EquipmentSlot.OFF_HAND) return;
        if(Clans.getOptions().isVotingCratesEnabled()) {
            Player p = e.getPlayer();
            if (p.getInventory().getItemInMainHand() != null) {
                if (p.getInventory().getItemInMainHand().getType().name().toLowerCase().contains("chest")) {
                /*if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                    if (p.getItemInHand() != null) {
                        if (p.getInventory().getItemInMainHand().hasItemMeta()) {
                            if (CrateManager.isCrate(p.getItemInHand())) {
                                CrateManager.openPreview(p, CrateManager.getCrate(p.getItemInHand().getItemMeta().getDisplayName()));
                            }
                        }
                    }
                } else*/
                    if (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR) {
                        if (p.getInventory().getItemInMainHand() != null) {
                            if (p.getInventory().getItemInMainHand().hasItemMeta()) {
                                if (CrateManager.isCrate(p.getInventory().getItemInMainHand())) {
                                    CrateManager.openCrate(getInstance(), p, CrateManager.getCrate(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()));
                                    if (p.getInventory().getItemInMainHand().getAmount() - 1 <= 0) {
                                        p.getInventory().setItemInMainHand(null);
                                        return;
                                    }
                                    p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (e.getItemInHand() != null) {
            if (e.getItemInHand().hasItemMeta()) {
                if (CrateManager.isCrate(e.getItemInHand())) {
                    e.setCancelled(true);
                }
            }
        }


    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (CrateManager.isCrate(e.getPlayer().getInventory().getItemInMainHand())) {
                e.setCancelled(true);
            }
        }
    }


}