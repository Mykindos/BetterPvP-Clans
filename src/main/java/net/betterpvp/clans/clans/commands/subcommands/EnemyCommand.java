package net.betterpvp.clans.clans.commands.subcommands;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.*;
import net.betterpvp.clans.clans.commands.IClanCommand;
import net.betterpvp.clans.clans.events.ClanEnemyClanEvent;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EnemyCommand implements IClanCommand {


    public void run(Player player, String[] args) {

        if (!Clans.getOptions().isEnemySystemEnabled()) {
            UtilMessage.message(player, "Clans", "You cannot enemy this clan until the second day of the season.");
            return;
        }
        Clan clan = ClanUtilities.getClan(player);
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }

        if (!clan.getMember(player.getUniqueId()).hasRole(ClanMember.Role.ADMIN)) {
            UtilMessage.message(player, "Clans", "Only the Clan Leader and Admins can manage Enemies.");
            return;
        }

        if (args.length == 1) {
            UtilMessage.message(player, "Clans", "You did not input a Clan to enemy.");
            return;
        }

        Clan target = ClanUtilities.getClan(args[1]);
        if (target == null) {
            ClanUtilities.searchClan(player, args[1], true);
            return;
        }

        if (target instanceof AdminClan) {
            UtilMessage.message(player, "Clans", "You cannot enemy this clan.");
            return;
        }

        int hours = 0;
        for (ClanMember mem : clan.getMembers()) {
            Client client = ClientUtilities.getClient(mem.getUUID());
            hours += client.getTimePlayed();
        }

        if(Clans.getOptions().isProtectNewClans()) {
            if (hours < 24) {
                UtilMessage.message(player, "Clans", "Your clan has under 24 hours of total play time. You may not enemy anybody.");
                return;
            }
        }

        hours = 0;
        for (ClanMember mem : target.getMembers()) {
            Client client = ClientUtilities.getClient(mem.getUUID());
            hours += client.getTimePlayed();
        }

        if(Clans.getOptions().isProtectNewClans()) {
            if (hours < 24) {
                UtilMessage.message(player, "Clans", target.getName() + " has under 24 hours total play time.");
                UtilMessage.message(player, "Clans", "This stat is never reset, and only exists to protect new players.");
                return;
            }
        }


        if (clan.equals(target)) {
            UtilMessage.message(player, "Clans", "You cannot enemy yourself.");
            return;
        }
        if (clan.isEnemy(target)) {
            UtilMessage.message(player, "Clans", "You are already enemies with " + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".");
            return;
        }

        if (Pillage.isPillaging(clan, target)) {
            UtilMessage.message(player, "Clans", "You cannot enemy a clan that you are currently pillaging!");
            return;
        }

        Bukkit.getPluginManager().callEvent(new ClanEnemyClanEvent(player, clan, target));


    }

    @Override
    public String getName() {

        return "Enemy";
    }
}
