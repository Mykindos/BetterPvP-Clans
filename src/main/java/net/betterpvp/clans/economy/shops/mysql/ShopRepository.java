package net.betterpvp.clans.economy.shops.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.economy.shops.ShopManager;
import net.betterpvp.clans.economy.shops.menu.buttons.DynamicShopItem;
import net.betterpvp.core.database.*;
import org.bukkit.scheduler.BukkitRunnable;

public class ShopRepository implements Repository<Clans> {
	
	public static final String TABLE_NAME = "kitmap_shops";

	
	public static void updateStock(DynamicShopItem s){
		String query = "UPDATE " + TABLE_NAME + " set currentStock='" + s.getCurrentStock() + "' WHERE Store='" + s.getStore() + "' AND ItemName ='" + s.getItemName() + "'";
		QueryFactory.runQuery(query);
	}

	public static void loadShops(Clans clans){
		new BukkitRunnable(){

			@Override
			public void run() {
				int count = 0;

				try {
					ShopManager.getShopItems().clear();
					PreparedStatement statement = Connect.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME);
					ResultSet result = statement.executeQuery();

					while (result.next()) {
						String store = result.getString(1);
						int ID = result.getInt(2);
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
						ShopManager.addItem(store, itemName, ID, slot, data, amount, buyPrice, sellPrice, legendary,
								glow, dynamic, quest, minSell, baseSell, maxSell, minBuy, baseBuy, maxBuy, baseStock, maxStock, currentStock);
						count++;
					}

					statement.close();
					result.close();


					Log.debug("MySQL", "Loaded " + count + " Shop Items");
					ShopKeeperRepository.loadShopKeepers(clans);

				} catch (SQLException ex) {
					Log.debug("Connection", "Could not load Shops (Connection Error), ");
					ex.printStackTrace();
				}

			}


		}.runTaskAsynchronously(clans);
	}

	@Override
	public void initialize() {

	}

	@Override
	public void load(Clans clans) {
		loadShops(clans);
	}



	@Override
	public LoadPriority getLoadPriority() {
		return null;
	}
}
