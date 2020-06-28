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
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.client.listeners.ClientLoginEvent;
import net.betterpvp.core.client.listeners.ClientQuitEvent;
import net.betterpvp.core.client.mysql.SettingsRepository;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.utility.UtilFormat;
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
            loadDefaults(gamer);

        } else {

            BuildRepository.loadBuilds(getInstance(), gamer.getUUID());
            if (gamer.getClient() == null) {
                gamer.setClient(e.getClient());
            }
        }


        SettingsRepository.saveSetting(e.getClient().getUUID(), "General.Sidebar", 1);
        SettingsRepository.saveSetting(e.getClient().getUUID(), "General.Recharge Bar", 1);
        SettingsRepository.saveSetting(e.getClient().getUUID(), "General.Killfeed", 1);

        RatingRepository.saveRatings(gamer);

        SettingsRepository.loadSettings(getInstance(), gamer.getClient());

        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = Bukkit.getPlayer(e.getClient().getUUID());
                if (player != null) {
                    Bukkit.getPluginManager().callEvent(new ScoreboardUpdateEvent(player));
                }
            }
        }.runTaskLater(getInstance(), 20);

        Player player = Bukkit.getPlayer(e.getClient().getUUID());
        if (player != null) {
            gamer.setScoreboard(new Scoreboard(player));
            if(Clans.getOptions().isHub()) {
                player.setResourcePack(Clans.getOptions().getTexturePackURL(),
                        UtilFormat.hexStringToByteArray(Clans.getOptions().getTexturePackSHA()));
            }
        }

        GamerManager.addOnlineGamer(gamer);


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

    private void loadDefaults(Gamer c) {


        for (int d = 1; d < 5; d++) {

            RoleBuild a = new RoleBuild("Assassin", d);
            if (d == 1) {
                a.setActive(true);

            }
            a.setSkill(Types.SWORD, SelectorManager.getSkills().get("Sever"), 3);
            a.setSkill(Types.AXE, SelectorManager.getSkills().get("Leap"), 5);
            a.setSkill(Types.PASSIVE_A, SelectorManager.getSkills().get("Backstab"), 1);
            a.setSkill(Types.PASSIVE_B, SelectorManager.getSkills().get("Smoke Bomb"), 3);
            a.takePoints(12);

            RoleBuild g = new RoleBuild("Gladiator", d);
            if (d == 1) {
                g.setActive(true);
            }
            g.setSkill(Types.SWORD, SelectorManager.getSkills().get("Takedown"), 5);
            g.setSkill(Types.AXE, SelectorManager.getSkills().get("Seismic Slam"), 3);
            g.setSkill(Types.PASSIVE_A, SelectorManager.getSkills().get("Colossus"), 1);
            g.setSkill(Types.PASSIVE_B, SelectorManager.getSkills().get("Stampede"), 3);
            g.takePoints(12);

            RoleBuild r = new RoleBuild("Ranger", d);
            if (d == 1) {
                r.setActive(true);
            }
            r.setSkill(Types.SWORD, SelectorManager.getSkills().get("Disengage"), 3);
            r.setSkill(Types.BOW, SelectorManager.getSkills().get("Incendiary Shot"), 5);
            r.setSkill(Types.PASSIVE_A, SelectorManager.getSkills().get("Longshot"), 3);
            r.setSkill(Types.PASSIVE_B, SelectorManager.getSkills().get("Sharpshooter"), 1);
            r.takePoints(12);

            RoleBuild p = new RoleBuild("Paladin", d);
            if (d == 1) {
                p.setActive(true);
            }
            p.setSkill(Types.SWORD, SelectorManager.getSkills().get("Inferno"), 5);
            p.setSkill(Types.AXE, SelectorManager.getSkills().get("Molten Blast"), 3);
            p.setSkill(Types.PASSIVE_A, SelectorManager.getSkills().get("Holy Light"), 2);
            p.setSkill(Types.PASSIVE_B, SelectorManager.getSkills().get("Immolate"), 2);
            p.takePoints(12);

            RoleBuild k = new RoleBuild("Knight", d);
            if (d == 1) {
                k.setActive(true);
            }
            k.setSkill(Types.SWORD, SelectorManager.getSkills().get("Riposte"), 3);
            k.setSkill(Types.AXE, SelectorManager.getSkills().get("Bulls Charge"), 5);
            k.setSkill(Types.PASSIVE_A, SelectorManager.getSkills().get("Fury"), 3);
            k.setSkill(Types.PASSIVE_B, SelectorManager.getSkills().get("Swordsmanship"), 1);
            k.takePoints(12);

            RoleBuild n = new RoleBuild("Warlock", d);
            if (d == 1) {
                n.setActive(true);
            }

            n.setSkill(Types.SWORD, SelectorManager.getSkills().get("Leech"), 4);
            n.setSkill(Types.AXE, SelectorManager.getSkills().get("Bloodshed"), 5);
            n.setSkill(Types.PASSIVE_B, SelectorManager.getSkills().get("Soul Harvest"), 3);
            n.takePoints(12);

            c.getBuilds().add(k);
            c.getBuilds().add(r);
            c.getBuilds().add(g);
            c.getBuilds().add(p);
            c.getBuilds().add(a);
            c.getBuilds().add(n);

            BuildRepository.saveBuild(c.getUUID(), a);
            BuildRepository.saveBuild(c.getUUID(), p);
            BuildRepository.saveBuild(c.getUUID(), r);
            BuildRepository.saveBuild(c.getUUID(), g);
            BuildRepository.saveBuild(c.getUUID(), k);
            BuildRepository.saveBuild(c.getUUID(), n);
        }
    }
}