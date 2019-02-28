package net.betterpvp.clans.economy.shops.menu;

import net.betterpvp.clans.economy.shops.Shop;
import net.betterpvp.clans.economy.shops.ShopManager;
import net.betterpvp.core.interfaces.Button;
import net.betterpvp.core.interfaces.Menu;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PortableShopMenu extends Menu {

    public PortableShopMenu(Player player) {
        super(player, 9, ChatColor.RED + "Portable Shop", new Button[]{});
        int i = 0;
        for (Shop s : ShopManager.getShops()) {
            if (s.getName().equalsIgnoreCase("Travel Hub") || s.getName().equalsIgnoreCase("Boss Teleport")) continue;
            addButton(new Button(i, new ItemStack(Material.GOLD_INGOT), ChatColor.GREEN + s.getName()));
            i++;
        }
        construct();
        // TODO Auto-generated constructor stub
    }

}
