package net.betterpvp.clans.clans.mysql;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Alliance;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.core.database.*;
import net.betterpvp.core.utility.UtilFormat;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AllianceRepository {

    private static String TABLE_NAME;
    private static String CREATE_ALLY_TABLE;


    private static void initialize() {
        TABLE_NAME = Clans.getOptions().getTablePrefix() + "_alliances";
        CREATE_ALLY_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " "
                + "(Clan VARCHAR(16), "
                + "Other VARCHAR(16), "
                + "Trusted TINYINT); ";
        QueryFactory.runQuery(CREATE_ALLY_TABLE);
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
                        Clan target = ClanUtilities.getClan(result.getString(2));
                        boolean trusted = result.getBoolean(3);

                        if (clan != null && target != null) {
                            clan.getAlliances().add(new Alliance(target, trusted));
                            count++;
                        }
                    }

                    statement.close();
                    result.close();

                    Log.debug("MySQL", "Linked " + count + " alliances to Clans");

                } catch (SQLException ex) {
                    Log.debug("Connection", "Could not load Clan Alliances (Connection Error), ");
                    ex.printStackTrace();
                }
            }


        }.runTaskAsynchronously(i);

    }

    public static void saveAlly(Clan clan, Clan other, boolean trusted) {
        String query = "REPLACE INTO " + TABLE_NAME + " (Clan, Other, Trusted) VALUES "
                + "('" + clan.getName() + "', "
                + "'" + other.getName() + "', "
                + "'" + UtilFormat.toTinyInt(trusted) + "') ";
        QueryFactory.runQuery(query);
    }

    public static void updateAlly(Clan clan, Clan other) {
        String query = "UPDATE " + TABLE_NAME + " SET Trusted='" + UtilFormat.toTinyInt(clan.hasTrust(other)) + "' WHERE Clan='" + clan.getName() + "' AND Other='" + other.getName() + "'";
        QueryFactory.runQuery(query);
    }

    public static void deleteAlly(Clan clan, Clan other) {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE Clan='" + clan.getName() + "' AND Other='" + other.getName() + "' OR "
                + "Clan='" + other.getName() + "' AND Other='" + clan.getName() + "'";
        QueryFactory.runQuery(query);
    }

    public static void wipe() {
        String query = "TRUNCATE TABLE " + TABLE_NAME;
        QueryFactory.runQuery(query);
    }



}
