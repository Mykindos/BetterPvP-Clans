package net.betterpvp.clans.classes.roles;

import net.betterpvp.clans.classes.Role;
import org.bukkit.Material;


public class Paladin extends Role {

    public Paladin() {
        super("Paladin");

    }

    @Override
    public Material getHelmet() {
        return Material.GOLDEN_HELMET;
    }

    @Override
    public Material getChestplate() {
        return Material.GOLDEN_CHESTPLATE;
    }

    @Override
    public Material getLeggings() {
        return Material.GOLDEN_LEGGINGS;
    }

    @Override
    public Material getBoots() {
        return Material.GOLDEN_BOOTS;
    }


}
