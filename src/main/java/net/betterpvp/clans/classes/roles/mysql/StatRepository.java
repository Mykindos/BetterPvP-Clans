package net.betterpvp.clans.classes.roles.mysql;


import net.betterpvp.clans.Clans;
import net.betterpvp.core.database.LoadPriority;
import net.betterpvp.core.database.QueryFactory;
import net.betterpvp.core.database.Repository;

public class StatRepository implements Repository<Clans> {

    public static final String TABLE_NAME = "kitmap_class_stats";

    public static void addClassStat(String name) {
        String query = "UPDATE " + TABLE_NAME + " set Count = Count+1 WHERE Class = '" + name + "'";
        QueryFactory.runQuery(query);
    }


    @Override
    public void initialize() {

    }

    @Override
    public void load(Clans clans) {

    }

    @Override
    public LoadPriority getLoadPriority() {
        return LoadPriority.HIGHEST;
    }
}
