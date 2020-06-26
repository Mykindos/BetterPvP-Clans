package net.betterpvp.clans.skills.mysql;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.RoleBuild;
import net.betterpvp.clans.skills.selector.SelectorManager;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.database.*;
import net.betterpvp.core.utility.UtilFormat;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


public class BuildRepository implements Repository<Clans> {

    public static final String TABLE_NAME = "clans_builds";

    private static final String CREATE_BUILDS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            "  `UUID` varchar(255) DEFAULT NULL," +
            "  `Role` varchar(255) DEFAULT NULL," +
            "  `ID` int(25) DEFAULT NULL," +
            "  `Sword` varchar(255) DEFAULT NULL," +
            "  `Axe` varchar(255) DEFAULT NULL," +
            "  `Bow` varchar(255) DEFAULT NULL," +
            "  `PassiveA` varchar(255) DEFAULT NULL," +
            "  `PassiveB` varchar(255) DEFAULT NULL," +
            "  `Global` varchar(255) DEFAULT NULL," +
            "  `Active` tinyint(1) DEFAULT NULL" +
            ")";

    @Override
    public void initialize() {
        QueryFactory.runQuery(CREATE_BUILDS_TABLE);
    }


    public static synchronized void loadBuilds(Clans i, final UUID uuid) {
        new BukkitRunnable() {

            @Override
            public void run() {
                int count = 0;
                try {
                    PreparedStatement statement = Connect.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME + " where UUID = '" + uuid.toString() + "'");
                    ResultSet result = statement.executeQuery();

                    Gamer g = GamerManager.getOnlineGamer(uuid);

                    if(g != null) {
                        while (result.next()) {
                            String role = result.getString(2);
                            int id = result.getInt(3);
                            RoleBuild build = new RoleBuild(role, id);

                            String sword = result.getString(4);
                            setSkill(build, Types.SWORD, sword);

                            String axe = result.getString(5);
                            setSkill(build, Types.AXE, axe);

                            String bow = result.getString(6);
                            setSkill(build, Types.BOW, bow);

                            String passiveA = result.getString(7);
                            setSkill(build, Types.PASSIVE_A, passiveA);

                            String passiveB = result.getString(8);
                            setSkill(build, Types.PASSIVE_B, passiveB);

                            String global = result.getString(9);
                            setSkill(build, Types.GLOBAL, global);

                            boolean active = result.getBoolean(10);
                            build.setActive(active);

                            g.getBuilds().add(build);
                            count++;
                        }

                    }

                    statement.close();
                    result.close();


                    Log.debug("MySQL", "Loaded " + count + " Builds");

                } catch (SQLException ex) {
                    Log.debug("Connection", "Could not load Punishments (Connection Error), ");
                    ex.printStackTrace();
                }
            }


        }.runTaskAsynchronously(i);

    }

    public static void saveBuild(UUID uuid, RoleBuild build) {
        String query = "INSERT INTO " + TABLE_NAME + " VALUES ('"
                + uuid.toString() + "', '" + build.getRole() + "', '" + build.getID() + "', "
                + (build.getSword() == null ? null + ", " : "'" + (build.getSword().getSkill().getName() + "," + build.getSword().getLevel() + "', "))
                + (build.getAxe() == null ? null + ", " : "'" + (build.getAxe().getSkill().getName() + "," + build.getAxe().getLevel() + "', "))
                + (build.getBow() == null ? null + ", " : "'" + (build.getBow().getSkill().getName() + "," + build.getBow().getLevel() + "', "))
                + (build.getPassiveA() == null ? null + ", " : "'" + (build.getPassiveA().getSkill().getName() + "," + build.getPassiveA().getLevel() + "', "))
                + (build.getPassiveB() == null ? null + ", " : "'" + (build.getPassiveB().getSkill().getName() + "," + build.getPassiveB().getLevel() + "', "))
                + (build.getGlobal() == null ? null + ", " : "'" + (build.getGlobal().getSkill().getName() + "," + build.getGlobal().getLevel() + "', "))
                + "'" + UtilFormat.toTinyInt(build.isActive()) + "')";
        QueryFactory.runQuery(query);

    }

    public static void updateBuild(UUID uuid, RoleBuild build) {

        String query = "UPDATE " + TABLE_NAME + " SET Sword=" + (build.getSword() == null ? null + ", " : "'" + (build.getSword().getSkill().getName() + "," + build.getSword().getLevel() + "', "))
                + "Axe=" + (build.getAxe() == null ? null + ", " : "'" + (build.getAxe().getSkill().getName() + "," + build.getAxe().getLevel() + "', "))
                + "Bow=" + (build.getBow() == null ? null + ", " : "'" + (build.getBow().getSkill().getName() + "," + build.getBow().getLevel() + "', "))
                + "PassiveA=" + (build.getPassiveA() == null ? null + ", " : "'" + (build.getPassiveA().getSkill().getName() + "," + build.getPassiveA().getLevel() + "', "))
                + "PassiveB=" + (build.getPassiveB() == null ? null + ", " : "'" + (build.getPassiveB().getSkill().getName() + "," + build.getPassiveB().getLevel() + "', "))
                + "Global=" + (build.getGlobal() == null ? null + ", " : "'" + (build.getGlobal().getSkill().getName() + "," + build.getGlobal().getLevel() + "', "))
                + "Active='" + UtilFormat.toTinyInt(build.isActive()) + "'"
                + " WHERE UUID = ('" + uuid.toString() + "') AND ID = " + build.getID() + " AND Role='" + build.getRole() + "'";

        QueryFactory.runQuery(query);
    }

    private static void setSkill(RoleBuild build, Types type, String str) {
        if (str != null) {
            String[] split = str.split(",");
            if (SelectorManager.getSkills().containsKey(split[0])) {
                Skill swordSkill = SelectorManager.getSkills().get(split[0]);
                int level = Integer.valueOf(split[1]);
                build.setSkill(type, swordSkill, level);
                build.takePoints(level);

            }
        }
    }


    @Override
    public void load(Clans clans) {
        Log.write("MySQL", "Builds are loaded on player login");
    }

    @Override
    public LoadPriority getLoadPriority() {
        return LoadPriority.HIGHEST;
    }
}
