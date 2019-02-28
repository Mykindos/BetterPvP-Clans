package net.betterpvp.clans.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;


public class Scoreboard {


    private Player player;
    private org.bukkit.scoreboard.Scoreboard sb;

    public Scoreboard(Player player) {
        this.player = player;
        this.sb = Bukkit.getScoreboardManager().getNewScoreboard();
        player.setScoreboard(this.sb);

    }


    public UUID getUUID() {
        return player.getUniqueId();
    }

    public org.bukkit.scoreboard.Scoreboard getScoreboard() {
        return sb;
    }


}
