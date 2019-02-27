package net.betterpvp.clans.clans.listeners;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Alliance;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.commands.AllyChatCommand;
import net.betterpvp.clans.clans.commands.ClanChatCommand;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.client.commands.ChatToggle;
import net.betterpvp.core.client.commands.StaffChatCommand;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.punish.PunishManager;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.fancymessage.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;

public class ChatListener extends BPVPListener<Clans> {

	public ChatListener(Clans i){
		super(i);
	}

	private static final String[] filter = {"fuck", "shit", "cunt", "kys", "nigger", "faggot", "gook", "chink", "nigga", "nigguh", "cock", "bitch"};

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onGlobalChat(AsyncPlayerChatEvent e) {

		Gamer gamer = GamerManager.getOnlineGamer(e.getPlayer());
		Client client = gamer.getClient();

		if(client == null) return;
		if(client.hasRank(Rank.ADMIN, false) || e.getPlayer().isOp()){
			if(!client.isLoggedIn()){
				e.setCancelled(true);
				return;
			}
		}

		Player p = e.getPlayer();
		Clan clan = ClanUtilities.getClan(p);

		if(clan != null){
			if (ClanChatCommand.enabled.contains(p.getName())) {

				clan.messageClan(ChatColor.AQUA + p.getName() + " " + ChatColor.DARK_AQUA + e.getMessage(), null, false);

				e.setCancelled(true);
				return;
			}else if(AllyChatCommand.enabled.contains(p.getName())){

				for(Alliance c : clan.getAlliances()){
					c.getClan().messageClan(ChatColor.DARK_GREEN + clan.getName() + " " + p.getName() + " "
							+ ChatColor.GREEN + e.getMessage(), null, false);
				}

				clan.messageClan(ChatColor.DARK_GREEN + clan.getName() + " " + p.getName() + " "
						+ ChatColor.GREEN + e.getMessage(), null, false);

				e.setCancelled(true);
				return;
			}
		}

		if(StaffChatCommand.enabled.contains(p.getName())){
			ClientUtilities.messageStaff(ChatColor.RED + p.getName() + "> " + ChatColor.WHITE + e.getMessage(), Rank.MODERATOR);
			e.setCancelled(true);
			return;
		}

		if(ChatToggle.enabled || client.isAdministrating()){

			if(PunishManager.isMuted(e.getPlayer().getUniqueId())){
				UtilMessage.message(e.getPlayer(), "Punish", "You are muted!");
				e.setCancelled(true);
				return;

			}


			boolean sendSelfOnly = false;


			List<Player> tempIgnore = new ArrayList<>();
			for (Player online : Bukkit.getOnlinePlayers()) {
				if(sendSelfOnly){
					if(online != p){
						continue;
					}
				}

				Clan target = ClanUtilities.getClan(online);

				Client onlineClient = ClientUtilities.getOnlineClient(online);
				if(onlineClient != null){
					if (onlineClient.getIgnore().contains(e.getPlayer().getUniqueId())) {
						continue;
					}

				/*
					if(onlineClient.getSettings().getSettings().get("ChatFilter")){

						String message = e.getMessage().replaceAll("[^A-Za-z0-9]", "").toLowerCase();
						for(String s : filter){
							if(message.contains(s.toLowerCase())){
								if(!tempIgnore.contains(online)){
									tempIgnore.add(online);
								}
							}

						}
					}
				*/

					if(tempIgnore.contains(online)){
						if(p == online) {
							UtilMessage.message(p, "Chat", "Your chat filter is on! Only people with their filter off can see bad messages.");
						}
						continue;
					}

					if(onlineClient.getIgnore().contains(p.getUniqueId())){
						continue;
					}


					String rank = "";
					if (client.hasRank(Rank.GRIEF, false)) {
						if(!client.hasRank(Rank.DEVELOPER, false)){
							rank = client.getRank().getTag(true) + " ";
						}
					}

					String donationRank = "";

					/*
					if(CosmeticManager.hasActiveCosmetic(p, CosmeticType.PREFIX)){
						Prefix pref = (Prefix) CosmeticManager.getActiveCosmetic(p, CosmeticType.PREFIX);
						donationRank = DonationRank.valueOf(pref.getPerk().toUpperCase()).getTag(true) + " ";
					}
					*/
					

					if (ClanUtilities.getClan(p) == null || Clans.getOptions().isFNG()) {
						online.sendMessage( rank + donationRank + ChatColor.YELLOW + p.getName() + ": " + ChatColor.RESET + e.getMessage());
					} else {
						new FancyMessage(rank + donationRank + ClanUtilities.getRelation(clan, target).getSecondary() + clan.getName() + " "
								+ ClanUtilities.getRelation(clan, target).getPrimary() + p.getName() + ": ")
								.tooltip(ClanUtilities.getClanTooltip(p, clan)).then(e.getMessage()).send(online);
					}
				}
			}
		}

		e.setCancelled(true);
	}
}
