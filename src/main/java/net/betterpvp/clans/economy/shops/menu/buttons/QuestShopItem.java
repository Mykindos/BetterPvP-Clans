package net.betterpvp.clans.economy.shops.menu.buttons;

import net.betterpvp.clans.dailies.perks.QuestPerkManager;

public class QuestShopItem extends ShopItem {

    public QuestShopItem(String store, int itemID, byte data, int slot,
                         int amount, String itemName, int buyPrice) {
        super(store, itemID, data, slot, amount, itemName, QuestPerkManager.getPerk(itemName).getDescription(buyPrice));
        this.buyPrice = buyPrice;
    }

    private int buyPrice;


    public int getBuyPrice() {
        return buyPrice;
    }

    @Override
    public int getSellPrice() {

        return 0;
    }

}
