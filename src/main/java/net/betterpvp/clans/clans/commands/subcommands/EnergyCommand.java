package net.betterpvp.clans.clans.commands.subcommands;

import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.commands.IClanCommand;
import net.betterpvp.clans.clans.menus.EnergyMenu;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.entity.Player;

public class EnergyCommand implements IClanCommand {


    @Override
    public void run(Player p, String[] args) {
        Clan clan = ClanUtilities.getClan(p);

        if (clan == null) {
            UtilMessage.message(p, "Clans", "You are not in a Clan.");
            return;
        }

        p.openInventory(new EnergyMenu(p).getInventory());

    }

    @Override
    public String getName() {

        return "Energy";
    }

}
