package net.betterpvp.clans.crates.crates;

import net.betterpvp.clans.crates.Crate;
import net.betterpvp.clans.utilities.UtilClans;
import net.betterpvp.clans.weapon.ILegendary;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class VotingCrate extends Crate {

    public VotingCrate() {
        super(UtilPlayer.createItem(Material.CHEST, 1, ChatColor.GREEN + "Voting Crate",
                ChatColor.GRAY + "Left click to open"));
        loot.put(UtilPlayer.createItem(Material.GOLD_NUGGET, 1, ChatColor.YELLOW + "$10000"), 150.0);
        loot.put(UtilPlayer.createItem(Material.GOLD_INGOT, 1, ChatColor.YELLOW + "$15000"), 70.0);
        loot.put(UtilPlayer.createItem(Material.GOLD_BLOCK, 1, ChatColor.YELLOW + "$100000"), 5.0);
        loot.put(UtilPlayer.createItem(Material.DIAMOND_SWORD, 1, ChatColor.YELLOW + "Power Sword"), 10.0);
        loot.put(UtilClans.updateNames(new ItemStack(Material.NETHERITE_SWORD, 1)), 2.5);
        loot.put(UtilClans.updateNames(new ItemStack(Material.NETHERITE_AXE, 1)), 2.5);

        loot.put(UtilClans.updateNames(new ItemStack(Material.TNT, 1)), 4.0);
        loot.put(UtilClans.updateNames(new ItemStack(Material.GOLD_INGOT, 14)), 45.0);
        loot.put(UtilClans.updateNames(new ItemStack(Material.IRON_INGOT, 14)), 45.0);
        loot.put(UtilClans.updateNames(new ItemStack(Material.LEATHER, 14)), 50.0);
        loot.put(UtilClans.updateNames(new ItemStack(Material.EMERALD, 14)), 50.0);
        loot.put(UtilClans.updateNames(new ItemStack(Material.DIAMOND, 14)), 50.0);
        loot.put(UtilClans.updateNames(new ItemStack(Material.NETHERITE_INGOT, 14)), 50.0);
        loot.put(UtilClans.updateNames(new ItemStack(Material.DIAMOND_PICKAXE)), 35.0);
        loot.put(UtilClans.updateNames(new ItemStack(Material.DIAMOND_AXE)), 10.0);
        loot.put(UtilClans.updateNames(new ItemStack(Material.DIAMOND_SHOVEL)), 35.0);

        for(Weapon w : WeaponManager.weapons){
            if(w instanceof ILegendary){
                loot.put(w.createWeapon(), 0.06);
            }
        }

    }

    @Override
    public String getName() {

        return ChatColor.GREEN + "Voting Crate";
    }

    @Override
    public int getSize() {

        return 27;
    }



}