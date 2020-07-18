package net.betterpvp.clans.economy.shops;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.insurance.InsuranceManager;
import net.betterpvp.clans.dailies.perks.QuestPerkManager;
import net.betterpvp.clans.economy.shops.events.ShopTradeEvent;
import net.betterpvp.clans.economy.shops.events.TradeAction;
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onShopTrade(ShopTradeEvent e) {
        if (e.isCancelled()) {
            UtilMessage.message(e.getPlayer(), "Shop", e.getCancelReason());
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 0.6F);
            return;
        }

        Gamer g = GamerManager.getOnlineGamer(e.getPlayer());

        if (e.getAction() == TradeAction.BUY_FRAGMENTITEM) {
            int amount = e.isShift() ? 64 : e.getItem().getAmount();
            double cost = e.isShift() ? e.getItem().getBuyPrice() * 64 : e.getItem().getBuyPrice();

            if (e.getPlayer().getInventory().firstEmpty() != -1) {

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


                if (g.hasFragments(cost)) {
                    g.takeFragments((int) cost);
                    UtilMessage.message(e.getPlayer(), "Shop", "You have purchased " + ChatColor.YELLOW + amount + " " + e.getItem().getItemName() +
                            ChatColor.GRAY + " for " + ChatColor.GREEN + UtilFormat.formatNumber((int) cost) + ChatColor.GRAY + " fragments");
                    Log.write("Shop", e.getPlayer().getName() + " " + "purchased " + +amount + " "
                            + e.getItem().getItemName() + " for " + UtilFormat.formatNumber((int) cost) + " fragments");
                } else {
                    UtilMessage.message(e.getPlayer(), "Shop", "You have insufficient funds to purchase this item.");
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 0.6F);
                    return;
                }


                if (e.isGiveItem()) {
                    UtilItem.insert(e.getPlayer(), k);

                    if(wep != null && wep instanceof ILegendary){
                        e.getPlayer().updateInventory();
                    }
                }
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
            }else{
                UtilMessage.message(e.getPlayer(), "Shop", "Your inventory is currently too full to purchase this item.");
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 0.6F);
            }
        }
    }




    @EventHandler
    public void buttonClick(ButtonClickEvent e) {
        if (e.getButton() instanceof ShopItem) {
            Player p = e.getPlayer();
            ShopItem item = (ShopItem) e.getButton();

            if (e.getClickType() == ClickType.LEFT) {
                if (item.getStore().equalsIgnoreCase(ChatColor.stripColor("Fragment Vendor"))) {
                    if (item instanceof QuestShopItem) {
                        // Bukkit.getPluginManager().callEvent(new ShopTradeEvent(p, item, TradeAction.BUY_QUESTITEM, false));
                        buyQuestItem(p, item, false);
                        return;
                    }
                    Gamer gamer = GamerManager.getOnlineGamer(p);
                    Bukkit.getPluginManager().callEvent(new ShopTradeEvent(gamer, p, item, TradeAction.BUY_FRAGMENTITEM, false));
                    //buyItem(p, item, false, true);
                    return;
                }
            } else if (e.getClickType() == ClickType.RIGHT) {

                if (item.getSellPrice() == 0) {
                    UtilMessage.message(e.getPlayer(), "Shop", "You cannot sell this!");
                    return;
                }

                if (item.getStore().equalsIgnoreCase(ChatColor.stripColor("Fragment Vendor"))) {
                    UtilMessage.message(p, "Shop", "You cannot sell items here!");
                    return;
                }
            }

            if (item.getStore().equalsIgnoreCase(ChatColor.stripColor("Fragment Vendor"))) {
                return;
            }


            switch (e.getClickType()) {
                case LEFT:
                    buyItem(p, item, false, false);
                    break;
                case RIGHT:
                    sellItem(p, item, false);
                    break;
                case SHIFT_LEFT:
                    buyItem(p, item, true, false);
                    break;
                case SHIFT_RIGHT:
                    sellItem(p, item, true);
                    break;
                default:
                    return;
            }
        }
    }


    private void sellItem(Player p, ShopItem item, boolean shift) {
        Gamer g = GamerManager.getOnlineGamer(p);
        if (item instanceof LegendaryShopItem || item instanceof QuestShopItem) {
            UtilMessage.message(p, "Shop", "You cannot sell this type of item!");
            return;
        }


        int amount;
        int cost = item.getSellPrice();
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
                    //	if(i.getData().getData() == item.getData()){


                    if (item.getItemStack().getType() == i.getType()) {
                        if (i.getAmount() >= amount) {
                            if (item.getStore().equalsIgnoreCase("Farmer") && !(item.getData() == i.getData().getData())) {
                                continue;
                            }
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

                            g.addCoins(cost);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
                            UtilMessage.message(p, "Shop", "You sold " + ChatColor.YELLOW + amount + " "
                                    + item.getItemName() + ChatColor.GRAY + " for "
                                    + ChatColor.GREEN + "$" + UtilFormat.formatNumber(cost));

                            Log.write("Shop", p.getName() + " " + "sold " + +amount + " " + item.getItemName() +
                                    " for " + "$" + UtilFormat.formatNumber((int) cost));

                            return;
                        }
                    }
                }
                //}
            }
        }


    }

    @SuppressWarnings("deprecation")
    private void buyItem(Player p, ShopItem item, boolean shift, boolean shards) {
        int amount = shift ? 64 : item.getAmount();
        double cost = shift ? item.getBuyPrice() * 64 : item.getBuyPrice();
        Gamer g = GamerManager.getOnlineGamer(p);
        if (p.getInventory().firstEmpty() != -1) {

            ItemStack k;
            Weapon wep = WeaponManager.getWeapon(item.getItemName());
            if (wep != null) {
                if (wep instanceof ILegendary) {
                    k = wep.createWeapon(true);
                } else {
                    k = wep.createWeaponNoGlow();
                    k.setAmount(amount);
                }

            } else {
                k = UtilClans.updateNames(new ItemStack(item.getItemStack().getType(), amount, item.getData()));
                k.getItemMeta().setLore(null);
            }


            if (shards) {
                if (g.hasFragments(cost)) {
                    g.takeFragments((int) cost);
                    UtilMessage.message(p, "Shop", "You have purchased " + ChatColor.YELLOW + amount + " " + item.getItemName() +
                            ChatColor.GRAY + " for " + ChatColor.GREEN + UtilFormat.formatNumber((int) cost) + ChatColor.GRAY + " fragments");
                    Log.write("Shop", p.getName() + " " + "purchased " + +amount + " "
                            + item.getItemName() + " for " + UtilFormat.formatNumber((int) cost) + " fragments");
                } else {
                    UtilMessage.message(p, "Shop", "You have insufficient funds to purchase this item.");
                    p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 0.6F);
                    return;
                }
            } else {

                if(g.getClient().hasDonation("ConveniencePackage")){
                    if (k.getType() != Material.MUSIC_DISC_13 && k.getType() != Material.MUSIC_DISC_PIGSTEP && k.getType() != Material.MUSIC_DISC_WAIT) {
                        cost = cost * 0.90;
                    }
                }

                if (QuestPerkManager.hasPerk(p, "5% Shop Discount")) {
                    if (k.getType() != Material.MUSIC_DISC_13 && k.getType() != Material.MUSIC_DISC_PIGSTEP && k.getType() != Material.MUSIC_DISC_WAIT) {
                        cost = cost * 0.95;
                    }
                }


                if (!g.hasCoins((int) cost)) {
                    UtilMessage.message(p, "Shop", "You have insufficient funds to purchase this item.");
                    p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 0.6F);
                    return;
                }


                g.removeCoins(cost);
                if (k.getType() == Material.TNT) {
                    Log.write("TNT Purchase", p.getName() + " purchased 1 tnt");
                    ClientUtilities.messageStaff("TNT Purchase", ChatColor.YELLOW + p.getName() + ChatColor.GRAY + " purchased 1 TnT!");
                }


                if (item instanceof DynamicShopItem) {
                    DynamicShopItem di = (DynamicShopItem) item;
                    int newStock = di.getCurrentStock() - amount;
                    if (newStock < di.getMinStock()) {
                        newStock = di.getMinStock();
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

                UtilMessage.message(p, "Shop", "You have purchased " + ChatColor.YELLOW + amount + " " + item.getItemName() +
                        ChatColor.GRAY + " for " + ChatColor.GREEN + "$" + UtilFormat.formatNumber((int) cost));
                Log.write("Shop", p.getName() + " " + "purchased " + +amount + " " + item.getItemName() +
                        " for " + "$" + UtilFormat.formatNumber((int) cost));

            }

            if (item.getItemName().equalsIgnoreCase("Clan Recovery")) {
                Clan c = ClanUtilities.getClan(p);
                if (c != null) {
                    if (!UtilTime.elapsed(c.getLastTnted(), 900000)) {
                        UtilMessage.message(p, "Clan Recovery", "You must wait before using Clan Recovery (" + ChatColor.GREEN
                                + UtilTime.getTime2((c.getLastTnted() + 900000) - System.currentTimeMillis(),
                                UtilTime.TimeUnit.MINUTES, 2) + ChatColor.GRAY + ")");
                        g.addFragments(50);
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
                        return;
                    }
                    if (c.getInsurance().size() > 0) {
                        InsuranceManager.startRollback(c);
                        UtilMessage.message(p, "Clan Recovery", "Clan recovery has begun!");
                    } else {
                        UtilMessage.message(p, "Clan Recovery", "Your clan has not been damaged recently!");
                        g.addFragments(50);
                    }
                } else {
                    UtilMessage.message(p, "Clan Recovery", "You need to be in a clan for this!");
                    g.addFragments(50);
                }
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
                return;
            }

            UtilItem.insert(p, k);
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
        } else {
            UtilMessage.message(p, "Shop", "Your inventory is currently too full to purchase this item.");
            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 0.6F);

        }
    }

    private void buyQuestItem(Player p, ShopItem item, boolean shift) {
        int amount = shift ? 64 : item.getAmount();
        double cost = shift ? item.getBuyPrice() * 64 : item.getBuyPrice();
        Gamer g = GamerManager.getOnlineGamer(p);
        if (p.getInventory().firstEmpty() != -1) {

            ItemStack k;
            k = UtilClans.updateNames(new ItemStack(item.getItemStack().getType(), amount, item.getData()));
            k.getItemMeta().setLore(null);


            String unmod = ChatColor.stripColor(item.getName());
            if (QuestPerkManager.getPerk(unmod) != null) {

                if (QuestPerkManager.hasPerk(p, unmod)) {
                    UtilMessage.message(p, "Shop", "You already own this perk!");
                    return;
                }
            }


            if (g.hasFragments((int) cost)) {
                g.takeFragments((int) cost);
                UtilMessage.message(p, "Shop", "You have purchased " + ChatColor.YELLOW + amount + " " + item.getItemName() +
                        ChatColor.GRAY + " for " + ChatColor.GREEN + UtilFormat.formatNumber((int) cost) + ChatColor.GRAY + " fragments");
                Log.write("Shop", p.getName() + " " + "purchased " + +amount + " " + item.getItemName() + " for " + UtilFormat.formatNumber((int) cost) + " fragments");
            } else {
                UtilMessage.message(p, "Shop", "You have insufficient funds to purchase this item.");
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 0.6F);
                return;
            }
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);


            if (QuestPerkManager.getPerk(unmod) != null) {

                QuestPerkManager.addPerk(p, unmod);
                return;
            }

            UtilItem.insert(p, k);


        } else {
            UtilMessage.message(p, "Shop", "Your inventory is currently too full to purchase this item.");
            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 0.6F);

        }
    }


}
