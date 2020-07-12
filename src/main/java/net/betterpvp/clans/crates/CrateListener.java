package net.betterpvp.clans.crates;

import net.betterpvp.clans.Clans;
import net.betterpvp.core.framework.BPVPListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class CrateListener extends BPVPListener<Clans> {


    public CrateListener(Clans i){
        super(i);
    }

    @EventHandler
    public void onCrateUse(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(p.getInventory().getItemInHand() != null){
            if(p.getItemInHand().getType().name().toLowerCase().contains("chest")){
                if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK){
                    if(p.getItemInHand() != null){
                        if(p.getItemInHand().hasItemMeta()){
                            if(CrateManager.isCrate(p.getItemInHand())){
                                CrateManager.openPreview(p, CrateManager.getCrate(p.getItemInHand().getItemMeta().getDisplayName()));
                            }
                        }
                    }
                }else if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
                    if(p.getItemInHand() != null){
                        if(p.getItemInHand().hasItemMeta()){
                            if(CrateManager.isCrate(p.getItemInHand())){
                                CrateManager.openCrate(getInstance(), p, CrateManager.getCrate(p.getItemInHand().getItemMeta().getDisplayName()));
                                if(p.getItemInHand().getAmount() == 1){
                                    p.getInventory().setItemInHand(null);
                                    return;
                                }
                                p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);

                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        if(e.getPlayer().getItemInHand() != null){
            if(e.getPlayer().getItemInHand().hasItemMeta()){
                if(CrateManager.isCrate(e.getPlayer().getItemInHand())){
                    e.setCancelled(true);
                }
            }
        }

    }


}