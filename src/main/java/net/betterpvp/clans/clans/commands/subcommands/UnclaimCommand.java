package net.betterpvp.clans.clans.commands.subcommands;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanMember;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.commands.IClanCommand;
import net.betterpvp.clans.clans.events.ChunkClaimEvent;
import net.betterpvp.clans.clans.mysql.ClanRepository;
import net.betterpvp.core.database.Log;
import net.betterpvp.core.utility.UtilFormat;
import net.betterpvp.core.utility.UtilLocation;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class UnclaimCommand implements IClanCommand{

	
	private Clans i;

	public UnclaimCommand(Clans i) {
		
		this.i = i;
		
	}

	public void run(Player player, String[] args) {
		Clan clan = ClanUtilities.getClan(player);

		if (clan == null) {
			UtilMessage.message(player, "Clans", "You are not in a Clan.");
			return;
		}

		Clan other = ClanUtilities.getClan(player.getLocation());
		if (other != null && !other.equals(clan)) {
			
			if(!(other instanceof AdminClan)){
				if (other.getTerritory().size() > other.getMembers().size() + 3) { // Previously other.getMembers().size() + 3
					other.getTerritory().remove(UtilFormat.chunkToFile(player.getLocation().getChunk()));
					ClanRepository.updateClaims(other);
					//ClanRepository.updateDynmap(i, clan);
					other.messageClan(ChatColor.YELLOW
							+ player.getName() + ChatColor.GRAY + " unclaimed territory at " + ChatColor.YELLOW
							+ UtilLocation.chunkToString(player.getLocation().getChunk()) + ChatColor.GRAY + ".", null, true);
					UtilMessage.message(player, "Clans", "You unclaimed Territory " + ChatColor.YELLOW
							+ UtilLocation.chunkToString(player.getLocation().getChunk()) + ChatColor.GRAY + ".");
					Bukkit.getPluginManager().callEvent(new ChunkClaimEvent(player.getLocation().getChunk()));
					return;
				}
			}
		}

		if (other == null || !other.equals(clan)) {
			UtilMessage.message(player, "Clans", "This Territory is not owned by you.");
			return;
		}

		if (ClanUtilities.getClan(player.getLocation()).getTerritory().size() > ClanUtilities.getClan(player.getLocation()).getTerritory().size() + 3) {
			ClanUtilities.getClan(player.getLocation()).getTerritory().remove(UtilFormat.chunkToFile(player.getLocation().getChunk()));
			//ClanRepository.updateDynmap(i, clan);
			ClanUtilities.getClan(player.getLocation()).messageClan(ChatColor.YELLOW
					+ player.getName() + ChatColor.GRAY + " unclaimed territory at " + ChatColor.YELLOW
					+ UtilLocation.chunkToString(player.getLocation().getChunk()) + ChatColor.GRAY + ".", null, true);
			UtilMessage.message(player, "Clans", "You unclaimed Territory " + ChatColor.YELLOW
					+ UtilLocation.chunkToString(player.getLocation().getChunk()) + ChatColor.GRAY + ".");
			Bukkit.getPluginManager().callEvent(new ChunkClaimEvent(player.getLocation().getChunk()));
			return;
		}

		if (!clan.getMember(player.getUniqueId()).hasRole(ClanMember.Role.ADMIN)) {
			UtilMessage.message(player, "Clans", "Only the Clan Leader and Admins can unclaim Territory.");
			return;
		}

		clan.getTerritory().remove(UtilFormat.chunkToFile(player.getLocation().getChunk()));
		clan.messageClan(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " unclaimed Territory " + ChatColor.YELLOW
				+ UtilLocation.chunkToString(player.getLocation().getChunk()) + ChatColor.GRAY + ".", player.getUniqueId(), true);
		UtilMessage.message(player, "Clans", "You unclaimed Territory " + ChatColor.YELLOW
				+ UtilLocation.chunkToString(player.getLocation().getChunk()) + ChatColor.GRAY + ".");
		Log.write("Clans", "[" + player.getName() + "] unclaimed territory [" + UtilFormat.chunkToFile(player.getLocation().getChunk()) + "]");
		Bukkit.getPluginManager().callEvent(new ChunkClaimEvent(player.getLocation().getChunk()));
		ClanRepository.updateClaims(clan);
		//ClanRepository.updateDynmap(i, clan);
	}

	

	@Override
	public String getName() {
		
		return "Unclaim";
	}
}
