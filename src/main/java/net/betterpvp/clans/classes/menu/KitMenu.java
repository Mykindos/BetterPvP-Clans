package net.betterpvp.clans.classes.menu;

import net.betterpvp.core.interfaces.Button;
import net.betterpvp.core.interfaces.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitMenu extends Menu {

    private boolean newPlayer;

    public KitMenu(Player p, boolean newPlayer) {
        super(p, 36, ChatColor.GREEN.toString() + ChatColor.BOLD + "Select a Class!", new Button[]{
                new KitButton(0, new ItemStack(Material.DIAMOND_HELMET), ChatColor.GREEN.toString() + ChatColor.BOLD + "Gladiator Class"),
                new KitButton(9, new ItemStack(Material.DIAMOND_CHESTPLATE), ChatColor.GREEN.toString() + ChatColor.BOLD + "Gladiator Class"),
                new KitButton(18, new ItemStack(Material.DIAMOND_LEGGINGS), ChatColor.GREEN.toString() + ChatColor.BOLD + "Gladiator Class"),
                new KitButton(27, new ItemStack(Material.DIAMOND_BOOTS), ChatColor.GREEN.toString() + ChatColor.BOLD + "Gladiator Class"),

                new KitButton(2, new ItemStack(Material.LEATHER_HELMET), ChatColor.GREEN.toString() + ChatColor.BOLD + "Assassin Class"),
                new KitButton(11, new ItemStack(Material.LEATHER_CHESTPLATE), ChatColor.GREEN.toString() + ChatColor.BOLD + "Assassin Class"),
                new KitButton(20, new ItemStack(Material.LEATHER_LEGGINGS), ChatColor.GREEN.toString() + ChatColor.BOLD + "Assassin Class"),
                new KitButton(29, new ItemStack(Material.LEATHER_BOOTS), ChatColor.GREEN.toString() + ChatColor.BOLD + "Assassin Class"),

                new KitButton(4, new ItemStack(Material.IRON_HELMET), ChatColor.GREEN.toString() + ChatColor.BOLD + "Knight Class"),
                new KitButton(13, new ItemStack(Material.IRON_CHESTPLATE), ChatColor.GREEN.toString() + ChatColor.BOLD + "Knight Class"),
                new KitButton(22, new ItemStack(Material.IRON_LEGGINGS), ChatColor.GREEN.toString() + ChatColor.BOLD + "Knight Class"),
                new KitButton(31, new ItemStack(Material.IRON_BOOTS), ChatColor.GREEN.toString() + ChatColor.BOLD + "Knight Class"),

                new KitButton(6, new ItemStack(Material.CHAINMAIL_HELMET), ChatColor.GREEN.toString() + ChatColor.BOLD + "Ranger Class"),
                new KitButton(15, new ItemStack(Material.CHAINMAIL_CHESTPLATE), ChatColor.GREEN.toString() + ChatColor.BOLD + "Ranger Class"),
                new KitButton(24, new ItemStack(Material.CHAINMAIL_LEGGINGS), ChatColor.GREEN.toString() + ChatColor.BOLD + "Ranger Class"),
                new KitButton(33, new ItemStack(Material.CHAINMAIL_BOOTS), ChatColor.GREEN.toString() + ChatColor.BOLD + "Ranger Class"),

                new KitButton(8, new ItemStack(Material.GOLDEN_HELMET), ChatColor.GREEN.toString() + ChatColor.BOLD + "Paladin Class"),
                new KitButton(17, new ItemStack(Material.GOLDEN_CHESTPLATE), ChatColor.GREEN.toString() + ChatColor.BOLD + "Paladin Class"),
                new KitButton(26, new ItemStack(Material.GOLDEN_LEGGINGS), ChatColor.GREEN.toString() + ChatColor.BOLD + "Paladin Class"),
                new KitButton(35, new ItemStack(Material.GOLDEN_BOOTS), ChatColor.GREEN.toString() + ChatColor.BOLD + "Paladin Class")});
        this.newPlayer = newPlayer;
    }

    public boolean getNewPlayer(){
        return newPlayer;
    }

}