package net.betterpvp.clans.farming.bee;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Alliance;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.core.database.Connect;
import net.betterpvp.core.database.LoadPriority;
import net.betterpvp.core.database.Log;
import net.betterpvp.core.database.QueryFactory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class BeeRepository {

    private static String TABLE_NAME;
    private static String CREATE_BEE_TABLE;


    public static void initialize() {
        TABLE_NAME = Clans.getOptions().getTablePrefix() + "_bees";
        CREATE_BEE_TABLE = "CREATE TABLE IF NOT EXISTS `" + TABLE_NAME + "` ("
                + "Clan VARCHAR(255), "
                + "World varchar(255),"
                + "X double,"
                + "Y double,"
                + "Z double); ";
        QueryFactory.runQuery(CREATE_BEE_TABLE);
    }


    public static void load(Clans i) {
        initialize();
        new BukkitRunnable() {

            @Override
            public void run() {
                int count = 0;
                try {
                    PreparedStatement statement = Connect.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME);
                    ResultSet result = statement.executeQuery();

                    while (result.next()) {
                        Clan clan = ClanUtilities.getClan(result.getString(1));
                        if(clan != null) {
                            String world = result.getString(2);
                            double x = result.getDouble(3);
                            double y = result.getDouble(4);
                            double z = result.getDouble(5);
                            Location loc = new Location(Bukkit.getWorld(world), x, y, z);

                            clan.getBeeData().add(new BeeData(loc));
                        }
                        count++;
                    }

                    statement.close();
                    result.close();


                    Log.debug("MySQL", "Linked " + count + " Beehives to Clans");

                } catch (SQLException ex) {
                    Log.debug("Connection", "Could not load Clan Bees (Connection Error), ");
                    ex.printStackTrace();
                }
            }


        }.runTaskAsynchronously(i);
    }

    public static void saveBeeData(Clan clan, BeeData bee){
        String query = "INSERT INTO " + TABLE_NAME + " VALUES ('" + clan.getName() + "', '" + bee.getLoc().getWorld().getName()
                + "', " + bee.getLoc().getX() + ", " + bee.getLoc().getY() + ", " + bee.getLoc().getZ() + ");";
        QueryFactory.runQuery(query);
    }

    public static void removeBeeData(Location loc){
        String query = "DELETE FROM " + TABLE_NAME + " WHERE World='" + loc.getWorld().getName() + "' AND X='" + loc.getX() + "' AND Y='" + loc.getY() + "' AND Z='" + loc.getZ() + "';";
        QueryFactory.runQuery(query);
    }

    public static void wipeBees(Clan clan){
        String query = "DELETE FROM " + TABLE_NAME + " WHERE Clan='" + clan.getName() + "';";
        QueryFactory.runQuery(query);
    }


    public LoadPriority getLoadPriority() {
        return LoadPriority.HIGHEST;
    }
}
