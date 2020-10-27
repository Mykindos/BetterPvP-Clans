package net.betterpvp.clans.cosmetics.menu.menus;

import net.betterpvp.core.interfaces.Button;
import net.betterpvp.core.interfaces.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CosmeticMenu extends Menu {

    public CosmeticMenu(Player player) {
        super(player, 9, ChatColor.RED.toString() + ChatColor.BOLD + "  -  Cosmetic menu  -  ", new Button[]{});
        fillPage();
        construct();
    }

    private void fillPage(){
        addButton(new Button(0, new ItemStack(Material.RED_DYE), ChatColor.GREEN + "Particle Effects", ChatColor.GRAY + "View Owned / Select active particle effects"));
        addButton(new Button(2, new ItemStack(Material.DIAMOND_SWORD), ChatColor.GREEN + "Kill Effects",ChatColor.GRAY +  "View Owned / Select active kill effects"));
        addButton(new Button(4, new ItemStack(Material.CAKE), ChatColor.GREEN + "Death Effects", ChatColor.GRAY + "View Owned / Select active death effects"));
        addButton(new Button(6, new ItemStack(Material.ENDER_CHEST), ChatColor.GREEN + "Other Effects", ChatColor.GRAY + "View Owned / Select active misc effects"));
        addButton(new Button(8, new ItemStack(Material.ELYTRA), ChatColor.GREEN + "Wings", ChatColor.GRAY + "View Owned / Select active Wings"));
    }
}
