package net.betterpvp.clans.clans.listeners;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.*;
import net.betterpvp.clans.clans.InsuranceType;
import net.betterpvp.clans.clans.insurance.Insurance;
import net.betterpvp.clans.clans.mysql.InsuranceRepository;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Iterator;


public class PillageListener extends BPVPListener<Clans> {
	
	public PillageListener(Clans i){
		super(i);
	}

	@EventHandler
	public void onPillageUdate(UpdateEvent e) {
		if (e.getType() == UpdateEvent.UpdateType.SEC) {
			if(Pillage.pillages.isEmpty()) return;
			Iterator<Pillage> iterator = Pillage.pillages.iterator();
			while (iterator.hasNext()) {
				Pillage pillage = iterator.next();



				if (UtilTime.elapsed(pillage.getLastUpdate(), 60000)) {

					if (pillage.getRemaining() <= 0) {
						pillage.getPillaged().messageClan("The Pillage on your Clan has finished!", null, true);
						pillage.getPillager().messageClan("The Pillage on " + ChatColor.YELLOW + "Clan " 
								+ pillage.getPillaged().getName() + ChatColor.GRAY + " has finished!", null, true);
						
						iterator.remove();
						return;
					}

					String remaining = ChatColor.GREEN + Integer.toString(pillage.getRemaining()) 
					+ (pillage.getRemaining() == 1 ? " Minute" : " Minutes") + ChatColor.GRAY;
					pillage.getPillaged().messageClan("The Pillage on your Clan ends in " + remaining + ".", null, true);
					pillage.getPillager().messageClan("The Pillage on " + ChatColor.YELLOW + "Clan " 
					+ pillage.getPillaged().getName() + ChatColor.GRAY + " ends in "
							+ remaining + ".", null, true);
					pillage.setRemaining(pillage.getRemaining() - 1);
					pillage.setLastUpdate(System.currentTimeMillis());



				}
			}
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e){
		Clan pillager = ClanUtilities.getClan(e.getPlayer());
		Clan pillaged = ClanUtilities.getClan(e.getBlock().getLocation());
		if(pillager != null && pillaged != null){
			if(Pillage.isPillaging(pillager, pillaged)){
				Insurance i = new Insurance(e.getBlock().getLocation(), e.getBlock().getType(), e.getBlock().getData(),
						InsuranceType.PLACE, System.currentTimeMillis());
				InsuranceRepository.saveInsurance(pillaged, i);
				pillaged.getInsurance().add(i);
			}
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e){
		Clan pillager = ClanUtilities.getClan(e.getPlayer());
		Clan pillaged = ClanUtilities.getClan(e.getBlock().getLocation());
		if(pillager != null && pillaged != null){
			if(Pillage.isPillaging(pillager, pillaged)){
				
				Insurance i = new Insurance(e.getBlock().getLocation(), e.getBlock().getType(), e.getBlock().getData(), 
						InsuranceType.BREAK, System.currentTimeMillis());
				InsuranceRepository.saveInsurance(pillaged, i);
				pillaged.getInsurance().add(i);
			}
		}
	}
}
