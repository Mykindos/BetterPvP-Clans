package net.betterpvp.clans.gamer;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.events.ScoreboardUpdateEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.combat.ratings.RatingRepository;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.gamer.mysql.GamerRepository;
import net.betterpvp.clans.gamer.mysql.PlayerStatRepository;
import net.betterpvp.clans.scoreboard.Scoreboard;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.mysql.BuildRepository;
import net.betterpvp.clans.skills.selector.RoleBuild;
import net.betterpvp.clans.skills.selector.SelectorManager;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.client.listeners.ClientLoginEvent;
import net.betterpvp.core.client.listeners.ClientQuitEvent;
import net.betterpvp.core.client.mysql.SettingsRepository;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilFormat;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilProxy;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.UnsupportedEncodingException;

public class GamerConnectionListener extends BPVPListener<Clans> {

    private final World world;

    public GamerConnectionListener(Clans instance) {
        super(instance);
        this.world = Bukkit.getWorld("world");
    }


    @EventHandler(priority = EventPriority.LOW)
    public void onClientLogin(ClientLoginEvent e) throws UnsupportedEncodingException {

        Gamer gamer = GamerManager.getGamer(e.getClient().getUUID());
        if (gamer == null) {

            gamer = new Gamer(e.getClient().getUUID());
            gamer.setClient(e.getClient());
            GamerRepository.saveGamer(gamer);
            GamerManager.getGamers().add(gamer);
            gamer.loadDefaults();

        } else {

            BuildRepository.loadBuilds(getInstance(), gamer.getUUID());
            if (gamer.getClient() == null) {
                gamer.setClient(e.getClient());
            }
        }


        SettingsRepository.saveSetting(e.getClient().getUUID(), "General.Sidebar", 1);
        SettingsRepository.saveSetting(e.getClient().getUUID(), "General.Recharge Bar", 1);
        SettingsRepository.saveSetting(e.getClient().getUUID(), "General.Killfeed", 1);
        SettingsRepository.saveSetting(e.getClient().getUUID(), "General.Chat Filter", 1);

        RatingRepository.saveRatings(gamer);

        SettingsRepository.loadSettings(getInstance(), gamer.getClient());

        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = Bukkit.getPlayer(e.getClient().getUUID());
                if (player != null) {
                    Bukkit.getPluginManager().callEvent(new ScoreboardUpdateEvent(player));
                    if(!player.hasPlayedBefore() && GamerManager.getOnlineGamer(player).getStatValue("Time Played") > 1){
                        EffectManager.addEffect(player, EffectType.PROTECTION, 60_000 * 15);
                        UtilMessage.message(player, "Protection", "You have received " + ChatColor.GREEN + 15
                                + " minutes " + ChatColor.GRAY + "of PvP protection. " + ChatColor.YELLOW + "/protection " + ChatColor.GRAY + "to disable");
                    }
                }
            }
        }.runTaskLater(getInstance(), 20);

        Player player = Bukkit.getPlayer(e.getClient().getUUID());
        if (player != null) {
            gamer.setScoreboard(new Scoreboard(player));
            if (Clans.getOptions().isHub()) {
                try {
                    player.setResourcePack(Clans.getOptions().getTexturePackURL(),
                            UtilFormat.hexStringToByteArray(Clans.getOptions().getTexturePackSHA()));
                }catch(Exception ex){
                    ex.printStackTrace();
                    player.setResourcePack("https://puu.sh/FGt4O.zip",
                            UtilFormat.hexStringToByteArray(Clans.getOptions().getTexturePackSHA()));
                }
            }
        }

        gamer.setLastAction(System.currentTimeMillis());
        GamerManager.addOnlineGamer(gamer);

        if(!Clans.getOptions().isHub()) {
            new Thread(() -> {
                if (UtilProxy.isUsingProxy(player)) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ClientUtilities.messageStaff("Proxy", player.getName() + " may be using a VPN / Proxy", Rank.ADMIN);
                        }
                    }.runTask(getInstance());
                }
            }).start();
        }

    }

    @EventHandler
    public void onKickInactive(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.SEC_30) {
            if (!Clans.getOptions().isHub()) {
                int count = 0;
                for (Player p : Bukkit.getOnlinePlayers()) {
                    Client pClient = ClientUtilities.getOnlineClient(p);
                    if (pClient != null) {
                        if (pClient.hasDonation("ReservedSlot") || pClient.hasRank(Rank.TRIAL_MOD, false)) {
                            count++;
                        }
                    }
                }

                if (Bukkit.getOnlinePlayers().size() > Bukkit.getServer().getMaxPlayers() + count) {
                    for (Gamer gamer : GamerManager.getOnlineGamers()) {
                        if (gamer.getClient().hasDonation("ReservedSlot")) continue;
                        if (gamer.getClient().hasRank(Rank.TRIAL_MOD, false)) continue;
                        if (UtilTime.elapsed(gamer.getLastAction(), 60000 * 15)) {
                            Player player = Bukkit.getPlayer(gamer.getUUID());
                            if (player != null) {
                                player.kickPlayer(ChatColor.RED + "You were kicked for 10 minutes of inactivity!");
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRemoveOnlineGamer(PlayerQuitEvent e) {
        Gamer gamer = GamerManager.getOnlineGamer(e.getPlayer());
        if (gamer != null) {
            GamerManager.getOnlineGamers().remove(gamer);
        }
    }


    @EventHandler
    public void onTexturepackStatus(PlayerResourcePackStatusEvent e) {
        if (Clans.getOptions().isTexturePackForced()) {
            if (e.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED) {
                ClientUtilities.messageStaff(e.getPlayer().getName() + " was kicked for declining the texture pack", Rank.MODERATOR);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        e.getPlayer().kickPlayer(ChatColor.YELLOW + "You must allow the server resource pack. \nIn the server list set Server Resource Packs to enabled for BetterPvP. ");

                    }
                }.runTaskLater(getInstance(), 160);
            } else if (e.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
                ClientUtilities.messageStaff(e.getPlayer().getName() + " was kicked for failing to download the texture pack", Rank.MODERATOR);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        e.getPlayer().kickPlayer(ChatColor.YELLOW + "You must allow the server resource pack. \nIn the server list set Server Resource Packs to enabled for BetterPvP. ");

                    }
                }.runTaskLater(getInstance(), 160);
            }
        }

        if (e.getStatus() == PlayerResourcePackStatusEvent.Status.ACCEPTED) {
            System.out.println("Added Texture Pack immunity to " + e.getPlayer().getName());
            EffectManager.addEffect(e.getPlayer(), EffectType.TEXTURELOADING, 15000);
        } else if (e.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    System.out.println("Removed Texture Pack immunity from " + e.getPlayer().getName());
                    EffectManager.removeEffect(e.getPlayer(), EffectType.TEXTURELOADING);
                }
            }.runTaskLater(getInstance(), 10);

        }
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onClientQuit(ClientQuitEvent e) {
        Gamer g = GamerManager.getOnlineGamer(e.getClient().getUUID());
        if (g != null) {
            g.setScoreboard(null);

        }
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (String.valueOf(player.getLocation().getX()).equalsIgnoreCase("NaN")) {
            player.teleport(world.getSpawnLocation());
        }


        Gamer gamer = GamerManager.getOnlineGamer(player);
        if (gamer != null) {
            gamer.getClient().setLoggedIn(false);
            PlayerStatRepository.updateAllStats(gamer);
            GamerRepository.updateGamer(gamer);
            gamer.getBuilds().clear();
            String safe = LogManager.isSafe(player) ? ChatColor.GREEN + "Safe" + ChatColor.GRAY : ChatColor.RED + "Unsafe" + ChatColor.GRAY;
            event.setQuitMessage(ChatColor.RED + "Leave> " + ChatColor.GRAY + player.getName() + " (" + safe + ")");
        }
    }


}