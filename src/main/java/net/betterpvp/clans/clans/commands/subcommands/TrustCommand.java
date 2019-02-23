package net.betterpvp.clans.clans.commands.subcommands;

import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanMember;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.commands.IClanCommand;
import net.betterpvp.clans.clans.events.ClanTrustClanEvent;
import net.betterpvp.clans.clans.listeners.InviteHandler;
import net.betterpvp.clans.scoreboard.ScoreboardManager;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class TrustCommand implements IClanCommand{



	public void run(Player player, String[] args) {
		Clan clan = ClanUtilities.getClan(player);
		if (clan == null) {
			UtilMessage.message(player, "Clans", "You are not in a Clan.");
			return;
		}

		if (!clan.getMember(player.getUniqueId()).hasRole(ClanMember.Role.ADMIN)) {
			UtilMessage.message(player, "Clans", "Only the Clan Leader and Admins can manage Alliances.");
			return;
		}

		if (args.length == 1) {
			UtilMessage.message(player, "Clans", "You did not input a Clan to trust.");
			return;
		}

		Clan target = ClanUtilities.getClan(args[1]);
		if (target == null) {
			UtilPlayer.searchOnline(player, args[1], true);
			return;
		}

		if (clan.equals(target)) {
			UtilMessage.message(player, "Clans", "You cannot trust yourself.");
			return;
		}

		if (!clan.isAllied(target)) {
			UtilMessage.message(player, "Clans", "You are not allies with " + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".");
			return;
		}

		if (clan.getAlliance(target).hasTrust()) {
			UtilMessage.message(player, "Clans", "You have revoked trust with " + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".");
			clan.messageClan(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " has revoked trust to " + ChatColor.YELLOW + "Clan " + target
					.getName() + ChatColor.GRAY + ".", player.getUniqueId(), true);
			target.messageClan(ChatColor.YELLOW + "Clan " + clan.getName() + ChatColor.GRAY + " has revoked trust to you.", null, true);
			clan.getAlliance(target).setTrust(false);
			target.getAlliance(clan).setTrust(false);
			ScoreboardManager.updateRelation();
			return;
		}

		if(InviteHandler.isInvited(target, clan)){
			UtilMessage.message(player, "Clans", "You have already requested trust with " + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".");
			return;
		}

		if (InviteHandler.isInvited(clan, target)) {
			Bukkit.getPluginManager().callEvent(new ClanTrustClanEvent(player, clan, target));


			return;
		}

		UtilMessage.message(player, "Clans", "You requested trust with " + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".");
		clan.messageClan(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " requested alliance with "
				+ ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".", player.getUniqueId(), true);

		target.messageClan(ChatColor.YELLOW + "Clan " + clan.getName() + ChatColor.GRAY + " has requested trust with you.", null, true);
		InviteHandler.createInvite(clan, target, 20);

	}



	@Override
	public String getName() {
		
		return "Trust";
	}
}
