package net.betterpvp.clans.combat.armour;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import net.betterpvp.clans.Clans;
import net.betterpvp.core.database.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.mysql.Connect;
import net.betterpvp.clans.mysql.Log;
import net.betterpvp.clans.weapon.EnchantedWeapon;
import net.betterpvp.clans.weapon.Weapon;

public class ArmourRepository implements Repository<Clans> {
	
	public static final String TABLE_NAME = "kitmap_armour";
	public static String CREATE_ARMOUR_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (Item VARCHAR(255), Armour double); ";

	@Override
	public void initialize() {
		QueryFactory.runQuery(CREATE_ARMOUR_TABLE);
	}

	@Override
	public void load(Clans clans) {
		new BukkitRunnable(){

			@Override
			public void run() {

				try {
					PreparedStatement statement = Connect.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME);
					ResultSet result = statement.executeQuery();

					while (result.next()) {
						String itemName = result.getString(1);
						double armour = result.getDouble(2);

						getArmour().put(itemName, armour);
					}
					Log.debug("MySQL", "Loaded " + getArmour().size() + " armour values");
					statement.close();
					result.close();


				} catch (SQLException ex) {
					Log.debug("Connection", "Could not load Armour (Connection Error), ");
					ex.printStackTrace();
				}


			}

		}.runTaskAsynchronously(clans);
	}


	private static HashMap<String, Double> armour = new HashMap<>();

	public static HashMap<String, Double> getArmour(){
		return armour;
	}

	public static double getArmour(ItemStack item){

		Weapon w = Weapon.getWeapon(item);
		if(w != null){
			
			if(w instanceof EnchantedWeapon){
				EnchantedWeapon ew = (EnchantedWeapon) w;
				if(getArmour().containsKey(ew.getMaterial().name())){
				
					return getArmour().get(ew.getMaterial().name()) + ew.getBonus();
				}
			}
		}

		if(getArmour().containsKey(item.getType().name())){
			return getArmour().get(item.getType().name());
		}

		return 0;
	}




	@Override
	public LoadPriority getLoadPriority() {
		return LoadPriority.HIGHEST;
	}
}
