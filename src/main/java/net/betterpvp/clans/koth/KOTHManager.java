package net.betterpvp.clans.koth;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDeathEvent;
import net.betterpvp.clans.weapon.EnchantedWeapon;
import net.betterpvp.clans.weapon.ILegendary;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.clans.weapon.weapons.SupplyCrateData;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilFirework;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.WeakHashMap;

public class KOTHManager extends BPVPListener<Clans> {

    public static SupplyCrateData koth = null;
    public static boolean filledInventory = false;
    public static WeakHashMap<Clan, Integer> clanKills = new WeakHashMap<>();
    public static Map.Entry<Clan, Integer> winner = null;
    public static boolean broadcasted;
    public static long kothStart;
    public static String finalWinner;


    public KOTHManager(Clans i) {
        super(i);

    }

    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.SEC) {
            if (koth == null) return;

            if (koth.getCount() > 0) {
                FireworkEffect fe = FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(Color.RED).build();
                FireworkEffect fe2 = FireworkEffect.builder().with(FireworkEffect.Type.BALL).withColor(Color.GREEN).build();
                Location loc = new Location(koth.getLocation().getWorld(), koth.getLocation().getX() + 0.5,
                        Math.min(300, koth.getLocation().getY() + koth.getCount()),
                        koth.getLocation().getZ() + 0.5);
                UtilFirework.spawn(loc, fe);
                UtilFirework.spawn(loc, fe2);
                koth.takeCount();
            }
            if (koth.getCount() == 0) {
                koth.getLocation().getBlock().setType(Material.CHEST);

                for (Map.Entry<Clan, Integer> entry : clanKills.entrySet()) {
                    if (winner == null || entry.getValue().compareTo(winner.getValue()) > 0) {
                        winner = entry;
                    }
                }

                if(winner != null) {
                    if (!broadcasted) {
                        UtilMessage.broadcast("KOTH", ChatColor.YELLOW + "Clan " + winner.getKey().getName() + ChatColor.GRAY
                                + " has won the KOTH with " + ChatColor.GREEN + winner.getValue() + ChatColor.GRAY + " kills!");
                        finalWinner = winner.getKey()
                                .getName();
                        broadcasted = true;
                    }
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (koth.getLocation().getBlock().getType() == Material.CHEST) {

                            Chest chest = (Chest) koth.getLocation().getBlock().getState();
                            if (!filledInventory) {

                                chest.getInventory().addItem(new ItemStack(Material.DIAMOND_BLOCK, UtilMath.randomInt(16, 32)));
                                chest.getInventory().addItem(new ItemStack(Material.EMERALD_BLOCK, UtilMath.randomInt(16, 32)));
                                chest.getInventory().addItem(new ItemStack(Material.IRON_BLOCK, UtilMath.randomInt(16, 32)));
                                chest.getInventory().addItem(new ItemStack(Material.GOLD_BLOCK, UtilMath.randomInt(16, 32)));

                                chest.getInventory().addItem(new ItemStack(Material.TNT, UtilMath.randomInt(5, 15)));

                                for (Weapon w : WeaponManager.weapons) {
                                    if (w instanceof EnchantedWeapon) {
                                        EnchantedWeapon enchantedWeapon = (EnchantedWeapon) w;
                                        if (enchantedWeapon.getName().contains("Ascendant")) {
                                            if (UtilMath.randomInt(0, 100) > 98) {
                                                chest.getInventory().addItem(enchantedWeapon.createWeapon());
                                            }
                                        }
                                    } else if (w instanceof ILegendary) {
                                        if (UtilMath.randomInt(10000) > 9995) {
                                            chest.getInventory().addItem(w.createWeapon());
                                        }
                                    }
                                }
                                filledInventory = true;
                            }


                        }

                    }

                }.runTaskLater(getInstance(), 5);
            }
        }
    }

    @EventHandler
    public void onKill(CustomDeathEvent e) {
        if (koth != null) {
            Clan aClan = ClanUtilities.getClan(e.getKiller());
            Clan bClan = ClanUtilities.getClan(e.getKilled().getLocation());
            if (aClan != null && bClan != null) {
                if (bClan.getName().equalsIgnoreCase("Fields")) {
                    if (clanKills.containsKey(aClan)) {
                        clanKills.put(aClan, clanKills.get(aClan) + 1);
                    } else {
                        clanKills.put(aClan, 1);
                    }
                }
            }

        }
    }

    @EventHandler
    public void onBroadcast(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.MIN_01) {
            if (koth != null) {
                double left = UtilTime.convert(kothStart - System.currentTimeMillis(), UtilTime.TimeUnit.MINUTES, 0);
                if (left > 0) {
                    Bukkit.broadcastMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "The KOTH is landing in "
                            + ChatColor.RED + ChatColor.BOLD + left + ChatColor.AQUA + ChatColor.BOLD + " minutes!");
                }
            }
        }
    }


    @EventHandler
    public void interact(PlayerInteractEvent e) {
        if (e.getHand() == EquipmentSlot.OFF_HAND) return;
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block b = e.getClickedBlock();
            if (b.getType() == Material.CHEST) {
                if (koth != null) {


                    //	Bukkit.broadcastMessage(b.getLocation().getX() + ", " + b.getLocation().getZ() + " " + koth.getLocation().getX() + ", " + koth.getLocation().getZ());
                    if (b.getLocation().equals(koth.getLocation())) {

                        if (ClientUtilities.getOnlineClient(e.getPlayer()).isAdministrating()) {
                            return;
                        }
                        if (winner != null) {
                            Clan pClan = ClanUtilities.getClan(e.getPlayer());
                            if (pClan != null) {

                                if (!pClan.getName().equals(finalWinner)) {
                                    e.setCancelled(true);
                                }
                            } else {
                                e.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

}