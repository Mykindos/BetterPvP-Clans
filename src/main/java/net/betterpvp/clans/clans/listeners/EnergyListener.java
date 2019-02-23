package net.betterpvp.clans.clans.listeners;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanMember;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.mysql.ClanRepository;

import net.betterpvp.core.database.Log;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.Titles;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.Iterator;

public class EnergyListener extends BPVPListener<Clans> {

	public EnergyListener(Clans instance) {
		super(instance);
		// TODO Auto-generated constructor stub
	}

	@EventHandler
	public void onUpdate(UpdateEvent e){
		
		if(e.getType() == UpdateEvent.UpdateType.MIN_01){
		
			
			Iterator<Clan> it = ClanUtilities.clans.iterator();
			while(it.hasNext()){
				Clan clan = it.next();

				if(clan instanceof AdminClan){
					continue;
				}

				if(clan.getTerritory().size() > 0){
					
					//ukkit.broadcastMessage("Decreased " + clan.getName() + " by " +   (clan.getTerritory().size() * (25 / 60)));
				
					clan.setEnergy(Math.max(0, clan.getEnergy() - ((clan.getTerritory().size() * (25.0 / 60.0)))));

					if(clan.getEnergy() <= 0){
						ClanUtilities.disbandClan(clan);
						Log.write("Clans", clan.getName() + " was disbanded for running out of energy. "
						+ (clan.getHome() != null ? ChatColor.YELLOW + "Location: " + ChatColor.GREEN +  clan.getHome().getX() +  ChatColor.WHITE 
								+ "," + ChatColor.GREEN + clan.getHome().getZ() : ""));
						UtilMessage.broadcast("Clans", ChatColor.YELLOW + clan.getName() + ChatColor.GRAY
								+ " was disbanded for running out of energy." + (clan.getHome() != null ? ChatColor.YELLOW + " Location: " + ChatColor.GREEN +  clan.getHome().getX() +  ChatColor.WHITE 
										+ "," + ChatColor.GREEN + clan.getHome().getZ() : ""));
						it.remove();
						continue;
					}

				}
			}
		}
	}

	@EventHandler
	public void hourlyWarning(UpdateEvent e) {
		if(e.getType() == UpdateEvent.UpdateType.MIN_64) {
			Iterator<Clan> it = ClanUtilities.clans.iterator();
			while(it.hasNext()){
				Clan clan = it.next();

				if(clan instanceof AdminClan){
					continue;
				}

				if(clan.getTerritory().size() > 0){

					ClanRepository.updateEnergy(clan);

					clan.messageClan(ChatColor.GRAY.toString() + ChatColor.BOLD + "Your clan energy is now " + ChatColor.GREEN  
							+ ChatColor.BOLD  + (int)  clan.getEnergy() ,null, false);
					clan.messageClan(ChatColor.GRAY.toString() + ChatColor.BOLD + "Unless you purchase more, your clan will disband in " + ChatColor.GREEN + ChatColor.BOLD
							+ ClanUtilities.getHoursOfEnergy(clan) + ChatColor.GREEN + ChatColor.BOLD + " hours.", null, false);
					clan.messageClan(ChatColor.GRAY.toString() + ChatColor.BOLD + "Type '" + ChatColor.YELLOW + ChatColor.BOLD
							+ "/c energy" + ChatColor.GRAY + ChatColor.BOLD + "' to buy more energy", null, false);


					if(ClanUtilities.getHoursOfEnergy(clan) < 24){
						clan.playSound(Sound.NOTE_PLING, 1.0F, 2.0F);

						for(ClanMember member : clan.getMembers()) {
							Player p = Bukkit.getPlayer(member.getUUID());
							if(p != null) {
								Titles.sendTitle(p, 0, 0, 0, ChatColor.RED + "Clan Energy", ChatColor.YELLOW + "Your clan has "
										+ ChatColor.GREEN + ClanUtilities.getHoursOfEnergy(clan) + " hours" + ChatColor.YELLOW + " of energy left!");
							}
						}

					}
				}
			}
		}
	}

}


