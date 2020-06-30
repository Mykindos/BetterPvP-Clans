package net.betterpvp.clans.recipes;

import net.betterpvp.clans.Clans;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Iterator;

public class PowerAxe implements CustomRecipe{


    public PowerAxe(Clans instance){
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
        ShapedRecipe fireAxe = new ShapedRecipe(new NamespacedKey(instance, "DIAMOND_AXE"), axe);
        fireAxe.shape("** ", "*/ ", " / ");
        fireAxe.shape(" **", " /*", " / ");
        fireAxe.setIngredient('*', Material.DIAMOND_BLOCK);
        fireAxe.setIngredient('/', Material.STICK);
        Bukkit.getServer().addRecipe(fireAxe);
        System.out.println("Power axe recipe added");
    }
}