package net.betterpvp.clans.settings;

import net.betterpvp.clans.Clans;
import net.betterpvp.core.configs.Configs;

public class Options {

    private int maxClanMembers, maxClanAllies, smallClanMaxAllies;
    private int pillageLength;
    private int farmMaxY;
    private int farmMinY;
    private boolean mahCheck, morphsEnabled;
    private long resetTime; // Daily reset time
    private int minPlayersForWorldEvent;
    private String world;
    private boolean fng;
    private int timeUntilTNTProtection;
    private int bonusTimeUntilTNTProtection;
    private int tntBonusMemberThreshold;
    private int maxClaims;
    private int autoClickThreshold;
    private boolean logUpdateErrors = true;
    private boolean lastDay = false;
    private double reachDistance = 5.0;
    private int bank = 0;
    private boolean clearItems;
    private long openTime;
    private String motd;
    private boolean warpingEnabled;
    private int queueCount;
    private int startingBalance;
    private int costPerEnergy;
    public long regenTime;
    private boolean tntEnabled;
    private boolean starterKits;
    private boolean assassinKnockback, assassinDealKb;
    private boolean roulette;
    private boolean starterKitsCooldown;
    private boolean canClanHomeFromWilderness;
    private boolean isEnemySystemEnabled;
    private int onlineReward;
    private int MAHPort;
    private boolean bungee;
    private boolean isOpen;
    private boolean mapEnabled;
    private String texturePackURL;
    private String texturePackSHA;
    private boolean texturePackForce;
    private boolean hub;
    private String tablePrefix;
    private int hubPort;
    private int maxClanlevel;
    private int maxBees;
    private boolean protectNewClans;
    private boolean votingCratesEnabled;
    private Clans i;


    public Options(Clans i) {
        this.i = i;

        checkDefaults();
        reloadOptions();


    }


