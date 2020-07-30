package net.betterpvp.clans.weapon.weapons.legendaries;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.weapon.ChannelWeapon;
import net.betterpvp.clans.weapon.ILegendary;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.particles.ParticleEffect;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import net.betterpvp.core.utility.UtilVelocity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;


public class AlligatorsTooth extends Weapon implements ChannelWeapon, ILegendary {

    public List<String> active = new ArrayList<>();
    public WeakHashMap<Player, Long> wait = new WeakHashMap<>();

    public AlligatorsTooth(Clans i) {
        super(i, Material.MUSIC_DISC_STRAD, (byte) 0, ChatColor.RED + "Alligators Tooth",
                new String[]{"",
                        ChatColor.GRAY + "Damage: " + ChatColor.YELLOW + "7 + 2 in Water",
                        ChatColor.GRAY + "Ability: " + ChatColor.YELLOW + "Alliagtor Rush",
                        ChatColor.GRAY + "Passive: " + ChatColor.YELLOW + "Water Breathing",
                        ChatColor.GRAY + "Knockback: " + ChatColor.YELLOW + "100%",
                        "",
                        ChatColor.GRAY + "A blade forged from hundreds of ",
                        ChatColor.GRAY + "alligators teeth. It's powers allow ",
                        ChatColor.GRAY + "its owner to swim with great speed, ",
                        ChatColor.GRAY + "able to catch any prey.",
                        ""
                }, true, 4.0);
    }

    @EventHandler
    public void onAlligatorToothUse(PlayerInteractEvent event) {


        Player player = event.getPlayer();
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        if (player.getInventory().getItemInMainHand() == null) return;
        if (player.getInventory().getItemInMainHand().getType() != Material.MUSIC_DISC_STRAD) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (isThisWeapon(player)) {
                if (!UtilBlock.isInLiquid(player)) {
                    UtilMessage.message(player, getName(), "You cannot use " + ChatColor.LIGHT_PURPLE + "Alligator Rush" + ChatColor.GRAY + " out of water.");
                    return;
                }

                if (!active.contains(player.getName())) {
                    if (Energy.use(player, "Alligator Rush", 10.0, true)) {
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

                    if (player.isHandRaised()) {
                        if (!isThisWeapon(player)) {
                            active.remove(player.getName());
                        } else if (!Energy.use(player, "Alligator Rush", 0.25, true)) {

                            active.remove(player.getName());

                        } else if (!UtilBlock.isInLiquid(player)) {
                            active.remove(player.getName());
                            UtilMessage.message(player, getName(), "You cannot use " + ChatColor.LIGHT_PURPLE + "Alligator Rush" + ChatColor.GRAY + " out of water.");
                        } else {
                            UtilVelocity.velocity(player, 1.0D, 0.11D, 1.0D, true);
                            ParticleEffect.WATER_WAKE.display(player.getLocation());
                            // player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, Material.WATER);
                            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FISH_SWIM, 0.8F, 1.5F);
                        }
                    } else {
                        if (UtilTime.elapsed(wait.get(player), 500)) {
                            active.remove(player.getName());
                            wait.remove(player);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void entityDamEvent(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getCause() == DamageCause.DROWNING) {
                Player p = (Player) e.getEntity();
                if (p.getInventory().getItemInMainHand() == null) return;
                if (p.getInventory().getItemInMainHand().getType() != Material.MUSIC_DISC_STRAD) return;
                if (isThisWeapon(p)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(CustomDamageEvent event) {
        if (event.getCause() != DamageCause.ENTITY_ATTACK) return;
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (player.getInventory().getItemInMainHand() == null) return;
            if (player.getInventory().getItemInMainHand().getType() != Material.MUSIC_DISC_STRAD) return;

            if (isThisWeapon(player)) {
                event.setDamage(7);
                if (event.getDamager().getLocation().getBlock().isLiquid()) {
                    event.setDamage(event.getDamage() + 2);
                }
            }
        }
    }

    @Override
    public boolean isTextured() {
        return true;
    }
}
