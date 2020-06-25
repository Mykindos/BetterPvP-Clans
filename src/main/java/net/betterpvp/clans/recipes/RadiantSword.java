package net.betterpvp.clans.recipes;

import java.util.Iterator;

import net.betterpvp.clans.Clans;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

public class RadiantSword implements CustomRecipe{

    public RadiantSword(Clans instance){
        Iterator<Recipe> iter = Bukkit.getServer().recipeIterator();
        while (iter.hasNext()) {
            Recipe r = iter.next();
            if (r.getResult().getType() == Material.GOLDEN_SWORD) {
                iter.remove();
            }
        }

        ItemStack sword = new ItemStack(Material.GOLDEN_SWORD);
        ShapedRecipe  powerSword = new ShapedRecipe(new NamespacedKey(instance, "GOLDEN_SWORD"), sword);
        powerSword.shape(" * ", " * ", " / ");
        powerSword.setIngredient('*', Material.GOLD_BLOCK);
        powerSword.setIngredient('/', Material.STICK);
        Bukkit.getServer().addRecipe(powerSword);
        System.out.println("Radiant Sword recipe added");
    }

}
