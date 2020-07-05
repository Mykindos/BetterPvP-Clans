package net.betterpvp.clans.weapon.weapons.legendaries;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.weapon.ChannelWeapon;
import net.betterpvp.clans.weapon.ILegendary;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.particles.ParticleEffect;
import net.betterpvp.core.utility.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;


public class WindBlade extends Weapon implements ChannelWeapon, ILegendary {

    public List<String> active = new ArrayList<>();
    public WeakHashMap<Player, Long> wait = new WeakHashMap<>();

    public WindBlade(Clans i) {
        super(i, Material.MUSIC_DISC_MELLOHI, (byte) 0, ChatColor.RED + "Wind Blade",
                new String[]{"",
                        ChatColor.GRAY + "Damage: " + ChatColor.YELLOW + "7",
                        ChatColor.GRAY + "Ability: " + ChatColor.YELLOW + "Wind Rider",
                        ChatColor.GRAY + "Knockback: " + ChatColor.YELLOW + "300%",
                        "",
                        ChatColor.GRAY + "Once owned by the God Zephyrus,",
                        ChatColor.GRAY + "it is rumoured the Wind Blade",
                        ChatColor.GRAY + "grants its owner flight.",
                        ""}, true, 3.0);

    }

    @EventHandler
    public void onFall(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getCause() == DamageCause.FALL) {
                Player player = (Player) e.getEntity();

                if (player.getInventory().getItemInMainHand() == null) return;
                if (player.getInventory().getItemInMainHand().getType() != Material.MUSIC_DISC_MELLOHI) return;
                if (isThisWeapon(player)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onWindRiderUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();


        if(event.getHand() == EquipmentSlot.OFF_HAND) return;
        if (player.getInventory().getItemInMainHand() == null) return;
        if (player.getInventory().getItemInMainHand().getType() != Material.MUSIC_DISC_MELLOHI) return;


        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (isThisWeapon(player)) {
                if (UtilBlock.isInLiquid(player)) {
                    UtilMessage.message(player, getName(), "You cannot use " + ChatColor.LIGHT_PURPLE + "Wind Rider" + ChatColor.GRAY + " in water.");
                    return;
                }

                Clan clan = ClanUtilities.getClan(player.getLocation());
                if (clan != null) {
                    if (clan instanceof AdminClan) {
                        AdminClan adminClan = (AdminClan) clan;
                        if (adminClan.isSafe()) {
                            return;

                        }
                    }
                }

                if (!active.contains(player.getName())) {
                    if (Energy.use(player, "Wind Rider", 20.0, true)) {
                        active.add(player.getName());
                        wait.put(player, System.currentTimeMillis());
                    }


                }
            }
        }
    }

    @EventHandler
    public void Update(UpdateEvent event) {
        if (event.getType() == UpdateEvent.UpdateType.TICK) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (active.contains(player.getName())) {
                    Clan clan = ClanUtilities.getClan(player.getLocation());
                    if (clan != null) {
                        if (clan instanceof AdminClan) {
                            AdminClan adminClan = (AdminClan) clan;
                            if (adminClan.isSafe()) {
                                active.remove(player.getName());
                                continue;
                            }
                        }
                    }
                    if (player.isHandRaised()
                            && !UtilItem.isRanged(player.getInventory().getItemInMainHand().getType())) { // .ct() for 1.9.4

                        if (UtilBlock.isInLiquid(player)) {
                            UtilMessage.message(player, getName(), "You cannot use " + ChatColor.LIGHT_PURPLE + "Wind Rider" + ChatColor.GRAY + " in water.");
                            active.remove(player.getName());

                        } else if (!Energy.use(player, "Wind Rider", 1.0, true)) {
                            active.remove(player.getName());
                        } else {

                            if (!isThisWeapon(player)) {
                                active.remove(player.getName());
                                continue;
                            }

                            UtilVelocity.velocity(player, 0.6D, 0.11D, 1.0D, true);
                            //player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, 80);
                            ParticleEffect.FIREWORKS_SPARK.display(player.getLocation());
                            player.getWorld().spawnEntity(player.getLocation(), EntityType.LLAMA_SPIT);
                            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 0.5F, 1.5F);
                        }
                    } else {
                        if(wait.containsKey(player)) {
                            if (UtilTime.elapsed(wait.get(player), 500)) {
                                active.remove(player.getName());
                                wait.remove(player);
                            }
                        }
                    }
                }
            }
        }
    }


    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamage(CustomDamageEvent event) {
        if (event.getCause() == DamageCause.ENTITY_ATTACK) {
            if (event.getDamager() instanceof Player) {
                Player player = (Player) event.getDamager();
                if (player.getInventory().getItemInMainHand() == null) return;
                if (player.getInventory().getItemInMainHand().getType() != Material.MUSIC_DISC_MELLOHI) return;
                if (isThisWeapon(player)) {

                    event.setKnockback(false);
                    event.setDamage(7D);
                    LivingEntity ent = (LivingEntity) event.getDamager();
                    Vector vec = ent.getLocation().getDirection();
                    vec.setY(0);
                    UtilVelocity.velocity(event.getDamagee(), vec, 2D, false, 0.0D, 0.5D, 1.0D, true);
                }

            }

        }
    }

    @Override
    public boolean isTextured() {
        return true;
    }
}
