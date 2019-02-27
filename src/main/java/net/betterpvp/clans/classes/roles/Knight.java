package net.betterpvp.clans.classes.roles;

import net.betterpvp.clans.classes.Role;
import org.bukkit.Material;

public class Knight extends Role {

    public Knight() {
        super("Knight");

    }

    @Override
    public Material getHelmet() {
        return Material.IRON_HELMET;
    }

    @Override
    public Material getChestplate() {
        return Material.IRON_CHESTPLATE;
    }

    @Override
    public Material getLeggings() {
        return Material.IRON_LEGGINGS;
    }

    @Override
    public Material getBoots() {
        return Material.IRON_BOOTS;
    }



}
