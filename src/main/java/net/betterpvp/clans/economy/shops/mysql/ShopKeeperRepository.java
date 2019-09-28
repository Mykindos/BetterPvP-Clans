package net.betterpvp.clans.economy.shops.mysql;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.economy.shops.ShopManager;
import net.betterpvp.core.database.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShopKeeperRepository implements Repository<Clans> {

    public static final String TABLE_NAME = "kitmap_shopkeepers";

    private static final String CREATE_SHOPKEEPER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            "  `Name` varchar(255)," +
            "  `X` double(10,2)," +
            "  `Y` double(10,2)," +
            "  `Z` double(10,2)" +
            ") ";

    @Override
    public void initialize() {
        QueryFactory.runQuery(CREATE_SHOPKEEPER_TABLE);
    }

    @Override
    public void load(Clans clans) {
        loadShopKeepers(clans);
    }

    public static void loadShopKeepers(final Clans i) {
        new BukkitRunnable() {

            @Override
            public void run() {
                int count = 0;
                try {
                    PreparedStatement statement = Connect.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME);
                    ResultSet result = statement.executeQuery();

                    while (result.next()) {
                        String world = result.getString(1);
                        String name = result.getString(2);
                        double x = result.getDouble(3);
                        double y = result.getDouble(4);
                        double z = result.getDouble(5);
                        ShopManager.spawnShop(i, new Location(Bukkit.getWorld(world), x, y, z), name);

                        count++;
                    }

                    statement.close();
                    result.close();


                    Log.debug("MySQL", "Loaded " + count + " Shop Keepers");

                } catch (SQLException ex) {
                    Log.debug("Connection", "Could not load Shops (Connection Error), ");
                    ex.printStackTrace();
                }
            }


        }.runTaskAsynchronously(i);
    }

    public static void addKeeper(String name, Location loc) {
        String query = "INSERT INTO " + TABLE_NAME + " (World, Name, X, Y, Z) VALUES ('" + loc.getWorld().getName() + "', '" + name + "', '" + loc.getX() + "', '" + loc.getY() + "', '" + loc.getZ() + "')";
        QueryFactory.runQuery(query);
    }


    @Override
    public LoadPriority getLoadPriority() {

        return LoadPriority.HIGHEST;
    }
}
