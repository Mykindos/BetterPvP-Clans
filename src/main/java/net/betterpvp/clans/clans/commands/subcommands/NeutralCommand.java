package net.betterpvp.clans.clans.commands.subcommands;

import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanMember;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.commands.IClanCommand;
import net.betterpvp.clans.clans.events.ClanNeutralClanEvent;
import net.betterpvp.clans.clans.listeners.InviteHandler;
import net.betterpvp.clans.scoreboard.ScoreboardManager;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class NeutralCommand implements IClanCommand {

    public static HashMap<Clan, Clan> truce = new HashMap<Clan, Clan>();


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
            UtilMessage.message(player, "Clans", "You did not input a Clan to neutral.");
            return;
        }

        Clan target = ClanUtilities.getClan(args[1]);
        if (target == null) {
            ClanUtilities.searchClan(player, args[1], true);
            return;
        }

        if (clan.equals(target)) {
            UtilMessage.message(player, "Clans", "You cannot neutral yourself.");
            return;
        }

        if (InviteHandler.isInvited(target, clan, "Neutral")) {
            UtilMessage.message(player, "Clans", "You have already requested neutral with "
                    + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".");
            return;
        }

        Bukkit.getPluginManager().callEvent(new ClanNeutralClanEvent(player, clan, target));


    }

    public boolean isRequsted(Clan clan, Clan other) {
        return truce.containsKey(clan) && truce.get(clan).equals(other);
    }

    public void acceptRequest(Clan sender, Clan accepting) {
        if (truce.containsKey(sender)) {
            if (truce.get(sender) != null) {
                if (truce.get(sender).equals(accepting)) {
                    truce.remove(sender);
                    //ScoreboardManager.updateRelation();
                }
            }
        } else if (truce.containsKey(sender)) {
            if (truce.get(accepting) != null) {
                if (truce.get(accepting).equals(sender)) {
                    truce.remove(accepting);
                   // ScoreboardManager.updateRelation();
                }
            }

        }
    }

    @Override
    public String getName() {

        return "Neutral";
    }
}
