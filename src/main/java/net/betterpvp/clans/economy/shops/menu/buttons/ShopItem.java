package net.betterpvp.clans.economy.shops.menu.buttons;

import net.betterpvp.core.interfaces.Button;
import net.betterpvp.core.utility.UtilFormat;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.List;


public abstract class ShopItem extends Button {

    private String store, itemName;
    private int itemID, slot, amount;
    private byte data;

    @SuppressWarnings("deprecation")
    public ShopItem(String store, int itemID, byte data, int slot, int amount, String itemName) {
        super(slot, data > 0 ? new ItemStack(itemID, amount, data) : new ItemStack(itemID, amount),
                ChatColor.YELLOW + UtilFormat.cleanString(itemName));
        this.store = store;
        this.itemID = itemID;
        this.data = data;
        this.slot = slot;
        this.amount = amount;
        this.itemName = itemName;
    }

    public ShopItem(String store, int itemID, byte data, int slot, int amount, String itemName, String... lore) {
        super(slot, data > 0 ? new ItemStack(itemID, amount, data) : new ItemStack(itemID, amount),
                ChatColor.YELLOW + UtilFormat.cleanString(itemName),
                lore);
        this.store = store;
        this.itemID = itemID;
        this.data = data;
        this.slot = slot;
        this.amount = amount;
        this.itemName = itemName;
    }

    public ShopItem(String store, int itemID, byte data, int slot, int amount, String itemName, ItemStack stack, List<String> list) {
        super(slot, stack,
                ChatColor.YELLOW + UtilFormat.cleanString(itemName),
                list);
        this.store = store;
        this.itemID = itemID;
        this.data = data;
        this.slot = slot;
        this.amount = amount;
        this.itemName = itemName;
        System.out.println(itemName);
    }

    public abstract int getBuyPrice();

    public abstract int getSellPrice();

    public String getStore() {
        return store;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemID() {
        return itemID;
    }

    public int getSlot() {
        return slot;
    }

    public int getAmount() {
        return amount;
    }

    public byte getData() {
        return data;
    }

}
