package net.betterpvp.clans.gamer;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.skills.mysql.BuildRepository;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.database.*;
import net.betterpvp.core.utility.UtilFormat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class GamerRepository implements Repository<Clans> {
	
	public static final String TABLE_NAME = "kitmap_gamers";

	public static String CREATE_GAMER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " "
			+ "(UUID VARCHAR(64), "
			+ "Coins INT,"
			+ "Kills bigint(255),"
			+ "Deaths bigint(255),"
			+ "Votes INT(10),"
			+ "Fragments bigint(255),"
			+ "BattleCoins bitint(255),"
			+ "Filter tinyint(1)); ";


	@Override
	public void initialize() {
		QueryFactory.runQuery(CREATE_GAMER_TABLE);
	}

	@Override
	public void load(Clans clans) {
		new BukkitRunnable(){

			@Override
			public void run() {
				int count = 0;
				try {
					PreparedStatement statement = Connect.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME);
					ResultSet result = statement.executeQuery();

					while (result.next()) {
						UUID uuid = UUID.fromString(result.getString(1));
						int coins = result.getInt(2);

						int kills = result.getInt(3);
						int deaths = result.getInt(4);
						int votes = result.getInt(5);
						int fragments = result.getInt(6);
						int battlecoins = result.getInt(7);
						boolean filter = result.getBoolean(8);



						Client client = ClientUtilities.getClient(uuid);
						Gamer gamer = new Gamer(uuid);

						if(gamer != null && client != null){
							gamer.setCoins(coins);
							gamer.setKills(kills);
							gamer.setDeaths(deaths);
							gamer.setVotes(votes);
							gamer.setFragments(fragments);
							gamer.setBattleCoins(battlecoins);
							gamer.setFilter(filter);

							gamer.setClient(client);


						}

						count++;
					}
					statement.close();
					result.close();

					Log.debug("MySQL", "Hooked " + count + " Gamers to Clients");

				} catch (SQLException ex) {
					Log.debug("Connection", "Could not load Gamers (Connection Error), ");
					ex.printStackTrace();
				}
				for(Player p : Bukkit.getOnlinePlayers()){

					BuildRepository.loadBuilds(clans, p.getUniqueId());

				}


			}

		}.runTaskAsynchronously(clans);
	}

	@Override
	public LoadPriority getLoadPriority() {
		return LoadPriority.LOW;
	}

	public static void saveGamer(Gamer gamer) {
		String query = "INSERT INTO " + TABLE_NAME + " (UUID, Coins, Kills, Deaths, Votes, Fragments, BattleCoins, Filter) VALUES "
				+ "('" + gamer.getUUID().toString() + "', "
				+ "'" + gamer.getCoins() + "',"
				+ "'" + gamer.getKills() + "', "
				+ "'" + gamer.getDeaths() + "', "
				+ "'" + gamer.getVotes() + "', "
				+ "'" + gamer.getFragments() + "', "
				+ "'" + gamer.getBattleCoins() + "',"
				+ "'" + UtilFormat.toTinyInt(gamer.isFiltering())+ "')";
		Log.write("Clans", "Saved Gamer [" + ClientUtilities.getClient(gamer.getUUID()).getName() + "]");
		QueryFactory.runQuery(query);

	}

	
	public static void loadGamer(final Clans i, final Player p) {
		new BukkitRunnable(){

			@Override
			public void run() {
				int count = 0;
				try {
					PreparedStatement statement = Connect.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME
							+ " WHERE UUID = '" + p.getUniqueId().toString() + "'");
					ResultSet result = statement.executeQuery();

					while (result.next()) {
						UUID uuid = UUID.fromString(result.getString(1));
						int coins = result.getInt(2);

						int kills = result.getInt(3);
						int deaths = result.getInt(4);
						int votes = result.getInt(5);
						int fragments = result.getInt(6);
						int battlecoins = result.getInt(7);
						boolean filter = result.getBoolean(8);



						Gamer gamer = GamerManager.getOnlineGamer(uuid);
						Client client = ClientUtilities.getOnlineClient(uuid);


						if(gamer != null && client != null){
							gamer.setCoins(coins);
							gamer.setKills(kills);
							gamer.setDeaths(deaths);
							gamer.setVotes(votes);
							gamer.setFragments(fragments);
							gamer.setBattleCoins(battlecoins);
							gamer.setFilter(filter);

							gamer.setClient(client);


						}

						count++;
					}
					statement.close();
					result.close();

					Log.debug("MySQL", "Hooked " + count + " Gamers to Clients");

				} catch (SQLException ex) {
					Log.debug("Connection", "Could not load Gamers (Connection Error), ");
					ex.printStackTrace();
				}



			}

		}.runTaskAsynchronously(i);

	}
	private static final String[] roles = {"Assassin", "Paladin", "Knight", "Ranger", "Gladiator"};
	

	public static void updateGamer(Gamer gamer){
		String query = "UPDATE " + TABLE_NAME + " SET Coins='" + gamer.getCoins() + "',  Votes='" + gamer.getVotes() 
		+ "',  Fragments='" + gamer.getFragments() + "', Kills='" + gamer.getKills() + "', "
		+ " Deaths='" + gamer.getDeaths() + "', BattleCoins='" + gamer.getBattleCoins() + "' WHERE UUID='" + gamer.getUUID().toString() + "'";
		QueryFactory.runQuery(query);
	}

	public static void updateCoins(Gamer gamer) {
		String query = "UPDATE " + TABLE_NAME + " SET Coins='" + gamer.getCoins() + "' WHERE UUID='" + gamer.getUUID().toString() + "'";
		QueryFactory.runQuery(query);
	}

	public static void updateVotes(Gamer gamer) {
		String query = "UPDATE " + TABLE_NAME + " SET Votes='" + gamer.getVotes() + "' WHERE UUID='" + gamer.getUUID().toString() + "'";
		QueryFactory.runQuery(query);
	}

	public static void updateFragments(Gamer gamer) {
		String query = "UPDATE " + TABLE_NAME + " SET Fragments='" + gamer.getFragments() + "' WHERE UUID='" + gamer.getUUID().toString() + "'";
		QueryFactory.runQuery(query);
	}

	public static void updateBattleCoins(Gamer gamer) {
		String query = "UPDATE " + TABLE_NAME + " SET BattleCoins='" + gamer.getBattleCoins() + "' WHERE UUID='" + gamer.getUUID().toString() + "'";
		QueryFactory.runQuery(query);
	}

	public static void updateKills(Gamer gamer) {
		String query = "UPDATE " + TABLE_NAME + " SET Kills='" + gamer.getKills() + "' WHERE UUID='" + gamer.getUUID().toString() + "'";
		QueryFactory.runQuery(query);
	}

	public static void updateDeaths(Gamer gamer) {
		String query = "UPDATE " + TABLE_NAME + " SET Deaths='" + gamer.getDeaths() + "' WHERE UUID='" + gamer.getUUID().toString() + "'";
		QueryFactory.runQuery(query);
	}

	public static void wipe(){
		String query = "TRUNCATE TABLE " + TABLE_NAME;
		QueryFactory.runQuery(query);
	}


}
