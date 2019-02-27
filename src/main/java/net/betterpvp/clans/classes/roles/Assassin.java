package net.betterpvp.clans.classes.roles;

import net.betterpvp.clans.classes.Role;
import org.bukkit.Material;

public class Assassin extends Role {

    public Assassin() {
        super("Assassin");
  
    }

    @Override
    public Material getHelmet() {
        return Material.LEATHER_HELMET;
    }

    @Override
    public Material getChestplate() {
        return Material.LEATHER_CHESTPLATE;
    }

    @Override
    public Material getLeggings() {
        return Material.LEATHER_LEGGINGS;
    }

    @Override
    public Material getBoots() {
        return Material.LEATHER_BOOTS;
    }


}
