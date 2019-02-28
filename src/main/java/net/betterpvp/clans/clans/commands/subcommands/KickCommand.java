package net.betterpvp.clans.clans.commands.subcommands;

import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanMember;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.commands.IClanCommand;
import net.betterpvp.clans.clans.events.ClanKickMemberEvent;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.utility.UtilInvite;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KickCommand implements IClanCommand {


    public KickCommand() {


    }

    public void run(Player player, String[] args) {
        Clan clan = ClanUtilities.getClan(player);
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }

        if (args.length == 1) {
            UtilMessage.message(player, "Clans", "You did not input a player to kick.");
            return;
        }

        Client target = ClientUtilities.getClient(args[1]);
        if (target == null) {
            UtilPlayer.searchOnline(player, args[1], true);
            return;
        }

        if (!clan.getMember(player.getUniqueId()).hasRole(ClanMember.Role.ADMIN)) {
            UtilMessage.message(player, "Clans", "Only the Clan Leader and Admins can kick members.");
            return;
        }

        if (target.equals(player)) {
            UtilMessage.message(player, "Clans", "You cannot kick yourself.");
            return;
        }

        if (clan.getMember(target.getUUID()) == null) {
            UtilMessage.message(player, "Clans", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " is not in your Clan.");
            return;
        }

        if (clan.getMember(target.getUUID()).getRole().toInt() > clan.getMember(player.getUniqueId()).getRole().toInt()) {
            if (!ClientUtilities.getOnlineClient(player).isAdministrating()) {
                UtilMessage.message(player, "Clans", "You do not out rank " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
                return;
            }
        }


        Player temp = Bukkit.getPlayer(target.getUUID());
        if (temp != null) {
            Clan xx = ClanUtilities.getClan(temp.getLocation());
            if (xx != null) {
                if (clan.isEnemy(xx)) {
                    UtilMessage.message(player, "Clans", "You cannot kick a member who is in enemy territory!");
                    return;
                }
            }

        }

        UtilInvite.invites.remove(target.getUUID());
        Bukkit.getPluginManager().callEvent(new ClanKickMemberEvent(player, target, clan));

        //ClanRepository.updateDynmap(i, clan);
    }

    @Override
    public String getName() {

        return "Kick";
    }
}
