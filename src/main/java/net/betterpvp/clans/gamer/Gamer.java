package net.betterpvp.clans.gamer;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Invitable;

import net.betterpvp.clans.clans.events.ScoreboardUpdateEvent;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.dailies.perks.QuestPerk;

import net.betterpvp.clans.gamer.mysql.GamerRepository;
import net.betterpvp.clans.gamer.mysql.PlayerStatRepository;
import net.betterpvp.clans.scoreboard.Scoreboard;
import net.betterpvp.clans.skills.selector.RoleBuild;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.donation.Donation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Gamer implements Invitable {

    private UUID uuid;
    private Client client;
    private int coins, battlecoins;
    private double fragments;
    private int kills;
    private int deaths;
    private int votes;
    private List<Donation> donations;
    private List<QuestPerk> questPerks;
    private HashMap<String, RoleBuild> activeBuilds;
    private List<RoleBuild> builds;
    private boolean loaded;
    private long lastDamaged = 0;
    private boolean safeLogged;
    private Location home;
    private boolean filter;
    private Scoreboard scoreboard;
    private HashMap<String, Integer> ratings;
    private HashMap<String, Integer> playerStats;

    //private HashMap<Enum, Object> data;

    public Gamer(UUID uuid) {
        this.uuid = uuid;

        this.coins = Clans.getOptions().getStartingBalance();
        this.battlecoins = 0;
        this.kills = 0;
        this.deaths = 0;
        this.votes = 0;
        this.fragments = 0;
        activeBuilds = new HashMap<>();
        donations = new ArrayList<>();
        questPerks = new ArrayList<>();
        builds = new ArrayList<>();
        filter = false;
        ratings = new HashMap<>();
        playerStats = new HashMap<>();

        for (Role role : Role.roles) {
            ratings.put(role.getName(), 1500);
        }


        //this.data = new HashMap<>();
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client c) {
        this.client = c;
    }

    public int getMaxCoins() {

        int max = 2000000;


      /*  if (getClient() != null) {
            if (getClient().hasDonationRank(DonationRank.GUARDIAN)) {
                max = 50000000;
            } else if (getClient().hasDonationRank(DonationRank.DIVINE)) {
                max = 40000000;
            } else if (getClient().hasDonationRank(DonationRank.SAVIOR)) {
                max = 30000000;
            } else if (getClient().hasDonationRank(DonationRank.LEGACY)) {
                max = 20000000;
            }
        }*/

        return max;
    }


    public boolean isFiltering() {
        return filter;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }

    public Location getHome() {
        return home;
    }

    public void setHome(Location loc) {
        this.home = loc;
    }


    public void setLastDamaged(long l) {
        this.lastDamaged = l;
    }

    public long getLastDamaged() {
        return this.lastDamaged;
    }


    public void setSafeLogged(boolean b) {
        this.safeLogged = b;
    }

    public boolean safeLogged() {
        return safeLogged;
    }

    public double getFragments() {
        return fragments;
    }


    public boolean hasFragments(double amount) {
        return fragments >= amount;
    }

    public void setFragments(double amount) {
        fragments = amount;
    }

    public void addFragments(double amount) {
        fragments += amount;
    }

    public boolean hasBattleCoins(int amount) {
        return battlecoins >= amount;
    }

    public int getBattleCoins() {
        return battlecoins;
    }

    public void setBattleCoins(int amount) {
        battlecoins = amount;
    }

    public void addBattleCoins(int amount) {
        battlecoins += amount;

    }

    public void addFragment() {
        fragments++;
        GamerRepository.updateFragments(this);
    }

    public void takeFragments(int amount) {
        fragments -= amount;
        GamerRepository.updateFragments(this);
    }


    public void setLoaded() {
        this.loaded = true;
    }

    public boolean getLoaded() {
        return loaded;
    }


    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getVotes() {
        return this.votes;
    }

    public void addVote() {
        this.votes++;
        GamerRepository.updateVotes(this);
    }


    public List<QuestPerk> getQuestPerks() {
        return questPerks;
    }


    public List<RoleBuild> getBuilds() {
        return builds;
    }

    public RoleBuild getBuild(String role, int ID) {
        for (RoleBuild r : builds) {
            if (r.getRole().equalsIgnoreCase(role) && r.getID() == ID) {
                return r;
            }
        }
        return null;
    }

    public RoleBuild getActiveBuild(String role) {
        for (RoleBuild rb : builds) {
            if (rb.getRole().equalsIgnoreCase(role)) {
                if (rb.isActive()) {
                    return rb;
                }
            }
        }
        return null;
    }

    public void setActiveBuild(RoleBuild role) {
        activeBuilds.put(role.getRole(), role);
    }


    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = Math.min(Integer.MAX_VALUE, coins);

        Player player = Bukkit.getPlayer(getUUID());
        if (player != null) {
            Bukkit.getPluginManager().callEvent(new ScoreboardUpdateEvent(player));
        }
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void addKill() {
        this.kills++;
    }

    public int getDeaths() {
        return deaths;
    }

    public void addDeath() {
        this.deaths++;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void addCoins(double coins) {
        this.coins = Math.min(Integer.MAX_VALUE, this.coins + (int) coins);
        Player player = Bukkit.getPlayer(getUUID());
        if (player != null) {
            Bukkit.getPluginManager().callEvent(new ScoreboardUpdateEvent(player));
        }
    }

    public void removeCoins(double coins) {
        this.coins = Math.max(0, this.coins - (int) coins);

        Player player = Bukkit.getPlayer(getUUID());
        if (player != null) {
            Bukkit.getPluginManager().callEvent(new ScoreboardUpdateEvent(player));
        }


    }

    public boolean hasCoins(int coins) {
        return getCoins() >= coins;
    }

    public List<Donation> getDonations() {
        return donations;
    }

    public void setDonations(List<Donation> donations) {
        this.donations = donations;
    }

    /*
    public Donation getDonation(Perk perk) {
        for (Donation donation : getDonations()) {
            if (donation.getPerk().getName().equals(perk.getName())) {
                return donation;
            }
        }
        return null;
    }

    public List<Perk> getPerks() {
        List<Perk> perks = new ArrayList<>();
        for (Donation donation : getDonations()) {
            perks.add(donation.getPerk());
        }

        return perks;
    }

    public boolean hasPerk(Perk perk) {
        for (Donation donation : getDonations()) {
            if (donation.getPerk().getName().equals(perk.getName())) {
                return true;
            }
        }
        return false;
    }

     */

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }


    public int getRating(Role role) {
        return ratings.get(role.getName());
    }

    public HashMap<String, Integer> getRatings() {
        return ratings;
    }

    public HashMap<String, Integer> getPlayerStats() {
        return playerStats;
    }

    public void setPlayerStat(HashMap<String, Integer> playerStat) {
        this.playerStats = playerStat;
    }

    public int getStatValue(String key) {
        if (playerStats.containsKey(key)) {
            return playerStats.get(key);
        }

        playerStats.put(key, 0);
        PlayerStatRepository.saveStat(getUUID(), key, 0);
        return 0;
    }

    public void setStatValue(String key, int value) {
        playerStats.put(key, value);
        //PlayerStatRepository.updateStat(getUUID(), key, value);
    }

    public void updateAllStats() {

    }



	/*
    public HashMap<Enum, Object> getData() {
        return data;
    }

    public void setData(HashMap<Enum, Object> data) {
        this.data = data;
    }
	 */
}
