package net.betterpvp.clans.clans.commands.subcommands;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanMember;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.commands.IClanCommand;
import net.betterpvp.clans.clans.events.ChunkClaimEvent;
import net.betterpvp.clans.clans.mysql.ClanRepository;
import net.betterpvp.core.database.Log;
import net.betterpvp.core.utility.UtilFormat;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class UnclaimallCommand implements IClanCommand{

	private Clans i;
	public UnclaimallCommand(Clans i) {
		
		this.i = i;
	
	}

	public void run(Player player, String[] args) {
		Clan clan = ClanUtilities.getClan(player);

		if (clan == null) {
			UtilMessage.message(player, "Clans", "You are not in a Clan.");
			return;
		}




		if (!clan.getMember(player.getUniqueId()).hasRole(ClanMember.Role.ADMIN)) {
			UtilMessage.message(player, "Clans", "Only the Clan Leader and Admins can unclaim Territory.");
			return;
		}
		
			for(String s : clan.getTerritory()) {
				Chunk c = UtilFormat.stringToChunk(s);
				if(c != null) {
					Bukkit.getPluginManager().callEvent(new ChunkClaimEvent(c));
				}
			}
			
		
		
		clan.getTerritory().clear();

	
		clan.messageClan(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " unclaimed all Territory.", player.getUniqueId(), true);
		UtilMessage.message(player, "Clans", "You unclaimed all Territory.");
		Log.write("Clans", "[" + player.getName() + "] unclaimed all territory.");
		ClanRepository.updateClaims(clan);
		//ClanRepository.updateDynmap(i, clan);
	}

	@Override
	public String getName() {
		
		return "Unclaimall";
	}
}
