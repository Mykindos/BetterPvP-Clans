package net.betterpvp.clans.clans.mysql;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanMember;
import net.betterpvp.clans.clans.ClanMember.Role;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.core.database.*;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MemberRepository{

    public static final String TABLE_NAME = "clans_clanmembers";

    public static final String CREATE_CLANMEMBER_TABLE = "CREATE TABLE IF NOT EXISTS `" + TABLE_NAME + "`  (" +
            "  `Clan` varchar(255)," +
            "  `UUID` varchar(255)," +
            "  `Role` varchar(255)," +
            "PRIMARY KEY(UUID));";


    private static void initialize() {
        QueryFactory.runQuery(CREATE_CLANMEMBER_TABLE);

    }

    public static void load(Clans i) {
        initialize();
        new BukkitRunnable() {

            @Override
            public void run() {
                int count = 0;

                try {
                    PreparedStatement statement = Connect.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME);
                    ResultSet result = statement.executeQuery();

                    while (result.next()) {
                        Clan clan = ClanUtilities.getClan(result.getString(1));
                        UUID uuid = UUID.fromString(result.getString(2));
                        Role role = Role.valueOf(result.getString(3));

                        if (clan != null) {
                            System.out.println("Added " + uuid + " to " + clan.getName());
                            clan.getMembers().add(new ClanMember(uuid, role));

                            //clan.getTeam().addEntry(ClientUtilities.getClient(uuid).getName());
                        }
                        count++;
                    }
                    statement.close();
                    result.close();
                    Log.debug("MySQL", "Hooked " + count + " Members to Clans");

                } catch (SQLException ex) {
                    Log.debug("Connection", "Could not load clanmembers (Connection Error), ");
                    ex.printStackTrace();
                }

            }

        }.runTaskAsynchronously(i);

    }

    public static String CREATE_MEMBER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " "
            + "(Clan VARCHAR(14), "
            + "UUID VARCHAR(64), "
            + "Role VARCHAR(12)); ";

    public static void saveMembers(Clan clan) {
        for (ClanMember member : clan.getMembers()) {
            String query = "INSERT INTO " + TABLE_NAME + " (Clan, UUID, Role) VALUES "
                    + "('" + clan.getName() + "', "
                    + "'" + member.getUUID().toString() + "', "
                    + "'" + member.getRole().toString() + "')";
            QueryFactory.runQuery(query);
        }
    }

    public static void saveMember(Clan clan, ClanMember member) {
        String query = "INSERT INTO " + TABLE_NAME + " (Clan, UUID, Role) VALUES "
                + "('" + clan.getName() + "', "
                + "'" + member.getUUID().toString() + "', "
                + "'" + member.getRole().toString() + "')";
        QueryFactory.runQuery(query);
        Log.write("Clans", "Added [" + member.getUUID() + "/" + Bukkit.getPlayer(member.getUUID()).getName() + "] to [" + clan.getName() + "]");
    }

    public static void loadMembers(Clans i) {


    }

    public static void updateMember(ClanMember member) {
        String query = "UPDATE " + TABLE_NAME + " SET Role='" + member.getRole().toString() + "' WHERE UUID='" + member.getUUID().toString() + "'";
        QueryFactory.runQuery(query);
    }

    public static void deleteMember(ClanMember member) {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE UUID='" + member.getUUID().toString() + "'";
        QueryFactory.runQuery(query);
    }

    public static void wipe() {
        String query = "TRUNCATE TABLE " + TABLE_NAME;
        QueryFactory.runQuery(query);
    }


}