    public void reloadOptions() {

        world = i.getConfigManager().get(Configs.MAIN).getString("World");
        hub = i.getConfigManager().get(Configs.MAIN).getBool("Bungee.IsHub");
        maxClanMembers = i.getConfigManager().get(Configs.MAIN).getInt("Clans.MaxMembers");
        maxClanAllies = i.getConfigManager().get(Configs.MAIN).getInt("Clans.MaxAllies");
        smallClanMaxAllies = i.getConfigManager().get(Configs.MAIN).getInt("Clans.Small.MaxAllies");
        costPerEnergy = i.getConfigManager().get(Configs.MAIN).getInt("Clans.CostPerEnergy");
        isEnemySystemEnabled = i.getConfigManager().get(Configs.MAIN).getBool("Clans.EnemySystemEnabled");
        timeUntilTNTProtection = i.getConfigManager().get(Configs.MAIN).getInt("Clans.TNT.TimeUntilProtection");
        bonusTimeUntilTNTProtection = i.getConfigManager().get(Configs.MAIN).getInt("Clans.TNT.BonusTime");
        tntBonusMemberThreshold = i.getConfigManager().get(Configs.MAIN).getInt("Clans.TNT.MemberThreshold");
        pillageLength = i.getConfigManager().get(Configs.MAIN).getInt("PillageLength");
        farmMaxY = i.getConfigManager().get(Configs.MAIN).getInt("Farming.MaxY");
        farmMinY = i.getConfigManager().get(Configs.MAIN).getInt("Farming.MinY");
        maxBees = i.getConfigManager().get(Configs.MAIN).getInt("Farming.MaxBees");
        mahCheck = i.getConfigManager().get(Configs.MAIN).getBool("MAH.Check");
        morphsEnabled = i.getConfigManager().get(Configs.MAIN).getBool("Morphs.Enabled");
        resetTime = i.getConfigManager().get(Configs.MAIN).getLong("Daily-Reset-Time");
        minPlayersForWorldEvent = i.getConfigManager().get(Configs.MAIN).getInt("MinPlayersForWorldEvent");
        fng = i.getConfigManager().get(Configs.MAIN).getBoolean("FNG");
        autoClickThreshold = i.getConfigManager().get(Configs.MAIN).getInt("AutoClickThreshold");
        logUpdateErrors = i.getConfigManager().get(Configs.MAIN).getBoolean("LogUpdateErrors");
        lastDay = i.getConfigManager().get(Configs.MAIN).getBool("Last-Day");
        reachDistance = i.getConfigManager().get(Configs.MAIN).getDouble("Reach-Distance");
        clearItems = i.getConfigManager().get(Configs.MAIN).getBool("Lag.ClearItems");
        bank = i.getConfigManager().get(Configs.MAIN).getInt("Server-Bank");
        openTime = i.getConfigManager().get(Configs.MAIN).getLong("OpenTime");
        regenTime = i.getConfigManager().get(Configs.MAIN).getLong("Anti-Cheat.RegenTime");
        queueCount = i.getConfigManager().get(Configs.MAIN).getInt("Anti-Cheat.AutoQueue");
        motd = i.getConfigManager().get(Configs.MAIN).getString("MOTD");
        warpingEnabled = i.getConfigManager().get(Configs.MAIN).getBool("Warps-Enabled");
        startingBalance = i.getConfigManager().get(Configs.MAIN).getInt("New-Account.Starting-Balance");
        tntEnabled = i.getConfigManager().get(Configs.MAIN).getBool("TNT.Enabled");
        starterKits = i.getConfigManager().get(Configs.MAIN).getBool("New-Account.Starter-Kits");
        assassinKnockback = i.getConfigManager().get(Configs.MAIN).getBool("Assassin.Knockback-Enabled");
        assassinDealKb = i.getConfigManager().get(Configs.MAIN).getBool("Assassin.Deal-Knockback");
        roulette = i.getConfigManager().get(Configs.MAIN).getBool("Gambling.Roulette");
        starterKitsCooldown = i.getConfigManager().get(Configs.MAIN).getBool("New-Account.Starter-Kit-Cooldown");
        canClanHomeFromWilderness = i.getConfigManager().get(Configs.MAIN).getBool("Clans.CanClanHomeFromWilderness");
        onlineReward = i.getConfigManager().get(Configs.MAIN).getInt("Settings.OnlineReward");
        MAHPort = i.getConfigManager().get(Configs.MAIN).getInt("MAH.Port");
        bungee = i.getConfigManager().get(Configs.MAIN).getBool("Bungee");
        isOpen = i.getConfigManager().get(Configs.MAIN).getBool("Server.IsOpen");
        mapEnabled = i.getConfigManager().get(Configs.MAIN).getBool("Map.Enabled");
        texturePackURL = i.getConfigManager().get(Configs.MAIN).getString("TexturePack.URL");
        texturePackSHA = i.getConfigManager().get(Configs.MAIN).getString("TexturePack.SHA");
        texturePackForce = i.getConfigManager().get(Configs.MAIN).getBool("TexturePack.Forced");
        tablePrefix = i.getConfigManager().get(Configs.MAIN).getString("Database.Prefix");
        hubPort = i.getConfigManager().get(Configs.MAIN).getInt("Bungee.HubPort");
        maxClanlevel = i.getConfigManager().get(Configs.MAIN).getInt("Clans.MaxLevel");
        protectNewClans = i.getConfigManager().get(Configs.MAIN).getBool("Clans.Protect-New-Clans");
        votingCratesEnabled = i.getConfigManager().get(Configs.MAIN).getBool("Voting.CratesEnabled");
    }

    public int getMAHPort() {
        return MAHPort;
    }

