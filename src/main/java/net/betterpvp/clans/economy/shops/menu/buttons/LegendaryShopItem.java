package net.betterpvp.clans.economy.shops.menu.buttons;


import net.betterpvp.clans.weapon.WeaponManager;
import org.bukkit.Material;

public class LegendaryShopItem extends ShopItem {

    private boolean glow;
    private int buyPrice;

    public LegendaryShopItem(String store, Material mat, byte data, int slot,
                             int amount, int buyPrice, String itemName, boolean glow) {
        super(store, mat, data, slot, amount, itemName,
                glow ? WeaponManager.getWeapon(itemName).createWeapon() : WeaponManager.getWeapon(itemName).createWeaponNoGlow(),
                WeaponManager.getWeapon(itemName).getLoreWithPrice(store, buyPrice));
        this.buyPrice = buyPrice;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    @Override
    public int getSellPrice() {

        return 0;
    }

}
