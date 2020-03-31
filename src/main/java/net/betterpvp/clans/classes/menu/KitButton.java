package net.betterpvp.clans.classes.menu;

import net.betterpvp.clans.classes.Role;
import net.betterpvp.core.interfaces.Button;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class KitButton extends Button {

    private Role role;

    public KitButton(int slot, ItemStack item, String name, String... lore) {
        super(slot, item, name, lore);
        this.role = Role.getRole(ChatColor.stripColor(name.split(" ")[0]));
    }

    public Role getRole(){
        return role;
    }

}