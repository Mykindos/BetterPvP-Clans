package net.betterpvp.clans.weapon.weapons;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.particles.ParticleEffect;
import net.betterpvp.core.utility.UtilItem;
import net.betterpvp.core.utility.UtilPlayer;
import net.betterpvp.core.utility.UtilTime;
import net.betterpvp.core.utility.recharge.RechargeManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class GravityGrenade extends Weapon {

    private HashMap<Item, Long> gravity = new HashMap<>();

    public GravityGrenade(Clans i) {
        super(i, Material.BLACK_TERRACOTTA, (byte) 15, ChatColor.YELLOW + "Gravity Bomb", new String[]{
                ChatColor.GRAY + "Left-Click: " + ChatColor.YELLOW + "Throw",
                ChatColor.GRAY + "  " + "Creates a field that disrupts all players caught inside"}, false, 10.0);

    }


    @EventHandler
    public void onGrenadeUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getHand() == EquipmentSlot.OFF_HAND) return;
        if (player.getInventory().getItemInMainHand() == null) return;
        if (player.getInventory().getItemInMainHand().getType() != Material.BLACK_TERRACOTTA) return;


        if (isThisWeapon(player)) {
            if (ClanUtilities.canCast(player)) {
                if (event.getAction() == Action.LEFT_CLICK_AIR) {
                    if (RechargeManager.getInstance().add(player, "Gravity Bomb", 15, true)) {
                        Item item = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.BLACK_TERRACOTTA, 1));
                        UtilItem.remove(player, Material.BLACK_TERRACOTTA, 1);
                        item.setPickupDelay(Integer.MAX_VALUE);
                        item.setVelocity(player.getLocation().getDirection().multiply(1.3));
                        gravity.put(item, System.currentTimeMillis());
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void particle(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.FAST) {
            for (Item q : gravity.keySet()) {
                if (q.isOnGround()) {
                    Location loc = q.getLocation();
                    Clan c = ClanUtilities.getClan(q.getLocation());
                    if (c != null) {
                        if (c instanceof AdminClan) {
                            if (((AdminClan) c).isSafe()) {
                                continue;
                            }
                        }
                    }
                    int radius = 5;
                    int particles = 50;
                    for (int i = 0; i < particles; i++) {
                        for (int p = 0; p < radius; p++) {
                            double angle, x, z;
                            angle = 2 * Math.PI * i / particles;
                            x = Math.cos(angle) * p;
                            z = Math.sin(angle) * p;

                            loc.add(x, 0, z);
                            ParticleEffect.SMOKE_LARGE.display(loc);

                            loc.subtract(x, 0, z);
                        }
                    }

                    for (LivingEntity p : UtilPlayer.getAllInRadius(q.getLocation(), 5.0)) {
                        if (p instanceof Player) {
                            Player d = (Player) p;
                            Clan ca = ClanUtilities.getClan(d.getLocation());

                            if (ca != null) {
                                if (ca instanceof AdminClan) {
                                    AdminClan ac = (AdminClan) ca;
                                    if (ac.isSafe()) {
                                        continue;
                                    }
                                }
                            }
                        }
                        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1));
                        Vector x = q.getLocation().toVector();
                        x.setY(x.getY() + 1);
                        p.setVelocity(p.getLocation().toVector().subtract(x).normalize().multiply(-0.5));
                    }
                }
            }
        }
    }


    @EventHandler
    public void remove(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.SEC) {
            if (!gravity.isEmpty()) {
                Iterator<Entry<Item, Long>> it = gravity.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<Item, Long> next = it.next();
                    if (UtilTime.elapsed(next.getValue(), 5000)) {
                        next.getKey().remove();
                        it.remove();
                    }
                }
            }
        }
    }


}
