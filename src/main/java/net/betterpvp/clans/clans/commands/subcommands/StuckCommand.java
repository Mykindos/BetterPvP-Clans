package net.betterpvp.clans.clans.commands.subcommands;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.commands.IClanCommand;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.Titles;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import net.betterpvp.core.utility.UtilTime.TimeUnit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;
import java.util.Map.Entry;


public class StuckCommand extends BPVPListener<Clans> implements IClanCommand{


	private WeakHashMap<Player, Long> teleports = new WeakHashMap<>();


	public StuckCommand(Clans i) {
		super(i);


	}

	public void run(Player player, String[] args) {
		
		Clan clan = ClanUtilities.getClan(player);
		Clan locClan = ClanUtilities.getClan(player.getLocation());
		if(teleports.containsKey(player)) {
			UtilMessage.message(player, "Clans", "You already have a stuck teleport in progress.");
			return;
		}
		
		if (player.getWorld().getName().equalsIgnoreCase("bossworld2")) {
			UtilMessage.message(player, "Clans", "You can't use /c stuck from here.");
			return;
		}

		if(locClan != null) {
			if(clan != null) {
				if(clan.isEnemy(locClan)) {
					teleports.put(player, System.currentTimeMillis() + 300000);
					UtilMessage.message(player, "Clans", "Stuck teleport started...");
					return;
				}

			}

			if(locClan.isOnline()) {
				teleports.put(player, System.currentTimeMillis() + 120000);
				UtilMessage.message(player, "Clans", "Stuck teleport started...");
				return;
			}

			teleports.put(player, System.currentTimeMillis() + 120000);
			UtilMessage.message(player, "Clans", "Stuck teleport started...");
		}
	}

	@EventHandler
	public void onUodate(UpdateEvent e) {
		if(e.getType() == UpdateType.FASTEST) {
			if(teleports.isEmpty()) return;
			Iterator<Entry<Player, Long>> it = teleports.entrySet().iterator();
			while(it.hasNext()) {
				Entry<Player, Long> next = it.next();

				Titles.sendTitle(next.getKey(), 0, 20, 20, "", ChatColor.YELLOW + "Teleporting in " + ChatColor.GREEN +
						Math.max(0, UtilTime.convert((next.getValue()) - System.currentTimeMillis(), TimeUnit.BEST, 1)) 
				+ " " + UtilTime.getTimeUnit2(next.getValue() - System.currentTimeMillis())) ;

				if((next.getValue()) - System.currentTimeMillis() <= 0) {
					it.remove();
					
					Location loc = getBestPosition(next.getKey());
					if(loc != null) {
						next.getKey().teleport(Bukkit.getWorld("world").getHighestBlockAt(loc).getLocation());
						Titles.sendTitle(next.getKey(), 0, 20, 20, "", ChatColor.YELLOW + "Teleport finished");
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onDamage(CustomDamageEvent e) {
		if(e.getDamagee() instanceof Player) {
			Player player = (Player) e.getDamagee();
			if(teleports.containsKey(player)) {
				teleports.remove(player);
				Titles.sendTitle(player, 0, 20, 20, "", ChatColor.RED + "Teleport cancelled");
			}
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(teleports.containsKey(e.getPlayer())) {
			if(e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY()
					|| e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
				teleports.remove(e.getPlayer());
				Titles.sendTitle(e.getPlayer(), 0, 20, 20, "", ChatColor.RED + "Teleport cancelled");
			}
		}
	}

	private Location getBestPosition(final Player p) {
		List<Location> locs = new ArrayList<>();



		for(int i = 0; i < 64; i++) {
			Location loc = p.getLocation().clone().add(i, 0, 0);
			Clan aClan = ClanUtilities.getClan(loc);
			if(aClan == null) {
				locs.add(loc);
				break;
			}
		}

		for(int i = 0; i > -64; i--) {
			Location loc = p.getLocation().clone().add(i, 0, 0);
			Clan bClan = ClanUtilities.getClan(loc);
			if(bClan == null) {
				locs.add(loc);
				break;
			}
		}

		for(int i = 0; i < 64; i++) {
			Location loc = p.getLocation().clone().add(0, 0, i);
			Clan cClan = ClanUtilities.getClan(loc);
			if(cClan == null) {
				locs.add(loc);
				break;
			}
		}

		for(int i = 0; i > -64; i--) {
			Location loc = p.getLocation().clone().add(0, 0, i);
			Clan dClan = ClanUtilities.getClan(loc);
			if(dClan == null) {
				locs.add(loc);
				break;
			}
		}

		if(locs.size() > 0) {

			Collections.sort(locs, new Comparator<Location>(){

				@Override
				public int compare(Location a, Location b) {
					// TODO Auto-generated method stub
					return (int) p.getLocation().distance(a) - (int) p.getLocation().distance(b);
				}

			});

			return locs.get(0);
		}

		return null;

	}

	@Override
	public String getName() {

		return "Stuck";
	}
}
