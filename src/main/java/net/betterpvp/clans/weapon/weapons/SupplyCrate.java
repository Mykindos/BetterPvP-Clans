package net.betterpvp.clans.weapon.weapons;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.*;
import net.betterpvp.core.utility.restoration.BlockRestoreData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.World.Environment;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.WeakHashMap;

public class SupplyCrate extends Weapon {

    public static List<SupplyCrateData> crates = new ArrayList<>();
    private WeakHashMap<Location, Long> removeLater = new WeakHashMap<>();

    public SupplyCrate(Clans i) {
        super(i, Material.BEACON, (byte) 0, ChatColor.LIGHT_PURPLE + "Supply Crate", new String[]{
                ChatColor.GRAY + "Placing this block will call",
                ChatColor.GRAY + "down a supply crate which contains",
                ChatColor.GRAY + "a wide variety of items",
                "",
                ChatColor.GRAY + "This can only be placed in the wilderness."
        }, false, 0.0);
        // TODO Auto-generated constructor stub
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlace(BlockPlaceEvent e) {

        if (e.isCancelled()) {
            return;
        }
        if (e.getBlock().getType() == Material.BEACON) {

            if (e.getBlock().getWorld().getEnvironment() != Environment.NORMAL) {
                UtilMessage.message(e.getPlayer(), "Supply Crate", "You can only call a supply crate in the main world!");
                e.setCancelled(true);
                return;
            }
            Player p = e.getPlayer();
            Chunk chunk = p.getLocation().getChunk();
            World world = p.getWorld();
            for (SupplyCrateData s : crates) {
                if (s.getLocation().getChunk() == e.getBlock().getChunk()) {
                    UtilMessage.message(p, "Supply Crate", "There can only be 1 Supply Crate per chunk at a time.");
                    e.setCancelled(true);
                    return;
                }
            }
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    Chunk testedChunk = world.getChunkAt(chunk.getX() + x, chunk.getZ() + z);
                    Clan c = ClanUtilities.getClan(e.getBlock().getLocation());
                    for (Clan clans : ClanUtilities.clans) {
                        if (clans.getTerritory().contains(UtilFormat.chunkToFile(testedChunk))) {
                            if (c != null) {
                                if (clans.equals(c)) {
                                    continue;
                                }
                            }
                            e.setCancelled(true);
                            UtilMessage.message(p, "Supply Crate", "You cannot place this next to another clans territory.");
                            return;
                        }
                    }
                }
            }

            if (ClanUtilities.getClan(e.getBlock().getLocation()) != null) {
                UtilMessage.message(p, "Supply Crate", "Supply crates may only be called in the wilderness!");
                e.setCancelled(true);
                return;
            }

            removeLater.put(e.getBlock().getLocation(), System.currentTimeMillis());
            UtilMessage.broadcast("Supply Crate", ChatColor.GREEN + p.getName() + ChatColor.GRAY + " has called in a " + ChatColor.YELLOW + "Supply Crate" + ChatColor.GRAY
                    + " at X: " + e.getBlock().getX() + " Z: " + e.getBlock().getZ());
            crates.add(new SupplyCrateData(e.getBlock().getLocation()));

            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    new BlockRestoreData(e.getBlock().getLocation().add(x, -1, z).getBlock(), 42, (byte) 0, 70000);
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.BEACON) {
            if (!ClientUtilities.getOnlineClient(e.getPlayer()).isAdministrating()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void removeLater(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.SLOW) {
            Iterator<Entry<Location, Long>> it = removeLater.entrySet().iterator();
            while (it.hasNext()) {
                Entry<Location, Long> next = it.next();
                if (UtilTime.elapsed(next.getValue(), 140000L)) {
                    if (next.getKey().getBlock().getType() == Material.CHEST) {
                        next.getKey().getBlock().setType(Material.AIR);
                        it.remove();
                    }
                }
            }
        }
    }


    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.SEC) {
            if (crates.isEmpty()) return;
            final Iterator<SupplyCrateData> it = crates.iterator();
            while (it.hasNext()) {
                final SupplyCrateData next = it.next();
                FireworkEffect fe = FireworkEffect.builder().with(Type.BALL_LARGE).withColor(Color.RED).build();
                Location loc = new Location(next.getLocation().getWorld(), next.getLocation().getX() + 0.5, next.getLocation().getY() + next.getCount(), next.getLocation().getZ() + 0.5);
                UtilFirework.spawn(loc, fe);
                next.takeCount();
                if (next.getCount() == 0) {
                    next.getLocation().getBlock().setType(Material.CHEST);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (next.getLocation().getBlock().getType() == Material.CHEST) {

                                Chest chest = (Chest) next.getLocation().getBlock().getState();

                                if (!Clans.getOptions().isLastDay()) {
                                    int rand = UtilMath.randomInt(5);
                                    switch (rand) {
                                        case 5:
                                            chest.getInventory().addItem(new ItemStack(Material.LEATHER_HELMET));
                                            chest.getInventory().addItem(new ItemStack(Material.LEATHER_CHESTPLATE));
                                            chest.getInventory().addItem(new ItemStack(Material.LEATHER_LEGGINGS));
                                            chest.getInventory().addItem(new ItemStack(Material.LEATHER_BOOTS));
                                            break;
                                        case 1:
                                            chest.getInventory().addItem(new ItemStack(Material.IRON_HELMET));
                                            chest.getInventory().addItem(new ItemStack(Material.IRON_CHESTPLATE));
                                            chest.getInventory().addItem(new ItemStack(Material.IRON_LEGGINGS));
                                            chest.getInventory().addItem(new ItemStack(Material.IRON_BOOTS));
                                            break;
                                        case 2:
                                            chest.getInventory().addItem(new ItemStack(Material.CHAINMAIL_HELMET));
                                            chest.getInventory().addItem(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
                                            chest.getInventory().addItem(new ItemStack(Material.CHAINMAIL_LEGGINGS));
                                            chest.getInventory().addItem(new ItemStack(Material.CHAINMAIL_BOOTS));
                                            break;
                                        case 3:
                                            chest.getInventory().addItem(new ItemStack(Material.GOLD_HELMET));
                                            chest.getInventory().addItem(new ItemStack(Material.GOLD_CHESTPLATE));
                                            chest.getInventory().addItem(new ItemStack(Material.GOLD_LEGGINGS));
                                            chest.getInventory().addItem(new ItemStack(Material.GOLD_BOOTS));
                                            break;
                                        case 4:
                                            chest.getInventory().addItem(new ItemStack(Material.DIAMOND_HELMET));
                                            chest.getInventory().addItem(new ItemStack(Material.DIAMOND_CHESTPLATE));
                                            chest.getInventory().addItem(new ItemStack(Material.DIAMOND_LEGGINGS));
                                            chest.getInventory().addItem(new ItemStack(Material.DIAMOND_BOOTS));
                                            break;
                                    }

                                    for (Weapon w : WeaponManager.weapons) {
                                        if (UtilMath.randomInt(10) > 3) {
                                            if (w instanceof EnergyApple || w instanceof IncendiaryGrenade
                                                    || w instanceof WaterBottle || w instanceof EnderPearl || w instanceof Web || w instanceof GravityGrenade) {
                                                ItemStack k = w.createWeaponNoGlow();
                                                k.setAmount(UtilMath.randomInt(1, 8));
                                                chest.getInventory().addItem(k);
                                            }
                                        }
                                    }


                                    if (UtilMath.randomInt(1000) > 998) {
                                        chest.getInventory().addItem(new ItemStack(Material.TNT, 5));
                                    } else if (UtilMath.randomInt(1000) < 2.5) {
                                        chest.getInventory().addItem(UtilItem.updateNames(new ItemStack(Material.RECORD_11)));
                                    }
                                } else {

                                    for (Weapon w : WeaponManager.weapons) {
                                        if (w.isLegendary()) {
                                            if (Math.random() > 0.50) {
                                                chest.getInventory().addItem(w.createWeapon());
                                            }
                                        }
                                    }
                                    for (int x = 0; x < 3; x++) {
                                        chest.getInventory().addItem(new ItemStack(Material.TNT, 64));
                                    }

                                }
                            }
                        }
                    }.runTaskLater(getInstance(), 5);
                    it.remove();

                }
            }
        }
    }


}


