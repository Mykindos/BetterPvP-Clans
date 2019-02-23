package net.betterpvp.clans.clans.mysql;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.core.database.*;
import net.betterpvp.core.utility.UtilFormat;
import net.betterpvp.core.utility.UtilLocation;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClanRepository implements Repository<Clans>{



	public static final String DATABASE_NAME = "kitmap_clans";

	public static String CREATE_CLANS_TABLE = "CREATE TABLE IF NOT EXISTS " + DATABASE_NAME + " "
			+ "(Name VARCHAR(14), "
			+ "Created LONG, "
			+ "Leader VARCHAR(64), "
			+ "Home VARCHAR(64), "
			+ "Territory BLOB, "
			+ "Admin TINYINT, "
			+ "Safe TINYINT, "
			+ "LastLogin BIGINT(255)); ";

	public static void saveClan(Clan clan) {
		String query = "";
		if (clan instanceof AdminClan) {
			AdminClan admin = (AdminClan) clan;
			query = "INSERT INTO " + DATABASE_NAME + " (Name, Created, Leader, Home, Territory, Admin, Safe, LastLogin, Energy, Points, Cooldown) VALUES "
					+ "('" + admin.getName() + "', "
					+ "'" + admin.getCreated() + "', "
					+ "'" + admin.getLeader().toString() + "', "
					+ "'" + UtilFormat.locationToFile(admin.getHome()) + "', "
					+ "'" + UtilFormat.toString(admin.getTerritory()) + "', "
					+ "'" + UtilFormat.toTinyInt(true) + "', "
					+ "'" + UtilFormat.toTinyInt(admin.isSafe()) + "', " 
					+ "'" + System.currentTimeMillis() + "', "
					+ "'" + clan.getEnergy() + "', "
					+ "'" + clan.getPoints() + "', "
					+ "'" + clan.getRawCooldown() + "')";
		} else {
			query = "INSERT INTO " + DATABASE_NAME + " (Name, Created, Leader, Home, Territory, Admin, Safe, LastLogin, Energy, Points, Cooldown) VALUES "
					+ "('" + clan.getName() + "', "
					+ "'" + clan.getCreated() + "', "
					+ "'" + clan.getLeader().toString() + "', "
					+ "'" + UtilFormat.locationToFile(clan.getHome()) + "', "
					+ "'" + UtilFormat.toString(clan.getTerritory()) + "', "
					+ "'" + 0 + "', "
					+ "'" + 0 + "', "
					+ "'" + System.currentTimeMillis() + "', "
					+ "'" + clan.getEnergy() + "', "
					+ "'" + clan.getPoints() + "', "
					+ "'" + clan.getRawCooldown() + "')";
		}

		MemberRepository.saveMembers(clan);

		new Query(query);
		Log.write("Clans", "Saved Clan [" + clan.getName() + "]");
	}

	public static void loadClans(final Clans i) {
		new BukkitRunnable(){

			@Override
			public void run() {
				int count = 0;
				try {
					PreparedStatement statement = Connect.getConnection().prepareStatement("SELECT * FROM " + DATABASE_NAME);
					ResultSet result = statement.executeQuery();

					while (result.next()) {
						String name = result.getString(1);
						long created = result.getLong(2);
						UUID leader = UUID.fromString(result.getString(3));

						String home = result.getString(4);

						Blob blob = result.getBlob(5);
						List<String> territory = new ArrayList<String>();

						byte[] bdata = blob.getBytes(1, (int) blob.length());
						String territoryString = new String(bdata);

						if (territoryString.length() > 1) {
							String[] claims = territoryString.split(", ");
							for (String claim : claims) {
								territory.add(claim);
							}
						}

						boolean admin = UtilFormat.fromTinyInt(result.getInt(6));
						boolean safe = UtilFormat.fromTinyInt(result.getInt(7));
						long lastLogin = result.getLong(8);
						int points = result.getInt(10);
						int energy = result.getInt(9);
						long cooldown = result.getLong(11);

						if (admin) {
							final AdminClan clan = new AdminClan(name);
							clan.setCreated(created);
							clan.setLeader(leader);
							clan.setHome(UtilLocation.stringToLocation(home));
							clan.setTerritory(territory);
							clan.setSafe(safe);
							clan.setLastLogin(lastLogin);
							clan.setEnergy(energy);
							clan.setPoints(points);

							ClanUtilities.addClan(clan);
							if(!clan.getName().equalsIgnoreCase("Outskirts")){
								//updateDynmap(i, clan);
							}
						} else {

							Clan clan = new Clan(name);
							clan.setCreated(created);
							clan.setLeader(leader);
							clan.setHome(UtilLocation.stringToLocation(home));
							clan.setTerritory(territory);
							clan.setVulnerable(false);
							clan.setLastLogin(lastLogin);
							clan.setEnergy(energy);
							clan.setPoints(points);
							clan.setCooldown(cooldown);
							ClanUtilities.addClan(clan);
							//updateDynmap(i, clan);
						}

						count++;
					}

					statement.close();
					result.close();



					Log.debug("MySQL", "Loaded " + count + " Clans");

				} catch (SQLException ex) {
					Log.debug("Connection", "Could not load Clans (Connection Error), ");
					ex.printStackTrace();
				}
				MemberRepository.loadMembers(i);
				AllianceRepository.loadAlliances(i);
				EnemyRepository.loadEnemies(i);
				InsuranceRepository.loadInsurance(i);
				//ClanRepository.loadRaidHistory(i);
			}

		}.runTaskAsynchronously(i);

	}

	public static void loadRaidHistory(final Clans i) {
		new BukkitRunnable(){

			@Override
			public void run() {
				int count = 0;
				try {
					PreparedStatement statement = Connect.getConnection().prepareStatement("SELECT * FROM " + "kitmap_raidhistory");
					ResultSet result = statement.executeQuery();

					while (result.next()) {
						String clan = result.getString(1);
						String raided = result.getString(2);
						int timesRaided = result.getInt(3);


						Clan aClan = ClanUtilities.getClan(clan);
						if(aClan != null) {
							Clan raidedClan = ClanUtilities.getClan(raided);
							if(raidedClan != null) {
								aClan.getRaids().put(raidedClan, timesRaided);
							}
						}
						count++;
					}

					statement.close();
					result.close();



					Log.debug("MySQL", "Loaded " + count + " Raids");

				} catch (SQLException ex) {
					Log.debug("Connection", "Could not load Clans (Connection Error), ");
					ex.printStackTrace();
				}

			}

		}.runTaskAsynchronously(i);

	}

	public static void updateRaidHistory(Clan a, Clan raided) {
		if(a.getRaids().containsKey(raided)) {
			a.getRaids().put(raided, a.getRaids().get(raided) + 1);
			String query = "UPDATE kitmap_raidhistory SET Times = '" + a.getRaids().get(raided) + "' WHERE Clan ='" + a.getName() + "' AND Raided ='" + raided.getName() + "'";
			new Query(query);
		}else {
			a.getRaids().put(raided, 1);
			String query2 = "INSERT INTO kitmap_raidhistory (Clan, Raided, Times) VALUES ('" + a.getName() + "', '" 
					+ raided.getName() + "', '" +a.getRaids().get(raided) + "')";
			new Query(query2);
		
		}

		
	}

	public static void deleteClan(Clan clan) {
		String query = "DELETE FROM " + DATABASE_NAME + " WHERE Name='" + clan.getName() + "'";
		new Query(query);
	}

	public static void updateMembers(Clan clan) {
		String query = "UPDATE " + DATABASE_NAME + " SET Members='" + UtilFormat.toString(clan.getMembers()) + "' WHERE Name='" + clan.getName() + "'";
		new Query(query);
	}

	public static void updateClaims(Clan clan) {
		String query = "UPDATE " + DATABASE_NAME + " SET Territory='" + UtilFormat.toString(clan.getTerritory()) + "' WHERE Name='" + clan.getName() + "'";
		new Query(query);
	}

	public static void updateHome(Clan clan) {
		String query = "UPDATE " + DATABASE_NAME + " SET Home='" + UtilFormat.locationToFile(clan.getHome()) + "' WHERE Name='" + clan.getName() + "'";
		new Query(query);
	}

	public static void updateLastLogin(Clan clan) {
		String query = "UPDATE " + DATABASE_NAME + " SET LastLogin='" + System.currentTimeMillis() + "' WHERE Name='" + clan.getName() + "'";
		new Query(query);
	}

	public static void updateEnergy(Clan clan) {
		String query = "UPDATE " + DATABASE_NAME + " SET Energy='" + clan.getEnergy() + "' WHERE Name='" + clan.getName() + "'";
		new Query(query);
	}

	public static void updatePoints(Clan clan) {
		String query = "UPDATE " + DATABASE_NAME + " SET Points='" + clan.getPoints() + "' WHERE Name='" + clan.getName() + "'";
		new Query(query);
	}

	public static void updateCooldown(Clan clan) {
		String query = "UPDATE " + DATABASE_NAME + " SET Cooldown='" + clan.getRawCooldown() + "' WHERE Name='" + clan.getName() + "'";
		new Query(query);
	}


	public static void wipe(){
		String query = "TRUNCATE TABLE " + DATABASE_NAME;
		new Query(query);
	}

	@Override
	public LoadPriority getLoadPriority() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void load(Clans i) {
		// TODO Auto-generated method stub
		
	}

	/*
	private static AreaMarker getMarker(MarkerSet set, String clanName){
		for(AreaMarker m : set.getAreaMarkers()){
			if(m.getMarkerID().equalsIgnoreCase(clanName)){
				return m;
			}
		}

		return null;
	}


	public static void updateDynmap(Clans i, final Clan clan){
		new BukkitRunnable(){
			@Override
			public void run(){
				if(clan == null){
					return;
				}
				if(clan.getName().equalsIgnoreCase("Outskirts")){
					return;
				}
				Chunk bottomLeft = null;
				Chunk topRight = null;
				World world = Bukkit.getWorld("world");
				MarkerSet set = Clans.dynmap.getMarkerAPI().getMarkerSet("clans.markerset");
				List<Block> blocks = new ArrayList<>();

				if(getMarker(set, clan.getName()) != null){
					getMarker(set, clan.getName()).deleteMarker();
				}



				for(String d : clan.getTerritory()){
					String[] z = d.split("/");
					Chunk c = world.getChunkAt(Integer.valueOf(z[1].replace(" ", "")), Integer.valueOf(z[2].replace(" ", "")));
					if(bottomLeft == null){
						bottomLeft = c;
					}else{
						Block a = c.getBlock(0, 0, 15);
						Block b = bottomLeft.getBlock(0, 0, 15);

						if(a.getZ() >= b.getZ() && a.getX() <= b.getX()){
							bottomLeft = c;
						}
					}

					if(topRight == null){
						topRight = c;
					}else{
						Block a = c.getBlock(15, 0, 0);
						Block b = topRight.getBlock(15, 0, 0);
						if(a.getZ() <= b.getZ() && a.getX() >= b.getX()){
							topRight = c;
						}
					}




				}
				if(bottomLeft != null && topRight != null){
					blocks.add(bottomLeft.getBlock(0, 0, 15));

					//new BlockRestoreData(bottomLeft.getBlock(0, 100, 0), 46, (byte) 0, 60000);
					blocks.add(topRight.getBlock(15, 0, 0));
					double[] temp = {1,1};
					double[] temp2 = {1,1};
					AreaMarker area = set.createAreaMarker(clan.getName(), clan.getName() , false, "world", temp, temp2, false);

					if(area != null){
						area.setLineStyle(0, 0, 0);
						if(clan instanceof AdminClan){
							AdminClan adminClan = (AdminClan) clan;
							if(adminClan.isSafe()){
								area.setFillStyle(0.1, 65535);
							}

						}else{
							area.setFillStyle(0.2, 16776960);
							area.setLineStyle(1, 0.7, 16776960);
						}
						area.setCornerLocation(0, blocks.get(0).getX(), blocks.get(1).getZ());
						area.setCornerLocation(1, blocks.get(1).getX(), blocks.get(0).getZ());
						//area.setLabel(clan.getName(), true);

						//area.setDescription("Members: " + ChatColor.stripColor(ClanUtilities.getMembersList(clan)));
					}

				}


			}
		}.runTask(i);
	}

	public static void removeDynmap(Clan c){
		if(getMarker(Clans.set, c.getName()) != null){
			getMarker(Clans.set, c.getName()).deleteMarker();
		}
	}

	 */
}
