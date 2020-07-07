package net.betterpvp.clans.weapon.weapons;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.insurance.InsuranceManager;
import net.betterpvp.clans.economy.shops.events.ShopTradeEvent;
import net.betterpvp.clans.economy.shops.events.TradeAction;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

public class ClanRecovery extends Weapon {

    public ClanRecovery(Clans i) {
        super(i, Material.COOKIE, (byte) 0, ChatColor.YELLOW + "Clan Recovery",
                new String[]{"",
                ChatColor.GRAY + "Reverts any damage caused to your base by TNT or Pillaging.",
                ChatColor.GRAY + "Does not include expensive blocks or storage."},
                false, 0);
    }

    @EventHandler
    public void onBuyClanRecovery(ShopTradeEvent e) {
        if (e.getAction() == TradeAction.BUY_FRAGMENTITEM) {
            if (e.getGamer().hasFragments(e.getItem().getBuyPrice())) {
                if (e.getItem().getItemName().contains("Clan Recovery")) {
                    Clan c = ClanUtilities.getClan(e.getPlayer());
                    if (c != null) {
                        if (!UtilTime.elapsed(c.getLastTnted(), 900000)) {
                            e.setCancelled("You must wait before using Clan Recovery (" + net.md_5.bungee.api.ChatColor.GREEN
                                    + UtilTime.getTime2((c.getLastTnted() + 900000) - System.currentTimeMillis(),
                                    UtilTime.TimeUnit.MINUTES, 2) + net.md_5.bungee.api.ChatColor.GRAY + ")");
                            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
                            return;
                        }
                        if (c.getInsurance().size() > 0) {
                            InsuranceManager.startRollback(c);
                            UtilMessage.message(e.getPlayer(), "Clan Recovery", "Clan recovery has begun!");
                        } else {
                            e.setCancelled("Your clan has not been damaged recently!");
                            return;
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