    public boolean isBungee() {
        return bungee;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setDailyResetTime() {
        resetTime = System.currentTimeMillis();
    }

    public long getOpenTime() {
        return openTime;
    }

    public int getAutoQueueAmount() {
        return queueCount;
    }

    public int getMaxClanMembers() {
        return maxClanMembers;
    }

    public int getMaxClanAllies() {
        return maxClanAllies;
    }

    public boolean isEnemySystemEnabled() {
        return isEnemySystemEnabled;
    }

    public int getMaxAlliesSmallClan() {
        return smallClanMaxAllies;
    }

    public int getOnlineReward() {
        return onlineReward;
    }

    public String getWorld() {
        return world;
    }

    public int getPillageLength() {
        return pillageLength;
    }

    public boolean isStarterKitACooldown() {
        return starterKitsCooldown;
    }

    public String getMOTD() {
        return motd;
    }

    public boolean isRouletteEnabled() {
        return roulette;
    }

    public void setBank(int bal) {
        this.bank = bal;

    }

    public int getBank() {
        return bank;
    }

    public boolean isFNG() {
        return fng;
    }

    public boolean isTNTEnabled() {
        return tntEnabled;
    }

    public boolean isClearingItems() {
        return clearItems;
    }

    public void updateFNGStatus() {
        fng = i.getConfigManager().get(Configs.MAIN).getBoolean("FNG");
    }

    public int getFarmingMaxY() {
        return farmMaxY;
    }

    public int getFarmingMinY() {
        return farmMinY;
    }

    public double getReachDistance() {
        return reachDistance;
    }

    public boolean canClanHomeFromWilderness() {
        return canClanHomeFromWilderness;
    }

    public boolean isAssassinKnockbackEnabled() {
        return assassinKnockback;
    }

    public boolean isAssassinDealKb() {
        return assassinDealKb;
    }

    public boolean isLastDay() {
        return lastDay;
    }

    public long getDailyResetTime() {
        return resetTime;
    }


    public int MinPlayersForWorldEvent() {
        return minPlayersForWorldEvent;
    }

    public boolean isLoggingUpdateErrors() {
        return logUpdateErrors;
    }

    public boolean getMAHCheck() {
        return mahCheck;
    }

    public boolean areMorphsEnabled() {
        return morphsEnabled;
    }

    public int getStartingBalance() {
        return startingBalance;
    }

    public int getCostPerEnergy() {
        return costPerEnergy;
    }

    public int getAutoClickThreshold() {
        return autoClickThreshold;
    }

    public long getRegenTime() {
        return regenTime;
    }

    public boolean isStarterKitEnabled() {
        return starterKits;
    }

    public boolean isMapEnabled() {
        return mapEnabled;
    }

    public boolean isWarpingEnabled() {
        return warpingEnabled;
    }

    public String getTablePrefix(){
        return tablePrefix;
    }

    public void checkDefaults() {
        i.getConfigManager().get(Configs.MAIN).check("World", "world");
        i.getConfigManager().get(Configs.MAIN).check("Clans.MaxMembers", 4);
        i.getConfigManager().get(Configs.MAIN).check("Clans.MaxAllies", 1);
        i.getConfigManager().get(Configs.MAIN).check("Clans.Small.MaxAllies", 2);
        i.getConfigManager().get(Configs.MAIN).check("Clans.CostPerEnergy", 5.0);
        i.getConfigManager().get(Configs.MAIN).check("Clans.EnemySystemEnabled", false);
        i.getConfigManager().get(Configs.MAIN).check("Clans.TNT.TimeUntilProtection", 10);
        i.getConfigManager().get(Configs.MAIN).check("Clans.TNT.BonusTime", 5);
        i.getConfigManager().get(Configs.MAIN).check("Clans.TNT.MemberThreshold", 4);
        i.getConfigManager().get(Configs.MAIN).check("PillageLength", 10);
        i.getConfigManager().get(Configs.MAIN).check("Farming.MaxY", 60);
        i.getConfigManager().get(Configs.MAIN).check("Farming.MinY", 44);
        i.getConfigManager().get(Configs.MAIN).check("Daily-Reset-Time", System.currentTimeMillis());
        i.getConfigManager().get(Configs.MAIN).check("MAH.Check", true);
        i.getConfigManager().get(Configs.MAIN).check("Morphs.Enabled", true);
        i.getConfigManager().get(Configs.MAIN).check("MinPlayersForWorldEvent", 10);
        i.getConfigManager().get(Configs.MAIN).check("FNG", false);
        i.getConfigManager().get(Configs.MAIN).check("AutoClickThreshold", 13);
        i.getConfigManager().get(Configs.MAIN).check("LogUpdateErrors", true);
        i.getConfigManager().get(Configs.MAIN).check("Last-Day", false);
        i.getConfigManager().get(Configs.MAIN).check("Reach-Distance", 5.0);
        i.getConfigManager().get(Configs.MAIN).check("Server-Bank", 0);
        i.getConfigManager().get(Configs.MAIN).check("Lag.ClearItems", true);
        i.getConfigManager().get(Configs.MAIN).check("OpenTime", 0);
        i.getConfigManager().get(Configs.MAIN).check("Anti-Cheat.RegenTime", 3250);
        i.getConfigManager().get(Configs.MAIN).check("Anti-Cheat.AutoQueue", 10);
        i.getConfigManager().get(Configs.MAIN).check("MOTD", "");
        i.getConfigManager().get(Configs.MAIN).check("Warps-Enabled", false);
        i.getConfigManager().get(Configs.MAIN).check("New-Account.Starting-Balance", 5000);
        i.getConfigManager().get(Configs.MAIN).check("New-Account.Starter-Kits", false);
        i.getConfigManager().get(Configs.MAIN).check("TNT.Enabled", true);
        i.getConfigManager().get(Configs.MAIN).check("Assassin.Knockback-Enabled", true);
        i.getConfigManager().get(Configs.MAIN).check("Assassin.Deal-Knockback", false);
        i.getConfigManager().get(Configs.MAIN).check("Gambling.Roulette", false);
        i.getConfigManager().get(Configs.MAIN).check("New-Account.Starter-Kit-Cooldown", false);
        i.getConfigManager().get(Configs.MAIN).check("Clans.CanClanHomeFromWilderness", false);
        i.getConfigManager().get(Configs.MAIN).check("Settings.OnlineReward", 500000);
        i.getConfigManager().get(Configs.MAIN).check("MAH.Port", 1441);
        i.getConfigManager().get(Configs.MAIN).check("Bungee.IsHub", false);
        i.getConfigManager().get(Configs.MAIN).check("Server.IsOpen", true);
        i.getConfigManager().get(Configs.MAIN).check("Map.Enabled", true);
        i.getConfigManager().get(Configs.MAIN).check("TexturePack.URL", "https://mykindos.me/betterpvp.zip");
        i.getConfigManager().get(Configs.MAIN).check("TexturePack.SHA", "ebd67f43392e2d6694bad73ed8a1534ce9ce25a3");
        i.getConfigManager().get(Configs.MAIN).check("TexturePack.Forced", false);
        i.getConfigManager().get(Configs.MAIN).check("Database.Prefix", "clans");
        i.getConfigManager().get(Configs.MAIN).check("Bungee.HubPort", 0);
        i.getConfigManager().get(Configs.MAIN).check("Clans.MaxLevel", 5);
        i.getConfigManager().get(Configs.MAIN).check("Clans.Protect-New-Clans", false);
        i.getConfigManager().get(Configs.MAIN).check("Farming.MaxBees", 20);
        i.getConfigManager().get(Configs.MAIN).check("Voting.CratesEnabled", false);

    }

    public String getTexturePackURL() {
        return texturePackURL;
    }

    public String getTexturePackSHA() {
        return texturePackSHA;
    }

    public boolean isTexturePackForced() {
        return texturePackForce;
    }



    public int getTimeUntilTNTProtection() {
        return timeUntilTNTProtection;
    }

    public int getBonusTimeUntilTNTProtection() {
        return bonusTimeUntilTNTProtection;
    }

    public int getTntBonusMemberThreshold() {
        return tntBonusMemberThreshold;
    }

    public boolean isHub() {
        return hub;
    }

    public int getHubPort(){
        return hubPort;
    }

    public int getMaxClanlevel() {
        return maxClanlevel;
    }

    public int getMaxBees() {
        return maxBees;
    }

    public boolean isProtectNewClans() {
        return protectNewClans;
    }

    public boolean isVotingCratesEnabled() {
        return votingCratesEnabled;
    }
}
