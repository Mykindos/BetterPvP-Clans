package net.betterpvp.clans.economy.shops.menu;

import net.betterpvp.core.interfaces.Button;
import net.betterpvp.core.interfaces.Menu;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TravelHubMenu extends Menu {

    public TravelHubMenu(Player player) {
        super(player, 45, ChatColor.GREEN + "Travel Hub", new Button[]{});
        addButton(new Button(20, new ItemStack(Material.REDSTONE), ChatColor.RED + "Red Shop", ChatColor.WHITE + "Teleport to the Red Shops"));
        addButton(new Button(24, new ItemStack(Material.DIAMOND), ChatColor.AQUA + "Blue Shop", ChatColor.WHITE + "Teleport to the Blue Shops"));

        addButton(new Button(13, new ItemStack(Material.WOOL, 1, (byte) 11), ChatColor.AQUA + "Blue Spawn", ChatColor.WHITE + "Teleport to the Blue Spawn"));
        addButton(new Button(31, new ItemStack(Material.WOOL, 1, (byte) 14), ChatColor.RED + "Red Spawn", ChatColor.WHITE + "Teleport to the Red Spawn"));
        // TODO Auto-generated constructor stub

        // TODO Auto-generated constructor stub

        construct();
    }

}
