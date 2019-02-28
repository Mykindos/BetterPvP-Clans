package net.betterpvp.clans.weapon;

import net.betterpvp.clans.Clans;
import org.bukkit.Material;

public class EnchantedWeapon extends Weapon {

    private double bonus;

    public EnchantedWeapon(Clans i, Material material, byte data, String name,
                           String[] lore, double bonus, double chance) {
        super(i, material, data, name, lore, false, chance);
        this.bonus = bonus;
        // TODO Auto-generated constructor stub
    }

    public double getBonus() {
        return bonus;
    }

}
