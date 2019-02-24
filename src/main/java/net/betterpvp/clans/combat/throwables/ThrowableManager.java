package net.betterpvp.clans.combat.throwables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.combat.throwables.events.ThrowableCollideEntityEvent;
import net.betterpvp.clans.combat.throwables.events.ThrowableHitGroundEvent;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilEntity;
import net.betterpvp.core.utility.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class ThrowableManager extends BPVPListener<Clans> {

	private static List<Throwables> throwables = new ArrayList<>();

	public ThrowableManager(Clans i){
		super(i);
	}

	public static List<Throwables> getThrowables(){
		return throwables;
	}

	public static void addThrowable(Item i, LivingEntity ent, String skillName, long expire){
		throwables.add(new Throwables(i, ent, skillName, expire));
	}

	public Throwables getThrowable(Item i){
		for(Throwables t : getThrowables()){
			if(t.getItem().equals(i)){
				return t;
			}
		}
		return null;
	}

	@EventHandler
	public void removeThrowables(UpdateEvent e){
		if(e.getType() == UpdateEvent.UpdateType.FASTEST){
			Iterator<Throwables> it = throwables.iterator();
			while(it.hasNext()){
				Throwables throwable = it.next();
				if(System.currentTimeMillis() - throwable.getExpireTime() > 0){
					throwable.getItem().remove();
					it.remove();
				}
			}
		}
	}

	@EventHandler
	public void onPickup(PlayerPickupItemEvent e){
		if(getThrowable(e.getItem()) != null){

			//Bukkit.getPluginManager().callEvent(new ThrowableCollideEntityEvent(getThrowable(e.getItem()), e.getPlayer()));
			e.setCancelled(true);
		}

	}

	@EventHandler
	public void onHopperPickup(InventoryPickupItemEvent event) {
		if (event.isCancelled()) {
			return;
		}

		if(getThrowable(event.getItem()) != null){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onMerge(ItemMergeEvent e){
		if(getThrowable(e.getEntity()) != null){
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void checkCollision(UpdateEvent e){
		if(e.getType() == UpdateEvent.UpdateType.TICK){
			for(Throwables t : getThrowables()){
			
				if(!t.getItem().isDead()) {
					
					if(UtilBlock.isGrounded(t.getItem())){
						Bukkit.getPluginManager().callEvent(new ThrowableHitGroundEvent(t));
					}

					for(LivingEntity ent : t.getItem().getWorld().getLivingEntities()){
						
						if(t.getImmunes().contains(ent)){
							continue;
						}
					
						if(ent instanceof Player){
							if(((Player)ent).getGameMode() == GameMode.SPECTATOR || ((Player)ent).getGameMode() == GameMode.CREATIVE) continue;
						}
						if(UtilEntity.collide(t.getItem().getLocation(), ent, 1.5)){
							Bukkit.getPluginManager().callEvent(new ThrowableCollideEntityEvent(t, ent));
							
						}
						
						if(t.isCheckingHead()){
							if(UtilEntity.collide(t.getItem().getLocation().add(0, 1, 0), ent, 1.0)){
								Bukkit.getPluginManager().callEvent(new ThrowableCollideEntityEvent(t, ent));
								
							}
						}

					}

					for(LivingEntity ent : UtilPlayer.getAllInRadius(t.getItem().getLocation(), 1.0)){
						if(ent == t.getThrower()) continue;
						if(ent instanceof Player){
							if(((Player)ent).getGameMode() != GameMode.SURVIVAL) continue;
						}
						Bukkit.getPluginManager().callEvent(new ThrowableCollideEntityEvent(t, ent));
					}

				}

			}

		}
	}
	
	@EventHandler
	public void playerQuit(PlayerQuitEvent e){
		ListIterator<Throwables> it = getThrowables().listIterator();
		while(it.hasNext()){
			Throwables t = it.next();
			if(t.getThrower() == e.getPlayer()){
				t.getItem().remove();
			}
		}
	}



}
