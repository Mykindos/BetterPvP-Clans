package net.betterpvp.clans.classes.roles;

import net.betterpvp.clans.clans.ClanMember;
import net.betterpvp.clans.classes.Role;
import org.bukkit.Material;



public class Paladin extends Role {

    public Paladin() {
        super("Paladin");

    }

    @Override
    public Material getHelmet() {
        return Material.GOLD_HELMET;
    }

    @Override
    public Material getChestplate() {
        return Material.GOLD_CHESTPLATE;
    }

    @Override
    public Material getLeggings() {
        return Material.GOLD_LEGGINGS;
    }

    @Override
    public Material getBoots() {
        return Material.GOLD_BOOTS;
    }

    

}
