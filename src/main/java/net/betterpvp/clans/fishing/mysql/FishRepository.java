package net.betterpvp.clans.fishing.mysql;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.fishing.Fish;
import net.betterpvp.clans.fishing.FishManager;
import net.betterpvp.core.database.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FishRepository implements Repository<Clans> {
    public static final String TABLE_NAME = "clans_fish";

    public static String CREATE_FISH_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " "
            + "(Player VARCHAR(255), "
            + "Size INT, "
            + "Name VARCHAR(255),"
            + "SystemTime LONG); ";

    @Override
    public void initialize() {
        QueryFactory.runQuery(CREATE_FISH_TABLE);
    }

    @Override
    public void load(Clans clans) {
        new BukkitRunnable(){

            @Override
            public void run() {
                int count = 0;
                try {
                    PreparedStatement statement = Connect.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME);
                    ResultSet result = statement.executeQuery();

                    while (result.next()) {
                        String name = result.getString(1);
                        int size = result.getInt(2);
                        String fish = result.getString(3);
                        long systemTime = Long.valueOf(result.getLong(4));

                        FishManager.addFish(new Fish(name, size, fish, systemTime));
                        count++;
                    }
                    statement.close();
                    result.close();

                    Log.debug("MySQL", "Loaded " + count + " Fish");

                } catch (SQLException ex) {
                    Log.debug("Connection", "Could not load Fish (Connection Error), ");
                    ex.printStackTrace();
                }


            }

        }.runTaskAsynchronously(clans);
    }


    public static void saveFish(Fish f) {

        String query = "INSERT INTO " + TABLE_NAME + " (Player, Size, Name, SystemTime) VALUES "
                + "('" + f.getPlayerName() + "', "
                + "'" + f.getSize() + "',"
                + "'" + f.getFishName() + "', "
                + "'" + f.getSystemTime() + "')";
        QueryFactory.runQuery(query);
    }


    @Override
    public LoadPriority getLoadPriority() {
        return LoadPriority.HIGHEST;
    }
}