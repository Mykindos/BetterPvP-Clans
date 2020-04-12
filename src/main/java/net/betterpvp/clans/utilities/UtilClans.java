package net.betterpvp.clans.utilities;

import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.core.utility.UtilFormat;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class UtilClans {

    /**
     * General method that updates the name of almost every item that is picked up by players
     * E.g. Names leather armour after assassins
     * E.g. Turns the colour of the items name to yellow from white
     * @param abc ItemStack to update
     * @return An ItemStack with an updated name
     */
    public static ItemStack updateNames(ItemStack abc){
        if(WeaponManager.getWeapon(abc) != null){
            return abc;
        }

        if(abc.hasItemMeta()){
            //if(Perk.getPerk(abc.getItemMeta().getDisplayName()) != null){
            //    return abc;
           // }
        }
        List<String> lore = new ArrayList<>();
        Material m = abc.getType();
        ItemMeta a = abc.getItemMeta();

        if(m == Material.LEATHER_HELMET){
            a.setDisplayName("Assassin Helmet");
        }else if(m == Material.LEATHER_CHESTPLATE){
            a.setDisplayName("Assassin Vest");
        }else if(m == Material.LEATHER_LEGGINGS){
            a.setDisplayName("Assassin Leggings");
        }else if(m == Material.LEATHER_BOOTS){
            a.setDisplayName("Assassin Boots");
        }else if(m == Material.IRON_HELMET){
            a.setDisplayName("Knight Helmet");
        }else if(m == Material.IRON_CHESTPLATE){
            a.setDisplayName("Knight Chestplate");
        }else if(m == Material.IRON_LEGGINGS){
            a.setDisplayName("Knight Leggings");
        }else if(m == Material.IRON_BOOTS){
            a.setDisplayName("Knight Boots");
        }else if(m == Material.DIAMOND_HELMET){
            a.setDisplayName("Gladiator Helmet");
        }else if(m == Material.DIAMOND_CHESTPLATE){
            a.setDisplayName("Gladiator Chestplate");
        }else if(m == Material.DIAMOND_LEGGINGS){
            a.setDisplayName("Gladiator Leggings");
        }else if(m == Material.DIAMOND_BOOTS){
            a.setDisplayName("Gladiator Boots");
        }else if(m == Material.GOLDEN_HELMET){
            a.setDisplayName("Paladin Helmet");
        }else if(m == Material.GOLDEN_CHESTPLATE){
            a.setDisplayName("Paladin Vest");
        }else if(m == Material.GOLDEN_LEGGINGS){
            a.setDisplayName("Paladin Leggings");
        }else if(m == Material.GOLDEN_BOOTS){
            a.setDisplayName("Paladin Boots");
        }else if(m == Material.CHAINMAIL_HELMET){
            a.setDisplayName("Ranger Helmet");
        }else if(m == Material.CHAINMAIL_CHESTPLATE){
            a.setDisplayName("Ranger Vest");
        }else if(m == Material.CHAINMAIL_LEGGINGS){
            a.setDisplayName("Ranger Leggings");
        }else if(m == Material.CHAINMAIL_BOOTS){
            a.setDisplayName("Ranger Boots");
        }else if(m == Material.GOLDEN_AXE){
            a.setDisplayName("Radiant axe");
            lore.add(ChatColor.GRAY + "Damage: " + ChatColor.GREEN + "5");

        }else if(m == Material.MUSIC_DISC_13){
            a.setDisplayName("$100,000");
        }else if(m == Material.MUSIC_DISC_11){
            a.setDisplayName("$50,000");
        }else if(m == Material.MUSIC_DISC_WAIT){
            a.setDisplayName("$1,000,000");
        }else if(m == Material.CARROT){
            a.setDisplayName("Carrot");
        }else if(m == Material.POTATO){
            a.setDisplayName("Potato");
        }else if(m == Material.IRON_SWORD){

            a.setDisplayName("Standard Sword");
            lore.add(ChatColor.GRAY + "Damage: " + ChatColor.GREEN + "4");

        }else if(m == Material.GOLDEN_SWORD){
            a.setDisplayName("Radiant Sword");
            lore.add(ChatColor.GRAY + "Damage: " + ChatColor.GREEN + "6");
        }else if(m == Material.DIAMOND_SWORD){
            a.setDisplayName("Power Sword");
            lore.add(ChatColor.GRAY + "Damage: " + ChatColor.GREEN + "5");
            lore.add(ChatColor.GRAY + "Bonus 1 Level to Sword Skills");
        }else if(m == Material.IRON_AXE){
            a.setDisplayName("Standard Axe");
            lore.add(ChatColor.GRAY + "Damage: " + ChatColor.GREEN + "3");

        }else if(m == Material.DIAMOND_AXE){
            a.setDisplayName("Power Axe");
            lore.add(ChatColor.GRAY + "Damage: " + ChatColor.GREEN + "4");
            lore.add(ChatColor.GRAY + "Bonus 1 Level to Axe Skills");
        }

        if(a.hasDisplayName()){
            if(a.getDisplayName().equalsIgnoreCase(ChatColor.stripColor("Base Fishing"))){
                lore.add(ChatColor.WHITE + "Allows a player to fish inside their base");
            }
            if(!a.getDisplayName().contains("Crate")){
                a.setDisplayName(ChatColor.YELLOW + ChatColor.stripColor(a.getDisplayName()));
            }
        }else{
            a.setDisplayName(ChatColor.YELLOW + UtilFormat.cleanString(abc.getType().name()));
        }
        a.setLore(lore);
        abc.setItemMeta(a);
        return abc;
    }

    /**
     * Check if a player has any valuable items in their inventory
     * @param p Players inventory to check
     * @return Returns true if a player has any items of value in their inventory
     */
    public static boolean hasValuables(Player p){


        for(ItemStack i : p.getInventory().getContents()){
            if(i != null){
                Weapon w = WeaponManager.getWeapon(i);
                if(w != null){
                    if(w.isLegendary()){
                        return true;
                    }
                }

                if(i.getType() == Material.MUSIC_DISC_WAIT || i.getType() == Material.MUSIC_DISC_11
                        || i.getType() == Material.MUSIC_DISC_13 || i.getType() == Material.TNT){
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isUsableWithShield(ItemStack item){

        if(item.getType().name().contains("_SWORD")){
            return true;
        }

        //Windblade
        if(item.getType() == Material.MUSIC_DISC_11){
            return true;
        }

        return false;
    }
}
