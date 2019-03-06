package net.betterpvp.clans.economy.shops.menu.buttons;


import net.betterpvp.clans.weapon.WeaponManager;

public class LegendaryShopItem extends ShopItem {

    private boolean glow;
    private int buyPrice;

    public LegendaryShopItem(String store, int itemID, byte data, int slot,
                             int amount, int buyPrice, String itemName, boolean glow) {
        super(store, itemID, data, slot, amount, itemName,
                glow ? WeaponManager.getWeapon(itemName).createWeapon() : WeaponManager.getWeapon(itemName).createWeaponNoGlow(),
                WeaponManager.getWeapon(itemName).getLoreWithPrice(buyPrice));
        this.buyPrice = buyPrice;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    @Override
    public int getSellPrice() {
        // TODO Auto-generated method stub
        return 0;
    }

}
