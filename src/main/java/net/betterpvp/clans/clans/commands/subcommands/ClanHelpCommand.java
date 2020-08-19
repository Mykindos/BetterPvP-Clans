package net.betterpvp.clans.clans.commands.subcommands;

import net.betterpvp.clans.clans.commands.IClanCommand;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ClanHelpCommand implements IClanCommand {


    public void run(Player p, String[] args) {
        UtilMessage.message(p, ChatColor.YELLOW + "----- Clans Help -----");
        UtilMessage.message(p, ChatColor.AQUA + "/c create {name}" + ChatColor.GRAY + " - Creates a clan");
        UtilMessage.message(p, ChatColor.AQUA + "/c menu" + ChatColor.GRAY + " - Opens a menu where you can manage your clan");
        UtilMessage.message(p, ChatColor.AQUA + "/c invite {player}" + ChatColor.GRAY + " - Invites a player to your clan");
        UtilMessage.message(p, ChatColor.AQUA + "/c promote {player}" + ChatColor.GRAY + " - Promotes a player in your clan");
        UtilMessage.message(p, ChatColor.AQUA + "/c demote {player}" + ChatColor.GRAY + " - Demotes a player in your clan");
        UtilMessage.message(p, ChatColor.AQUA + "/c kick {player}" + ChatColor.GRAY + " - Kicks a player from yoru clan (admin)");
        UtilMessage.message(p, ChatColor.AQUA + "/c claim" + ChatColor.GRAY + " - Claims a chunk of land for your clan");
        UtilMessage.message(p, ChatColor.AQUA + "/c unclaim" + ChatColor.GRAY + " - Unclaims a chunk of land for your clan");
        UtilMessage.message(p, ChatColor.AQUA + "/c sethome" + ChatColor.GRAY + " - Sets your clan home");
        UtilMessage.message(p, ChatColor.AQUA + "/c shop" + ChatColor.GRAY + " - Open the clan shop");
        UtilMessage.message(p, ChatColor.AQUA + "/c stuck" + ChatColor.GRAY + " - Teleport out of neutral or ally clan territory.");
        UtilMessage.message(p, ChatColor.AQUA + "/c home" + ChatColor.GRAY + " - Teleports you to your clan home");
        UtilMessage.message(p, ChatColor.AQUA + "/c leave" + ChatColor.GRAY + " - Leaves your clan");
        UtilMessage.message(p, ChatColor.AQUA + "/c disband" + ChatColor.GRAY + " - Disbands your clan, kicking all members and losing all land");
        UtilMessage.message(p, ChatColor.AQUA + "/c enemy {clan}" + ChatColor.GRAY + " - Wages war against another clan");
        UtilMessage.message(p, ChatColor.AQUA + "/c neutral {clan}" + ChatColor.GRAY + " - Requets neutrality between your clan and another");
        UtilMessage.message(p, ChatColor.AQUA + "/c ally {clan}" + ChatColor.GRAY + " - Requests an alliance with another clan");
        UtilMessage.message(p, ChatColor.AQUA + "/c trust {clan}" + ChatColor.GRAY + " - Gives another clan access to your claims");
        UtilMessage.message(p, ChatColor.AQUA + "/c map" + ChatColor.GRAY + " - Shows a map of all clan claims around you");
        UtilMessage.message(p, ChatColor.AQUA + "/c stuck" + ChatColor.GRAY + " - Stuck in a claim and can't get out? This command will help");
    }

    @Override
    public String getName() {

        return "Help";
    }
}
