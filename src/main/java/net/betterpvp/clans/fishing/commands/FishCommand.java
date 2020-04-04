package net.betterpvp.clans.fishing.commands;

import net.betterpvp.clans.fishing.Fish;
import net.betterpvp.clans.fishing.FishManager;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FishCommand extends Command {

    public FishCommand() {
        super("fish", new String[]{}, Rank.PLAYER);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void execute(Player player, String[] args) {
        if(args == null) return;

        if(args[0].equalsIgnoreCase("top")){
            UtilMessage.message(player, ChatColor.RED + "Top 10 Fish of the Map");
            int count = 1;
            for(Fish f : FishManager.getTop()){
                UtilMessage.message(player, ChatColor.GREEN.toString() + count + ChatColor.YELLOW + " " + f.getSize() + ChatColor.GRAY + " pound "
                        + ChatColor.YELLOW + f.getFishName() +  ChatColor.GRAY + " caught by " + ChatColor.YELLOW + f.getPlayerName());
                count++;
            }
        }else if(args[0].equalsIgnoreCase("day")){
            UtilMessage.message(player, ChatColor.RED + "Top 10 Fish of the Day");
            int count = 1;
            for(Fish f : FishManager.getTopToday()){
                UtilMessage.message(player, ChatColor.GREEN.toString() + count + ChatColor.YELLOW + " " + f.getSize() + ChatColor.GRAY + " pound "
                        + ChatColor.YELLOW + f.getFishName() +  ChatColor.GRAY + " caught by " + ChatColor.YELLOW + f.getPlayerName());
                count++;
            }
        }else if(args[0].equalsIgnoreCase("week")){
            UtilMessage.message(player, ChatColor.RED + "Top 10 Fish of the Week");
            int count = 1;
            for(Fish f : FishManager.getTopWeek()){
                UtilMessage.message(player, ChatColor.GREEN.toString() + count + ChatColor.YELLOW + " " + f.getSize() + ChatColor.GRAY + " pound "
                        + ChatColor.YELLOW + f.getFishName() +  ChatColor.GRAY + " caught by " + ChatColor.YELLOW + f.getPlayerName());
                count++;
            }
        }else if(args[0].equalsIgnoreCase("help")) {
            UtilMessage.message(player, ChatColor.GREEN + "Fishing Commands");
            UtilMessage.message(player, ChatColor.AQUA + "/fish top" + ChatColor.GRAY + " - Display the top catches of the current season.");
            UtilMessage.message(player, ChatColor.AQUA + "/fish week" + ChatColor.GRAY + " - Display the top catches of this week.");
            UtilMessage.message(player, ChatColor.AQUA + "/fish day" + ChatColor.GRAY + " - Display todays top catches.");
        }

    }

    @Override
    public void help(Player player) {
        // TODO Auto-generated method stub

    }

}