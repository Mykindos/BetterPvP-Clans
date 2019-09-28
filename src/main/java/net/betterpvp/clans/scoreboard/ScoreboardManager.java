package net.betterpvp.clans.scoreboard;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanMember;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.events.ClanAllyClanEvent;
import net.betterpvp.clans.clans.events.ClanCreateEvent;
import net.betterpvp.clans.clans.events.ClanDeleteEvent;
import net.betterpvp.clans.clans.events.ClanRelationshipEvent;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.client.listeners.ClientLoginEvent;
import net.betterpvp.core.client.listeners.ClientQuitEvent;
import net.betterpvp.core.framework.BPVPListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.scoreboard.Team;

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
            addPlayer(p.getName());
        }
    }

    @EventHandler
    public void onClientLogin(ClientLoginEvent e) {

        Clan clan = ClanUtilities.getClan(e.getClient().getUUID());
        for (Gamer g : GamerManager.getOnlineGamers()) {

        }
    }

    @EventHandler
    public void onClientQuit(ClientQuitEvent e) {
        Clan c = ClanUtilities.getClan(e.getClient().getUUID());
        for (Gamer g : GamerManager.getOnlineGamers()) {
            if (c != null) {
                Scoreboard s = g.getScoreboard();
                if (s == null) continue;
                removePlayer(s, e.getClient().getName(), c);

            }

        }
    }

    @EventHandler
    public void onClanCreate(ClanCreateEvent e){
        for(Gamer g : GamerManager.getOnlineGamers()){
            Scoreboard s = g.getScoreboard();

            addClan(s, e.getClanName());
        }
    }

    @EventHandler
    public void onClanDisband(ClanDeleteEvent e){

        for(Gamer g : GamerManager.getOnlineGamers()){
            Scoreboard s = g.getScoreboard();

            removeClan(s, e.getClan().getName());


        }
    }

    @EventHandler
    public void onClanRelationshipChange(ClanRelationshipEvent e){

    }


    private void removePlayer(Scoreboard s, String name, Clan c) {
        Iterator<Team> teams = s.getScoreboard().getTeams().iterator();
        while (teams.hasNext()) {
            Team t = teams.next();
            if (t.getName().equals(c.getName())) {
                if (t.hasEntry(name)) {
                    t.removeEntry(name);
                }

                if (!c.isOnline()) {
                    t.unregister();
                }
            }
        }

        addNone(s, name);
    }

    private void addNone(Scoreboard s, String name) {

        Team noTeam = s.getScoreboard().getTeam("None");
        if (noTeam == null) {
            s.getScoreboard().registerNewTeam("None");
            s.getScoreboard().getTeam("None").setPrefix(ChatColor.YELLOW.toString());
        }
        if (!s.getScoreboard().getTeam("None").hasEntry(name)) {
            s.getScoreboard().getTeam("None").addEntry(name);
        }

    }

    public static void addPlayer(Gamer gamer) {
        Clan clan = ClanUtilities.getClan(gamer.getUUID());

        if (clan != null) {
            for (Gamer g : GamerManager.getOnlineGamers()) {
              //  Gamer clan
            }
        }
    }


    public void addPlayer(String name) {

        Clan c = ClanUtilities.getClan(Bukkit.getPlayer(name));
        if (getScoreboard(Bukkit.getPlayer(name)) == null) {
            scoreboards.add(new Scoreboard(Bukkit.getPlayer(name)));
        }
        for (Scoreboard s : getScoreboards()) {

            if (c != null) {

                addClan(s, c.getName());

                for (Team team : s.getScoreboard().getTeams()) {
                    if (team.getName().equals(c.getName())) {
                        if (!team.hasEntry(name)) {
                            team.addEntry(name);
                        }

                    }
                }
            } else {
                if (s.getScoreboard().getTeam("None") == null) {
                    Team none = s.getScoreboard().registerNewTeam("None");
                    none.setPrefix(ChatColor.YELLOW.toString());
                }
                if (!s.getScoreboard().getTeam("None").hasEntry(name)) {
                    s.getScoreboard().getTeam("None").addEntry(name);
                }
            }
        }


        Scoreboard z = getScoreboard(Bukkit.getPlayer(name));


        for (Team t : z.getScoreboard().getTeams()) {
            t.unregister();
        }

        for (Clan d : ClanUtilities.clans) {
            if (d.isOnline()) {
                addClan(z, d.getName());
                Team team = z.getScoreboard().getTeam(d.getName());
                for (ClanMember member : d.getMembers()) {
                    if (Bukkit.getPlayer(member.getUUID()) != null) {
                        String names = Bukkit.getPlayer(member.getUUID()).getName();
                        if (!team.hasEntry(names)) {
                            team.addEntry(names);
                        }
                    }
                }
            }
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (ClanUtilities.getClan(p) == null) {
                addNone(z, p.getName());
            }
        }

    }

    public static void addPlayer(Player p, Clan c) {


        for (Scoreboard s : getScoreboards()) {

            if (c != null) {

               // addClan(s, c.getName());

                for (Team team : s.getScoreboard().getTeams()) {
                    if (team.getName().equals(c.getName())) {
                        if (!team.hasEntry(p.getName())) {
                            team.addEntry(p.getName());
                        }

                    }
                }
            } else {
                if (s.getScoreboard().getTeam("None") == null) {
                    Team none = s.getScoreboard().registerNewTeam("None");
                    none.setPrefix(ChatColor.YELLOW.toString());
                }
                if (!s.getScoreboard().getTeam("None").hasEntry(p.getName())) {
                    s.getScoreboard().getTeam("None").addEntry(p.getName());
                }
            }
        }


    }

    public static Scoreboard getScoreboard(Player p) {
        for (Scoreboard s : scoreboards) {
            if (s.getUUID().equals(p.getUniqueId())) {
                return s;
            }
        }
        return null;
    }

    public static Set<Scoreboard> getScoreboards() {
        return scoreboards;
    }

    public static void removeScoreboard(Scoreboard s) {
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

    public static void updateRelation(Clan a, Clan b) {
        for (ClanMember m : a.getMembers()) {
            Player p = Bukkit.getPlayer(m.getUUID());
            if (p != null) {
                Scoreboard s = getScoreboard(p);
                for (Team team : s.getScoreboard().getTeams()) {
                    if (team.getName().equals("None")) {
                        team.setPrefix(ChatColor.YELLOW.toString());
                        continue;
                    }
                 //   setPrefix(team, a, ClanUtilities.getClan(team.getName()));
                }
            }
        }

        for (ClanMember m : b.getMembers()) {
            Player p = Bukkit.getPlayer(m.getUUID());
            if (p != null) {
                Scoreboard s = getScoreboard(p);
                for (Team team : s.getScoreboard().getTeams()) {
                    if (team.getName().equals("None")) {
                        team.setPrefix(ChatColor.YELLOW.toString());
                        continue;
                    }
                   // setPrefix(team, b, ClanUtilities.getClan(team.getName()));
                }
            }
        }

    }


    public void removeClan(Scoreboard s, String name) {

            Iterator<Team> iterator = s.getScoreboard().getTeams().iterator();
            while (iterator.hasNext()) {
                Team team = iterator.next();
                if (team.getName().equals(name)) {
                    team.unregister();
                    for (ClanMember c : ClanUtilities.getClan(name).getMembers()) {
                        addNone(s, Bukkit.getPlayer(c.getUUID()).getName());

                    }
                }
            }


    }

    public void addClan(Scoreboard s, String name) {

        if (!isTeam(s, name)) {
            Team team = s.getScoreboard().registerNewTeam(name);

            Player p = Bukkit.getPlayer(s.getUUID());
            if (p != null) {
                setPrefix(team, ClanUtilities.getClan(p), ClanUtilities.getClan(name));
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

    public static void updatePlayer(String name) {

        Scoreboard z = getScoreboard(Bukkit.getPlayer(name));

        if (z != null) {
            if (!z.getScoreboard().getTeams().isEmpty()) {
                for (Team t : z.getScoreboard().getTeams()) {
                    t.unregister();
                }
            }

            for (Clan d : ClanUtilities.clans) {
                if (d.isOnline()) {
                  //  addClan(z, d.getName());
                    Team team = z.getScoreboard().getTeam(d.getName());
                    for (ClanMember member : d.getMembers()) {
                        if (Bukkit.getPlayer(member.getUUID()) != null) {
                            String names = Bukkit.getPlayer(member.getUUID()).getName();
                            if (!team.hasEntry(names)) {
                                team.addEntry(names);
                            }
                        }
                    }
                }
            }


            for (Player p : Bukkit.getOnlinePlayers()) {
                if (ClanUtilities.getClan(p) == null) {
                  //  addNone(z, p.getName());
                }
            }
        }
    }

    public static void removePlayer(String name) {
        Clan c = ClanUtilities.getClan(name);
        if (c != null) {
            for (Scoreboard s : getScoreboards()) {
                Iterator<Team> iterator = s.getScoreboard().getTeams().iterator();
                while (iterator.hasNext()) {
                    Team team = iterator.next();
                    if (team.getName().equals(c.getName())) {
                        if (team.hasEntry(name)) {
                            team.removeEntry(name);
                            if (!c.isOnline()) {
                                team.unregister();
                            }

                        }
                    }
                }

            }

        }
       // addNone(name);
    }


    private void setPrefix(Team team, Clan c, Clan d) {
        if (Clans.getOptions().isFNG()) {
            team.setPrefix(ChatColor.YELLOW + "");
            team.setSuffix("");
            return;
        }

        if (d != null) {

            String prefix = d.getName().length() > 11 ? d.getName().substring(0, 10) : d.getName();
            if (d.isAllied(c)) {
                team.setPrefix(d.hasTrust(c) ? ChatColor.DARK_GREEN + prefix + ChatColor.DARK_GREEN + " "
                        : ChatColor.DARK_GREEN + prefix + ChatColor.GREEN + " ");
                team.setSuffix("");
            } else if (d.isEnemy(c)) {
                team.setPrefix(ChatColor.DARK_RED + prefix + ChatColor.RED + " ");
                team.setSuffix(d.getSimpleDominanceString(c));
            } else if (d == c) {
                team.setPrefix(ChatColor.DARK_AQUA + prefix + ChatColor.AQUA + " ");
                team.setSuffix("");
            } else {
                team.setPrefix(ChatColor.GOLD + prefix + ChatColor.YELLOW + " ");
                team.setSuffix("");
            }
        } else {
            team.setPrefix(ChatColor.YELLOW + "");
            team.setSuffix("");
        }
    }
}