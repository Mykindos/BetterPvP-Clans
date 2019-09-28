package net.betterpvp.clans.clans.commands;

import net.betterpvp.clans.clans.Alliance;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.core.client.Rank;

import net.betterpvp.core.client.commands.admin.StaffChatCommand;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class AllyChatCommand extends Command {

    public static Set<String> enabled = new HashSet<String>();

    public AllyChatCommand() {
        super("allychat", new String[]{"ac"}, Rank.PLAYER);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args == null || args.length == 0) {
            Clan clan = ClanUtilities.getClan(player);
            if (clan == null) {
                UtilMessage.message(player, "Clans", "You are not in a Clan.");
                return;
            }


            if (!enabled.contains(player.getName())) {
                UtilMessage.message(player, "Ally Chat", "Ally Chat: " + ChatColor.GREEN + "Enabled");
                enabled.add(player.getName());
            } else {
                UtilMessage.message(player, "Ally Chat", "Ally Chat: " + ChatColor.RED + "Disabled");
                enabled.remove(player.getName());
            }

            ClanChatCommand.enabled.remove(player.getName());
            StaffChatCommand.enabled.remove(player.getName());


            return;
        }

        if (args.length >= 1) {
            Clan clan = ClanUtilities.getClan(player);
            if (clan == null) {
                UtilMessage.message(player, "Clans", "You are not in a Clan.");
                return;
            }

            String msg = UtilMessage.getFinalArg(args, 0);
            for (Alliance c : clan.getAlliances()) {
                c.getClan().messageClan(ChatColor.DARK_GREEN + clan.getName() + " " + player.getName() + " " + ChatColor.GREEN + msg, null, false);
            }

            clan.messageClan(ChatColor.DARK_GREEN + clan.getName() + " " + player.getName() + " " + ChatColor.GREEN + msg, null, false);
        }
    }

    @Override
    public void help(Player player) {

    }

}
