package net.betterpvp.clans.gamer.mysql;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.database.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerStatRepository implements Repository<Clans> {

    private static String TABLE_NAME = "_playerstats";

    @Override
    public void initialize() {
        TABLE_NAME = Clans.getOptions().getTablePrefix() + TABLE_NAME;
        String createQuery = "CREATE TABLE IF NOT EXISTS `" + TABLE_NAME + "` ("
                + "UUID varchar(255) not null,"
                + "Stat varchar(255) not null,"
                + "Value double default 0 not null,"
                + "UNIQUE clans_playerstats_pk (UUID, Stat))";

        QueryFactory.runQuery(createQuery);
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
                        UUID uuid = UUID.fromString(result.getString(1));
                        String key = result.getString(2);
                        double value = result.getInt(3);

                        Gamer gamer = GamerManager.getGamer(uuid);
                        if(gamer != null){
                            gamer.getPlayerStats().put(key, value);
                        }


                        count++;
                    }




                    statement.close();
                    result.close();


                    Log.debug("MySQL", "Loaded " + count + " player stats");

                } catch (SQLException ex) {
                    Log.debug("Connection", "Could not load player stats (Connection Error), ");
                    ex.printStackTrace();
                }
            }


        }.runTaskAsynchronously(clans);
    }

    @Override
    public LoadPriority getLoadPriority() {
        return LoadPriority.HIGHEST;
    }

    public static void saveStat(UUID uuid, String key, int value){
        String query = "INSERT IGNORE INTO `" + TABLE_NAME + "` VALUES ('" + uuid.toString() + "', '" + key + "', " + value + ")";
        QueryFactory.runQuery(query);
    }

    public static void updateStat(UUID uuid, String key, int value){
        String query = "UPDATE `" + TABLE_NAME + "` SET Value = " + value + " WHERE Stat='" + key + "' AND UUID ='" + uuid.toString() + "'";
        QueryFactory.runQuery(query);
    }

    public static void updateAllStats(Gamer gamer){
        List<String> queries = new ArrayList<>();
        for (Map.Entry<String, Double> entry : gamer.getPlayerStats().entrySet()) {
            String query = "UPDATE `" + TABLE_NAME + "` SET Value = " + entry.getValue() + " WHERE Stat ='" + entry.getKey() + "' AND UUID='" + gamer.getUUID() + "'";
            queries.add(query);
        }

        QueryFactory.runTransaction(queries);
    }

}
