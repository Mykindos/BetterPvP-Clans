package net.betterpvp.clans.donations;

import net.betterpvp.clans.anticheat.Suspicion;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.utilities.UtilClans;
import net.betterpvp.core.donation.DonationExpiryTimes;
import net.betterpvp.core.donation.IDonation;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.*;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class AgilityHelmet implements IDonation, Listener {

    private List<UUID> active = new ArrayList<>();

    @Override
    public String getName() {
        return "AgilityHelmet";
    }

    @Override
    public String getDisplayName() {
        return "Agility Helmet";
    }

    @Override
    public long getExpiryTime() {
        return DonationExpiryTimes.NONE;
    }

    @EventHandler
    public void onAgilityLeap(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.OFF_HAND) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {

                return;

            }

            Player player = event.getPlayer();


            if (UtilItem.isBlockDoor(player)) {
                return;
            }

            if (!hasAgilityHelmetEquipped(player)) {
                return;
            }

            if (event.getAction() == Action.LEFT_CLICK_AIR) {
                if (!UtilItem.isWeapon(player.getInventory().getItemInMainHand().getType())) {
                    if (customCheck(player)) {
                        if (ClanUtilities.canCast(player)) {

                            if (active.contains(player.getUniqueId())) {
                                if (Suspicion.suspicions.containsKey(player.getUniqueId())) {
                                    Suspicion.suspicions.remove(player.getUniqueId());
                                }

                                if (Energy.use(player, "Agility Leap", 0.3F, true)) {
                                    if (!WallJump(player)) {
                                        if (RechargeManager.getInstance().add(player, "Agility Leap", 5, true)) {
                                            DoLeap(player, false);
                                        }
                                    }
                                }


                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {


        Iterator<ItemStack> iterator = e.getDrops().iterator();
        while (iterator.hasNext()) {
            ItemStack next = iterator.next();
            if (next.getType() == Material.TURTLE_HELMET) {
                iterator.remove();
            }
        }


    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Gamer gamer = GamerManager.getOnlineGamer(player);
        if (gamer != null) {
            if (gamer.getClient().hasDonation(getName())) {
                UtilItem.insert(player, UtilClans.updateNames(new ItemStack(Material.TURTLE_HELMET, 1)));
            }
        }
    }

    @EventHandler
    public void onDropTurtleHelmet(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack() != null) {
            if (e.getItemDrop().getItemStack().getType() == Material.TURTLE_HELMET) {
                e.getItemDrop().remove();
            }
        }
    }

    @EventHandler
    public void onAgilityUpdate(UpdateEvent event) {
        if (event.getType() == UpdateEvent.UpdateType.SEC) {
            for (Player player : Bukkit.getOnlinePlayers()) {

                if (hasAgilityHelmetEquipped(player)) {

                    Gamer gamer = GamerManager.getOnlineGamer(player);
                    if (gamer != null) {
                        if (!gamer.getClient().hasDonation(getName())) {
                            player.getInventory().setHelmet(null);
                            continue;
                        } else {
                            if (!active.contains(player.getUniqueId())) {
                                if (!UtilTime.elapsed(gamer.getLastDamaged(), 10000)) {
                                    UtilMessage.message(player, "Agility Helmet", "You cannot use agility helmet while in combat.");
                                    player.getEquipment().setHelmet(null);
                                    UtilItem.insert(player, UtilClans.updateNames(new ItemStack(Material.TURTLE_HELMET)));
                                } else {
                                    active.add(player.getUniqueId());
                                    UtilMessage.message(player, "Agility Helmet", "Increased agility activated.");
                                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1.f, 1.f);
                                }
                            }
                        }
                    }
                    if (otherArmour(player)) {
                        for (ItemStack d : player.getInventory().getArmorContents()) {
                            if (d == null) continue;
                            if (d.getType() == Material.TURTLE_HELMET) {
                                UtilItem.insert(player, d);
                                UtilMessage.message(player, "Agility", "You cannot use the agility helmet with other armour!");
                                player.getEquipment().setHelmet(null);
                                return;
                            }
                        }


                    }
                } else {
                    if (active.contains(player.getUniqueId())) {
                        active.remove(player.getUniqueId());
                    }
                }

            }
        }
    }

    @EventHandler
    public void onDamage(CustomDamageEvent e) {
        if (e.getDamager() instanceof Player) {
            if (e.getDamagee() instanceof Player) {
                Player p = (Player) e.getDamager();
                if (p.getEquipment().getHelmet() != null) {
                    if (hasAgilityHelmetEquipped(p)) {
                        e.setDamage(1);
                        //e.setCancelled("Cant damage with Agility Helmet");
                    }
                }
            }
        }
    }


    private boolean otherArmour(Player p) {
        for (ItemStack i : p.getInventory().getArmorContents()) {
            if (i == null) continue;
            if (i.getType() == Material.TURTLE_HELMET) continue;
            if (i.getType() != Material.AIR) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onFall(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (hasAgilityHelmetEquipped(player)) {
                if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    public boolean customCheck(Player player) {
        if (UtilBlock.isInLiquid(player)) {
            UtilMessage.message(player, "Agility", "You cannot use " + ChatColor.GREEN + "Agility Leap" + ChatColor.GRAY + " in liquid.");
            return false;
        }

        if (player.hasPotionEffect(PotionEffectType.SLOW)) {
            UtilMessage.message(player, "Agility", "You cannot use " + ChatColor.GREEN + "Agility Leap" + ChatColor.GRAY + " while Slowed.");
            return false;
        }

        if (!ClanUtilities.canCast(player)) {
            return false;
        }

        if(active.contains(player.getUniqueId())) {
            if (WallJump(player)) {
                return false;
            }
        }

        return true;
    }

    public void DoLeap(Player player, boolean wallkick) {
        if (!wallkick) {
            UtilVelocity.velocity(player, 1.1D, 0.2D, 1.0D, true);

        } else {
            Vector vec = player.getLocation().getDirection();
            vec.setY(0);

            UtilVelocity.velocity(player, vec, 0.9D, false, 0.0D, 0.8D, 2.0D, true);

        }

        if (!wallkick) {
            UtilMessage.message(player, "Agility", "You used " + ChatColor.GREEN + "Agility Leap" + ChatColor.GRAY + ".");
        } else {
            UtilMessage.message(player, "Agility", "You used " + ChatColor.GREEN + "Agility Wall Kick" + ChatColor.GRAY + ".");
        }

        player.getWorld().spawnEntity(player.getLocation(), EntityType.LLAMA_SPIT);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 2.0F, 1.2F);
    }

    public boolean WallJump(Player player) {

        if (RechargeManager.getInstance().add(player, "Agility WallKick", 0.5, false)) {
            Vector vec = player.getLocation().getDirection();

            boolean xPos = true;
            boolean zPos = true;

            if (vec.getX() < 0.0D) {
                xPos = false;
            }
            if (vec.getZ() < 0.0D) {
                zPos = false;
            }

            for (int y = 0; y <= 0; y++) {
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        if ((x != 0) || (z != 0)) {
                            if (((!xPos) || (x <= 0))
                                    && ((!zPos) || (z <= 0))
                                    && ((xPos) || (x >= 0)) && ((zPos) || (z >= 0))) {
                                if (!UtilBlock.airFoliage(player.getLocation().getBlock().getRelative(x, y, z))) {
                                    Block forward = null;

                                    if (Math.abs(vec.getX()) > Math.abs(vec.getZ())) {
                                        if (xPos) {
                                            forward = player.getLocation().getBlock().getRelative(1, 0, 0);
                                        } else {
                                            forward = player.getLocation().getBlock().getRelative(-1, 0, 0);
                                        }

                                    } else if (zPos) {
                                        forward = player.getLocation().getBlock().getRelative(0, 0, 1);
                                    } else {
                                        forward = player.getLocation().getBlock().getRelative(0, 0, -1);
                                    }

                                    if (UtilBlock.airFoliage(forward)) {
                                        if (Math.abs(vec.getX()) > Math.abs(vec.getZ())) {
                                            if (xPos) {
                                                forward = player.getLocation().getBlock().getRelative(1, 1, 0);
                                            } else {
                                                forward = player.getLocation().getBlock().getRelative(-1, 1, 0);
                                            }
                                        } else if (zPos) {
                                            forward = player.getLocation().getBlock().getRelative(0, 1, 1);
                                        } else {
                                            forward = player.getLocation().getBlock().getRelative(0, 1, -1);
                                        }

                                        if (UtilBlock.airFoliage(forward)) {
                                            DoLeap(player, true);
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean hasAgilityHelmetEquipped(Player player) {
        return player.getEquipment().getHelmet() != null && player.getEquipment().getHelmet().getType() == Material.TURTLE_HELMET;
    }
}



