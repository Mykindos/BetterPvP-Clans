package net.betterpvp.clans.general.commands;

import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class CraftCommand extends Command {

    public CraftCommand() {
        super("craft", new String[]{}, Rank.PLAYER);
    }

    @Override
    public void execute(Player player, String[] strings) {

        Gamer gamer = GamerManager.getOnlineGamer(player);
        if (gamer != null) {

            if(gamer.getClient().hasDonation("ConveniencePackage") || gamer.getClient().hasRank(Rank.ADMIN, false)) {
                player.openWorkbench(null, true);
            }else{
                UtilMessage.message(player, "Donation", "You need to own the convenience package to use this command. " + ChatColor.GREEN + "/buy");
            }
        }
    }

    @Override
    public void help(Player player) {

    }
}
