package net.betterpvp.clans.classes.roles;

import net.betterpvp.clans.classes.Role;
import org.bukkit.Material;

public class Gladiator extends Role {

    public Gladiator() {
        super("Gladiator");

    }

    @Override
    public Material getHelmet() {
        return Material.DIAMOND_HELMET;
    }

    @Override
    public Material getChestplate() {
        return Material.DIAMOND_CHESTPLATE;
    }

    @Override
    public Material getLeggings() {
        return Material.DIAMOND_LEGGINGS;
    }

    @Override
    public Material getBoots() {
        return Material.DIAMOND_BOOTS;
    }

}
