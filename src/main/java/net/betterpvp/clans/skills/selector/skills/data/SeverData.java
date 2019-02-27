package net.betterpvp.clans.skills.selector.skills.data;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SeverData implements Listener{



	private Player damagee;
	private BukkitTask task;

	public SeverData(Clans i, final Player damagee, final Player damager, final int level){
		Bukkit.getPluginManager().registerEvents(this, i);
		this.damagee = damagee;
		task = new BukkitRunnable(){
			int count = 0;
			@Override
			public void run() {
				if(count >= (level)){
					this.cancel();
				}else{
					if(damagee != null){
						if(damager != null){
							if(RechargeManager.getInstance().add(damagee, "Sever-Damage", 0.75, false)){
								Bukkit.getPluginManager().callEvent(new CustomDamageEvent(damagee, damager, null,
										DamageCause.CUSTOM, 1.5, false, "Sever"));
								LogManager.addLog(damagee, damager, "Sever");
							}
						}
					}

					count++;
				}

			}


		}.runTaskTimer(i, 20L, 20L);
	}


	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		if(e.getEntity().equals(damagee)){
			task.cancel();
			damagee = null;
			task = null;
		}
	}

}
