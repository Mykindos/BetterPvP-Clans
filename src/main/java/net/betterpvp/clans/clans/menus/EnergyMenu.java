package net.betterpvp.clans.clans.menus;

import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.menus.buttons.energybuttons.Buy1KEnergy;
import net.betterpvp.clans.clans.menus.buttons.energybuttons.BuyOneDayEnergy;
import net.betterpvp.clans.clans.menus.buttons.energybuttons.BuyOneHourEnergy;
import net.betterpvp.core.interfaces.Button;
import net.betterpvp.core.interfaces.Menu;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class EnergyMenu extends Menu {

    private Clan clan;


    public EnergyMenu(Player player) {
        super(player, 9, ChatColor.GREEN + "Buy Energy", new Button[]{});
        this.clan = ClanUtilities.getClan(player);
        fillPage();
        construct();
    }

    private void fillPage() {
        if (clan != null) {
            addButton(new BuyOneHourEnergy(clan));

            addButton(new BuyOneDayEnergy(clan));

            addButton(new Buy1KEnergy(clan));
        }
    }

}
