package net.betterpvp.clans.recipes;

import java.util.Iterator;

import net.betterpvp.clans.Clans;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

public class PowerSword implements CustomRecipe{

    public PowerSword(Clans instance){
        Iterator<Recipe> iter = Bukkit.getServer().recipeIterator();
        while (iter.hasNext()) {
            Recipe r = iter.next();
            // May not be safe to depend on == here for recipe comparison
            // Probably safer to compare the recipe result (an ItemStack)
            if (r.getResult().getType() == Material.DIAMOND_SWORD) {
                iter.remove();
            }
        }

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ShapedRecipe  powerSword = new ShapedRecipe(new NamespacedKey(instance, instance.getDescription().getName()), sword);
        powerSword.shape(" * ", " * ", " / ");
        powerSword.setIngredient('*', Material.DIAMOND_BLOCK);
        powerSword.setIngredient('/', Material.STICK);
        Bukkit.getServer().addRecipe(powerSword);
        System.out.println("Power Sword recipe added");
    }

}
