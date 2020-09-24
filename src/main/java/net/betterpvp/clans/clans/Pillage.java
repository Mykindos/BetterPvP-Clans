package net.betterpvp.clans.clans;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.events.ClanRelationshipEvent;
import net.betterpvp.clans.clans.mysql.ClanRepository;
import net.betterpvp.clans.clans.mysql.EnemyRepository;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.client.Rank;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

import java.util.*;

public class Pillage {

    public static Set<Pillage> pillages = new HashSet<Pillage>();
    public static WeakHashMap<String, List<LastPillage>> data = new WeakHashMap<>();

    private Clan pillager;
    private Clan pillaged;
    private Long pillageFinish;
    private Long lastUpdate;
    private Long start;
    private int remaining;

    public Pillage(Clan pillager, Clan pillaged) {
        this.pillager = pillager;
        this.pillaged = pillaged;
        this.pillageFinish = System.currentTimeMillis() + 600000L;
        this.lastUpdate = System.currentTimeMillis();
        this.start = System.currentTimeMillis();
        this.remaining = Clans.getOptions().getPillageLength();

        pillaged.setCooldown(System.currentTimeMillis());
        ClanRepository.updateCooldown(pillaged);

        Dominance dom = pillager.getDominance(pillaged);
        Dominance tarDom = pillaged.getDominance(pillager); // Added 16/06/2017

        EnemyRepository.deleteEnemy(pillager.getDominance(pillaged));
        EnemyRepository.deleteEnemy(pillaged.getDominance(pillager));

        pillager.getEnemies().remove(dom);
        pillaged.getEnemies().remove(tarDom);


        int bonus = 0;


        if (pillaged.getPoints() > 0) {
            ClanUtilities.sort();
            if (ClanUtilities.clans.size() >= 10) {
                for (int i = 0; i < 10; i++) {
                    Clan c = ClanUtilities.clans.get(i);
                    if (c.getName().equalsIgnoreCase(pillaged.getName())) {
                        if (pillager.getPoints() < pillaged.getPoints()) {
                            bonus += (10 - i);
                        }
                    }
                }
            }
        }

        if (!data.containsKey(pillager.getName())) {
            data.put(pillager.getName(), new ArrayList<LastPillage>());
        }

        LastPillage lp = getLastPillage(pillager, pillaged);
        if (lp == null || (lp != null && (lp.getLastPillaged() + (3600000 * 12)) - System.currentTimeMillis() <= 0)) {

            if (lp != null) {
                data.get(pillager.getName()).remove(lp);
            }


            ClanRepository.updateRaidHistory(pillager, pillaged);

           int raids = pillager.getRaids().get(pillaged);
            int pointsGained = (pillaged.getMembers().size() + bonus);
            int pointsLost = (pillaged.getMembers().size() + bonus);

            if (raids > 1) {
                pointsGained = (int) (pointsGained * Math.pow(0.5, raids - 1));
                pointsLost = (int) (pointsLost * Math.pow(0.5, raids - 1));

                pillager.messageClan(ChatColor.GREEN + "Since you have already raided this clan, you received less points. (Effect stacks)", null, false);
            }

            Bukkit.getPluginManager().callEvent(new ClanRelationshipEvent(pillaged, pillager));
            pillaged.setPoints(pillaged.getPoints() - pointsLost);
            pillager.setPoints(pillager.getPoints() + pointsGained);
            ClanRepository.updatePoints(pillaged);
            ClanRepository.updatePoints(pillager);
            pillaged.messageClan(ChatColor.RED + "Your clan lost " + ChatColor.YELLOW
                    + pointsLost + ChatColor.RED + " points", null, false);
            pillager.messageClan(ChatColor.GREEN + "Your clan gained " + ChatColor.YELLOW
                    + (pointsGained) + ChatColor.GREEN + " points", null, false);
            data.get(pillager.getName()).add(new LastPillage(pillaged));
            if (bonus > 0) {
                pillager.messageClan(ChatColor.GRAY + "You received " + ChatColor.GREEN + bonus + ChatColor.GRAY
                                + " bonus points for raiding this clan.",
                        null, false);
                pillaged.messageClan(ChatColor.GRAY + "You lost an additional " + ChatColor.RED + bonus + ChatColor.GRAY + " points for being a top clan."
                        , null, false);
            }
        } else {
            pillager.messageClan(ChatColor.GRAY + "You received no points as you pillaged this clan under 12 hours ago.", null, false);
            pillaged.messageClan(ChatColor.GRAY + "You lost no points as you were pillaged under 12 hours ago by this clan.", null, false);
        }
        ClientUtilities.messageStaffSound("Raid", pillaged.getName() + " has been conquered. Please spectate in /gamemode 3", Sound.BLOCK_NOTE_BLOCK_PLING, Rank.ADMIN);

        pillages.add(this);
    }

    public Clan getPillager() {
        return pillager;
    }

    public void setPillager(Clan pillager) {
        this.pillager = pillager;
    }

    public Clan getPillaged() {
        return pillaged;
    }

    public void setPillaged(Clan pillaged) {
        this.pillaged = pillaged;
    }

    public Long getPillageFinish() {
        return pillageFinish;
    }

    public void setPillageFinish(Long pillageFinish) {
        this.pillageFinish = pillageFinish;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public static boolean isPillaging(Clan clan, Clan target) {
        for (Pillage pillage : pillages) {
            if (pillage.getPillager().equals(clan) && pillage.getPillaged().equals(target)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBeingPillaged(Clan clan){
        for (Pillage pillage : pillages) {
            if (pillage.getPillaged().equals(clan)) {
                return true;
            }
        }

        return false;
    }

    public class LastPillage {

        public String pillaged;
        public long lastPillage;

        public LastPillage(Clan pillaged) {
            this.pillaged = pillaged.getName();
            this.lastPillage = System.currentTimeMillis();
        }

        public String getPillaged() {
            return pillaged;
        }

        public long getLastPillaged() {
            return lastPillage;
        }
    }

    public static LastPillage getLastPillage(Clan pillager, Clan pillaged) {
        if (data.containsKey(pillager.getName())) {
            for (LastPillage pillage : data.get(pillager.getName())) {
                if (pillage.getPillaged().equalsIgnoreCase(pillaged.getName())) {
                    return pillage;
                }
            }

        }
        return null;
    }
}
