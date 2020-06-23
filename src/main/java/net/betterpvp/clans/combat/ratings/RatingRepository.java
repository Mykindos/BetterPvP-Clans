package net.betterpvp.clans.combat.ratings;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.database.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RatingRepository implements Repository<Clans> {

    private static String TABLE_NAME = "_ratings";
    private static String CREATE_TABLE = "";

    @Override
    public void initialize() {
        TABLE_NAME = Clans.getOptions().getTablePrefix() + TABLE_NAME;
        CREATE_TABLE =  "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (" +
                "UUID varchar(255)," +
                "Class varchar(255)," +
                "Rating int," +
                "unique clans_rat_pk (UUID, Class)" +
                ")";
        QueryFactory.runQuery(CREATE_TABLE);
    }

    @Override
    public void load(Clans clans) {
        for (Gamer gamer : GamerManager.getGamers()) {
            try {
                PreparedStatement statement = Connect.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE UUID = '" + gamer.getClient().getUUID() + "'");
                ResultSet result = statement.executeQuery();

                while (result.next()) {
                    String role = result.getString(2);
                    int rating = result.getInt(3);
                    gamer.getRatings().put(role, rating);
                }

                statement.close();
                result.close();


            } catch (SQLException ex) {
                Log.debug("Connection", "Could not load Ratings (Connection Error)");
                ex.printStackTrace();
            }
        }
    }


    @Override
    public LoadPriority getLoadPriority() {
        return LoadPriority.HIGHEST;
    }

    public static void updateRating(Gamer gamer, String role) {
        String query = "UPDATE " + TABLE_NAME + " SET Rating = " + gamer.getRatings().get(role) + " WHERE UUID ='" + gamer.getClient().getUUID() + "' AND Class ='" + role + "'";
        QueryFactory.runQuery(query);
    }

    public static void saveRatings(Gamer gamer){
        String query = "INSERT IGNORE INTO " + TABLE_NAME + " VALUES ";
        for(Role role : Role.roles){
            query += "('" + gamer.getUUID().toString() + "', '" + role.getName() + "', " + 1500 + "),";
        }

        query = query.substring(0, query.length() -1);
        QueryFactory.runQuery(query);
    }
}
