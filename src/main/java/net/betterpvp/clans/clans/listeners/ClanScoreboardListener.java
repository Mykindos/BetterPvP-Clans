package net.betterpvp.clans.clans.listeners;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.events.ScoreboardUpdateEvent;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.worldevents.WEManager;
import net.betterpvp.clans.worldevents.WorldEvent;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.settings.SettingChangedEvent;
import net.betterpvp.core.utility.UtilFormat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class ClanScoreboardListener extends BPVPListener<Clans> {

    public ClanScoreboardListener(Clans instance) {
        super(instance);

    }

    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.FASTER) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Objective health = player.getScoreboard().getObjective("healthDisplay");
                if (health != null) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        health.getScore(p.getName()).setScore((int) p.getHealth());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onScoreboardUpdate(ScoreboardUpdateEvent e) {


        Gamer gamer = GamerManager.getOnlineGamer(e.getPlayer());

        Scoreboard scoreboard = e.getPlayer().getScoreboard();
        if (scoreboard != null) {
            if (gamer != null) {
                Objective health = scoreboard.getObjective("healthDisplay");
                if (health == null) {
                    health = scoreboard.registerNewObjective("healthDisplay", "dummy", "BetterPvP");
                    health.setDisplayName(ChatColor.RED + "❤");
                    health.setDisplaySlot(DisplaySlot.BELOW_NAME);
                }

                if (gamer.getClient().getSettingAsBoolean("General.Sidebar")) {
                    if (Clans.getOptions().isHub()) {
                        return;
                    }
                    Objective side = scoreboard.getObjective("BetterPvP");
                    if (side == null) {
                        side = scoreboard.registerNewObjective("BetterPvP", "dummy", "BetterPvP");
                        side.setDisplaySlot(DisplaySlot.SIDEBAR);
                        side.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD + "  Season 6  ");
                    }


                    for (String s : scoreboard.getEntries()) {
                        scoreboard.resetScores(s);
                    }

                    Clan clan = ClanUtilities.getClan(e.getPlayer());

                    if (clan != null) {

                        side.getScore(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Clan").setScore(15);
                        String name = clan.getName() + "§r";

                        side.getScore(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + name + "").setScore(14);
                        side.getScore("§r").setScore(13);

                        if (clan.getTerritory().size() > 0) {
                            side.getScore(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Clan Energy").setScore(12);
                            //side.getScore(ChatColor.GREEN.toString() + ChatColor.BOLD + (ClanUtilities.getHoursOfEnergy(clan)) + " hours").setScore(11);
                            side.getScore(ChatColor.GREEN.toString() + ChatColor.BOLD + ClanUtilities.getEnergyTimeRemaining(clan)).setScore(11);
                            side.getScore("§r§r").setScore(10);
                        }
                    }


                    side.getScore(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Coins").setScore(9);
                    side.getScore(ChatColor.GOLD.toString() + ChatColor.BOLD + UtilFormat.formatNumber(gamer.getCoins())).setScore(8);

                    side.getScore("§r§r§r").setScore(7);
                    side.getScore(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Territory").setScore(6);
                    Clan standing = ClanUtilities.getClan(e.getPlayer().getLocation());
                    if (standing != null) {
                        side.getScore(ClanUtilities.getRelation(clan, standing).getPrimary(true) + standing.getName()).setScore(5);
                    } else {
                        side.getScore(ChatColor.GREEN.toString() + ChatColor.BOLD + "Wilderness").setScore(5);
                    }

                    if (WEManager.isWorldEventActive()) {
                        WorldEvent we = WEManager.getActiveWorldEvent();

                        side.getScore("§r§r§r§r").setScore(4);

                        side.getScore(ChatColor.WHITE.toString() + ChatColor.BOLD + "World Event").setScore(3);
                        side.getScore(we.getDisplayName()).setScore(2);

                    }
                } else {
                    e.getPlayer().getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
                    Objective side = scoreboard.getObjective("BetterPvP");
                    if (side != null) {
                        side.unregister();
                    }

                }
            }
        }
    }

    @EventHandler
    public void onSettingsChange(SettingChangedEvent e) {
        Bukkit.getPluginManager().callEvent(new ScoreboardUpdateEvent(e.getPlayer()));
    }


}
