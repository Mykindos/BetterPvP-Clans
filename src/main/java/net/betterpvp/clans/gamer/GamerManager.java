package net.betterpvp.clans.gamer;

import net.betterpvp.core.client.Client;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class GamerManager {

    private static List<Gamer> gamers = new ArrayList<>();
    private static List<Gamer> onlineGamers = new ArrayList<>();

    public static List<Gamer> getGamers() {
        return gamers;
    }

    public static List<Gamer> getOnlineGamers() {
        return onlineGamers;
    }

    public static void addOnlineGamer(Gamer c) {
        onlineGamers.add(c);


    }

    public static Gamer getOnlineGamer(UUID uuid) {
        for (Gamer gamer : onlineGamers) {

            if (gamer.getUUID().equals(uuid)) {
                return gamer;
            }
        }
        return null;
    }

    public static Gamer getOnlineGamer(Player p) {
        return getOnlineGamer(p.getUniqueId());
    }

    public static void addGamer(Gamer c) {
        gamers.removeIf(x -> x.getUUID().toString().equalsIgnoreCase(c.getUUID().toString()));
        onlineGamers.removeIf(x -> x.getUUID().toString().equalsIgnoreCase(c.getUUID().toString()));
        gamers.add(c);

        if (Bukkit.getPlayer(c.getUUID()) != null) {
            onlineGamers.add(c);
        }
    }

    public static void addGamerOnLoad(Gamer c) {

        gamers.add(c);


    }

    public static Gamer getGamer(UUID uuid) {
        for (Gamer gamer : gamers) {
            if (gamer.getUUID().equals(uuid)) {
                return gamer;
            }
        }
        return null;
    }

    public static Gamer getGamer(String string) {
        for (Gamer gamer : gamers) {
            if (gamer.getClient().getName().equalsIgnoreCase(string)
                    || gamer.getClient().getUUID().toString().equalsIgnoreCase(string)) {
                return gamer;
            }
        }
        return null;
    }

    public static Gamer getOnlineGamer(String string) {
        for (Gamer gamer : onlineGamers) {
            if (gamer.getClient().getName().equalsIgnoreCase(string)
                    || gamer.getClient().getUUID().toString().equalsIgnoreCase(string)) {
                return gamer;
            }
        }
        return null;
    }

    public static Gamer getGamer(Player player) {
        Gamer uuid = getGamer(player.getUniqueId());
        if (uuid != null) {
            return uuid;
        }

        Gamer name = getGamer(player.getName());
        if (name != null) {
            return name;
        }

        return null;
    }

    public static boolean isGamer(String string) {
        if (getGamer(string) != null) {
            return true;
        }

        if (Bukkit.getPlayer(string) != null) {
            return getGamer(Bukkit.getPlayer(string)) != null;
        }

        return false;
    }


    public static boolean isGamer(UUID uuid) {
        return getGamer(uuid) != null;


    }

    public static Gamer searchGamer(Player caller, String client, boolean inform) {
        LinkedList<Gamer> matchList = new LinkedList<>();
        for (Gamer cur : gamers) {
            if (cur.getClient().getName().equalsIgnoreCase(client)) {
                return cur;
            }
            if (cur.getClient().getName().toLowerCase().contains(client.toLowerCase())) {
                matchList.add(cur);
            }
        }
        if (matchList.size() != 1) {
            if (!inform) {
                return null;
            }

            UtilMessage.message(caller, "Client Search", ChatColor.YELLOW.toString() + matchList.size() + ChatColor.GRAY
                    + " matches for [" + ChatColor.YELLOW + client + ChatColor.GRAY + "].");

            if (matchList.size() > 0) {
                String matchString = "";
                for (Gamer cur : matchList) {
                    matchString = matchString + ChatColor.YELLOW + cur.getClient().getName() + ChatColor.GRAY + ", ";
                }
                if (matchString.length() > 1) {
                    matchString = matchString.substring(0, matchString.length() - 2);
                }
                UtilMessage.message(caller, "Client Search", ChatColor.GRAY + "Matches [" + ChatColor.YELLOW + matchString + ChatColor.GRAY + "].");
            }
            return null;
        }
        return matchList.get(0);
    }
}
