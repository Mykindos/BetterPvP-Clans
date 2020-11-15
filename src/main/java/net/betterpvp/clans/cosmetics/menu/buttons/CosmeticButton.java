package net.betterpvp.clans.cosmetics.menu.buttons;

import net.betterpvp.clans.cosmetics.Cosmetic;
import net.betterpvp.core.interfaces.Button;
import net.betterpvp.core.utility.UtilItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CosmeticButton extends Button {
    
    private Cosmetic cosmetic;
    
    public CosmeticButton(Cosmetic cosmetic, int slot, String name, boolean glow) {
        super(slot, glow ? UtilItem.addGlow(new ItemStack(Material.BOOK)) : new ItemStack(Material.BOOK), name);
        this.cosmetic = cosmetic;
    }

    public Cosmetic getCosmetic() {
        return cosmetic;
    }
}
