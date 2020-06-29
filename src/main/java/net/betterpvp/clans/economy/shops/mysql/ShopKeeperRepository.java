package net.betterpvp.clans.economy.shops.mysql;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.economy.shops.Shop;
import net.betterpvp.clans.economy.shops.ShopManager;
import net.betterpvp.core.database.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShopKeeperRepository implements Repository<Clans> {

    private static String TABLE_NAME;

    private String CREATE_SHOPKEEPER_TABLE;

    @Override
    public void initialize() {
        TABLE_NAME = Clans.getOptions().getTablePrefix() + "_shopkeepers";
        CREATE_SHOPKEEPER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                "  `World` varchar(255)," +
                "  `Name` varchar(255)," +
                "  `X` double(10,2)," +
                "  `Y` double(10,2)," +
                "  `Z` double(10,2)" +
                ") ";
        QueryFactory.runQuery(CREATE_SHOPKEEPER_TABLE);
    }

    @Override
    public void load(Clans clans) {
        loadShopKeepers(clans);
    }

    public static void loadShopKeepers(final Clans i) {

        for (World w : Bukkit.getWorlds()) {
            for (LivingEntity e : w.getLivingEntities()) {
                if (e instanceof Player || e instanceof ArmorStand) continue;

                for (Shop s : ShopManager.getShops()) {
                    if (e.getCustomName() != null) {

                        if (s.getName().equalsIgnoreCase(ChatColor.stripColor(e.getCustomName()))) {

                            e.setHealth(0);
                            e.remove();
                        }
                    }

                }
            }

        }
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
