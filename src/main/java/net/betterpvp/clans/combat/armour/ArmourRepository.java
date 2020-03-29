package net.betterpvp.clans.combat.armour;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.weapon.EnchantedWeapon;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.core.database.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import javax.xml.bind.annotation.XmlType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ArmourRepository implements Repository<Clans> {

    public static final String TABLE_NAME = "clans_armour";
    public static String CREATE_ARMOUR_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (Item VARCHAR(255), Armour double, CONSTRAINT armour_pk PRIMARY KEY (Item)); ";
    private static String DEFAULT_ARMOUR_VALUES = "INSERT IGNORE INTO betterpvp.clans_armour (Item, Armour) VALUES" +
            "('DIAMOND_CHESTPLATE', 26),('DIAMOND_LEGGINGS', 18),('DIAMOND_HELMET', 12),('DIAMOND_BOOTS', 12)," +
            "('LEATHER_HELMET', 4),('LEATHER_CHESTPLATE', 12),('LEATHER_LEGGINGS', 8),('LEATHER_BOOTS', 4)," +
            "('IRON_CHESTPLATE', 24), ('IRON_LEGGINGS', 20), ('IRON_HELMET', 8),('IRON_BOOTS', 8)," +
            "('GOLDEN_CHESTPLATE', 24), ('GOLDEN_LEGGINGS', 20), ('GOLDEN_HELMET', 8), ('GOLDEN_BOOTS', 8)," +
            "('CHAINMAIL_CHESTPLATE', 20), ('CHAINMAIL_LEGGINGS', 16),('CHAINMAIL_HELMET', 8), ('CHAINMAIL_BOOTS', 8);";
    @Override
    public void initialize() {
        QueryFactory.runQuery(CREATE_ARMOUR_TABLE);
        QueryFactory.runQuery(DEFAULT_ARMOUR_VALUES);

    }

    @Override
    public void load(Clans clans) {
        new BukkitRunnable() {

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

    public static HashMap<String, Double> getArmour() {
        return armour;
    }

    public static double getArmour(ItemStack item) {

        Weapon w = WeaponManager.getWeapon(item);
        if (w != null) {

            if (w instanceof EnchantedWeapon) {
                EnchantedWeapon ew = (EnchantedWeapon) w;
                if (getArmour().containsKey(ew.getMaterial().name())) {

                    return getArmour().get(ew.getMaterial().name()) + ew.getBonus();
                }
            }
        }

        if (getArmour().containsKey(item.getType().name())) {
            return getArmour().get(item.getType().name());
        }

        return 0;
    }


    @Override
    public LoadPriority getLoadPriority() {
        return LoadPriority.HIGHEST;
    }
}
