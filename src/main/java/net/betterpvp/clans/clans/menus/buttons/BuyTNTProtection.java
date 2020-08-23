package net.betterpvp.clans.clans.menus.buttons;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;

public class BuyTNTProtection extends ClanMenuButton {


    public BuyTNTProtection(Clan clan) {
        super(clan, 22, new ItemStack(Material.TNT_MINECART), ChatColor.GREEN + "Buy Instant TNT Protection",
                "",
                ChatColor.GRAY + "The next time everyone in your clan logs out, ",
                ChatColor.GRAY + "you will be immediately protected from TNT.",
                "",
                ChatColor.GRAY + "Note: If anyone in your clan logs back in, the effect is removed,",
                ChatColor.GRAY + "and will need to be purchased again if you wish to regain instant protection.",
                "",
                ChatColor.RED + "Only purchase this if your entire clan is planning to log off for a while e.g. to sleep.",
                "",
                ChatColor.RED + "Takes 15 minutes of no tnt to activate.",
                ChatColor.RED + "If you are TNT'd, the 15 minute timer resets.",
                "",
                ChatColor.GRAY + "Price: " + ChatColor.YELLOW + "$250,000 coins",
                "",
                ChatColor.GREEN + "Left-Click: " + ChatColor.GRAY + "Confirm");

    }


}
