package net.betterpvp.clans.weapon;

import org.bukkit.ChatColor;

public enum Qualities {

    APPRENTICE(ChatColor.GREEN + "Apprentice ", 1.0, 20),
    MASTERCRAFT(ChatColor.BLUE + "Mastercraft ", 2.0, 7),
    ASCENDANT(ChatColor.RED + "Ascendant ", 3.0, 5);

    private String quality;
    private double bonus;
    private double chance;

    Qualities(String quality, double bonus, double chance) {
        this.quality = quality;
        this.bonus = bonus;
        this.chance = chance;
    }

    public String getQuality() {
        return quality;
    }

    public double getBonus() {
        return bonus;
    }

    public double getChance() {
        return chance;
    }

}
