package net.betterpvp.clans.economy.shops.mysql;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.economy.shops.ShopManager;
import net.betterpvp.clans.economy.shops.menu.buttons.DynamicShopItem;
import net.betterpvp.core.database.*;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShopRepository implements Repository<Clans> {

    private static String TABLE_NAME;
    private static String CREATE_SHOP_TABLE;


    @Override
    public void initialize() {
        TABLE_NAME = Clans.getOptions().getTablePrefix() + "_shops";
        CREATE_SHOP_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                "  `Store` varchar(255)," +
                "  `Material` VARCHAR(255)," +
                "   `Data` int(5)," +
                "  `Slot` int(5)," +
                "  `Amount` int(5)," +
                "  `BuyPrice` int(255)," +
                "  `SellPrice` int(255)," +
                "  `ItemName` varchar(255)," +
                "  `Legendary` tinyint(1)," +
                "  `Glow` tinyint(1)," +
                "  `Dynamic` tinyint(1)," +
                "  `Quest` tinyint(1)," +
                "  `MinSellPrice` int(255)," +
                "  `BaseSellPrice` int(255)," +
                "  `MaxSellPrice` int(255)," +
                "  `MinBuyPrice` int(255)," +
                "  `BaseBuyPrice` int(255)," +
                "  `MaxBuyPrice` int(255)," +
                "  `BaseStock` int(255)," +
                "  `MaxStock` int(255)," +
                "  `CurrentStock` int(255)" +
                ")";
        QueryFactory.runQuery(CREATE_SHOP_TABLE);
    }

    public static void updateStock(DynamicShopItem s) {
        String query = "UPDATE " + TABLE_NAME + " set currentStock='" + s.getCurrentStock() + "' WHERE Store='" + s.getStore() + "' AND ItemName ='" + s.getItemName() + "'";
        QueryFactory.runQuery(query);
    }

    public static void loadShops(Clans clans) {
        new BukkitRunnable() {

            @Override
            public void run() {
                int count = 0;

                try {
                    ShopManager.getShopItems().clear();
                    PreparedStatement statement = Connect.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME);
                    ResultSet result = statement.executeQuery();

                    while (result.next()) {
                        String store = result.getString(1);
                        Material mat = Material.getMaterial(result.getString(2));
                        if(mat == null){
                            System.out.println("Failed to load shop item " + result.getString(2));
                        }
                        byte data = (byte) result.getInt(3);
                        int slot = result.getInt(4);
                        int amount = result.getInt(5);
                        int buyPrice = result.getInt(6);
                        int sellPrice = result.getInt(7);
                        String itemName = result.getString(8);
                        boolean legendary = result.getBoolean(9);
                        boolean glow = result.getBoolean(10);
                        boolean dynamic = result.getBoolean(11);
                        boolean quest = result.getBoolean(12);
                        int minSell = result.getInt(13);
                        int baseSell = result.getInt(14);
                        int maxSell = result.getInt(15);
                        int minBuy = result.getInt(16);
                        int baseBuy = result.getInt(17);
                        int maxBuy = result.getInt(18);
                        int baseStock = result.getInt(19);
                        int maxStock = result.getInt(20);
                        int currentStock = result.getInt(21);

                        ShopManager.addItem(store, itemName, mat, slot, data, amount, buyPrice, sellPrice, legendary,
                                glow, dynamic, quest, minSell, baseSell, maxSell, minBuy, baseBuy, maxBuy, baseStock, maxStock, currentStock);
                        count++;
                    }

                    statement.close();
                    result.close();


                    Log.debug("MySQL", "Loaded " + count + " Shop Items");

                } catch (SQLException ex) {
                    Log.debug("Connection", "Could not load Shops (Connection Error), ");
                    ex.printStackTrace();
                }

            }


        }.runTaskAsynchronously(clans);
    }


    @Override
    public void load(Clans clans) {
        loadShops(clans);
    }


    @Override
    public LoadPriority getLoadPriority() {

        return LoadPriority.HIGH;
    }
}