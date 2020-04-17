package net.betterpvp.clans.scoreboard;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanMember;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.events.*;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.client.listeners.ClientLoginEvent;
import net.betterpvp.core.client.listeners.ClientQuitEvent;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.minecraft.server.v1_15_R1.BlockSkull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;


public class ScoreboardManager extends BPVPListener<Clans> {


    private static Set<Scoreboard> scoreboards = new HashSet<>();


    public ScoreboardManager(Clans i) {
        super(i);
        scoreboards.clear();
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            addPlayer(ClientUtilities.getOnlineClient(p));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClientLogin(ClientLoginEvent e) {

        Player player = Bukkit.getPlayer(e.getClient().getUUID());
        if (player != null) {
            if (getScoreboard(player) == null) {
                scoreboards.add(new Scoreboard(player));
            }

            Scoreboard scoreboard = getScoreboard(player);
            initialise(scoreboard);

            scoreboards.forEach(s -> {
                addPlayer(e.getClient());
            });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClientQuit(PlayerQuitEvent e) {
        Clan clan = ClanUtilities.getClan(e.getPlayer().getUniqueId());

        scoreboards.forEach(s -> {
            removePlayer(s, e.getPlayer().getName(), clan);
        });

        scoreboards.remove(getScoreboard(e.getPlayer()));

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClanCreate(ClanCreateEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                scoreboards.forEach(s -> {
                    addClan(s, e.getClanName());
                    Bukkit.getPluginManager().callEvent(new ScoreboardUpdateEvent(e.getPlayer()));
                });
            }
        }.runTaskLater(getInstance(), 10);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClanDisband(ClanDeleteEvent e) {


        e.getClan().getMembers().forEach(m -> {
            Player player = Bukkit.getPlayer(m.getUUID());
            if (player != null) {
                Scoreboard s = getScoreboard(player);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        initialise(s);
                        Bukkit.getPluginManager().callEvent(new ScoreboardUpdateEvent(e.getPlayer()));

                        scoreboards.forEach(s -> {
                            removeClan(s, e.getClan().getName());
                        });
                    }
                }.runTaskLater(getInstance(), 10);
            }
        });


    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClanAlly(ClanRelationshipEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateRelation(e.getClanA());
                updateRelation(e.getClanB());
            }
        }.runTaskLater(getInstance(), 10);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMemberJoin(MemberJoinClanEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getPluginManager().callEvent(new ScoreboardUpdateEvent(e.getPlayer()));
                scoreboards.forEach(s -> {
                    removePlayer(s, e.getPlayer().getName(), null);
                    addPlayer(ClientUtilities.getOnlineClient(e.getPlayer()));
                });
            }
        }.runTaskLater(getInstance(), 10);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMemberLeaveClan(MemberLeaveClanEvent e) {

        Player player = Bukkit.getPlayer(e.getClient().getUUID());

        Scoreboard scoreboard = getScoreboard(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                initialise(scoreboard);
                Bukkit.getPluginManager().callEvent(new ScoreboardUpdateEvent(Bukkit.getPlayer(e.getClient().getUUID())));

                scoreboards.forEach(s -> {
                    removePlayer(s, e.getClient().getName(), e.getClan());
                    addPlayer(e.getClient());
                });
            }
        }.runTaskLater(getInstance(), 10);


    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChunkClaim(ChunkClaimEvent e){
        for(Entity ent : e.getChunk().getEntities()){
            if(ent instanceof Player){
                Bukkit.getPluginManager().callEvent(new ScoreboardUpdateEvent((Player) ent));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMemberKicked(ClanKickMemberEvent e) {

        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getPluginManager().callEvent(new ScoreboardUpdateEvent(Bukkit.getPlayer(e.getTarget().getUUID())));
                scoreboards.forEach(s -> {
                    removePlayer(s, e.getTarget().getName(), e.getClan());
                    Player player = Bukkit.getPlayer(e.getTarget().getUUID());
                    if (player != null) {
                        addPlayer(e.getTarget());
                    }
                });
            }
        }.runTaskLater(getInstance(), 10);

    }

    public void initialise(Scoreboard s) {
        s.getScoreboard().getTeams().forEach(t -> t.unregister());

        ClanUtilities.getClans().forEach(clan -> {
            if (clan.isOnline()) {
                addClan(s, clan);
                Team team = s.getScoreboard().getTeam(clan.getName());
                for (ClanMember member : clan.getMembers()) {
                    Player pMember = Bukkit.getPlayer(member.getUUID());
                    if (pMember != null) {
                        String names = pMember.getName();
                        if (!team.hasEntry(names)) {
                            team.addEntry(names);
                        }
                    }
                }
            }
        });

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (ClanUtilities.getClan(p) == null) {
                addNone(s, p.getName());
            }
        }
    }

