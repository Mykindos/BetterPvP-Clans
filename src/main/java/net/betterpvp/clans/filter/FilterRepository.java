package net.betterpvp.clans.filter;

import net.betterpvp.clans.Clans;
import net.betterpvp.core.database.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FilterRepository implements Repository<Clans> {

    private static String TABLE_NAME;
    private static String CREATE_FILTER_TABLE;

    public static List<String> CHAT_FILTER = new ArrayList<>();

    @Override
    public void initialize() {
        TABLE_NAME = Clans.getOptions().getTablePrefix() + "_filter";
        CREATE_FILTER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (Word varchar(255), CONSTRAINT armour_pk PRIMARY KEY (Word)); ";
        QueryFactory.runQuery(CREATE_FILTER_TABLE);
    }

    @Override
    public void load(Clans i) {
        new BukkitRunnable() {

            @Override
            public void run() {

                try {
                    PreparedStatement statement = Connect.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME);
                    ResultSet result = statement.executeQuery();

                    while (result.next()) {
                        String word = result.getString(1);
                        FilterRepository.CHAT_FILTER.add(word);

                    }

                    statement.close();
                    result.close();


                } catch (SQLException ex) {
                    Log.debug("Connection", "Could not load Armour (Connection Error), ");
                    ex.printStackTrace();
                }
            }


        }.runTaskAsynchronously(i);
    }

    @Override
    public LoadPriority getLoadPriority() {
        return LoadPriority.HIGHEST;
    }
}
