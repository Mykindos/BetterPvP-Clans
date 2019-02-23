package net.betterpvp.clans.farming;

import org.bukkit.Material;

public class FarmBlocks {

    public static boolean isCultivation(Material b){
        return b == Material.PUMPKIN_SEEDS || b == Material.MELON_SEEDS
                || b == Material.SEEDS|| b == Material.SUGAR_CANE
                || b == Material.SUGAR_CANE_BLOCK
                || b == Material.POTATO_ITEM
                || b == Material.POTATO
                || b == Material.CARROT
                || b == Material.CACTUS
                || b == Material.MELON_STEM
                || b == Material.CROPS
                || b == Material.PUMPKIN_STEM
                || b == Material.PUMPKIN
                || b == Material.MELON_BLOCK
                || b == Material.NETHER_STALK
                || b == Material.NETHER_WARTS;
    }


    public static boolean isSeed(Material b){
        return b == Material.PUMPKIN_SEEDS || b == Material.MELON_SEEDS
                || b == Material.SEEDS|| b == Material.SUGAR_CANE
                || b == Material.SUGAR_CANE_BLOCK
                || b == Material.POTATO
                || b == Material.CROPS
                || b == Material.CARROT
                || b == Material.CACTUS
                || b == Material.NETHER_STALK
                || b == Material.NETHER_WARTS;
    }
}
