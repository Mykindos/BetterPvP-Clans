package net.betterpvp.clans.clans.mysql;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.InsuranceType;
import net.betterpvp.clans.clans.insurance.Insurance;
import net.betterpvp.core.database.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsuranceRepository implements Repository<Clans> {

    public static final String TABLE_NAME = "kitmap_insurance";

    public static void removeInsurance() {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE ((Time+86400000)-" + System.currentTimeMillis() + ") < 0";
		/*String query = "DELETE FROM insurance WHERE Type='" + i.getType().toString() 
				+ "' AND Material='" + i.getMaterial().name() 
				+ "' AND Data='" + i.getData()
				+ "' AND Time='" + i.getTime()
				+ "' AND X='" + i.getLocation().getX()
				+ "' AND Y='" + i.getLocation().getY()
				+ "' AND Z='" + i.getLocation().getZ() + "'";*/
        QueryFactory.runQuery(query);
    }

    @Override
    public void initialize() {


    }

    @Override
    public void load(Clans i) {
        final World world = Bukkit.getWorld("world");
        new BukkitRunnable() {

            @Override
            public void run() {
                int count = 0;
                try {
                    PreparedStatement statement = Connect.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME);
                    ResultSet result = statement.executeQuery();

                    while (result.next()) {
                        String clan = result.getString(1);
                        InsuranceType type = InsuranceType.valueOf(result.getString(2));
                        Material mat = Material.valueOf(result.getString(3));
                        byte data = result.getByte(4);
                        long time = result.getLong(5);
                        double x = result.getDouble(6);
                        double y = result.getDouble(7);
                        double z = result.getDouble(8);

                        Clan c = ClanUtilities.getClan(clan);
                        if (c != null) {
                            if (type != null) {
                                if (mat != null) {
                                    c.getInsurance().add(new Insurance(new Location(world, x, y, z), mat, data, type, time));
                                }
                            }
                        }

                        count++;
                    }

                    statement.close();
                    result.close();


                    Log.debug("MySQL", "Loaded " + count + " blocks of clan insurance");

                } catch (SQLException ex) {
                    Log.debug("Connection", "Could not load Insurance (Connection Error), ");
                    ex.printStackTrace();
                }

            }

        }.runTaskAsynchronously(i);

    }

    public static void saveInsurance(Clan c, Insurance i) {
        if (Clans.getOptions().isLastDay()) {
            return;
        }
        if (i.getMaterial() == Material.AIR) {
            return;
        }
        String query = "INSERT INTO " + TABLE_NAME + " VALUES ('" + c.getName() + "', '"
                + i.getType().toString() + "', '"
                + i.getMaterial().name() + "', '"
                + i.getData() + "', '"
                + i.getTime() + "', '"
                + i.getLocation().getX() + "', '"
                + i.getLocation().getY() + "', '"
                + i.getLocation().getZ() + "')";
        QueryFactory.runQuery(query);
    }

    @Override
    public LoadPriority getLoadPriority() {
        // TODO Auto-generated method stub
        return LoadPriority.HIGHEST;
    }


}
