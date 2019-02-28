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


public final class PromoteCommand implements IClanCommand {

    @Override
    public void run(Player player, String[] args) {
        Clan clan = ClanUtilities.getClan(player);
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }

        if (args.length == 1) {
            UtilMessage.message(player, "Clans", "You did not input player to promote.");
            return;
        }
        Client target = ClientUtilities.getClient(args[1]);


        if (target != null) {


            if (target.equals(player)) {
                UtilMessage.message(player, "Clans", "You cannot promote yourself.");
                return;
            }

            if (clan != ClanUtilities.getClan(target.getUUID())) {
                UtilMessage.message(player, "Clans", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " is not apart of you Clan.");
                return;
            }

            if (clan.getMember(target.getUUID()).getRole().toInt() >= clan.getMember(player.getUniqueId()).getRole().toInt()) {
                UtilMessage.message(player, "Clans", "You do not outrank " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
                return;
            }

            String newRank = "Unknown";
            if (clan.getMember(target.getUUID()).getRole() == Role.RECRUIT) {
                clan.getMember(target.getUUID()).setRole(Role.MEMBER);
                newRank = "Member";
            } else if (clan.getMember(target.getUUID()).getRole() == Role.MEMBER) {
                clan.getMember(target.getUUID()).setRole(Role.ADMIN);
                newRank = "Admin";
            } else if (clan.getMember(target.getUUID()).getRole() == Role.ADMIN) {
                clan.getMember(player.getUniqueId()).setRole(Role.ADMIN);
                clan.getMember(target.getUUID()).setRole(Role.LEADER);
                clan.setLeader(target.getUUID());
                newRank = "Leader";
                MemberRepository.updateMember(clan.getMember(target.getUUID()));
                MemberRepository.updateMember(clan.getMember(player.getUniqueId()));
            }
            clan.messageClan(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " promote " + ChatColor.YELLOW + target.getName()
                    + ChatColor.GRAY + " to " + ChatColor.GREEN + newRank + ChatColor.GRAY + ".", player.getUniqueId(), true);
            UtilMessage.message(player, "Clans", "You promoted " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to "
                    + ChatColor.GREEN + newRank + ChatColor.GRAY + ".");
            MemberRepository.updateMember(clan.getMember(target.getUUID()));
            Log.write("Clans", "[" + player.getName() + "] promoted [" + target.getName() + "] to [" + newRank + "]");


        } else {
            UtilMessage.message(player, "Clans", "No clan member found to promote");
            return;
        }
    }

    @Override
    public String getName() {

        return "Promote";
    }
}
