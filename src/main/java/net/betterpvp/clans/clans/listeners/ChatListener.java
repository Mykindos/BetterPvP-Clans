package net.betterpvp.clans.clans.listeners;

import me.mykindos.MAH.user.MAHManager;
import me.mykindos.MAH.user.MAHUser;
import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Alliance;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.commands.AllyChatCommand;
import net.betterpvp.clans.clans.commands.ClanChatCommand;
import net.betterpvp.clans.filter.FilterRepository;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.client.commands.admin.ChatToggle;
import net.betterpvp.core.client.commands.admin.StaffChatCommand;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.networking.NetworkReceiver;
import net.betterpvp.core.networking.discord.DiscordWebhook;
import net.betterpvp.core.punish.PunishManager;
import net.betterpvp.core.utility.UtilDiscord;
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

    public ChatListener(Clans i) {
        super(i);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGlobalChat(AsyncPlayerChatEvent e) {

        Gamer gamer = GamerManager.getOnlineGamer(e.getPlayer());
        Client client = gamer.getClient();

        if (client == null) return;
        if (client.hasRank(Rank.ADMIN, false) || e.getPlayer().isOp()) {
            if (!client.isLoggedIn()) {
                e.setCancelled(true);
                return;
            }
        }

        Player p = e.getPlayer();
        Clan clan = ClanUtilities.getClan(p);

        if (clan != null) {
            if (ClanChatCommand.enabled.contains(p.getName())) {

                clan.messageClan(ChatColor.AQUA + p.getName() + " " + ChatColor.DARK_AQUA + e.getMessage(), null, false);
                submitChatToWebhook(p, "Clan Chat> " + e.getMessage());
                e.setCancelled(true);
                return;
            } else if (AllyChatCommand.enabled.contains(p.getName())) {

                for (Alliance c : clan.getAlliances()) {
                    c.getClan().messageClan(ChatColor.DARK_GREEN + clan.getName() + " " + p.getName() + " "
                            + ChatColor.GREEN + e.getMessage(), null, false);
                }

                clan.messageClan(ChatColor.DARK_GREEN + clan.getName() + " " + p.getName() + " "
                        + ChatColor.GREEN + e.getMessage(), null, false);

                submitChatToWebhook(p, "Ally Chat> " + e.getMessage());

                e.setCancelled(true);
                return;
            }
        }

        if (StaffChatCommand.enabled.contains(p.getName())) {
            ClientUtilities.messageStaff(ChatColor.RED + p.getName() + "> " + ChatColor.WHITE + e.getMessage(), Rank.MODERATOR);
            submitChatToWebhook(p, "Staff Chat> " + e.getMessage());
            e.setCancelled(true);
            return;
        }

        if (ChatToggle.enabled || client.isAdministrating()) {

            if (PunishManager.isMuted(e.getPlayer().getUniqueId())) {
                UtilMessage.message(e.getPlayer(), "Punish", "You are muted!");
                e.setCancelled(true);
                return;

            }


            boolean sendSelfOnly = false;


            List<Player> tempIgnore = new ArrayList<>();
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (sendSelfOnly) {
                    if (!online.equals(p)) {
                        continue;
                    }
                }

                Clan target = ClanUtilities.getClan(online);

                Client onlineClient = ClientUtilities.getOnlineClient(online);
                if (onlineClient != null) {
                    if (onlineClient.getIgnore().contains(e.getPlayer().getUniqueId())) {
                        continue;
                    }


					if(onlineClient.getSettingAsBoolean("General.Chat Filter")){
//
						String message = e.getMessage().replaceAll("[^A-Za-z0-9]", "").toLowerCase();
						for(String s : FilterRepository.CHAT_FILTER){
							if(message.contains(s.toLowerCase())){
								if(!tempIgnore.contains(online)){
									tempIgnore.add(online);
								}
							}
//
						}
					}


                    if (tempIgnore.contains(online)) {
                        if (p.equals(online)) {
                            UtilMessage.message(p, "Chat", "Your chat filter is on! Only people with their filter off can see bad messages.");
                        }
                        continue;
                    }

                    if (onlineClient.getIgnore().contains(p.getUniqueId())) {
                        continue;
                    }


                    String rank = "";
                    if (client.hasRank(Rank.GRIEF, false)) {
                        if (!client.hasRank(Rank.DEVELOPER, false)) {
                            rank = client.getRank().getTag(true) + " ";
                        }
                    }

                    String donationRank = "";
                    if(gamer.getClient().hasDonation("VIP")){
                        donationRank = ChatColor.LIGHT_PURPLE.toString()  + "VIP ";
                    }

					/*
					if(CosmeticManager.hasActiveCosmetic(p, CosmeticType.PREFIX)){
						Prefix pref = (Prefix) CosmeticManager.getActiveCosmetic(p, CosmeticType.PREFIX);
						donationRank = DonationRank.valueOf(pref.getPerk().toUpperCase()).getTag(true) + " ";
					}
					*/

					String prefix = "";
                    MAHUser mahUser = MAHManager.getOnlineMAHUser(p);
                    if(mahUser != null){
                        if(mahUser.isAuthenticated()){
                            prefix = ChatColor.GREEN + "* ";
                        }

                        if(mahUser.isForced()){
                            if(!mahUser.isAuthenticated()){
                                prefix = ChatColor.RED + "* ";
                            }
                        }
                    }

                    if (ClanUtilities.getClan(p) == null || Clans.getOptions().isFNG()) {
                        online.sendMessage(prefix + rank + donationRank + ChatColor.YELLOW + p.getName() + ": " + ChatColor.RESET + e.getMessage());
                    } else {
                        new FancyMessage(prefix + rank + donationRank + ClanUtilities.getRelation(clan, target).getSecondary() + clan.getName() + " "
                                + ClanUtilities.getRelation(clan, target).getPrimary() + p.getName() + ": ")
                                .tooltip(ClanUtilities.getClanTooltip(p, clan)).then(e.getMessage()).send(online);
                    }


                }
            }
            submitChatToWebhook(p,  e.getMessage());
        }

        e.setCancelled(true);
    }

    private void submitChatToWebhook(Player author, String message){
        UtilDiscord.sendWebhook(Clans.getOptions().getDiscordChatWebhook(), author.getName(), message);

    }
}
