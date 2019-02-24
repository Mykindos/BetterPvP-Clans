package net.betterpvp.clans.classes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.DegenerateEnergyEvent;
import net.betterpvp.clans.classes.events.RegenerateEnergyEvent;
import net.betterpvp.clans.events.UpdateEvent;
import net.betterpvp.clans.events.UpdateEvent.UpdateType;
import net.betterpvp.clans.module.BAUListener;
import net.betterpvp.clans.utility.UtilMessage;

public class Energy extends BAUListener {
	
	public Energy(Clans i){
		super(i);
	}

	public static double baseEnergy = 150.0D;
	public static double playerEnergy = 0.0D;

	public static double getEnergy(Player player) {
		return player.getExp();
	}

	public static void setEnergy(Player player, float energy) {
		player.setExp(energy);
	}

	public static double getMax(Player player) {
		return baseEnergy;
	}

	public static boolean use(Player player, String ability, double amount, boolean inform) {
		amount = 0.999 * (amount / 100);



		if (amount > getEnergy(player)) {
			if (inform) {
				UtilMessage.message(player, "Energy", "You are too exhausted to use " + ChatColor.GREEN + ability + ChatColor.GRAY + ".");
				player.getWorld().playEffect(player.getLocation(), Effect.SMOKE, 1);
			}

			return false;
		}

		Bukkit.getPluginManager().callEvent(new DegenerateEnergyEvent(player, amount));
		
		return true;
	}

	public static void regenerateEnergy(Player player, double energy) {
		player.setExp(Math.min(0.999F, (float) getEnergy(player) + (float) energy));
	}

	public static void degenerateEnergy(Player player, double energy) {
		if(getEnergy(player) <= 0F) return;
		player.setExp(Math.min(0.999F, (float) getEnergy(player) - (float) energy));
	}

	public void updateEnergy(Player cur) {
		if (cur.isDead()) {
			return;
		}
		double energy = 0.006D;

		if (cur.isSprinting() || cur.getLocation().getBlock().isLiquid()) {
			energy = 0.0008D;

			
		}
		
		Bukkit.getPluginManager().callEvent(new RegenerateEnergyEvent(cur, energy));

		
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onRegen(RegenerateEnergyEvent e){
		regenerateEnergy(e.getPlayer(), e.getEnergy());
	
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onDegen(DegenerateEnergyEvent e){
		degenerateEnergy(e.getPlayer(), e.getEnergy());
	}

	@EventHandler
	public void handleRespawn(PlayerRespawnEvent event) {
		setEnergy(event.getPlayer(), 150.0F);
	}

	@EventHandler
	public void handleExp(PlayerExpChangeEvent event) {
		event.setAmount(0);
	}

	@EventHandler
	public void update(UpdateEvent event) {
		if (event.getType() == UpdateType.TICK) {
			for (Player players : Bukkit.getOnlinePlayers()) {
				if(players.getExp() == 0.999) continue;
				updateEnergy(players);
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getAction() == Action.LEFT_CLICK_AIR) {
			use(player, "Attack", 0.02, false);
		}
	}
}
