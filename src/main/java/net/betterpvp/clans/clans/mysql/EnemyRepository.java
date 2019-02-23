package net.betterpvp.clans.clans.mysql;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.Dominance;
import net.betterpvp.core.database.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnemyRepository implements Repository<Clans> {

	public static final String TABLE_NAME = "kitmap_dominance";
	
	public static String CREATE_ENEMY_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " "
			+ "(Clan VARCHAR(16), "
			+ "Other VARCHAR(16), "
			+ "Points INT, "
			+ "Time LONG); ";



	@Override
	public void initialize() {
		QueryFactory.runQuery(CREATE_ENEMY_TABLE);
	}

	@Override
	public void load(Clans clans) {
		new BukkitRunnable() {

			@Override
			public void run() {
				int count = 0;
				if (!Connect.isConnected()) {
					Connect.openConnection();
				}
				try {
					PreparedStatement statement = Connect.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME);
					ResultSet result = statement.executeQuery();

					while (result.next()) {

						if (ClanUtilities.getClan(result.getString(1)) != null) {
							if (ClanUtilities.getClan(result.getString(2)) != null) {
								Clan clan = ClanUtilities.getClan(result.getString(1));
								Clan target = ClanUtilities.getClan(result.getString(2));
								if (clan != target) {
									int points = result.getInt(3);
									Long time = result.getLong(4);


									Dominance dom = new Dominance(clan, target, points);
									dom.setTime(time);
									clan.getEnemies()
											.add(dom);
									count++;
								}
							}
						}

					}
					statement.close();
					result.close();

					Log.debug("MySQL", "Linked " + count + " Enemies to Clans");

				} catch (SQLException ex) {
					Log.debug("Connection", "Could not load Clan Dominance (Connection Error), ");
					ex.printStackTrace();
				}
			}
		}.runTaskAsynchronously(clans);
	}

	@Override
	public LoadPriority getLoadPriority() {
		return LoadPriority.HIGHEST;
	}


	public static void saveDominance(Dominance dom) {
		String query = "REPLACE INTO " + TABLE_NAME + " (Clan, Other, Points) VALUES ('"
				+ dom.getSelf().getName() + "', '" + dom.getClan().getName() + "', '" + dom.getPoints() + "')";
		QueryFactory.runQuery(query);
		
	
	
		
	}

	public static void updateDominance(Dominance dom) {
		String query = "UPDATE " + TABLE_NAME + " SET Points='" + dom.getPoints() + "' WHERE (Clan='" + dom.getSelf().getName() + "' AND Other='" + dom.getClan().getName() + "')";
		QueryFactory.runQuery(query);
	}

	public static void deleteEnemy(Dominance dom) {
		String query = "DELETE FROM " + TABLE_NAME + " WHERE Clan='" + dom.getSelf().getName() + "' AND Other='" + dom.getClan().getName() + "' OR "
				+ "Clan='" + dom.getClan().getName() + "' AND Other='" + dom.getSelf().getName() + "'";
		QueryFactory.runQuery(query);
	}
	
	public static void wipe(){
		String query = "TRUNCATE TABLE " + TABLE_NAME;
		QueryFactory.runQuery(query);
	}

}
