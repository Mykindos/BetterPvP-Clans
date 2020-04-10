package net.betterpvp.clans.combat.safelog;

import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SafeLog {

    public static List<SafeLog> loggers = new ArrayList<>();

    private Player player;
    private Location location;
    private Long time;
    private int seconds;

    public SafeLog(Player player) {
            this.player = player;
            this.location = player.getLocation();
            this.time = System.currentTimeMillis();
            this.seconds = 10;
            loggers.add(this);

    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void cancel() {
        if (isLoggingOut(getPlayer())) {
            loggers.remove(getSafeLog(getPlayer()));
        }
    }

    public boolean checkItems(Player caller) {
        return true;
    }

    public void updateLog() {
        SafeLog log = getSafeLog(getPlayer());

        int remaining = log.getSeconds();
        Long lastUpdate = log.getTime();

        if (UtilTime.elapsed(lastUpdate, 1000L)) {
            UtilMessage.message(getPlayer(), "Log", "Logging out in " + ChatColor.GREEN + remaining + " Seconds" + ChatColor.GRAY + ".");
            log.setSeconds(remaining - 1);
            log.setTime(System.currentTimeMillis());
        }
    }

    public static SafeLog getSafeLog(Player player) {
        for (SafeLog log : loggers) {
            if (player.getUniqueId().equals(log.getPlayer().getUniqueId())) {
                return log;
            }
        }
        return null;
    }

    public static boolean isLoggingOut(Player player) {
        if (getSafeLog(player) != null) {
            return true;
        }
        return false;
    }
}