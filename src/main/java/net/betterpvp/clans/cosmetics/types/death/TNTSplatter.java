package net.betterpvp.clans.cosmetics.types.death;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.cosmetics.Cosmetic;
import net.betterpvp.clans.cosmetics.CosmeticType;
import net.betterpvp.core.donation.DonationExpiryTimes;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TNTSplatter extends Cosmetic {

    public TNTSplatter(Clans instance) {
        super(instance);
    }

    @Override
    public CosmeticType getCosmeticType() {
        return CosmeticType.DEATHEFFECT;
    }

    @Override
    public String getName() {
        return "TNTSplatter";
    }

    @Override
    public String getDisplayName() {
        return "TNT Splatter";
    }

    @Override
    public long getExpiryTime() {
        return DonationExpiryTimes.NONE;
    }

    private HashMap<Item, Long> blood = new HashMap<>();
    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        if(getActive().contains(e.getEntity().getUniqueId())){
            for(int i = 0; i < 10; i++){
                final Item item = e.getEntity().getWorld().dropItem(e.getEntity().getEyeLocation(), new ItemStack(Material.TNT));
                item.setVelocity(new Vector((Math.random() - 0.5D) * 0.5, Math.random() * 0.5, (Math.random() - 0.5D) * 0.5));
                item.setPickupDelay(Integer.MAX_VALUE);
                blood.put(item, System.currentTimeMillis());
            }
        }
    }

    @EventHandler
    public void onHopperPickup(InventoryPickupItemEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if(blood.containsKey(event.getItem())){
            event.setCancelled(true);
        }
    }



    @EventHandler
    public void blood(UpdateEvent e){
        if(e.getType() == UpdateEvent.UpdateType.TICK){
            Iterator<Map.Entry<Item, Long>> it = blood.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<Item, Long> next = it.next();
                if(UtilTime.elapsed(next.getValue(), 500)){
                    next.getKey().remove();
                    it.remove();
                }
            }
        }
    }
}
