package net.betterpvp.clans.clans.commands.subcommands;

import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanMember.Role;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.commands.IClanCommand;
import net.betterpvp.clans.clans.mysql.ClanRepository;
import net.betterpvp.core.database.Log;
import net.betterpvp.core.utility.UtilFormat;
import net.betterpvp.core.utility.UtilLocation;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SetHomeCommand implements IClanCommand{



    public void run(Player player, String[] args) {
        Clan clan = ClanUtilities.getClan(player);
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }

        if (!clan.getMember(player.getUniqueId()).hasRole(Role.ADMIN)) {
            UtilMessage.message(player, "Clans", "Only the Clan Leader and Admins can manage the Clan Home.");
            return;
        }

        Clan locationClan = ClanUtilities.getClan(player.getLocation());
        if (locationClan == null || !locationClan.equals(clan)) {
            UtilMessage.message(player, "Clans", "You must set your Clan Home in your own Territory.");
            return;
        }

        clan.setHome(player.getLocation());
        clan.messageClan(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " set Clan Home at "
                + ChatColor.YELLOW + UtilLocation.locationToString(player.getLocation()) + ChatColor.GRAY + ".", player.getUniqueId(), true);
        UtilMessage.message(player, "Clans", "You set the Clan Home at " + ChatColor.YELLOW
                + UtilLocation.locationToString(player.getLocation()) + ChatColor.GRAY + ".");
        ClanRepository.updateHome(clan);
        Log.write("Clans", player.getName() + " set [" + clan.getName() + "] home at [" + UtilFormat.locationToFile(clan.getHome()) + "]");
    }

	@Override
	public String getName() {
		
		return "SetHome";
	}
}
