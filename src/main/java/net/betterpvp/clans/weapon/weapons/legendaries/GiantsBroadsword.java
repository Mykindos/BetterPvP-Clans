package net.betterpvp.clans.weapon.weapons.legendaries;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.weapon.ILegendary;
import net.betterpvp.clans.weapon.Weapon;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class GiantsBroadsword extends Weapon implements ILegendary {


    public GiantsBroadsword(Clans i) {
        super(i, Material.DIAMOND_SWORD, (byte) 0, ChatColor.RED + "Giants Broadsword",
                new String[]{"", ChatColor.GRAY + "Damage: " + ChatColor.YELLOW + "9",
                        ChatColor.GRAY + "Ability: " + ChatColor.YELLOW + "Bonus Damage",
                        ChatColor.GRAY + "Passive: " + ChatColor.YELLOW + "Slow attack speed",
                        ChatColor.WHITE + "You deal massive damage, however you ",
                        ChatColor.WHITE + "attack slower than usual", ""}, true, 2);


    }


    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamage(CustomDamageEvent event) {
        if (event.getCause() != DamageCause.ENTITY_ATTACK) return;
        if (event.getDamager() instanceof Player) {

            Player player = (Player) event.getDamager();
            if (player.getInventory().getItemInMainHand() == null) return;
            if (player.getInventory().getItemInMainHand().getType() != Material.DIAMOND_SWORD) return;
            if (isThisWeapon(player)) {

                event.setDamage(9);

                event.setDamageDelay(600);

            }
        }


    }

    @Override
    public boolean isTextured() {
        return false;
    }
}