    @EventHandler
    public void updateScoreboardTimer(UpdateEvent e){
        if(e.getType() == UpdateEvent.UpdateType.SEC_30){
            Bukkit.getOnlinePlayers().forEach(p -> Bukkit.getPluginManager().callEvent(new ScoreboardUpdateEvent(p)));
        }
    }


    private void removePlayer(Scoreboard s, String name, Clan c) {
        Iterator<Team> teams = s.getScoreboard().getTeams().iterator();
        while (teams.hasNext()) {
            Team t = teams.next();
            if (c != null) {
                if (t.getName().equals(c.getName())) {
                    if (t.hasEntry(name)) {
                        t.removeEntry(name);
                    }

                    if (!c.isOnline()) {
                        t.unregister();
                    }
                }
            } else {
                if (t.getName().equals("None")) {
                    if (t.hasEntry(name)) {
                        t.removeEntry(name);
                    }
                }
            }
        }

        if (Bukkit.getPlayer(s.getUUID()) != null) {
            addNone(s, name);
        }
    }

    private void addNone(Scoreboard s, String name) {

        org.bukkit.scoreboard.Scoreboard scoreboard = s.getScoreboard();
        Team noTeam = scoreboard.getTeam("None");
        if (noTeam == null) {
            scoreboard.registerNewTeam("None");

            Team team = scoreboard.getTeam("None");
            team.setColor(ChatColor.YELLOW);
            team.setPrefix(ChatColor.YELLOW.toString());
        }
        if (!scoreboard.getTeam("None").hasEntry(name)) {
            scoreboard.getTeam("None").addEntry(name);
        }

    }

    public void addPlayer(Client client) {
        Clan clan = ClanUtilities.getClan(client.getUUID());

        scoreboards.forEach(s -> {
            if (clan != null) {
                addClan(s, clan);
                s.getScoreboard().getTeams().forEach(team -> {
                    if (team.getName().equals(clan.getName())) {
                        if (!team.hasEntry(client.getName())) {
                            team.addEntry(client.getName());
                        }
                    }
                });

            } else {
                addNone(s, client.getName());
            }
        });

    }

    public Scoreboard getScoreboard(Player p) {
        for (Scoreboard s : scoreboards) {
            if (s.getUUID().equals(p.getUniqueId())) {
                return s;
            }
        }
        return null;
    }

    public Set<Scoreboard> getScoreboards() {
        return scoreboards;
    }

    public void removeScoreboard(Scoreboard s) {
        scoreboards.remove(s);
    }


    public void updateRelation() {
        for (Scoreboard s : getScoreboards()) {

            Player p = Bukkit.getPlayer(s.getUUID());
            if (p != null) {
                Clan c = ClanUtilities.getClan(p);
                if (c != null) {
                    for (Team team : s.getScoreboard().getTeams()) {
                        if (team.getName().equals("None")) {
                            team.setPrefix(ChatColor.YELLOW.toString());
                            continue;
                        }
                        setPrefix(team, c, ClanUtilities.getClan(team.getName()));
                    }
                }
            }
        }
    }

    public void updateRelation(Clan clan) {
        for (ClanMember member : clan.getMembers()) {
            Player pMember = Bukkit.getPlayer(member.getUUID());
            if (pMember != null) {
                Scoreboard scoreboard = getScoreboard(pMember);
                scoreboard.getScoreboard().getTeams().forEach(team -> {
                    if (team.getName().equals("None")) {
                        team.setPrefix(ChatColor.YELLOW.toString());
                    } else {
                        setPrefix(team, clan, ClanUtilities.getClan(team.getName()));
                    }
                });
            }
        }

    }

