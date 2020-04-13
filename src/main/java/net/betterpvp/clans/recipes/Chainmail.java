package net.betterpvp.clans.recipes;

import net.betterpvp.clans.Clans;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class Chainmail implements CustomRecipe{

    public Chainmail(Clans instance){

        NamespacedKey key = new NamespacedKey(instance, instance.getDescription().getName());

        ItemStack helmet = new ItemStack(Material.CHAINMAIL_HELMET);
        ShapedRecipe  chainHelmet = new ShapedRecipe(key, helmet);
        chainHelmet.shape("***", "* *", "   ");
        chainHelmet.setIngredient('*', Material.EMERALD);
        Bukkit.getServer().addRecipe(chainHelmet);
        System.out.println("Chain helmet recipe added");

        ItemStack chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        ShapedRecipe  chainChest = new ShapedRecipe(key, chest);
        chainChest.shape("* *", "***", "***");
        chainChest.setIngredient('*', Material.EMERALD);
        Bukkit.getServer().addRecipe(chainChest);
        System.out.println("Chain chest recipe added");

        ItemStack legs = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        ShapedRecipe  chainLegs = new ShapedRecipe(key, legs);
        chainLegs.shape("***", "* *", "* *");
        chainLegs.setIngredient('*', Material.EMERALD);
        Bukkit.getServer().addRecipe(chainLegs);
        System.out.println("Chain legs recipe added");

        ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);
        ShapedRecipe  chainBoots = new ShapedRecipe(key, boots);
        chainBoots.shape("* *", "* *", "   ");
        chainBoots.setIngredient('*', Material.EMERALD);
        Bukkit.getServer().addRecipe(chainBoots);
        System.out.println("Chain boots recipe added");
    }

}