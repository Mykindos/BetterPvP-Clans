package net.betterpvp.clans.general.commands;

import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilFormat;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class InfoCommand extends Command {
    public InfoCommand() {
        super("info", new String[]{"information", "stats"}, Rank.PLAYER);

    }

    @Override
    public void execute(Player player, String[] args) {
        if (args != null) {
            if (args.length > 0) {
                String name = "";
                for (String s : args) {
                    name += s + " ";
                }

                Gamer search = GamerManager.getGamer(name.trim());

                if (search != null) {
                    displayInfo(search, player);
                }
            }
        } else {
            Gamer gamer = GamerManager.getOnlineGamer(player);
            displayInfo(gamer, player);
        }
    }

    private void displayInfo(Gamer gamer, Player player) {

        UtilMessage.message(player, ChatColor.YELLOW + "Displaying Information for " + ChatColor.GREEN + gamer.getClient().getName());
        UtilMessage.message(player, ChatColor.BLUE + "Time Played: " + ChatColor.GREEN + UtilTime.getTime2((long) gamer.getClient().getTimePlayed() * 3600000L, UtilTime.TimeUnit.DAYS, 2));
        UtilMessage.message(player, ChatColor.BLUE + "Coins: " + ChatColor.GREEN + UtilFormat.formatNumber(gamer.getCoins()) + "  "
                + ChatColor.BLUE + "Fragments: " + ChatColor.GREEN + gamer.getFragments());
        UtilMessage.message(player, ChatColor.BLUE + "Kills: " + ChatColor.GREEN + gamer.getKills() + "  "
                + ChatColor.BLUE + "Deaths: " + ChatColor.GREEN + gamer.getDeaths() + "  "
                + ChatColor.BLUE + "KD: " + ChatColor.GREEN +
                (gamer.getDeaths() > 0 ? new DecimalFormat("#.00").format((double) gamer.getKills() / (double) gamer.getDeaths()) : gamer.getKills()));
        Clan clan = ClanUtilities.getClan(gamer.getUUID());
        UtilMessage.message(player, ChatColor.BLUE + "Clan: " + ChatColor.GREEN + (clan == null ? "None" : clan.getName()));
        UtilMessage.message(player, ChatColor.BLUE + "Blocks Placed: " + ChatColor.GREEN +  gamer.getStatValue("BlocksPlaced")
                + " " + ChatColor.BLUE + "Blocks Broken: " + ChatColor.GREEN +  gamer.getStatValue("BlocksBroken"));
        UtilMessage.message(player, ChatColor.YELLOW + "Class Information");
        String msg = "";
        String msg2 = "";
        boolean alt = false;
        for (Role r : Role.roles) {
            if(alt){
                msg += ChatColor.BLUE + r.getName() + ": " + ChatColor.GREEN + gamer.getStatValue(r.getName()) + " ";
                alt = false;
            }else{
                msg2 += ChatColor.BLUE + r.getName() + ": " + ChatColor.GREEN + gamer.getStatValue(r.getName()) + " ";
                alt = true;
            }

        }
        UtilMessage.message(player, msg);
        UtilMessage.message(player, msg2);

        UtilMessage.message(player, ChatColor.YELLOW + "PvE Information");
        UtilMessage.message(player, ChatColor.BLUE + "Slime King: " + ChatColor.GREEN + gamer.getStatValue("Slime King")
                + ChatColor.BLUE + " Skeleton King: " + ChatColor.GREEN +  gamer.getStatValue("Skeleton King")
                + ChatColor.BLUE + " Broodmother: " + ChatColor.GREEN +  gamer.getStatValue("Broodmother")
                + ChatColor.BLUE + " Witherton: " + ChatColor.GREEN +  gamer.getStatValue("Charles Witherton"));

    }

    @Override
    public void help(Player player) {
    }
}
