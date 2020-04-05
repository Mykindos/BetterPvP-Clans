package net.betterpvp.clans.fields;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Blocks {

    DIAMOND(Material.DIAMOND_ORE, new ItemStack(Material.DIAMOND, 2)),
    IRON(Material.IRON_ORE, new ItemStack(Material.IRON_INGOT, 2)),
    GOLD(Material.GOLD_ORE, new ItemStack(Material.GOLD_INGOT, 2)),
    LAPIS(Material.LAPIS_ORE, null),
    REDSTONE(Material.REDSTONE_ORE, new ItemStack(Material.REDSTONE, 3)),
    COAL(Material.COAL_ORE, new ItemStack(Material.COAL, 2)),
    EMERALD(Material.EMERALD_ORE, new ItemStack(Material.EMERALD, 2)),
    PUMPKIN(Material.PUMPKIN, new ItemStack(Material.PUMPKIN, 5)),
    POTATO(Material.POTATO, new ItemStack(Material.POTATO, 3)),
    CARROT(Material.CARROT, new ItemStack(Material.CARROT, 3)),
    WATERMELON(Material.MELON, new ItemStack(Material.MELON, 2)),
    ENDERCHEST(Material.ENDER_CHEST, new ItemStack(Material.DIAMOND, 1));

    private Material mat;
    private ItemStack drop;


    Blocks(Material mat, ItemStack drop) {
        this.mat = mat;
        this.drop = drop;

    }


    public Material getMaterial() {
        return mat;
    }

    public ItemStack getDrop() {
        return drop;
    }


}
