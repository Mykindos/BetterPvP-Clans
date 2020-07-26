package net.betterpvp.clans.clans.commands.subcommands;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanMember.Role;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.commands.IClanCommand;
import net.betterpvp.clans.clans.listeners.InviteHandler;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import net.betterpvp.core.utility.fancymessage.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class InviteCommand implements IClanCommand {


    public void run(Player player, String[] args) {
        Clan clan = ClanUtilities.getClan(player);
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }

        if (!clan.getMember(player.getUniqueId()).hasRole(Role.ADMIN)) {
            UtilMessage.message(player, "Clans", "Only the Clan Leader and Admins can send invites.");
            return;
        }

        if (args.length == 1) {
            UtilMessage.message(player, "Clans", "You did not input a player to invite.");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            UtilPlayer.searchOnline(player, args[1], true);
            return;
        }

        Gamer tGamer = GamerManager.getOnlineGamer(target.getUniqueId());

        if (player.getName().equals(target.getName())) {
            UtilMessage.message(player, "Clans", "You cannot invite yourself.");
            return;
        }

        if (ClanUtilities.getClan(target) != null) {
            UtilMessage.message(player, "Clans", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " is apart of "
                    + ChatColor.YELLOW + "Clan " + ClanUtilities.getClan(target).getName() + ChatColor.GRAY + ".");
            return;
        }

        if (InviteHandler.isInvited(tGamer, clan,"Invite")) {
            UtilMessage.message(player, "Clans", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " has already been invited.");
            return;
        }

        if(ClanUtilities.getSquadCount(clan) >= Clans.getOptions().getMaxClanMembers()){
            UtilMessage.message(player, "Clans", "Your clan has too many members or allies to invite another member.");
            return;
        }

        UtilMessage.message(player, "Clans", "You invited " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to join your Clan.");
        clan.messageClan(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " invited " + ChatColor.YELLOW
                + target.getName() + ChatColor.GRAY + " to join your Clan.", player.getUniqueId(), true);


        UtilMessage.message(target, "Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " invited you to join " + ChatColor.YELLOW
                + "Clan " + clan.getName() + ChatColor.GRAY + ".");
        new FancyMessage(ChatColor.BLUE + "Clans> " + ChatColor.GOLD.toString() + ChatColor.UNDERLINE + "Click Here").command("/c join " + clan.getName()).then(ChatColor.GRAY + " or type '"
                + ChatColor.YELLOW + "/c join " + clan.getName() + ChatColor.GRAY + "'" + ChatColor.GRAY + " to accept!").send(target);

        InviteHandler.createInvite(clan, tGamer, "Invite", 20);
        //sendInvite(clan, target.getUniqueId());
    }


    @Override
    public String getName() {

        return "Invite";
    }
}
