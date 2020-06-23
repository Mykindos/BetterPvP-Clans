package net.betterpvp.clans.weapon.weapons.legendaries;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.weapon.ILegendary;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.core.framework.UpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class WingsOfZanzul extends Weapon implements ILegendary {
    public WingsOfZanzul(Clans i) {
        super(i, Material.ELYTRA, (byte) 0, ChatColor.RED + "Wings of Zanzul", new String[]{"",
                ChatColor.GRAY + "Ability: " + ChatColor.YELLOW + "Gliding",
                "",
                ChatColor.RESET + "It is noted in history that Zanzul",
                ChatColor.RESET + "created these wings to assist in",
                ChatColor.RESET + "the great rebellion against Zephyrus.",
                ""}, true, 2.0);
    }

    @EventHandler
    public void updateEvent(PlayerItemDamageEvent e) {

        if(e.getItem().getType() == Material.ELYTRA) {
          e.setCancelled(true);
        }

    }

    @Override
    public boolean isTextured() {
        return false;
    }
}
