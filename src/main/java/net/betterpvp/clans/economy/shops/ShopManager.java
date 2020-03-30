package net.betterpvp.clans.economy.shops;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.economy.shops.menu.PortableShopMenu;
import net.betterpvp.clans.economy.shops.menu.ShopMenu;
import net.betterpvp.clans.economy.shops.menu.TravelHubMenu;
import net.betterpvp.clans.economy.shops.menu.buttons.*;
import net.betterpvp.clans.economy.shops.mysql.ShopRepository;
import net.betterpvp.clans.economy.shops.nms.ShopSkeleton;
import net.betterpvp.clans.economy.shops.nms.ShopVillager;
import net.betterpvp.clans.economy.shops.nms.ShopZombie;
import net.betterpvp.clans.economy.shops.nms.UtilShop;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.worldevents.WEManager;
import net.betterpvp.clans.worldevents.WorldEvent;
import net.betterpvp.clans.worldevents.types.Environmental;
import net.betterpvp.clans.worldevents.types.nms.*;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.interfaces.events.ButtonClickEvent;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ShopManager extends BPVPListener<Clans> {

    private static List<ShopItem> shopItems = new ArrayList<>();
    private static List<Shop> shops = new ArrayList<>();
    private Location[] returnLocs;
    private WorldEvent lastEvent;

    public ShopManager(Clans i) {
        super(i);


        addShops("Farmer", "Weapons / Tools", "Armour", "Resources", "Building", "Fragment Vendor", "Travel Hub", "Boss Teleport");

        World world = Bukkit.getWorld("world");
        returnLocs = new Location[]{new Location(world, -5.5, 45.5, -39.5),
                new Location(world, 17.5, 42, -32.5),
                new Location(world, 32.5, 49, -14.5),
                new Location(world, 59.5, 42, -5.5),
                new Location(world, 74.5, 41.5, 25.5),
                new Location(world, 18.5, 51, 39.5),
                new Location(world, 11.5, 56.5, 77.5),
                new Location(world, -15.5, 45, 23.5),
                new Location(world, -52.5, 44.5, 26.5)};
    }


    public static List<Shop> getShops() {
        return shops;
    }

    public static List<ShopItem> getShopItems() {
        return shopItems;
    }

    public static void addShop(String name) {
        shops.add(new Shop(name));
    }


    @EventHandler
    public void refreshStocks(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.MIN_128) {
            for (ShopItem i : getShopItems()) {
                if (i instanceof DynamicShopItem) {
                    DynamicShopItem di = (DynamicShopItem) i;
                    if (di.getCurrentStock() < di.getBaseStock()) {
                        di.setCurrentStock((int) (di.getCurrentStock() + (di.getBaseStock() / 100 * 2.5)));
                    } else if (di.getCurrentStock() > di.getBaseStock()) {
                        di.setCurrentStock((int) (di.getCurrentStock() - (di.getBaseStock() / 100 * 2.5)));
                    }

                }
            }
            UtilMessage.broadcast("Shop", "Dynamic Prices have been updated!");
        }
    }


    @EventHandler
    public void updateStock(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.MIN_02) {
            for (ShopItem i : getShopItems()) {
                if (i instanceof DynamicShopItem) {
                    DynamicShopItem di = (DynamicShopItem) i;
                    ShopRepository.updateStock(di);

                }
            }

        }
    }


    public static void spawnShop(Clans i, final Location loc, final String str) {

        new BukkitRunnable() {

            @Override
            public void run() {
                if (!loc.getChunk().isLoaded()) {
                    loc.getChunk().load();
                }
                switch (str.toLowerCase()) {
                    case "armour":

                        ShopSkeleton as = new ShopSkeleton(((CraftWorld) loc.getWorld()).getHandle());
                        Skeleton armour = as.spawn(loc);
                        armour.getEquipment().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
                        createShop(ChatColor.GREEN.toString() + ChatColor.BOLD + "Armour", armour);
                        break;
                    case "weapons":

                        ShopZombie wz = new ShopZombie(((CraftWorld) loc.getWorld()).getHandle());
                        Zombie weapons = wz.spawn(loc);
                        createShop(ChatColor.GREEN.toString() + ChatColor.BOLD + "Weapons / Tools", weapons);

                        break;
                    case "farmer":
                        ShopVillager fv = new ShopVillager(((CraftWorld) loc.getWorld()).getHandle());
                        Villager farmer = fv.spawn(loc);
                        createShop(ChatColor.GREEN.toString() + ChatColor.BOLD + "Farmer", farmer);
                        break;
                    case "building":
                        ShopVillager bv = new ShopVillager(((CraftWorld) loc.getWorld()).getHandle());
                        Villager builder = bv.spawn(loc);
                        createShop(ChatColor.GREEN.toString() + ChatColor.BOLD + "Building", builder);

                        break;
                    case "resources":
                        ShopVillager rv = new ShopVillager(((CraftWorld) loc.getWorld()).getHandle());
                        Villager resources = rv.spawn(loc);
                        createShop(ChatColor.GREEN.toString() + ChatColor.BOLD + "Resources", resources);


                        break;
                    case "fragment":
                        Bukkit.broadcastMessage("Meme");
                        ShopSkeleton bs = new ShopSkeleton(((CraftWorld) loc.getWorld()).getHandle());
                        Skeleton battle = bs.spawn(loc);
                        battle.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
                        createShop(ChatColor.GREEN.toString() + ChatColor.BOLD + "Fragment Vendor", battle);

                        break;

                    case "disc":
                        ShopVillager dv = new ShopVillager(((CraftWorld) loc.getWorld()).getHandle());
                        Villager disc = dv.spawn(loc);
                        createShop(ChatColor.GREEN.toString() + ChatColor.BOLD + "Disc", disc);

                        break;
                    case "travel":
                        ShopVillager tv = new ShopVillager(((CraftWorld) loc.getWorld()).getHandle());
                        Villager travel = tv.spawn(loc);
                        createShop(ChatColor.GREEN.toString() + ChatColor.BOLD + "Travel Hub", travel);

                        break;
                    case "boss":
                        ShopZombie sv = new ShopZombie(((CraftWorld) loc.getWorld()).getHandle());
                        Zombie boss = sv.spawn(loc);

                        createShop(ChatColor.RED.toString() + ChatColor.BOLD + "Boss Teleport", boss);

                        break;

                }


            }

        }.runTask(i);

    }

    public static boolean isShop(LivingEntity ent) {
        if (!(ent instanceof Player)) {
            for (Shop s : getShops()) {
                if (ent.getCustomName() != null) {
                    if (ChatColor.stripColor(ent.getCustomName()).equals(s.getName())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static LivingEntity createShop(String name, LivingEntity ent) {
        ent.setCustomName(name);
        ent.setCustomNameVisible(true);

        ent.setRemoveWhenFarAway(false);
        ent.setCanPickupItems(false);
        return ent;
    }


    public static void addShops(String... names) {
        for (String str : names) {
            shops.add(new Shop(str));
        }
    }

    public static void addItem(String store, String itemName, int ID, int slot, byte data, int amount, int buyPrice, int sellPrice, boolean legendary, boolean glow, boolean dynamic,
                               boolean quest, int minSell, int baseSell, int maxSell, int minBuy, int baseBuy, int maxBuy, int baseStock, int maxStock, int currentStock) {
        if (legendary) {
            shopItems.add(new LegendaryShopItem(store, ID, data, slot, amount, buyPrice, itemName, glow));
        } else if (quest) {
            shopItems.add(new QuestShopItem(store, ID, data, slot, amount, itemName, buyPrice));
        } else if (dynamic) {
            shopItems.add(new DynamicShopItem(store, ID, data, slot, amount, itemName, minBuy, baseBuy, maxBuy, minSell, baseSell, maxSell, baseStock, maxStock, currentStock));
        } else {
            shopItems.add(new NormalShopItem(store, ID, data, slot, amount, itemName, buyPrice, sellPrice));
        }

    }

    public static List<ShopItem> getItemsFor(String shop) {
        List<ShopItem> temp = new ArrayList<>();
        for (ShopItem si : getShopItems()) {
            if (si.getStore().equalsIgnoreCase(shop)) {
                temp.add(si);
            }
        }

        return temp;
    }


    @EventHandler
    public void onCombust(EntityCombustEvent e) {
        if (e.getEntity() instanceof LivingEntity) {
            LivingEntity ent = (LivingEntity) e.getEntity();
            if (ent.getCustomName() != null && !ent.getCustomName().equals("")) {
                for (Shop s : getShops()) {
                    if (s.getName().equals(ChatColor.stripColor(ent.getCustomName()))) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }


    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof LivingEntity) {
            LivingEntity ent = (LivingEntity) e.getRightClicked();
            if (ent.getCustomName() != null && !ent.getCustomName().equals("")) {
                for (Shop s : getShops()) {
                    if (s.getName().equalsIgnoreCase(ChatColor.stripColor(ent.getCustomName()))) {
                        if (e.getRightClicked() instanceof Villager) {
                            e.setCancelled(true);
                        }
                        if (s.getName().toLowerCase().contains("travel")) {
                            e.getPlayer().openInventory(new TravelHubMenu(e.getPlayer()).getInventory());
                        } else if (s.getName().equalsIgnoreCase("Boss Teleport")) {

                            Clan c = ClanUtilities.getClan(e.getPlayer().getLocation());
                            Gamer gamer = GamerManager.getOnlineGamer(e.getPlayer());
                            if (gamer != null) {
                                if (UtilTime.elapsed(gamer.getLastDamaged(), 15000)) {
                                    if (!WEManager.isWorldEventActive()) {
                                        if (e.getPlayer().getWorld().getName().equalsIgnoreCase("bossworld2")) {
                                            e.getPlayer().teleport(returnLocs[UtilMath.randomInt(returnLocs.length)]);
                                            UtilMessage.message(e.getPlayer(), "World Event", "You teleported back to Fields.");
                                        } else {
                                            if (lastEvent != null) {
                                                e.getPlayer().teleport(lastEvent.getTeleportLocations()
                                                        [UtilMath.randomInt(lastEvent.getTeleportLocations().length)]);
                                                UtilMessage.message(e.getPlayer(), "World Event", "You teleported to the World Event Arena");
                                                return;
                                            }
                                        }
                                    }
                                    if (c != null) {
                                        if (c instanceof AdminClan) {
                                            if (c.getName().equalsIgnoreCase("Fields")
                                                    || c.getName().equalsIgnoreCase("Outskirts")) {
                                                WorldEvent we = WEManager.getActiveWorldEvent();
                                                if (!(we instanceof Environmental)) {


                                                    lastEvent = we;

                                                    e.getPlayer().teleport(we.getTeleportLocations()[UtilMath.randomInt(we.getTeleportLocations().length)]);
                                                    UtilMessage.message(e.getPlayer(), "World Event", "You teleported to the World Event Arena");


                                                }
                                            }
                                        }

                                    } else {
                                        if (e.getPlayer().getWorld().getName().equalsIgnoreCase("bossworld2")) {
                                            e.getPlayer().teleport(returnLocs[UtilMath.randomInt(returnLocs.length)]);
                                            UtilMessage.message(e.getPlayer(), "World Event", "You teleported back to Fields.");
                                        }
                                    }
                                } else {
                                    UtilMessage.message(e.getPlayer(), "World Event", "You cannot teleport to / from the boss arena while in combat.");
                                }
                            }
                        } else {
                            e.getPlayer().openInventory(new ShopMenu(ChatColor.stripColor(ent.getCustomName()), e.getPlayer()).getInventory());
                        }
                    }
                }
            }
        }
    }


    @EventHandler
    public void onPortableShopClick(ButtonClickEvent e) {
        if (e.getMenu() instanceof PortableShopMenu) {
            e.getPlayer().openInventory(new ShopMenu(ChatColor.stripColor(e.getButton().getName()), e.getPlayer()).getInventory());
        }
    }
}
