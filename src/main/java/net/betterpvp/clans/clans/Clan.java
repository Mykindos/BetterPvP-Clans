package net.betterpvp.clans.clans;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanMember.Role;
import net.betterpvp.clans.clans.insurance.Insurance;
import net.betterpvp.clans.farming.bee.BeeData;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import net.betterpvp.core.utility.UtilTime.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Clan implements Invitable {

    private String name;
    private UUID leader;
    private long created;
    private Location home;
    //private Team team;
    private boolean vulnerable;
    private List<String> territory;
    private List<ClanMember> members;
    private List<Alliance> alliances;
    private List<Dominance> enemies;
    private HashMap<Object, Object> data;
    private long lastTnted;
    private long lastLogin;
    private List<Insurance> insurance;
    private int points;
    private double energy;
    private long cooldown;
    private HashMap<Clan, Integer> raidCount;
    private int level;
    private List<BeeData> beeData;
    private List<Location> chunkOutlines;
    private boolean instantTntProtection = false;

    public Clan(String name) {
        this.name = name;
        this.leader = null;
        this.home = null;
        this.vulnerable = true;
        this.members = new ArrayList<>();
        this.territory = new ArrayList<>();
        this.alliances = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.created = System.currentTimeMillis();
        this.raidCount = new HashMap<>();
        this.data = new HashMap<>();
        lastTnted = 0;
        lastLogin = System.currentTimeMillis();
        this.insurance = new ArrayList<>();
        this.energy = 2400;
        this.points = 0;
        this.cooldown = 0;
        this.level = 1;
        beeData = new ArrayList<>();
        chunkOutlines = new ArrayList<>();


    }

    public HashMap<Clan, Integer> getRaids() {
        return raidCount;
    }

    public long getRawCooldown() {
        return cooldown;
    }

    public void setCooldown(long l) {
        this.cooldown = l;
    }


    public long getCooldown() {
        return (cooldown + 14_400_000) - System.currentTimeMillis();
    }


    public boolean isOnCooldown() {
        return (cooldown + 14_400_000) - System.currentTimeMillis() >= 0;
    }

    public List<Insurance> getInsurance() {
        return insurance;
    }

    public void setPoints(int points) {
        this.points = Math.max(0, points);

    }

    public int getPoints() {
        return points;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getLeader() {

        for (ClanMember m : getMembers()) {
            if (m.getRole() == Role.LEADER) {
                return m.getUUID();
            }
        }
        return leader;
    }

    public void setLastTnted(long l) {
        this.lastTnted = l;
    }

    public long getLastTnted() {
        return lastTnted;
    }

    public void setLastLogin(long l) {
        this.lastLogin = l;
    }

    public long getLastLogin() {
        return this.lastLogin;
    }

    public void setLeader(UUID leader) {
        this.leader = leader;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public String getAge() {
        return UtilTime.getTime(System.currentTimeMillis() - getCreated(), TimeUnit.BEST, 1);
    }

    public Location getHome() {
        return home;
    }

    public void setHome(Location home) {
        this.home = home;
    }

    //public Team getTeam(){
    //	return team;
    //}


    public boolean isVulnerable() {
        return Clans.getOptions().isLastDay() || vulnerable;
    }

    public void setVulnerable(boolean vulnerable) {
        this.vulnerable = vulnerable;
    }

    public String getVulnerableString() {

        if (Clans.getOptions().isLastDay()) {
            return ChatColor.GOLD + "LAST DAY OF MAP - NO PROTECTION";
        }
        for (ClanMember member : getMembers()) {
            if (Bukkit.getPlayer(member.getUUID()) != null) {
                return ChatColor.GOLD + "No, clan members are online";
            }
        }

        if (getData().get(DataType.PROTECTION) != null) {
            Long time = (Long) getData().get(DataType.PROTECTION);
            return ChatColor.GOLD + "No, " + UtilTime.getTime(time - System.currentTimeMillis(), TimeUnit.BEST, 1) + " until protection";
        } else {
            return ChatColor.GREEN + "Yes, TNT protected";
        }
    }

    public List<String> getTerritory() {
        return territory;
    }

    public void setTerritory(List<String> territory) {
        this.territory = territory;
    }

    public List<ClanMember> getMembers() {
        return members;
    }

    public void setMembers(List<ClanMember> members) {
        this.members = members;
    }

    public ClanMember getMember(UUID uuid) {
        for (ClanMember member : getMembers()) {
            if (member.getUUID().equals(uuid)) {
                return member;
            }
        }
        return null;
    }

    public List<Alliance> getAlliances() {
        return alliances;
    }

    public void setAlliances(List<Alliance> alliances) {
        this.alliances = alliances;
    }

    public boolean isAllied(Clan clan) {
        if (getAlliances() != null) {
            for (Alliance alliance : getAlliances()) {
                if (alliance.getClan() == clan) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasTrust(Clan clan) {
        if (isAllied(clan)) {
            for (Alliance alliance : getAlliances()) {
                if (alliance.getClan().equals(clan)) {
                    return alliance.hasTrust();
                }
            }
        }
        return false;
    }

    public Alliance getAlliance(Clan clan) {
        if (isAllied(clan)) {
            for (Alliance alliance : getAlliances()) {
                if (alliance.getClan().equals(clan)) {
                    return alliance;
                }
            }
        }
        return null;
    }

    public List<Dominance> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Dominance> enemies) {
        this.enemies = enemies;
    }

    public boolean isEnemy(Clan clan) {
        for (Dominance dom : getEnemies()) {
            if (dom != null && clan != null) {
                if (dom.getClan().equals(clan)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Dominance getDominance(Clan clan) {
        for (Dominance dom : getEnemies()) {
            if (dom.getClan() != null) {
                if (dom.getClan()
                        .equals(clan)) {
                    return dom;
                }
            }
        }
        return null;
    }

    public String getDominanceString(Clan clan) {
        if (clan != null) {
            if (clan.getDominance(this) != null) {
                return ChatColor.GRAY + "(" + ChatColor.GREEN + clan.getDominance(this).getPoints() + ChatColor.GRAY + ":"
                        + ChatColor.RED + getDominance(clan).getPoints() + ChatColor.GRAY + ")" + ChatColor.GRAY;
            }
        }
        return "";
    }

    public String getSimpleDominanceString(Clan clan) {
        if (clan != null) {
            if (clan.getDominance(this) != null) {
                if (clan.getDominance(this).getPoints() == 0 && getDominance(clan).getPoints() == 0) {
                    return ChatColor.WHITE + " 0";
                }
                if (clan.getDominance(this).getPoints() > 0) {
                    return ChatColor.DARK_RED.toString() + " -" + (clan.getDominance(this).getPoints());
                } else {

                    return ChatColor.GREEN.toString() + " +" + (getDominance(clan).getPoints());
                }

            }
        }
        return "";
    }

    public HashMap<Object, Object> getData() {
        return data;
    }

    public void setData(HashMap<Object, Object> data) {
        this.data = data;
    }

    /**
     * Send message to all online clan members
     *
     * @param message The message to send
     * @param ignore  Ignore a specific clan member (perhaps the creator)
     * @param prefix  Whether to add a prefix or not. 'Clans>'
     */
    public void messageClan(String message, UUID ignore, boolean prefix) {
        for (ClanMember member : getMembers()) {
            if (ignore == null || !ignore.equals(member.getUUID())) {
                Player player = Bukkit.getPlayer(member.getUUID());
                if (player != null) {
                    if (prefix) {
                        UtilMessage.message(player, "Clans", message);
                    } else {
                        UtilMessage.message(player, message);
                    }
                }
            }
        }
    }

    public void playSound(Sound sound, float volume, float pitch) {
        for (ClanMember member : getMembers()) {
            Player p = Bukkit.getPlayer(member.getUUID());
            if (p != null) {
                p.playSound(p.getLocation(), sound, volume, pitch);
            }

        }
    }

    public boolean isOnline() {
        for (ClanMember m : getMembers()) {
            if (Bukkit.getPlayer(m.getUUID()) != null) {
                return true;
            }
        }
        return false;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public List<BeeData> getBeeData() {
        return beeData;
    }

    public List<Location> getChunkOutlines() {
        return chunkOutlines;
    }

    public void setChunkOutlines(List<Location> list){
        this.chunkOutlines = list;
    }

    public boolean isInstantTntProtection() {
        return instantTntProtection;
    }

    public void setInstantTntProtection(boolean instantTntProtection) {
        this.instantTntProtection = instantTntProtection;
    }

    public enum DataType {

        PROTECTION
    }
}
