package net.betterpvp.clans.clans.menus.buttons;

import net.betterpvp.clans.clans.Clan;
import org.bukkit.inventory.ItemStack;

public class ClaimButton extends ClanMenuButton {

    public ClaimButton(Clan clan, int slot, ItemStack item, String name,
                       String... lore) {
        super(clan, slot, item, name, lore);

    }


}
