package net.betterpvp.clans.general.commands;

import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CoordsCommand extends Command {

    public CoordsCommand() {
        super("coords", new String[]{"cords"}, Rank.PLAYER);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void execute(Player player, String[] args) {
        UtilMessage.message(player, "Coords", "The Fields are located at " + ChatColor.YELLOW + "(0x, 0z)");
        UtilMessage.message(player, "Coords", "The Lake is located at " + ChatColor.YELLOW + "(0, 0z)");
        //UtilMessage.message(player, "Coords", "The Shops are located at " + ChatColor.YELLOW + "(-25x, -300z)");
        //UtilMessage.message(player, "Coords", "The Outpost Shops are located at " + ChatColor.YELLOW + "(240x, 240z)");

        UtilMessage.message(player, "Coords", "The " + ChatColor.AQUA + "Blue Shops " + ChatColor.GRAY + "are located at " + ChatColor.YELLOW + "(400x, 0z)");
        UtilMessage.message(player, "Coords", "The " + ChatColor.RED + "Red Shops " + ChatColor.GRAY + "are located at " + ChatColor.YELLOW + "(-400x, 0z)");
        //UtilMessage.message(player, "Coords", "You can warp to either shop using either '/warp redshop' or '/warp blueshop' from spawn.");
    }

    @Override
    public void help(Player player) {
    }

}