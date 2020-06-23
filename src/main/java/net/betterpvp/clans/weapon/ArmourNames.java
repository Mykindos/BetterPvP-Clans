package net.betterpvp.clans.weapon;

import org.bukkit.Material;

public enum ArmourNames {


    IRON_BOOTS(Material.IRON_BOOTS, "Knight Boots"),
    IRON_LEGGINGS(Material.IRON_LEGGINGS, "Knight Leggings"),
    IRON_CHESTPLATE(Material.IRON_CHESTPLATE, "Knight Chestplate"),
    IRON_HELMET(Material.IRON_HELMET, "Knight Helmet"),

    GOLDEN_BOOTS(Material.GOLDEN_BOOTS, "Paladin Boots"),
    GOLDEN_LEGGINGS(Material.GOLDEN_LEGGINGS, "Paladin Leggings"),
    GOLDEN_CHESTPLATE(Material.GOLDEN_CHESTPLATE, "Paladin Chestplate"),
    GOLDEN_HELMET(Material.GOLDEN_HELMET, "Paladin Helmet"),

    CHAINMAIL_BOOTS(Material.CHAINMAIL_BOOTS, "Ranger Boots"),
    CHAINMAIL_LEGGINGS(Material.CHAINMAIL_LEGGINGS, "Ranger Leggings"),
    CHAINMAIL_CHESTPLATE(Material.CHAINMAIL_CHESTPLATE, "Ranger Chestplate"),
    CHAINMAIL_HELMET(Material.CHAINMAIL_HELMET, "Ranger Helmet"),

    DIAMOND_BOOTS(Material.DIAMOND_BOOTS, "Gladiator Boots"),
    DIAMOND_LEGGINGS(Material.DIAMOND_LEGGINGS, "Gladiator Leggings"),
    DIAMOND_CHESTPLATE(Material.DIAMOND_CHESTPLATE, "Gladiator Chestplate"),
    DIAMOND_HELMET(Material.DIAMOND_HELMET, "Gladiator Helmet");

    private Material m;
    private String n;

    ArmourNames(Material m, String n) {
        this.m = m;
        this.n = n;
    }

    public Material getMaterial() {
        return m;
    }

    public String getName() {
        return n;
    }

}
