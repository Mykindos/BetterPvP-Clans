package net.betterpvp.clans.recipes;

import net.betterpvp.clans.Clans;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Iterator;

public class FireAxe implements CustomRecipe{


    public FireAxe(Clans instance){
        Iterator<Recipe> iter = Bukkit.getServer().recipeIterator();
        while (iter.hasNext()) {
            Recipe r = iter.next();
            // May not be safe to depend on == here for recipe comparison
            // Probably safer to compare the recipe result (an ItemStack)
            if (r.getResult().getType() == Material.GOLDEN_AXE) {
                iter.remove();
            }
        }

        ItemStack axe = new ItemStack(Material.GOLDEN_AXE);
        ShapedRecipe  fireAxe = new ShapedRecipe(new NamespacedKey(instance, "FIRE_AXE"), axe);
        fireAxe.shape("** ", "*/ ", " / ");
        fireAxe.shape(" **", " /*", " / ");
        fireAxe.setIngredient('*', Material.GOLD_BLOCK);
        fireAxe.setIngredient('/', Material.STICK);
        Bukkit.getServer().addRecipe(fireAxe);
        System.out.println("Fire axe recipe added");
    }
}