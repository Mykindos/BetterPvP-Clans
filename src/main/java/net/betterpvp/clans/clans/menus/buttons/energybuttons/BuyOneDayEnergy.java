package net.betterpvp.clans.clans.menus.buttons.energybuttons;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.menus.buttons.ClanMenuButton;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;

public class BuyOneDayEnergy extends ClanMenuButton {


    public BuyOneDayEnergy(Clan clan) {
        super(clan, 4, new ItemStack(Material.EMERALD), ChatColor.GREEN + "Buy 1 Day of Energy",
                "",
                ChatColor.GRAY + "Buy " + ChatColor.GREEN + ((clan.getTerritory().size() * 25) * 24)
                        + ChatColor.GRAY + " energy for " + ChatColor.YELLOW + "$"
                        + NumberFormat.getInstance().format((((clan.getTerritory().size() * 25) * Clans.getOptions().getCostPerEnergy()) * 24))
                        + ChatColor.YELLOW + " coins.",
                "",
                ChatColor.GREEN + "Left-Click: " + ChatColor.GRAY + "Confirm");

    }


}
