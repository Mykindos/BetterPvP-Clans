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
                new KitButton(0, new ItemStack(Material.DIAMOND_HELMET).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Gladiator Class"),
                new KitButton(9, new ItemStack(Material.DIAMOND_CHESTPLATE).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Gladiator Class"),
                new KitButton(18, new ItemStack(Material.DIAMOND_LEGGINGS).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Gladiator Class"),
                new KitButton(27, new ItemStack(Material.DIAMOND_BOOTS).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Gladiator Class"),

                new KitButton(1, new ItemStack(Material.LEATHER_HELMET).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Assassin Class"),
                new KitButton(10, new ItemStack(Material.LEATHER_CHESTPLATE).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Assassin Class"),
                new KitButton(19, new ItemStack(Material.LEATHER_LEGGINGS).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Assassin Class"),
                new KitButton(28, new ItemStack(Material.LEATHER_BOOTS).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Assassin Class"),

                new KitButton(3, new ItemStack(Material.IRON_HELMET).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Knight Class"),
                new KitButton(12, new ItemStack(Material.IRON_CHESTPLATE).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Knight Class"),
                new KitButton(21, new ItemStack(Material.IRON_LEGGINGS).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Knight Class"),
                new KitButton(30, new ItemStack(Material.IRON_BOOTS).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Knight Class"),

                new KitButton(5, new ItemStack(Material.TURTLE_HELMET).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Warlock Class"),
                new KitButton(14, new ItemStack(Material.IRON_CHESTPLATE).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Warlock Class"),
                new KitButton(23, new ItemStack(Material.IRON_LEGGINGS).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Warlock Class"),
                new KitButton(32, new ItemStack(Material.IRON_BOOTS).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Warlock Class"),

                new KitButton(7, new ItemStack(Material.CHAINMAIL_HELMET).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Ranger Class"),
                new KitButton(16, new ItemStack(Material.CHAINMAIL_CHESTPLATE).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Ranger Class"),
                new KitButton(25, new ItemStack(Material.CHAINMAIL_LEGGINGS).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Ranger Class"),
                new KitButton(34, new ItemStack(Material.CHAINMAIL_BOOTS).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Ranger Class"),

                new KitButton(8, new ItemStack(Material.GOLDEN_HELMET).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Paladin Class"),
                new KitButton(17, new ItemStack(Material.GOLDEN_CHESTPLATE).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Paladin Class"),
                new KitButton(26, new ItemStack(Material.GOLDEN_LEGGINGS).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Paladin Class"),
                new KitButton(35, new ItemStack(Material.GOLDEN_BOOTS).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Paladin Class")});
        this.newPlayer = newPlayer;
    }

    public boolean getNewPlayer(){
        return newPlayer;
    }

}