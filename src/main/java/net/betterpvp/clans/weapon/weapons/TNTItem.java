package net.betterpvp.clans.weapon.weapons;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.mysql.ClanRepository;
import net.betterpvp.clans.economy.shops.events.ShopTradeEvent;
import net.betterpvp.clans.economy.shops.events.TradeAction;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

public class TNTItem extends Weapon {

    public TNTItem(Clans i) {
        super(i, Material.TNT, (byte) 0, ChatColor.YELLOW + "TNT",
                new String[]{
                        "",
                        ChatColor.GRAY + "Cause damage to another clans base without needing to conquer them.",
                        "",
                        ChatColor.GRAY + "Can only be used if your clan is " +ChatColor.RED + "+5 " + ChatColor.GRAY + "dominance on the target clan.",
                        ChatColor.GRAY + "When used, the target clan cannot place blocks for 5 minutes.",
                        ""
                }
                , false, 0.0);
    }


}
