package net.betterpvp.clans.economy.shops.menu.buttons;


import net.betterpvp.clans.weapon.WeaponManager;
import org.bukkit.Material;

public class LegendaryShopItem extends ShopItem {

    private int buyPrice;
    private int sellPrice;

    public LegendaryShopItem(String store, Material mat, byte data, int slot,
                             int amount, int buyPrice, int sellPrice, String itemName, boolean glow) {
        super(store, mat, data, slot, amount, itemName,
                glow ? WeaponManager.getWeapon(itemName).createWeapon() : WeaponManager.getWeapon(itemName).createWeaponNoGlow(),
                WeaponManager.getWeapon(itemName).getLoreWithPrice(store, buyPrice, sellPrice));
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    @Override
    public int getSellPrice() {

        return sellPrice;
    }

}
