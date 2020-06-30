package net.betterpvp.clans.economy.shops;

import net.betterpvp.clans.Clans;
import net.betterpvp.core.framework.BPVPListener;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTargetEvent;

public class ShopEntities extends BPVPListener<Clans> {

    public ShopEntities(Clans i) {
       super(i);

    }

    @EventHandler
    public void acquireTarget(EntityTargetEvent e) {

        if (e.getEntity().getCustomName() != null) {
            if (isShop(e.getEntity())) {
                e.setCancelled(true);
            }
        }

    }

	/*
	@EventHandler
	public void entDamage(EntityDamageByEntityEvent e){
		if(e.getEntity().getCustomName() != null){
			if(isShop(e.getEntity())){
				if(e.getDamager() instanceof Player){
					Client c = ClientUtilities.getClient((Player) e.getDamager());
					if(c.hasRank(Rank.ADMIN, false)){
						return;	
					}
				}
				LivingEntity ent = (LivingEntity) e.getEntity();
				ent.setHealth(ent.getMaxHealth());

				e.setDamage(0);
				e.setCancelled(true);
			}
		}
	}
	 */


    public static boolean isShop(Entity e) {
        return e.getCustomName().equalsIgnoreCase(ChatColor.GREEN + "Armour")
                || e.getCustomName().equalsIgnoreCase(ChatColor.GREEN + "Tools / Weapons")
                || e.getCustomName().equalsIgnoreCase(ChatColor.GREEN + "building")
                || e.getCustomName().equalsIgnoreCase(ChatColor.GREEN + "resources")
                || e.getCustomName().equalsIgnoreCase(ChatColor.GREEN + "food")
                || e.getCustomName().equalsIgnoreCase(ChatColor.GREEN + "Beta");
    }
}

