package net.betterpvp.clans.dailies.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.database.*;
import org.bukkit.scheduler.BukkitRunnable;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.dailies.perks.QuestPerk;
import net.betterpvp.clans.dailies.perks.QuestPerkManager;


public class QuestRepository implements Repository<Clans> {

	public static final String TABLE_NAME = "kitmap_questperks";
	private static final String CREATE_QUESTPERK_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
			"  `UUID` varchar(255) DEFAULT NULL," +
			"  `Perk` varchar(255) DEFAULT NULL" +
			");";


	@Override
	public void initialize() {
		QueryFactory.runQuery(CREATE_QUESTPERK_TABLE);
	}

	@Override
	public void load(Clans clans) {
		new BukkitRunnable(){

			@Override
			public void run() {
				int count = 0;
				if(!Connect.isConnected()){
					Connect.openConnection();
				}
				try {
					PreparedStatement statement = Connect.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME);
					ResultSet result = statement.executeQuery();

					while (result.next()) {
						UUID uuid = UUID.fromString(result.getString(1));
						QuestPerk perk = QuestPerkManager.getPerk(result.getString(2));



						if(perk != null){

							Gamer gamer = GamerManager.getGamer(uuid);
							if(gamer != null){
								gamer.getQuestPerks().add(perk);

								count++;
							}
						}
					}


					statement.close();
					result.close();

					Log.debug("MySQL", "Hooked " + count + " Quest Perks to Gamers");

				} catch (SQLException ex) {
					Log.debug("Connection", "Could not load Donations (Connection Error), ");
					ex.printStackTrace();
				}
			}


		}.runTaskAsynchronously(clans);

	}

	@Override
	public LoadPriority getLoadPriority() {
		return LoadPriority.HIGHEST;
	}

	public static void saveQuestPerk(UUID uuid, QuestPerk perk) {
		String query = "INSERT INTO " + TABLE_NAME + " (UUID, Perk) VALUES "
				+ "('" + uuid + "', "
				+ "'" + perk.getName() + "')";

		Log.write("Clans", "Saved Quest Perk [" + perk.getName() + "] to [" + ClientUtilities.getClient(uuid).getName() + "]");
		QueryFactory.runQuery(query);
	}

	public static void removeQuestPerk(UUID player, String perk) {
		String query = "DELETE FROM " + TABLE_NAME + " WHERE UUID='" + player + "' AND Perk='" + perk + "'";
		Log.write("Clans", "Removed QuestPerk [" + perk + "] from [" + ClientUtilities.getClient(player).getName() + "]");
		QueryFactory.runQuery(query);
	}
}
