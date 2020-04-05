package net.betterpvp.clans.skills.selector.page;

import net.betterpvp.core.interfaces.Button;
import net.betterpvp.core.interfaces.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ClassSelectionPage extends Menu {

    public ClassSelectionPage(Player player) {
        super(player, 36, "Class Customisation", new Button[]{
                new Button(0, new ItemStack(Material.DIAMOND_HELMET).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Gladiator Class"),
                new Button(9, new ItemStack(Material.DIAMOND_CHESTPLATE).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Gladiator Class"),
                new Button(18, new ItemStack(Material.DIAMOND_LEGGINGS).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Gladiator Class"),
                new Button(27, new ItemStack(Material.DIAMOND_BOOTS).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Gladiator Class"),

                new Button(2, new ItemStack(Material.LEATHER_HELMET).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Assassin Class"),
                new Button(11, new ItemStack(Material.LEATHER_CHESTPLATE).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Assassin Class"),
                new Button(20, new ItemStack(Material.LEATHER_LEGGINGS).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Assassin Class"),
                new Button(29, new ItemStack(Material.LEATHER_BOOTS).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Assassin Class"),

                new Button(4, new ItemStack(Material.IRON_HELMET).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Knight Class"),
                new Button(13, new ItemStack(Material.IRON_CHESTPLATE).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Knight Class"),
                new Button(22, new ItemStack(Material.IRON_LEGGINGS).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Knight Class"),
                new Button(31, new ItemStack(Material.IRON_BOOTS).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Knight Class"),

                new Button(6, new ItemStack(Material.CHAINMAIL_HELMET).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Ranger Class"),
                new Button(15, new ItemStack(Material.CHAINMAIL_CHESTPLATE).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Ranger Class"),
                new Button(24, new ItemStack(Material.CHAINMAIL_LEGGINGS).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Ranger Class"),
                new Button(33, new ItemStack(Material.CHAINMAIL_BOOTS).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Ranger Class"),

                new Button(8, new ItemStack(Material.GOLDEN_HELMET).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Paladin Class"),
                new Button(17, new ItemStack(Material.GOLDEN_CHESTPLATE).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Paladin Class"),
                new Button(26, new ItemStack(Material.GOLDEN_LEGGINGS).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Paladin Class"),
                new Button(35, new ItemStack(Material.GOLDEN_BOOTS).clone(), ChatColor.GREEN.toString() + ChatColor.BOLD + "Paladin Class")});
    }

}