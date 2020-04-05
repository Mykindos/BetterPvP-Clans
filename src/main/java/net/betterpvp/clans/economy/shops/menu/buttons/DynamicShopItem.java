package net.betterpvp.clans.economy.shops.menu.buttons;

import net.betterpvp.core.utility.UtilFormat;
import net.betterpvp.core.utility.UtilItem;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunctionLagrangeForm;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class DynamicShopItem extends ShopItem {

    private int minSellPrice, baseSellPrice, maxSellPrice, minBuyPrice, baseBuyPrice, maxBuyPrice;
    private int minStock, baseStock, maxStock, currentStock;
    private PolynomialFunctionLagrangeForm buyPolynomial;
    private PolynomialFunctionLagrangeForm sellPolynomial;


    public DynamicShopItem(String store, Material mat, byte data, int slot,
                           int amount, String itemName, int minBuyPrice, int baseBuyPrice, int maxBuyPrice,
                           int minSellPrice, int baseSellPrice, int maxSellPrice,
                           int baseStock, int maxStock, int currentStock) {
        super(store, mat, data, slot, amount, itemName);

        this.minBuyPrice = minBuyPrice;
        this.baseBuyPrice = baseBuyPrice;
        this.maxBuyPrice = maxBuyPrice;

        this.minSellPrice = minSellPrice;
        this.baseSellPrice = baseSellPrice;
        this.maxSellPrice = maxSellPrice;

        this.minStock = 0;
        this.baseStock = baseStock;
        this.currentStock = currentStock;
        this.maxStock = maxStock;

        this.buyPolynomial = new PolynomialFunctionLagrangeForm(new double[]{0, baseStock, maxStock}, new double[]{maxBuyPrice, baseBuyPrice, minBuyPrice});
        this.sellPolynomial = new PolynomialFunctionLagrangeForm(new double[]{maxStock, baseStock, 0}, new double[]{minSellPrice, baseSellPrice, maxSellPrice});
    }


    public void updateItem() {
        UtilItem.setItemNameAndLore(getItemStack(), ChatColor.YELLOW + UtilFormat.cleanString(getItemName()),
                new String[]{
                        ChatColor.GRAY + "Buy Price: " + ChatColor.YELLOW + (int) buyPolynomial.value(getCurrentStock()),
                        ChatColor.GRAY + "Sell Price: " + ChatColor.YELLOW + (int) sellPolynomial.value(getCurrentStock()),
                        "",
                        ChatColor.GRAY + "Shift Left Click: " + ChatColor.YELLOW + "Buy 64",
                        ChatColor.GRAY + "Shift Right Click: " + ChatColor.YELLOW + "Sell 64"
                });
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int i) {
        this.currentStock = i;
    }

    public int getBuyPrice() {
        return (int) buyPolynomial.value(getCurrentStock());
    }

    public int getSellPrice() {
        return (int) sellPolynomial.value(getCurrentStock());
    }


    public int getMinStock() {
        return minStock;
    }

    public int getBaseStock() {
        return baseStock;
    }

    public int getMaxStock() {
        return maxStock;
    }

    public int getMinSellPrice() {
        return minSellPrice;
    }

    public int getBaseSellPrice() {
        return baseSellPrice;
    }

    public int getMaxSellPrice() {
        return maxSellPrice;
    }

    public int getMinBuyPrice() {
        return minBuyPrice;
    }

    public int getBaseBuyPrice() {
        return baseBuyPrice;
    }

    public int getMaxBuyPrice() {
        return maxBuyPrice;
    }

}
