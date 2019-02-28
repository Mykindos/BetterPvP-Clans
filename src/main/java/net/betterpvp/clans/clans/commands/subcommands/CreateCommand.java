package net.betterpvp.clans.clans.commands.subcommands;

import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.commands.IClanCommand;
import net.betterpvp.clans.clans.events.ClanCreateEvent;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CreateCommand implements IClanCommand {

    private String[] denied = {"sethome", "promote", "demote",
            "admin", "help", "create", "disband", "delete", "invite", "join",
            "kick", "ally", "trust", "enemy", "claim", "unclaim", "territory",
            "home", "spawn", "fields", "shop", "shops", "plains", "leave", "top",
            "menu", "unclaimall", "map", "home", "neutral", "energy", "wilderness"};
    private String[] filtered = {"FILTERED WORDS GO HERE"};

    public final static int nameMax = 13;
    public final static int nameMin = 3;


    public void run(Player player, String[] args) {
        if (args.length == 1) {
            UtilMessage.message(player, "Clans", "You did not input a Clan name.");
            return;
        }

        if (ClanUtilities.getClan(player) != null) {
            UtilMessage.message(player, "Clans", "You are already in a Clan.");
            return;
        }

        String name = args[1];
        if (!ClientUtilities.getOnlineClient(player).isAdministrating()) {
            if (!UtilInput.valid(name)) {
                UtilMessage.message(player, "Clans", "Invalid characters in Clan name.");
                return;
            }

            if (name.length() < nameMin) {
                UtilMessage.message(player, "Clans", "Clan name too short. Minimum length is [" + nameMin + "].");
                return;
            }

            if (name.length() > nameMax) {
                UtilMessage.message(player, "Clans", "Clan name too long. Maximum length is [" + nameMax + "].");
                return;
            }

            for (String string : denied) {
                if (string.equalsIgnoreCase(name)) {
                    UtilMessage.message(player, "Clans", "Clan name cannot be a Clan command.");
                    return;
                }
            }

            for (String string : filtered) {
                if (name.toLowerCase().contains(string)) {
                    UtilMessage.message(player, "Clans", "Clan name cannot be inappropriate.");
                    return;
                }
            }
        }

        Clan c = ClanUtilities.getClan(name);
        if (c != null) {
            UtilMessage.message(player, "Clans", "Clan name is already used by another Clan.");
            return;
        }


        Bukkit.getPluginManager().callEvent(new ClanCreateEvent(player, name));
    }

    @Override
    public String getName() {

        return "Create";
    }
}
