package net.betterpvp.clans.clans.commands.subcommands;


import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanMember.Role;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.Pillage;
import net.betterpvp.clans.clans.commands.IClanCommand;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class DisbandCommand implements IClanCommand{


	public void run(Player player, String[] args) {
		Clan clan = ClanUtilities.getClan(player);
		if (clan == null) {
			UtilMessage.message(player, "Clans", "You are not in a Clan.");
			return;
		}

		if (!ClientUtilities.getClient(player).isAdministrating()) {
			if (!clan.getLeader().equals(player.getUniqueId())) {
				UtilMessage.message(player, "Clans", "Only the Clan Leader can disband the Clan.");
				return;
			}
		}
		
		if (!clan.getMember(player.getUniqueId()).hasRole(Role.LEADER)) {
			UtilMessage.message(player, "Clans", "You need to be a clan leader to disband your clan");
			return;
		}
		
		for(Clan c : ClanUtilities.clans){
			if(Pillage.isPillaging(c, clan)){
				UtilMessage.message(player, "Clans", "You cannot disband your clan while being pillaged!");
				return;
			}
		}
		
		if(System.currentTimeMillis() < clan.getLastTnted()){
			UtilMessage.message(player, "Clans", "You cannot disband your clan for " 
					+ChatColor.GREEN + UtilTime.getTime(clan.getLastTnted() - System.currentTimeMillis(), UtilTime.TimeUnit.BEST, 1));
			return;
		}


		ClanUtilities.disbandClan(player, clan);
	}

	@Override
	public String getName() {
		
		return "Disband";
	}
}
