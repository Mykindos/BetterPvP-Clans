package net.betterpvp.clans.scoreboard;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanMember;
import net.betterpvp.clans.clans.ClanUtilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class ScoreboardManager {

    private static Set<Scoreboard> scoreboards = new HashSet<>();

    public ScoreboardManager() {
        scoreboards.clear();
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            addPlayer(p.getName());
        }
    }


    public static void addPlayer(String name) {

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

                addClan(s, c.getName());

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

    public static void addNone(String name) {
        for (Scoreboard s : getScoreboards()) {
            Team noTeam = s.getScoreboard().getTeam("None");
            if (noTeam == null) {
                s.getScoreboard().registerNewTeam("None");
                s.getScoreboard().getTeam("None").setPrefix(ChatColor.YELLOW.toString());
            }
            if (!s.getScoreboard().getTeam("None").hasEntry(name)) {
                s.getScoreboard().getTeam("None").addEntry(name);
            }
        }
    }

    public static void addNone(Scoreboard z, String name) {

        if (z.getScoreboard().getTeam("None") == null) {
            z.getScoreboard().registerNewTeam("None");
            z.getScoreboard().getTeam("None").setPrefix(ChatColor.YELLOW.toString());
        }
        if (!z.getScoreboard().getTeam("None").hasEntry(name)) {
            z.getScoreboard().getTeam("None").addEntry(name);
        }

    }

    public static void updateRelation() {
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
                    setPrefix(team, a, ClanUtilities.getClan(team.getName()));
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
                    setPrefix(team, b, ClanUtilities.getClan(team.getName()));
                }
            }
        }

    }


    public static void removeClan(String name) {
        for (Scoreboard s : getScoreboards()) {
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

    }

    public static void addClan(Scoreboard s, String name) {

        if (!isTeam(s, name)) {
            Team team = s.getScoreboard().registerNewTeam(name);

            Player p = Bukkit.getPlayer(s.getUUID());
            if (p != null) {
                setPrefix(team, ClanUtilities.getClan(p), ClanUtilities.getClan(name));
            }
        }

    }

    public static boolean isTeam(Scoreboard s, String name) {
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
        addNone(name);
    }

    public static void removePlayerOnQuit(String name) {
        Clan c = ClanUtilities.getClan(Bukkit.getPlayer(name));
        if (c != null) {
            if (getScoreboard(Bukkit.getPlayer(name)) != null) {
                removeScoreboard(getScoreboard(Bukkit.getPlayer(name)));

            }
            for (Scoreboard s : getScoreboards()) {
                Iterator<Team> iterator = s.getScoreboard().getTeams().iterator();
                while (iterator.hasNext()) {
                    Team team = iterator.next();
                    if (team.getName().equals(c.getName())) {
                        if (team.getEntries().contains(name)) {
                            team.removeEntry(name);
                            if (!c.isOnline()) {
                                iterator.remove();
                            }
                        }
                    }
                }
            }

        }


    }


    public static void setPrefix(Team team, Clan c, Clan d) {
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