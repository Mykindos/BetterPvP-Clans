package net.betterpvp.clans.economy.shops;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.insurance.InsuranceManager;
import net.betterpvp.clans.dailies.perks.QuestPerkManager;
import net.betterpvp.clans.economy.shops.events.ShopTradeEvent;
import net.betterpvp.clans.economy.shops.events.TradeAction;
import net.betterpvp.clans.economy.shops.events.TradeCurrency;
import net.betterpvp.clans.economy.shops.menu.ShopMenu;
import net.betterpvp.clans.economy.shops.menu.buttons.DynamicShopItem;
import net.betterpvp.clans.economy.shops.menu.buttons.LegendaryShopItem;
import net.betterpvp.clans.economy.shops.menu.buttons.QuestShopItem;
import net.betterpvp.clans.economy.shops.menu.buttons.ShopItem;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.utilities.UtilClans;
import net.betterpvp.clans.weapon.ILegendary;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.database.Log;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.interfaces.Menu;
import net.betterpvp.core.interfaces.events.ButtonClickEvent;
import net.betterpvp.core.utility.UtilFormat;
import net.betterpvp.core.utility.UtilItem;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import net.betterpvp.core.utility.recharge.Recharge;
import net.betterpvp.core.utility.recharge.RechargeManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


public class ShopListener extends BPVPListener<Clans> {

