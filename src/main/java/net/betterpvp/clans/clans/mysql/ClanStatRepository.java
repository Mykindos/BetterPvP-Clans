package net.betterpvp.clans.clans.mysql;

import net.betterpvp.clans.Clans;
import net.betterpvp.core.database.LoadPriority;
import net.betterpvp.core.database.QueryFactory;
import net.betterpvp.core.database.Repository;

public class ClanStatRepository implements Repository<Clans> {


    public static final String TABLE_NAME = "clans_class_stats";

    public static String CREATE_CLASSSTAT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " "
            + "(Class VARCHAR(14), "
            + "Count int(10)); ";

    @Override
    public void initialize() {
        QueryFactory.runQuery(CREATE_CLASSSTAT_TABLE);

    }

    @Override
    public void load(Clans clans) {

    }

    @Override
    public LoadPriority getLoadPriority() {
        return LoadPriority.HIGHEST;
    }
}