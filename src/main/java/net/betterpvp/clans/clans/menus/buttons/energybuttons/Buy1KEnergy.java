package net.betterpvp.clans.clans.menus.buttons.energybuttons;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.menus.buttons.ClanMenuButton;
import net.betterpvp.core.utility.UtilFormat;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Buy1KEnergy extends ClanMenuButton {


    public Buy1KEnergy(Clan clan) {
        super(clan, 6, new ItemStack(Material.EMERALD), ChatColor.GREEN + "Buy 1000 Energy",
                "",
                ChatColor.GRAY + "Buy " + ChatColor.GREEN + "1000"
                        + ChatColor.GRAY + " energy for " + ChatColor.YELLOW + "$" + UtilFormat.formatNumber((int) (1000 * Clans.getOptions().getCostPerEnergy()))
                        + ChatColor.YELLOW + " coins.",
                "",
                ChatColor.GREEN + "Left-Click: " + ChatColor.GRAY + "Confirm");

    }

}
