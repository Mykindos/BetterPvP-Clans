package net.betterpvp.clans.economy.shops.menu.buttons;

import org.bukkit.ChatColor;

import java.text.NumberFormat;

public class NormalShopItem extends ShopItem {

    private int buyPrice;
    private int sellPrice;

    public NormalShopItem(String store, int itemID, byte data, int slot,
                          int amount, String itemName, int buyPrice, int sellPrice) {
        super(store, itemID, data, slot, amount, itemName,
                ChatColor.GRAY + "Buy Price: " + ChatColor.YELLOW + NumberFormat.getInstance().format(buyPrice),
                ChatColor.GRAY + "Sell Price: " + ChatColor.YELLOW + NumberFormat.getInstance().format(sellPrice),
                "",
                ChatColor.GRAY + "Shift Left Click: " + ChatColor.YELLOW + "Buy 64",
                ChatColor.GRAY + "Shift Right Click: " + ChatColor.YELLOW + "Sell 64");
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public int getSellPrice() {
        return sellPrice;
    }

}
