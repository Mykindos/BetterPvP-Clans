package net.betterpvp.clans.classes.roles;

import net.betterpvp.clans.classes.Role;
import org.bukkit.Material;

public class Warlock extends Role {

    public Warlock() {
        super("Warlock");

    }

    @Override
    public Material getHelmet() {
        return Material.NETHERITE_HELMET;
    }

    @Override
    public Material getChestplate() {
        return Material.NETHERITE_CHESTPLATE;
    }

    @Override
    public Material getLeggings() {
        return Material.NETHERITE_LEGGINGS;
    }

    @Override
    public Material getBoots() {
        return Material.NETHERITE_BOOTS;
    }


}
