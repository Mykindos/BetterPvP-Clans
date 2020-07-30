package net.betterpvp.clans.weapon.weapons.legendaries;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.weapon.ILegendary;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class HyperAxe extends Weapon implements ILegendary {


    public HyperAxe(Clans i) {
        super(i, Material.MUSIC_DISC_MALL, (byte) 0, ChatColor.RED + "Hyper Axe",
                new String[]{"", ChatColor.GRAY + "Damage: " + ChatColor.YELLOW + "4",
                        ChatColor.GRAY + "Ability: " + ChatColor.YELLOW + "Hyper Speed",
                        ChatColor.GRAY + "Passive: " + ChatColor.YELLOW + "Hyper Attack",
                        ChatColor.GRAY + "Knockback: " + ChatColor.YELLOW + "None", "",
                        ChatColor.GRAY + "Rumoured to attack foes 500% faster",
                        ChatColor.GRAY + "than any other weapon known to man.", ""}, true, 4.0);


    }


    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamage(CustomDamageEvent event) {
        if (event.getCause() != DamageCause.ENTITY_ATTACK) return;
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();

            if (player.getInventory().getItemInMainHand() == null) return;
            if (player.getInventory().getItemInMainHand().getType() != Material.MUSIC_DISC_MALL) return;


            Weapon weapon = WeaponManager.getWeapon(player.getInventory().getItemInMainHand());
            if (weapon != null && weapon.equals(this)) {

                event.setDamage(4);
                event.setKnockback(false);
                event.setDamageDelay(50);

            }


        }
    }

    @Override
    public boolean isTextured() {
        return true;
    }
}
