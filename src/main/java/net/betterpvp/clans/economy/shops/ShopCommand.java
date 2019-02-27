package net.betterpvp.clans.economy.shops;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.Dominance;
import net.betterpvp.clans.clans.Pillage;
import net.betterpvp.clans.economy.shops.mysql.ShopKeeperRepository;
import net.betterpvp.clans.economy.shops.mysql.ShopRepository;
import net.betterpvp.clans.economy.shops.nms.ShopSkeleton;
import net.betterpvp.clans.economy.shops.nms.ShopVillager;
import net.betterpvp.clans.economy.shops.nms.ShopZombie;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.configs.ConfigManager;
import net.betterpvp.core.configs.Configs;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;


public class ShopCommand extends Command implements Listener {

    private Clans i;
    private HashMap<UUID, Long> cd = new HashMap<>();

    public ShopCommand(Clans i) {
        super("shop", new String[]{"shops"}, Rank.PLAYER);
        Bukkit.getPluginManager().registerEvents(this, i);
        this.i = i;
        // TODO Auto-generated constructor stub
    }


    public void spawnShop(Player p, String str) {
        switch (str.toLowerCase()) {
            case "armour":
                ShopSkeleton as = new ShopSkeleton(((CraftWorld) p.getLocation().getWorld()).getHandle());
                Skeleton armour = as.spawn(p.getLocation());

                armour.getEquipment().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
                createShop(ChatColor.GREEN.toString() + ChatColor.BOLD + "Armour", armour);
                //Skeleton  armour = (Skeleton) createShop(ChatColor.GREEN + "Armour", p.getp.getLocation()ation(), EntityType.SKELETON);

                //armour.getEquipment().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
                break;
            case "weapons":
                //ShopZombie  weapons = (ShopZombie) createShop(ChatColor.GREEN + "Tools / Weapons", p.getp.getLocation()ation(), EntityType.ZOMBIE);
                ShopZombie wz = new ShopZombie(((CraftWorld) p.getLocation().getWorld()).getHandle());
                Zombie weapons = wz.spawn(p.getLocation());
                createShop(ChatColor.GREEN.toString() + ChatColor.BOLD + "Weapons / Tools", weapons);

                break;
            case "farmer":
                ShopVillager fv = new ShopVillager(((CraftWorld) p.getLocation().getWorld()).getHandle());
                Villager farmer = fv.spawn(p.getLocation());
                createShop(ChatColor.GREEN.toString() + ChatColor.BOLD + "Farmer", farmer);
                break;
            case "building":
                ShopVillager bv = new ShopVillager(((CraftWorld) p.getLocation().getWorld()).getHandle());
                Villager builder = bv.spawn(p.getLocation());
                createShop(ChatColor.GREEN.toString() + ChatColor.BOLD + "Building", builder);

                break;
            case "resources":
                ShopVillager rv = new ShopVillager(((CraftWorld) p.getLocation().getWorld()).getHandle());
                Villager resources = rv.spawn(p.getLocation());
                createShop(ChatColor.GREEN.toString() + ChatColor.BOLD + "Resources", resources);
                //createShop(ChatColor.GREEN + "Resources", p.getp.getLocation()ation(), EntityType.VILLAGER);

                break;
            case "fragment":
                ShopSkeleton bs = new ShopSkeleton(((CraftWorld) p.getLocation().getWorld()).getHandle());
                Skeleton battle = bs.spawn(p.getLocation());
                battle.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
                createShop(ChatColor.GREEN.toString() + ChatColor.BOLD + "Fragment Vendor", battle);

                break;

            case "disc":
                ShopVillager dv = new ShopVillager(((CraftWorld) p.getLocation().getWorld()).getHandle());
                Villager disc = dv.spawn(p.getLocation());

                createShop(ChatColor.GREEN.toString() + ChatColor.BOLD + "Disc", disc);

                break;
            case "travel":
                ShopVillager tv = new ShopVillager(((CraftWorld) p.getLocation().getWorld()).getHandle());
                Villager travel = tv.spawn(p.getLocation());

                createShop(ChatColor.GREEN.toString() + ChatColor.BOLD + "Travel Hub", travel);

                break;
            case "boss":
                ShopZombie sv = new ShopZombie(((CraftWorld) p.getLocation().getWorld()).getHandle());
                Zombie boss = sv.spawn(p.getLocation());

                createShop(ChatColor.RED.toString() + ChatColor.BOLD + "Boss Teleport", boss);

                break;

        }

    }

    private LivingEntity createShop(String name, LivingEntity ent) {
        ent.setCustomName(name);
        ent.setCustomNameVisible(true);
        ent.setRemoveWhenFarAway(false);
        ent.setCanPickupItems(false);

        return ent;
    }



    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.SEC) {
            Iterator<Entry<UUID, Long>> it = cd.entrySet().iterator();
            while (it.hasNext()) {
                Entry<UUID, Long> next = it.next();
                if (UtilTime.elapsed(next.getValue(), 60000 * 5)) {
                    Player p = Bukkit.getPlayer(next.getKey());
                    if (p != null) {
                        RechargeManager.getInstance().add(p, "Portable Shop", 7200, true, false, false);
                        UtilMessage.message(p, "Shop", "Portable Shop is now on cooldown!");
                        it.remove();
                    }

                }
            }
        }
    }


    @Override
    public void execute(final Player p, String[] args) {
        if (args != null) {
            if (ClientUtilities.getOnlineClient(p).hasRank(Rank.ADMIN, true)) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {

                        p.sendMessage("Shops have been reloaded");
                        for (World w : Bukkit.getWorlds()) {
                            for (LivingEntity e : w.getLivingEntities()) {
                                if (e instanceof Player || e instanceof ArmorStand) continue;

                                for (Shop s : ShopManager.getShops()) {
                                    if (e.getCustomName() != null) {

                                        if (s.getName().equalsIgnoreCase(ChatColor.stripColor(e.getCustomName()))) {

                                            e.setHealth(0);
                                            e.remove();
                                        }
                                    }

                                }
                            }
                            ShopRepository.loadShops(i);
                        }
                    }
                } else if (args.length > 0) {
                    if (ClientUtilities.getOnlineClient(p).hasRank(Rank.ADMIN, true)) {
                        if (args.length == 2) {
                            if (args[0].equalsIgnoreCase("create")) {
                                spawnShop(p, args[1]);
                                ShopKeeperRepository.addKeeper(args[1], p.getLocation());
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onUpdateSh(UpdateEvent ev) {
        if (ev.getType() == UpdateEvent.UpdateType.MIN_16) {
            for (World w : Bukkit.getWorlds()) {
                for (LivingEntity e : w.getLivingEntities()) {
                    if (e instanceof Player || e instanceof ArmorStand) continue;

                    for (Shop s : ShopManager.getShops()) {
                        //Bukkit.broadcastMessage(s.getName());
                        if (e.getCustomName() != null) {

                            if (s.getName().equalsIgnoreCase(ChatColor.stripColor(e.getCustomName()))) {

                                e.setHealth(0);
                                e.remove();
                            }
                        }
                    }
                }
            }
            ShopRepository.loadShops(i);
        }
    }


    @Override
    public void help(Player player) {
        // TODO Auto-generated method stub

    }


}
