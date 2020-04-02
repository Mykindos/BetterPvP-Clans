package net.betterpvp.clans.recipes;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

public class PowerAxe implements CustomRecipe{


    public PowerAxe(){
        Iterator<Recipe> iter = Bukkit.getServer().recipeIterator();
        while (iter.hasNext()) {
            Recipe r = iter.next();
            // May not be safe to depend on == here for recipe comparison
            // Probably safer to compare the recipe result (an ItemStack)
            if (r.getResult().getType() == Material.DIAMOND_AXE) {
                iter.remove();
            }
        }

        ItemStack axe = new ItemStack(Material.DIAMOND_AXE);
        ShapedRecipe  fireAxe = new ShapedRecipe(axe);
        fireAxe.shape("** ", "*/ ", " / ");
        fireAxe.shape(" **", " /*", " / ");
        fireAxe.setIngredient('*', Material.DIAMOND_BLOCK);
        fireAxe.setIngredient('/', Material.STICK);
        Bukkit.getServer().addRecipe(fireAxe);
        System.out.println("Power axe recipe added");
    }
}