    public void addClans(Scoreboard s) {
        ClanUtilities.getClans().forEach(clan -> {
            if (clan.isOnline()) {
                addClan(s, clan);
                Team team = s.getScoreboard().getTeam(clan.getName());
                for (ClanMember member : clan.getMembers()) {
                    Player pMember = Bukkit.getPlayer(member.getUUID());
                    if (pMember != null) {
                        String names = pMember.getName();
                        if (!team.hasEntry(names)) {
                            team.addEntry(names);
                        }
                    }
                }
            }
        });
    }


    public void removeClan(Scoreboard s, String name) {

        Iterator<Team> iterator = s.getScoreboard().getTeams().iterator();
        while (iterator.hasNext()) {
            Team team = iterator.next();
            if (team.getName().equals(name)) {


                for (String entry : team.getEntries()) {
                    Player player = Bukkit.getPlayer(entry);
                    if (player != null) {
                        addNone(s, player.getName());
                    }

                }
                team.unregister();

            }
        }


    }

    public void addClan(Scoreboard s, String name) {

        if (!isTeam(s, name)) {
            Team team = s.getScoreboard().registerNewTeam(name);
            Clan clan = ClanUtilities.getClan(name);
            Player p = Bukkit.getPlayer(s.getUUID());
            if (p != null) {
                setPrefix(team, ClanUtilities.getClan(p), clan);
                addMembersToTeam(team, clan);
            }
        }

    }

    public void addClan(Scoreboard s, Clan clan) {

        if (!isTeam(s, clan.getName())) {
            Team team = s.getScoreboard().registerNewTeam(clan.getName());

            Player player = Bukkit.getPlayer(s.getUUID());
            if (player != null) {
                Clan playerClan = ClanUtilities.getClan(player);
                setPrefix(team, playerClan, clan);

                addMembersToTeam(team, clan);
            }
        }

    }

    private void addMembersToTeam(Team team, Clan clan) {
        for (ClanMember member : clan.getMembers()) {
            Player pMember = Bukkit.getPlayer(member.getUUID());
            if (pMember != null) {
                String names = pMember.getName();
                if (!team.hasEntry(names)) {
                    team.addEntry(names);
                }
            }
        }
    }

    public boolean isTeam(Scoreboard s, String name) {
        for (Team team : s.getScoreboard().getTeams()) {
            if (team.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }


    private void setPrefix(Team team, Clan c, Clan d) {
        if (Clans.getOptions().isFNG()) {
            team.setPrefix(ChatColor.YELLOW + "");
            team.setSuffix("");
            return;
        }

        if (c != null && d != null) {

            String prefix = d.getName().length() > 11 ? d.getName().substring(0, 10) : d.getName();
            if (c.isAllied(d)) {
                team.setPrefix(c.hasTrust(d) ? ChatColor.DARK_GREEN + prefix + " "
                        : ChatColor.DARK_GREEN + prefix + " ");
                team.setColor(c.hasTrust(d) ? ChatColor.DARK_GREEN : ChatColor.GREEN);
                team.setSuffix("");
            } else if (c.isEnemy(d)) {
                team.setPrefix(ChatColor.DARK_RED + prefix + " ");
                team.setColor(ChatColor.RED);
                team.setSuffix(d.getSimpleDominanceString(c));
            } else if (c == d) {
                team.setPrefix(ChatColor.DARK_AQUA + prefix + " ");
                team.setColor(ChatColor.AQUA);
                team.setSuffix("");
            } else {
                team.setPrefix(ChatColor.GOLD + prefix + " ");
                team.setColor(ChatColor.YELLOW);
                team.setSuffix("");
            }
        } else {
            if (c == null && d != null) {
                String prefix = d.getName().length() > 11 ? d.getName().substring(0, 10) : d.getName();
                team.setPrefix(ChatColor.GOLD + prefix + " ");
                team.setColor(ChatColor.YELLOW);
                team.setSuffix("");
            } else {
                team.setPrefix(ChatColor.YELLOW + "");
                team.setColor(ChatColor.YELLOW);
                team.setSuffix("");
            }
        }
    }
}