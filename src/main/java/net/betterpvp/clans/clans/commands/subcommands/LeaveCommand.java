package net.betterpvp.clans.clans.commands.subcommands;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.commands.IClanCommand;
import net.betterpvp.clans.clans.events.ClanDeleteEvent;
import net.betterpvp.clans.clans.events.MemberLeaveClanEvent;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.utility.UtilInvite;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LeaveCommand implements IClanCommand {


    private Clans i;

    public LeaveCommand(Clans i) {

        this.i = i;

    }

    public void run(Player player, String[] args) {
        Clan clan = ClanUtilities.getClan(player);
        Client client = ClientUtilities.getOnlineClient(player);

        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }

        if (!(clan instanceof AdminClan)) {
            if (clan.getLeader().equals(player.getUniqueId()) && clan.getMembers().size() > 1) {
                UtilMessage.message(player, "Clans", "You must pass on " + ChatColor.GREEN + "Leadership" + ChatColor.GRAY + " before leaving.");
                return;
            }

            if (clan.getLeader().equals(player.getUniqueId()) && clan.getMembers().size() == 1) {

                Bukkit.getPluginManager().callEvent(new ClanDeleteEvent(player, clan));

                return;
            }
        }
        UtilInvite.invites.remove(player.getUniqueId());
        //clan.getTeam().removeEntry(ClientUtilities.getClient(player.getUniqueId()).getName());
        Clan xx = ClanUtilities.getClan(player.getLocation());
        if (xx != null) {
            if (clan.isEnemy(xx)) {
                UtilMessage.message(player, "Clans", "You cannot leave your clan while in enemy territory!");
                return;
            }
        }

        if (System.currentTimeMillis() < clan.getLastTnted()) {
            UtilMessage.message(player, "Clans", "You cannot leave your clan for "
                    + ChatColor.GREEN + UtilTime.getTime(clan.getLastTnted() - System.currentTimeMillis(), UtilTime.TimeUnit.BEST, 1));
            return;
        }

        Bukkit.getPluginManager().callEvent(new MemberLeaveClanEvent(client, clan));
        //ClanRepository.updateDynmap(i, clan);
    }

    @Override
    public String getName() {

        return "Leave";
    }
}
