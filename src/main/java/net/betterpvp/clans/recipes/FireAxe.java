package net.betterpvp.clans.recipes;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

public class FireAxe implements CustomRecipe{


    public FireAxe(){
        Iterator<Recipe> iter = Bukkit.getServer().recipeIterator();
        while (iter.hasNext()) {
            Recipe r = iter.next();
            // May not be safe to depend on == here for recipe comparison
            // Probably safer to compare the recipe result (an ItemStack)
            if (r.getResult().getType() == Material.GOLD_AXE) {
                iter.remove();
            }
        }

        ItemStack axe = new ItemStack(Material.GOLD_AXE);
        ShapedRecipe  fireAxe = new ShapedRecipe(axe);
        fireAxe.shape("** ", "*/ ", " / ");
        fireAxe.shape(" **", " /*", " / ");
        fireAxe.setIngredient('*', Material.GOLD_BLOCK);
        fireAxe.setIngredient('/', Material.STICK);
        Bukkit.getServer().addRecipe(fireAxe);
        System.out.println("Fire axe recipe added");
    }
}