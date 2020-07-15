package net.betterpvp.clans.recipes;

import net.betterpvp.clans.Clans;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Iterator;

public class Netherite implements CustomRecipe {

    public Netherite(Clans instance){
        NamespacedKey helmetKey = new NamespacedKey(instance, "NETHERITE_HELMET");
        NamespacedKey chestKey = new NamespacedKey(instance, "NETHERITE_CHEST");
        NamespacedKey leggingKey = new NamespacedKey(instance, "NETHERITE_LEGGINGS");
        NamespacedKey bootKey = new NamespacedKey(instance, "NETHERITE_BOOTS");

        Iterator<Recipe> iter = Bukkit.getServer().recipeIterator();
        while (iter.hasNext()) {
            Recipe r = iter.next();
            if (r.getResult().getType().name().contains("NETHERITE")) {
                if(r.getResult().getAmount() == 9 && r.getResult().getType() == Material.NETHERITE_INGOT) continue;
                iter.remove();
            }
        }

        ItemStack helmet = new ItemStack(Material.NETHERITE_HELMET);
        ShapedRecipe  chainHelmet = new ShapedRecipe(helmetKey, helmet);
        chainHelmet.shape("***", "* *", "   ");
        chainHelmet.setIngredient('*', Material.NETHERITE_INGOT);
        Bukkit.getServer().addRecipe(chainHelmet);
        System.out.println("Netherite helmet recipe added");

        ItemStack chest = new ItemStack(Material.NETHERITE_CHESTPLATE);
        ShapedRecipe  chainChest = new ShapedRecipe(chestKey, chest);
        chainChest.shape("* *", "***", "***");
        chainChest.setIngredient('*', Material.NETHERITE_INGOT);
        Bukkit.getServer().addRecipe(chainChest);
        System.out.println("Netherite chest recipe added");

        ItemStack legs = new ItemStack(Material.NETHERITE_LEGGINGS);
        ShapedRecipe  chainLegs = new ShapedRecipe(leggingKey, legs);
        chainLegs.shape("***", "* *", "* *");
        chainLegs.setIngredient('*', Material.NETHERITE_INGOT);
        Bukkit.getServer().addRecipe(chainLegs);
        System.out.println("Netherite legs recipe added");

        ItemStack boots = new ItemStack(Material.NETHERITE_BOOTS);
        ShapedRecipe  chainBoots = new ShapedRecipe(bootKey, boots);
        chainBoots.shape("* *", "* *", "   ");
        chainBoots.setIngredient('*', Material.NETHERITE_INGOT);
        Bukkit.getServer().addRecipe(chainBoots);
        System.out.println("Netherite boots recipe added");
    }
}
