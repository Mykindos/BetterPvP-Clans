package net.betterpvp.clans.clans.commands.subcommands;

import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.commands.IClanCommand;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.utility.UtilMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;


public class TopCommand implements IClanCommand {


    public void run(Player player, String[] args) {
        boolean showPoints = ClientUtilities.getOnlineClient(player).hasRank(Rank.ADMIN, false);
        ClanUtilities.sort();

        UtilMessage.message(player, ChatColor.GREEN + "Top Clans");
        for (int i = 0; i < 10; i++) {
            Clan c = ClanUtilities.clans.get(i);
            UtilMessage.message(player, ChatColor.WHITE.toString() + (i + 1) + " - "
                    + ClanUtilities.getRelation(c, ClanUtilities.getClan(player)).getPrimary()
                    + c.getName() + " "
                    + (showPoints ? ChatColor.WHITE + "(" + ChatColor.GREEN
                    + c.getPoints() + ChatColor.WHITE + ")" : "")
                    + (c.getPoints() > 0 ? " - " + ChatColor.RED + ChatColor.BOLD + "Raid Bonus: " + ChatColor.WHITE + (10 - i) + " points" : ""));
        }

        UtilMessage.message(player, ChatColor.GRAY + "Raid bonus is how many bonus points you get for raiding this clan.");

    }

    @Override
    public String getName() {

        return "Top";
    }

}
