package net.betterpvp.clans.weapon;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.weapon.weapons.*;
import net.betterpvp.clans.weapon.weapons.legendaries.*;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.utility.UtilItem;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeaponManager extends BPVPListener<Clans> {

    public static List<Weapon> weapons = new ArrayList<Weapon>();

    public WeaponManager(Clans i) {
        super(i);
        addWeapon(new IncendiaryGrenade(i));
        addWeapon(new ExtinguishingPotion(i));
        addWeapon(new EnderPearl(i));
        addWeapon(new Web(i));
        addWeapon(new FireAxe(i));
        addWeapon(new GravityGrenade(i));
        addWeapon(new EnergyApple(i));
        addWeapon(new Molotov(i));

        addWeapon(new MagneticHammer(i));
        addWeapon(new DurielsScepter(i));
        addWeapon(new FarmingRake(i));
        addWeapon(new HyperAxe(i));
        addWeapon(new MeteorBow(i));
        addWeapon(new FireWalkers(i));
        addWeapon(new WindBlade(i));
        addWeapon(new AlligatorsTooth(i));
        addWeapon(new WingsOfZanzul(i));
        addWeapon(new GiantsBroadsword(i));
        addWeapon(new DwarvenPickaxe(i));
        addWeapon(new SupplyCrate(i));
        addWeapon(new GlacialAxe(i));

        for (Qualities q : Qualities.values()) {
            for (ArmourNames an : ArmourNames.values()) {
                addWeapon(new EnchantedWeapon(i, an.getMaterial(), (byte) 0, q.getQuality() + ChatColor.YELLOW + an.getName(),
                        new String[]{ChatColor.WHITE + "Bonus Damage Reduction: " + ChatColor.YELLOW
                                + (q.getBonus() * 2) + "%"}, q.getBonus() * 2, q.getChance()));

            }

            addWeapon(new EnchantedWeapon(i, Material.IRON_SWORD, (byte) 0, q.getQuality() + ChatColor.YELLOW + "Iron Sword",
                    new String[]{ChatColor.WHITE + "Damage: " + ChatColor.YELLOW + (5 + q.getBonus())}, q.getBonus(), q.getChance()));
        }
    }

    private static void addWeapon(Weapon w) {
        weapons.add(w);
    }


    public static Weapon getWeapon(ItemStack item) {
        for (Weapon weapon : weapons) {

            if (weapon.getMaterial().equals(item.getType())) {

                if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().hasLore()) {
                    if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(weapon.getName()))) {

                        if (item.getItemMeta().getLore() != null && weapon.getLore() != null) {

                            if (item.getItemMeta().getLore().equals(Arrays.asList(weapon.getLore()))) {
                                return weapon;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static Weapon getWeapon(String name) {
        for (Weapon weapon : weapons) {
            if (ChatColor.stripColor(weapon.getName()).equalsIgnoreCase(ChatColor.stripColor(name))) {
                return weapon;
            }
        }

        System.out.println(name);
        return null;
    }


    public static boolean isWeapon(ItemStack item) {
        if (getWeapon(item) != null) {
            return true;
        }
        return false;
    }

    @EventHandler
    public void onWeaponPickup(EntityPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack();

        for (Weapon weapon : weapons) {
            if (!(weapon instanceof ILegendary) && !(weapon instanceof EnchantedWeapon)) {
                if (item.getType() == weapon.getMaterial()) {
                    if (!(weapon instanceof FireAxe)) {
                        UtilItem.setItemNameAndLore(item, weapon.getName(), weapon.getLore());
                    }
                }
            }
        }
    }


    @EventHandler
    public void renameSpawn(ItemSpawnEvent event) {
        ItemStack item = event.getEntity().getItemStack();

        for (Weapon weapon : weapons) {
            if (!(weapon instanceof ILegendary) && !(weapon instanceof EnchantedWeapon)) {
                if (item.getType() == weapon.getMaterial()) {
                    if (!(weapon instanceof FireAxe)) {
                        UtilItem.setItemNameAndLore(item, weapon.getName(), weapon.getLore());
                    }
                }
            }
        }
    }

    @EventHandler
    public void renameCraft(PrepareItemCraftEvent event) {
        ItemStack item = event.getInventory().getResult();

        for (Weapon weapon : weapons) {
            if (!(weapon instanceof ILegendary) && !(weapon instanceof EnchantedWeapon)) {
                if (item != null) {
                    if (!(weapon instanceof FireAxe)) {
                        if (item.getType() == weapon.getMaterial()) {
                            event.getInventory().setResult(UtilItem.setItemNameAndLore(item, weapon.getName(), weapon.getLore()));
                        }
                    }
                }
            }
        }
    }
}
