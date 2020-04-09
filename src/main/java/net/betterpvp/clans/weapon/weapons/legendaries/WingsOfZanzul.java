package net.betterpvp.clans.weapon.weapons.legendaries;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.weapon.Weapon;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class WingsOfZanzul extends Weapon {
    public WingsOfZanzul(Clans i) {
        super(i, Material.ELYTRA, (byte) 0, ChatColor.RED + "Wings of Zanzul",   new String[]{"",
                ChatColor.GRAY + "Ability: " + ChatColor.YELLOW + "Gliding",
                "",
                ChatColor.RESET + "It is noted in history that Zanzul",
                ChatColor.RESET + "created these wings to assist in",
                ChatColor.RESET + "the great rebellion against Zephyrus.",
                ""}, true, 2.0);
    }
}
