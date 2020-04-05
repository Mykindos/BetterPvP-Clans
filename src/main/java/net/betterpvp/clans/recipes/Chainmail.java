package net.betterpvp.clans.recipes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class Chainmail implements CustomRecipe{

    public Chainmail(){
        ItemStack helmet = new ItemStack(Material.CHAINMAIL_HELMET);
        ShapedRecipe  chainHelmet = new ShapedRecipe(helmet);
        chainHelmet.shape("***", "* *", "   ");
        chainHelmet.setIngredient('*', Material.EMERALD);
        Bukkit.getServer().addRecipe(chainHelmet);
        System.out.println("Chain helmet recipe added");

        ItemStack chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        ShapedRecipe  chainChest = new ShapedRecipe(chest);
        chainChest.shape("* *", "***", "***");
        chainChest.setIngredient('*', Material.EMERALD);
        Bukkit.getServer().addRecipe(chainChest);
        System.out.println("Chain chest recipe added");

        ItemStack legs = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        ShapedRecipe  chainLegs = new ShapedRecipe(legs);
        chainLegs.shape("***", "* *", "* *");
        chainLegs.setIngredient('*', Material.EMERALD);
        Bukkit.getServer().addRecipe(chainLegs);
        System.out.println("Chain legs recipe added");

        ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);
        ShapedRecipe  chainBoots = new ShapedRecipe(boots);
        chainBoots.shape("* *", "* *", "   ");
        chainBoots.setIngredient('*', Material.EMERALD);
        Bukkit.getServer().addRecipe(chainBoots);
        System.out.println("Chain boots recipe added");
    }

}