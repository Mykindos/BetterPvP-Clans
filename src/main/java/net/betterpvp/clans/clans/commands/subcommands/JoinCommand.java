package net.betterpvp.clans.clans.commands.subcommands;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanMember;
import net.betterpvp.clans.clans.ClanMember.Role;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.commands.IClanCommand;
import net.betterpvp.clans.clans.events.MemberJoinClanEvent;
import net.betterpvp.clans.clans.listeners.InviteHandler;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class JoinCommand implements IClanCommand {


    private Clans i;

    public JoinCommand(Clans i) {

        this.i = i;

    }

    public void run(Player player, String[] args) {
        Clan clan = ClanUtilities.getClan(player);
        if (!ClientUtilities.getOnlineClient(player.getUniqueId()).isAdministrating()) {
            if (clan != null) {
                UtilMessage.message(player, "Clans", "You are already in a Clan.");
                return;
            }
        }

        if (args.length == 1) {
            UtilMessage.message(player, "Clans", "You did not input a Clan name.");
            return;
        }

        if (args.length == 2) {
            Clan target = ClanUtilities.getClan(args[1]);
            if (target == null) {
                ClanUtilities.searchClan(player, args[1], true);
                return;
            }

            Gamer tGamer = GamerManager.getOnlineGamer(player.getUniqueId());

            if (tGamer.getClient().isAdministrating()) {
                target.getMembers().add(new ClanMember(player.getUniqueId(), Role.LEADER));
                //   ScoreboardManager.addPlayer(player.getName());
                UtilMessage.message(player, "Clans", "You joined " + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".");
                Bukkit.getPluginManager().callEvent(new MemberJoinClanEvent(player, target));
                return;
            }


            if (!InviteHandler.isInvited(tGamer, target, "Invite")) {
                UtilMessage.message(player, "Clans", "You are not invited to " + ChatColor.YELLOW + "Clan "
                        + target.getName() + ChatColor.GRAY + ".");
                return;
            }

            if (ClanUtilities.getSquadCount(target) >= Clans.getOptions().getMaxClanMembers()) {
                UtilMessage.message(player, "Clans", ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + " has too many members or allies");
                return;

            }


            Bukkit.getPluginManager().callEvent(new MemberJoinClanEvent(player, target));


        }
    }


    @Override
    public String getName() {

        return "Join";
    }
}
