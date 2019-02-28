package net.betterpvp.clans.clans.commands.subcommands;

import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanMember.Role;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.commands.IClanCommand;
import net.betterpvp.clans.clans.mysql.MemberRepository;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.database.Log;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DemoteCommand implements IClanCommand {


    public void run(Player player, String[] args) {
        Clan clan = ClanUtilities.getClan(player);
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }

        if (args.length == 1) {
            UtilMessage.message(player, "Clans", "You did not input player to demote.");
            return;
        }


        Client target = ClientUtilities.getClient(args[1]);

        if (target != null) {
            if (target.getName().equals(player.getName())) {
                UtilMessage.message(player, "Clans", "You cannot demote yourself.");
                return;
            }

            if (clan != ClanUtilities.getClan(target.getUUID())) {
                UtilMessage.message(player, "Clans", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " is not apart of you Clan.");
                return;
            }

            if (clan.getMember(player.getUniqueId()).getRole().toInt() <= clan.getMember(target.getUUID()).getRole().toInt()) {
                UtilMessage.message(player, "Clans", "You do not outrank " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
                return;
            }

            if (clan.getMember(target.getUUID()).getRole().equals(Role.RECRUIT)) {
                UtilMessage.message(player, "Clans", "You cannot demote " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " any further.");
                return;
            }

            String newRank = "Unknown";
            if (clan.getMember(target.getUUID()).getRole() == Role.ADMIN) {
                clan.getMember(target.getUUID()).setRole(Role.MEMBER);
                newRank = "Member";
            } else if (clan.getMember(target.getUUID()).getRole() == Role.MEMBER) {
                clan.getMember(target.getUUID()).setRole(Role.RECRUIT);
                newRank = "Recruit";
            }


            UtilMessage.message(player, "Clans", "You demoted " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to "
                    + ChatColor.GREEN + newRank + ChatColor.GRAY + ".");
            MemberRepository.updateMember(clan.getMember(target.getUUID()));

            clan.messageClan(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " demoted " + ChatColor.YELLOW + target.getName()
                    + ChatColor.GRAY + " to " + ChatColor.GREEN + newRank + ChatColor.GRAY + ".", player.getUniqueId(), true);
            Log.write("Clans", "[" + player.getName() + "] demoted [" + target.getName() + "] to [" + newRank + "]");
        }

    }

    @Override
    public String getName() {

        return "Demote";
    }
}
