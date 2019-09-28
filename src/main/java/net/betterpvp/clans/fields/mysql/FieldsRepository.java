package net.betterpvp.clans.fields.mysql;

import net.betterpvp.clans.Clans;
import net.betterpvp.core.database.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;


public class FieldsRepository implements Repository<Clans> {

    public static final String TABLE_NAME = "clans_fields";
    public static HashMap<Location, Integer> blocks = new HashMap<>();
    private static World world = Bukkit.getWorld("world");

    public static final String CREATE_FIELDS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "  (" +
            "  `ID` int(10)," +
            "  `X` double(10, 0)," +
            "  `Y` double(10, 0)," +
            "  `Z` double(10, 0)" +
            ");";





    @Override
    public void initialize() {
        QueryFactory.runQuery(CREATE_FIELDS_TABLE);
    }

    @Override
    public void load(Clans clans) {
        new BukkitRunnable() {

            @Override
            public void run() {
                int count = 0;
                try {
                    PreparedStatement statement = Connect.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME);
                    ResultSet result = statement.executeQuery();

                    while (result.next()) {
                        int id = result.getInt(1);
                        double x = result.getDouble(2);
                        double y = result.getDouble(3);
                        double z = result.getDouble(4);
                        Location loc = new Location(world, x, y, z);
                        loc.getBlock().setTypeId(id);

                        blocks.put(loc, id);
                        count++;
                    }

                    statement.close();
                    result.close();


                    Log.debug("MySQL", "Loaded " + count + " ores");

                } catch (SQLException ex) {
                    Log.debug("Connection", "Could not load Ores (Connection Error), ");
                    ex.printStackTrace();
                }
            }


        }.runTask(clans);
    }

    @Override
    public LoadPriority getLoadPriority() {
        return LoadPriority.HIGHEST;
    }


    public static void saveOre(Block b) {

        String query = "INSERT INTO " + TABLE_NAME + " (ID, X, Y, Z) VALUES "
                + "('" + b.getTypeId() + "', "
                + "'" + b.getLocation().getX() + "', "
                + "'" + b.getLocation().getY() + "', "
                + "'" + b.getLocation().getZ() + "')";

        QueryFactory.runQuery(query);
    }

    public static void deleteOre(Block b) {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE X='" + b.getLocation().getX() + "' AND Y='"
                + b.getLocation().getY() + "' AND Z='" + b.getLocation().getZ() + "'";
        QueryFactory.runQuery(query);

    }

}
