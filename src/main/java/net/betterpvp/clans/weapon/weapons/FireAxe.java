package net.betterpvp.clans.weapon.weapons;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class FireAxe extends Weapon {

    public FireAxe(Clans i) {
        super(i, Material.GOLDEN_AXE, (byte) 0, ChatColor.LIGHT_PURPLE + "Fire Axe", new String[]{
                ChatColor.GRAY + "Damage: " + ChatColor.YELLOW + "5",
                ChatColor.GRAY + "Ignites players on hit"
        }, false, 0);
    }

    @EventHandler
    public void damage(CustomDamageEvent e) {
        if (e.getCause() != DamageCause.ENTITY_ATTACK) return;
        if (e.getDamager().getEquipment() == null) return;
        if (e.getDamager().getEquipment().getItemInMainHand() == null) return;
        if (e.getDamager().getEquipment().getItemInMainHand().getType() != Material.GOLDEN_AXE) return;

        Weapon w = WeaponManager.getWeapon(e.getDamager().getEquipment().getItemInMainHand());
        if (w != null && w.equals(this)) {


            e.getDamagee().setFireTicks(40);
        }
    }

}



