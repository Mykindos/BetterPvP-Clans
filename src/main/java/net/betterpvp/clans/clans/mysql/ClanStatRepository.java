package net.betterpvp.clans.clans.mysql;

import net.betterpvp.clans.Clans;
import net.betterpvp.core.database.LoadPriority;
import net.betterpvp.core.database.QueryFactory;
import net.betterpvp.core.database.Repository;

public class ClanStatRepository implements Repository<Clans> {


    public String TABLE_NAME;

    public String CREATE_CLASSSTAT_TABLE;

    @Override
    public void initialize() {
        TABLE_NAME = Clans.getOptions().getTablePrefix() + "_class_stats";
        CREATE_CLASSSTAT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " "
                + "(Class VARCHAR(14), "
                + "Count int(10)); ";
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