package net.betterpvp.clans.clans;

import net.betterpvp.clans.clans.mysql.EnemyRepository;
import net.betterpvp.core.database.Log;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;

public class Dominance {

    private Clan self;
    private Clan enemy;
    private int points = 0;
    private long time;

    public Dominance(Clan self, Clan enemy, int points) {
        this.self = self;
        this.enemy = enemy;
        this.points = points;
        this.time = System.currentTimeMillis();
    }

    public Clan getSelf() {
        return self;
    }

    public void setSelf(Clan self) {
        this.self = self;
    }

    public Clan getClan() {
        return enemy;
    }

    public void setClan(Clan enemy) {
        this.enemy = enemy;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void addPoint() {
        if (getClan().getDominance(getSelf()).getPoints() < getSelf().getDominance(getClan()).getPoints()) {
            getSelf().getDominance(getClan()).setPoints(getSelf().getDominance(getClan()).getPoints() - 1);
            getSelf().messageClan("You recovered Dominance against " + ChatColor.RED + getClan().getName()
                    + " " + getSelf().getDominanceString(getClan()) + ChatColor.GRAY + ".", null, true);

            getClan().messageClan("You lost Dominance against " + ChatColor.RED + getSelf().getName()
                    + " " + getClan().getDominanceString(getSelf()) + ChatColor.GRAY + ".", null, true);

            EnemyRepository.updateDominance(getSelf().getDominance(getClan()));
        } else if (getClan().getDominance(getSelf()).getPoints() >= getSelf().getDominance(getClan()).getPoints()) {
            getClan().getDominance(getSelf()).setPoints(getClan().getDominance(getSelf()).getPoints() + 1);
            getSelf().messageClan("You gained Dominance against " + ChatColor.RED + getClan().getName() + " "
                    + getSelf().getDominanceString(getClan()) + ChatColor.GRAY + ".", null, true);

            getClan().messageClan("You lost Dominance against " + ChatColor.RED + getSelf().getName() + " "
                    + getClan().getDominanceString(getSelf()) + ChatColor.GRAY + ".", null, true);

            EnemyRepository.updateDominance(getClan().getDominance(getSelf()));
        }

        if (getClan().getDominance(getSelf()).getPoints() >= 16) {
            UtilMessage.broadcast("Clans", ChatColor.YELLOW + "Clan " + getSelf().getName() + ChatColor.GRAY + " has conquered "
                    + ChatColor.YELLOW + getClan().getName() + ChatColor.GRAY + ".");
            Log.write("Conquer", getSelf().getName() + " has conquered " + getClan().getName());
            new Pillage(getSelf(), getClan());
        }
    }
}
