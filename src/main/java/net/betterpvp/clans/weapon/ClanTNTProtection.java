package net.betterpvp.clans.weapon;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.mysql.ClanRepository;
import net.betterpvp.clans.economy.shops.events.ShopTradeEvent;
import net.betterpvp.clans.economy.shops.events.TradeAction;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

public class ClanTNTProtection extends Weapon {

    public ClanTNTProtection(Clans i) {
        super(i, Material.TNT_MINECART, (byte) 0, ChatColor.YELLOW + "TNT Protection",
                new String[]{
                        "",
                        ChatColor.GRAY + "Increases the level of your clan by one.",
                        "",
                        ChatColor.GRAY + "Certain features are dependent on the level of your clan",
                        ChatColor.GRAY + "such as unlocking extra farming levels.",
                        "",
                        ChatColor.GRAY + "Max Clan Level: " + ChatColor.GREEN + 5
                }
                , false, 0.0);
    }

    @EventHandler
    public void onBuyClanRecovery(ShopTradeEvent e) {
        if (e.getAction() == TradeAction.BUY_FRAGMENTITEM) {
            if (e.getGamer().hasFragments(e.getItem().getBuyPrice())) {
                if (e.getItem().getItemName().contains("Upgrade Clan")) {
                    Clan c = ClanUtilities.getClan(e.getPlayer());
                    if (c != null) {
                        if(c.getLevel() < Clans.getOptions().getMaxClanlevel()){
                            c.setLevel(c.getLevel() + 1);
                            UtilMessage.message(e.getPlayer(), "Clans", "You upgraded your clan to level "
                                    + ChatColor.GREEN + c.getLevel() + ChatColor.GRAY + ".");
                            ClanRepository.updateLevel(c);
                        }else{
                            e.setCancelled("Your clan is already at the max level");
                        }
                    } else {
                        e.setCancelled("You need to be in a clan for this!");

                    }

                    e.setGiveItem(false);
                    return;
                }
            }
        }
    }
}
