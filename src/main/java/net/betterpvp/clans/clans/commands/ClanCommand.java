package net.betterpvp.clans.clans.commands;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.ClanUtilities.ClanRelation;
import net.betterpvp.clans.clans.commands.subcommands.*;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.utility.UtilLocation;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import net.betterpvp.core.utility.UtilTime.TimeUnit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ClanCommand implements CommandExecutor {

	private Clans i;
	private static List<IClanCommand> commands;
	public ClanCommand(Clans i){
		this.i = i;
		commands = new ArrayList<>();
		
		commands.add(new AllyCommand());
		commands.add(new ClaimCommand());
		commands.add(new ClanHelpCommand());
		commands.add(new CreateCommand());
		commands.add(new DemoteCommand());
		commands.add(new DisbandCommand());
		commands.add(new EnemyCommand());
		commands.add(new EnergyCommand());
		commands.add(new InviteCommand());
		commands.add(new HomeCommand(i));
		commands.add(new JoinCommand(i));
		commands.add(new KickCommand());
		commands.add(new LeaveCommand(i));
		commands.add(new MapCommand());
		commands.add(new NeutralCommand());
		commands.add(new PromoteCommand());
		commands.add(new PromoteCommand());
		commands.add(new SetHomeCommand());
		commands.add(new TopCommand());
		commands.add(new TrustCommand());
		commands.add(new UnclaimallCommand(i));
		commands.add(new UnclaimCommand(i));
		commands.add(new MenuCommand(i));
		commands.add(new StuckCommand(i));
	}

	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("[BattleAU > Command] Invaliad command sender!");
			return true;
		}

		if(Clans.getOptions().isFNG()){
			UtilMessage.message(sender, "Clans", "Clans is not currently enabled!");
			return true;
		}

		Player player = (Player) sender;
		
		boolean showPoints = ClientUtilities.getOnlineClient(player).hasRank(Rank.ADMIN, false);
		if (args == null || args.length == 0) {
			Clan clan = ClanUtilities.getClan(player);
			if (clan != null) {

				UtilMessage.message(player, "Clans", clan.getName() + " Information: ");
				
				UtilMessage.message(player, "Age: " + ChatColor.YELLOW + clan.getAge());
				UtilMessage.message(player, "Territory: " + ChatColor.YELLOW + clan.getTerritory().size() + "/" + (3 + clan.getMembers().size()));
				UtilMessage.message(player, "Home: " + (clan.getHome() == null ? ChatColor.RED + "Not set" : ChatColor.YELLOW + UtilLocation.locationToString(clan.getHome())));
				UtilMessage.message(player, "Allies: " + ClanUtilities.getAllianceList(player, clan));
				UtilMessage.message(player, "Enemies: " + ClanUtilities.getEnemyListDom(player, clan));
				UtilMessage.message(player, "Members: " + ClanUtilities.getMembersList(clan));
				UtilMessage.message(player, "TNT Protection: " + clan.getVulnerableString());
				UtilMessage.message(player, "Cooldown: " + (!clan.isOnCooldown() ? ChatColor.GREEN + "No" 
						: ChatColor.RED + UtilTime.getTime(clan.getCooldown(), TimeUnit.BEST, 1)));
				UtilMessage.message(player, "Energy: " + ChatColor.YELLOW +(int) clan.getEnergy());
				
					UtilMessage.message(player, "Points: " + ChatColor.YELLOW + clan.getPoints());
				
			} else {
				UtilMessage.message(player, "Clans", "You are not in a Clan.");
			}
			return true;
		}
		
		IClanCommand com = getCommand(args[0]);
		if(com != null){
			com.run(player, args);
		}else{
			Clan clan = ClanUtilities.getClan(args[0]);
			Clan playerClan = ClanUtilities.getClan(player);

			ChatColor color = ChatColor.GRAY;
			if (ClanUtilities.getRelation(playerClan, clan) != ClanRelation.NEUTRAL) {
				color = ClanUtilities.getRelation(playerClan, clan).getSecondary();
			}
			
			


			if (clan != null) {

				UtilMessage.message(player, "Clans", color + clan.getName() + " Information: ");
				if(color == ChatColor.DARK_RED) {
					UtilMessage.message(player, "Dominance: " + playerClan.getDominanceString(clan));
				}
				UtilMessage.message(player, "Age: " + ChatColor.YELLOW + clan.getAge());
				UtilMessage.message(player, "Territory: " + ChatColor.YELLOW + clan.getTerritory().size() + "/" + (3 + clan.getMembers().size()));
				UtilMessage.message(player, "Allies: " + ClanUtilities.getAllianceList(player, clan));
				UtilMessage.message(player, "Enemies: " + ClanUtilities.getEnemyList(player, clan));
				UtilMessage.message(player, "Members: " + ClanUtilities.getMembersList(clan));
				UtilMessage.message(player, "TNT Protection: " + clan.getVulnerableString());
				UtilMessage.message(player, "Cooldown: " + (!clan.isOnCooldown() ? ChatColor.GREEN + "No" 
						: ChatColor.RED + UtilTime.getTime(clan.getCooldown(), TimeUnit.BEST, 2)));
				UtilMessage.message(player, "Energy: " + ChatColor.YELLOW +(int) clan.getEnergy());
				if(showPoints) {
					UtilMessage.message(player, "Points: " + ChatColor.YELLOW + clan.getPoints());
				}

			} else {
				ClanUtilities.searchClan(player, args[0], true);
			}
		}

		return true;
	}
	
	public static IClanCommand getCommand(String sub){
		return commands.stream().filter(s -> s.getName().equalsIgnoreCase(sub)).findFirst().orElse(null);
	}
}
