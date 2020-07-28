package net.betterpvp.clans.clans.commands.subcommands;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanMember.Role;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.commands.IClanCommand;
import net.betterpvp.clans.clans.events.ClanAllyClanEvent;
import net.betterpvp.clans.clans.listeners.InviteHandler;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AllyCommand implements IClanCommand {


    public void run(Player player, String[] args) {
        Clan clan = ClanUtilities.getClan(player);
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }

        if (!clan.getMember(player.getUniqueId()).hasRole(Role.ADMIN)) {
            UtilMessage.message(player, "Clans", "Only the Clan Leader and Admins can manage Alliances.");
            return;
        }

        if (args.length == 1) {
            UtilMessage.message(player, "Clans", "You did not input a Clan to ally.");
            return;
        }

        Clan target = ClanUtilities.getClan(args[1]);
        if (target == null) {
            UtilPlayer.searchOnline(player, args[1], true);
            return;
        }

        if (clan.equals(target)) {
            UtilMessage.message(player, "Clans", "You cannot ally yourself.");
            return;
        }

        if (clan.isEnemy(target)) {
            UtilMessage.message(player, "Clans", "You must neutral this clan before you can create an alliance.");
            return;
        }

        if (clan.isAllied(target)) {
            UtilMessage.message(player, "Clans", "You are already allies with " + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".");
            return;
        }

        if (ClanUtilities.getSquadCount(clan) >= Clans.getOptions().getMaxClanMembers()) {
            UtilMessage.message(player, "Clans", "Your clan has too many members / allies to ally another clan.");
            return;
        }

        if (ClanUtilities.getSquadCount(clan) + target.getMembers().size() > Clans.getOptions().getMaxClanMembers()) {
            UtilMessage.message(player, "Clans", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " has too many members to join your alliance.");
            return;
        }

        if (ClanUtilities.getSquadCount(target) + clan.getMembers().size() > Clans.getOptions().getMaxClanMembers()) {
            UtilMessage.message(player, "Clans", ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + " has too many members/allies.");
            return;
        }

        if (InviteHandler.isInvited(target, clan, "Ally")) {
            UtilMessage.message(player, "Clans", "You have already requested alliance with " + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".");
            return;
        }

        if (InviteHandler.isInvited(clan, target, "Ally")) {

            Bukkit.getPluginManager().callEvent(new ClanAllyClanEvent(player, clan, target));

            return;
        }

        UtilMessage.message(player, "Clans", "You requested alliance with " + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".");
        clan.messageClan(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " requested alliance with "
                + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".", player.getUniqueId(), true);

        target.messageClan(ChatColor.YELLOW + "Clan " + clan.getName() + ChatColor.GRAY + " has requested alliance with you.", null, true);
        InviteHandler.createInvite(clan, target, "Ally", 20);


    }


    @Override
    public String getName() {

        return "Ally";
    }
}
