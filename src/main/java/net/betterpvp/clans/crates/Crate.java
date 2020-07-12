package net.betterpvp.clans.crates;
import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

public abstract class Crate {

    protected HashMap<ItemStack, Double> loot;
    private ItemStack crate;

    public Crate(ItemStack crate){
        this.crate = crate;

        loot = new HashMap<>();
    }

    public abstract String getName();
    public abstract int getSize();

    public ItemStack getCrate(){
        return crate;
    }

    public HashMap<ItemStack, Double> getLoot(){
        return loot;
    }

}