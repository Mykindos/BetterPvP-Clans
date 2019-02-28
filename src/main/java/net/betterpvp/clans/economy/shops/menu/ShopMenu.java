package net.betterpvp.clans.economy.shops.menu;

import net.betterpvp.clans.economy.shops.ShopManager;
import net.betterpvp.clans.economy.shops.menu.buttons.DynamicShopItem;
import net.betterpvp.clans.economy.shops.menu.buttons.ShopItem;
import net.betterpvp.core.interfaces.Button;
import net.betterpvp.core.interfaces.Menu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;


public class ShopMenu extends Menu {

    private String name;

    public ShopMenu(String name, Player p) {
        super(p, 54, ChatColor.GREEN.toString() + ChatColor.BOLD + name, new Button[]{});
        this.name = name;
        loadShop();
        construct();
    }

    public void loadShop() {
        List<ShopItem> items = ShopManager.getItemsFor(name);
        for (ShopItem i : items) {
            if (i instanceof DynamicShopItem) {
                DynamicShopItem di = (DynamicShopItem) i;
                di.updateItem();

            }
            addButton(i);
        }
    }

}