    public ShopListener(Clans i) {
        super(i);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onShopTradeStart(ShopTradeEvent e) {

        ShopItem item = e.getItem();


        if (e.getAction().name().contains("BUY")) {
            double cost = e.isShift() ? e.getItem().getBuyPrice() * 64 : e.getItem().getBuyPrice();

            if (e.getPlayer().getInventory().firstEmpty() == -1) {
                e.setCancelled("Your inventory is full.");
                return;
            }

            if (e.getCurrency() == TradeCurrency.COINS) {
                if (e.getGamer().getClient().hasDonation("ConveniencePackage")) {
                    cost = cost * 0.90;
                }

                if (QuestPerkManager.hasPerk(e.getPlayer(), "5% Shop Discount")) {
                    cost = cost * 0.95;
                }

                if (!e.getGamer().hasCoins((int) cost)) {
                    e.setCancelled("You have insufficient funds to purchase this item.");
                    return;
                }
            } else if (e.getCurrency() == TradeCurrency.FRAGMENTS) {
                if (!e.getGamer().hasFragments(cost)) {
                    e.setCancelled("You have insufficient funds to purchase this item.");
                    return;
                }

                if (e.getAction() == TradeAction.BUY_QUESTITEM) {
                    String unmod = ChatColor.stripColor(item.getName());
                    if (QuestPerkManager.getPerk(unmod) != null) {
                        if (QuestPerkManager.hasPerk(e.getPlayer(), unmod)) {
                            e.setCancelled("You already own this perk!");
                            return;
                        }
                    }
                }
            }
            return;
        }

        if (e.getAction().name().contains("SELL")) {
            if (item.getSellPrice() == 0) {
                e.setCancelled("You cannot sell this!");
                return;
            }

            if (item.getStore().equalsIgnoreCase(ChatColor.stripColor("Fragment Vendor"))) {
                e.setCancelled("You cannot sell items here!");
                return;
            }

            if (e.getAction() != TradeAction.SELL_IGNATIUSITEM) {
                if (item instanceof LegendaryShopItem || item instanceof QuestShopItem) {
                    e.setCancelled("You cannot sell this type of item!");
                    return;
                }
            }

            return;
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onShopTradeFinish(ShopTradeEvent e) {
        if (e.isCancelled()) {
            UtilMessage.message(e.getPlayer(), "Shop", e.getCancelReason());
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 0.6F);
            return;
        }

        Gamer gamer = GamerManager.getOnlineGamer(e.getPlayer());

        if (e.getAction().name().contains("BUY")) {
            int amount = e.isShift() ? 64 : e.getItem().getAmount();
            double cost = e.isShift() ? e.getItem().getBuyPrice() * 64 : e.getItem().getBuyPrice();

            if (e.getCurrency() == TradeCurrency.COINS) {

                if (e.getGamer().getClient().hasDonation("ConveniencePackage")) {
                    cost = cost * 0.90;
                }

                if (QuestPerkManager.hasPerk(e.getPlayer(), "5% Shop Discount")) {
                    cost = cost * 0.95;
                }

                ItemStack k;
                Weapon wep = WeaponManager.getWeapon(e.getItem().getItemName());
                if (wep != null) {
                    if (wep instanceof ILegendary) {
                        k = wep.createWeapon(true);
                    } else {
                        k = wep.createWeaponNoGlow();
                        k.setAmount(amount);
                    }

                } else {
                    k = UtilClans.updateNames(new ItemStack(e.getItem().getItemStack().getType(), amount));
                    k.getItemMeta().setLore(null);
                }

                gamer.removeCoins(cost);
                if (k.getType() == Material.TNT) {
                    Log.write("TNT Purchase", e.getPlayer().getName() + " purchased 1 tnt");
                    ClientUtilities.messageStaff("TNT Purchase", ChatColor.YELLOW + e.getPlayer().getName() + ChatColor.GRAY + " purchased 1 TnT!");
                }


                if (e.getItem() instanceof DynamicShopItem) {
                    DynamicShopItem di = (DynamicShopItem) e.getItem();
                    int newStock = di.getCurrentStock() - amount;
                    if (newStock < di.getMinStock()) {
                        newStock = di.getMinStock();
                        di.setCurrentStock(newStock);
                    } else {
                        di.setCurrentStock(newStock);
                    }

                    Menu m = Menu.getMenu(e.getPlayer().getOpenInventory().getTopInventory(), e.getPlayer().getOpenInventory().getTitle(), e.getPlayer());
                    if (m != null) {
                        if (m.getButtons().contains(di)) {
                            m.getButtons().remove(di);
                            di.updateItem();
                            m.getButtons().add(di);
                            m.construct();
                        }
                    }
                    for (Menu menus : Menu.menus) {
                        if (menus instanceof ShopMenu) {
                            menus.construct();
                        }
                    }
                    e.getPlayer().updateInventory();


                }

                UtilMessage.message(e.getPlayer(), "Shop", "You have purchased " + ChatColor.YELLOW + amount + " " + e.getItem().getItemName() +
                        ChatColor.GRAY + " for " + ChatColor.GREEN + "$" + UtilFormat.formatNumber((int) cost));
                Log.write("Shop", e.getPlayer().getName() + " " + "purchased " + +amount + " " + e.getItem().getItemName() +
                        " for " + "$" + UtilFormat.formatNumber((int) cost));

                UtilItem.insert(e.getPlayer(), k);
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);

            } else if (e.getCurrency() == TradeCurrency.FRAGMENTS) {
                if (e.getAction() == TradeAction.BUY_FRAGMENTITEM) {
                    ItemStack k;
                    Weapon wep = WeaponManager.getWeapon(e.getItem().getItemName());
                    if (wep != null) {
                        if (wep instanceof ILegendary) {
                            k = wep.createWeapon(true);
                        } else {
                            k = wep.createWeaponNoGlow();
                            k.setAmount(amount);
                        }

                    } else {
                        k = UtilClans.updateNames(new ItemStack(e.getItem().getItemStack().getType(), amount));
                        k.getItemMeta().setLore(null);
                    }

                    gamer.takeFragments((int) cost);
                    UtilMessage.message(e.getPlayer(), "Shop", "You have purchased " + ChatColor.YELLOW + amount + " " + e.getItem().getItemName() +
                            ChatColor.GRAY + " for " + ChatColor.GREEN + UtilFormat.formatNumber((int) cost) + ChatColor.GRAY + " fragments");
                    Log.write("Shop", e.getPlayer().getName() + " " + "purchased " + amount + " "
                            + e.getItem().getItemName() + " for " + UtilFormat.formatNumber((int) cost) + " fragments");

                    if (e.isGiveItem()) {
                        UtilItem.insert(e.getPlayer(), k);

                        if (wep != null && wep instanceof ILegendary) {
                            e.getPlayer().updateInventory();
                        }
                    }
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
                } else if (e.getAction() == TradeAction.BUY_QUESTITEM) {
                    String unmod = ChatColor.stripColor(e.getItem().getName());
                    if (QuestPerkManager.getPerk(unmod) != null) {
                        QuestPerkManager.addPerk(e.getPlayer(), unmod);
                        return;
                    }

                    e.getGamer().takeFragments((int) cost);
                    UtilMessage.message(e.getPlayer(), "Shop", "You have purchased " + ChatColor.YELLOW + e.getItem().getAmount()
                            + " " + e.getItem().getItemName() + ChatColor.GRAY + " for " + ChatColor.GREEN
                            + UtilFormat.formatNumber((int) cost) + ChatColor.GRAY + " fragments");
                    Log.write("Shop", e.getPlayer().getName() + " " + "purchased " + e.getItem().getAmount()
                            + " " + e.getItem().getItemName() + " for " + UtilFormat.formatNumber((int) cost) + " fragments");
                }
            }

            return;
        }

        if (e.getAction().name().contains("SELL")) {
            if (e.getAction() == TradeAction.SELL_IGNATIUSITEM) {
                if (e.getItem() instanceof LegendaryShopItem) {
                    sellIgnatiusLegendary(e);
                    return;
                }
            }
            sellItem(gamer, e.getPlayer(), e.getItem(), e.isShift());
            return;
        }

    }


    @EventHandler
    public void buttonClick(ButtonClickEvent e) {
        if (e.getButton() instanceof ShopItem) {
            Player p = e.getPlayer();
            ShopItem item = (ShopItem) e.getButton();
            boolean isShifting = e.getClickType().name().contains("SHIFT");
            Gamer gamer = GamerManager.getOnlineGamer(p);
            if(!RechargeManager.getInstance().add(p, "Shop", 0.15, false, true)){
                return;
            }
            if (e.getClickType().name().contains("LEFT")) {

                if (item.getStore().equalsIgnoreCase(ChatColor.stripColor("Fragment Vendor"))) {
                    if (item instanceof QuestShopItem) {
                        Bukkit.getPluginManager().callEvent(new ShopTradeEvent(gamer, p, item, TradeAction.BUY_QUESTITEM, TradeCurrency.FRAGMENTS, isShifting));
                        return;
                    }

                    Bukkit.getPluginManager().callEvent(new ShopTradeEvent(gamer, p, item, TradeAction.BUY_FRAGMENTITEM, TradeCurrency.FRAGMENTS, isShifting));
                    return;
                }

                if (item.getStore().equalsIgnoreCase(ChatColor.stripColor("Ignatius"))) {
                    Bukkit.getPluginManager().callEvent(new ShopTradeEvent(gamer, p, item, TradeAction.BUY_IGNATIUSITEM, TradeCurrency.COINS, isShifting));
                    return;
                }

                Bukkit.getPluginManager().callEvent(new ShopTradeEvent(gamer, p, item, TradeAction.BUY, TradeCurrency.COINS, isShifting));
            } else if (e.getClickType().name().contains("RIGHT")) {


                if (item.getStore().equalsIgnoreCase(ChatColor.stripColor("Ignatius"))) {
                    Bukkit.getPluginManager().callEvent(new ShopTradeEvent(gamer, p, item, TradeAction.SELL_IGNATIUSITEM, TradeCurrency.COINS, isShifting));
                    return;
                }

                Bukkit.getPluginManager().callEvent(new ShopTradeEvent(gamer, p, item, TradeAction.SELL, TradeCurrency.COINS, isShifting));

            }

        }
    }


    private void sellItem(Gamer gamer, Player p, ShopItem item, boolean shift) {

        int amount;
        int cost;
        if (p.getInventory().contains(item.getItemStack().getType())) {
            for (int x = 0; x < p.getInventory().getSize(); x++) {
                ItemStack i = p.getInventory().getItem(x);

                if (i != null) {


                    if (i.hasItemMeta()) {
                        Weapon wep = WeaponManager.getWeapon(ChatColor.stripColor(i.getItemMeta().getDisplayName()));
                        if (wep != null) {
                            if (wep instanceof ILegendary) {
                                continue;
                            }
                        }
                    }
                    amount = shift ? i.getAmount() : item.getAmount();
                    cost = shift ? i.getAmount() * item.getSellPrice() : item.getSellPrice();


                    if (item.getItemStack().getType() == i.getType()) {
                        if (i.getAmount() >= amount) {
                            p.getInventory().setItem(x, new ItemStack(i.getType(), i.getAmount() - amount));
                            ItemStack temp = p.getInventory().getItem(x);
                            if (temp != null) {
                                if (temp.getAmount() < 1) {
                                    p.getInventory().setItem(x, new ItemStack(Material.AIR));
                                }
                            }

                            if (item instanceof DynamicShopItem) {
                                DynamicShopItem di = (DynamicShopItem) item;
                                int newStock = di.getCurrentStock() + amount;
                                if (newStock > di.getMaxStock()) {
                                    newStock = di.getMaxStock();
                                    di.setCurrentStock(newStock);
                                } else {
                                    di.setCurrentStock(newStock);

                                }

                                Menu m = Menu.getMenu(p.getOpenInventory().getTopInventory(), p.getOpenInventory().getTitle(), p);
                                if (m != null) {
                                    if (m.getButtons().contains(di)) {
                                        m.getButtons().remove(di);
                                        di.updateItem();
                                        m.getButtons().add(di);
                                        m.construct();
                                    }
                                }
                                for (Menu menus : Menu.menus) {
                                    if (menus instanceof ShopMenu) {
                                        menus.construct();
                                    }
                                }
                                p.updateInventory();

                            }

                            gamer.addCoins(cost);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
                            UtilMessage.message(p, "Shop", "You sold " + ChatColor.YELLOW + amount + " "
                                    + item.getItemName() + ChatColor.GRAY + " for "
                                    + ChatColor.GREEN + "$" + UtilFormat.formatNumber(cost));

                            Log.write("Shop", p.getName() + " sold " + +amount + " " + item.getItemName() +
                                    " for " + "$" + UtilFormat.formatNumber((int) cost));

                            return;
                        }
                    }
                }
            }
        }


    }

    private void sellIgnatiusLegendary(ShopTradeEvent e){
        if (e.getPlayer().getInventory().contains(e.getItem().getItemStack().getType())) {
            for (int x = 0; x < e.getPlayer().getInventory().getSize(); x++) {
                ItemStack i = e.getPlayer().getInventory().getItem(x);
                if (i != null) {
                    if (i.hasItemMeta()) {
                        Weapon wep = WeaponManager.getWeapon(ChatColor.stripColor(i.getItemMeta().getDisplayName()));
                        if (wep != null) {
                            if (wep instanceof ILegendary) {
                                int cost = e.getItem().getSellPrice();

                                e.getPlayer().getInventory().setItem(x, new ItemStack(Material.AIR));
                                e.getGamer().addCoins(cost);
                                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
                                UtilMessage.message(e.getPlayer(), "Shop", "You sold " + ChatColor.YELLOW + "1"
                                        + e.getItem().getItemName() + ChatColor.GRAY + " for "
                                        + ChatColor.GREEN + "$" + UtilFormat.formatNumber(cost));

                                Log.write("Shop", e.getPlayer().getName() + " sold 1 " + e.getItem().getItemName() +
                                        " for " + "$" + UtilFormat.formatNumber((int) cost));

                                break;
                            }
                        }
                    }

                }
            }
        }
    }


}
