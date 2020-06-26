package net.betterpvp.clans.weapon.weapons.legendaries;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.economy.shops.ShopManager;
import net.betterpvp.clans.weapon.ChannelWeapon;
import net.betterpvp.clans.weapon.ILegendary;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilVelocity;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class MagneticHammer extends Weapon implements ChannelWeapon, ILegendary {

    public List<String> active = new ArrayList<String>();

    public MagneticHammer(Clans i) {
        super(i, Material.MUSIC_DISC_CAT, (byte) 0, ChatColor.RED + "Magnetic Hammer",
                new String[]{"",
                        ChatColor.GRAY + "Damage: " + ChatColor.YELLOW + "7",
                        ChatColor.GRAY + "Ability: " + ChatColor.YELLOW + "Magnetic Pull",
                        "",
                        ChatColor.GRAY + "The Magnetic Hammer is said to be able",
                        ChatColor.GRAY + "to pull nearby enemies and objects towards itself",
                        ChatColor.GRAY + "with unstoppable force.",
                        ""}, true, 2.0);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (isThisWeapon(player)) {
                event.getEntity().getVelocity().multiply(-0.4D);
            }
        }
    }

    @EventHandler
    public void onWindRiderUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();


        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (player.getInventory().getItemInMainHand() == null) return;
            if (player.getInventory().getItemInMainHand().getType() != Material.MUSIC_DISC_CAT) return;
            if (isThisWeapon(player)) {
                if (UtilBlock.isInLiquid(player)) {
                    UtilMessage.message(player, getName(), "You cannot use " + ChatColor.LIGHT_PURPLE + getName() + ChatColor.GRAY + " in water.");
                    return;
                }

                if (!ClanUtilities.canCast(player)) {
                    return;
                }

                if (Energy.use(player, getName(), 10.0, true)) {
                    active.add(player.getName());
                }
            }
        }
    }

    @EventHandler
    public void energy(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.TICK) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (active.contains(p.getName())) {

                    if (p.isHandRaised()) {
                        if (!Energy.use(p, getName(), 3, true)) {
                            active.remove(p.getName());
                        } else if (UtilBlock.isInLiquid(p)) {
                            active.remove(p.getName());
                        } else {

                            p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, Material.GRAY_TERRACOTTA);

                            for (int i = 0; i <= 5; i++) {
                                pull(p, p.getEyeLocation().add(p.getLocation().getDirection().multiply(i)));
                            }
                        }
                    } else {
                        active.remove(p.getName());
                    }

                }
            }
        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(CustomDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (player.getInventory().getItemInMainHand() == null) return;
            if (player.getInventory().getItemInMainHand().getType() != Material.MUSIC_DISC_CAT) return;

            if (isThisWeapon(player)) {
                event.setDamage(7);

            }
        }
    }

    public void pull(Player p, Location loc) {

        for (Entity other : loc.getWorld().getEntities()) {

            if (loc.distance(other.getLocation()) > 2) continue;
            if (other instanceof Item || other instanceof LivingEntity) {
                if(other instanceof LivingEntity){
                    if(ShopManager.isShop((LivingEntity) other)) continue;
                }
                if (other instanceof Player) {
                    Player player = (Player) other;
                    if (!ClanUtilities.canHurt(p, player)) {
                        continue;
                    }


                }

                if (UtilMath.offset(p.getLocation(), other.getLocation()) >= 2.0D) {
                    UtilVelocity.velocity(other, UtilVelocity.getTrajectory(other, p), 0.3D, false, 0.0D,
                            0.0D, 1.0D, true);
                }
            }
        }

    }

    @Override
    public boolean isTextured() {
        return true;
    }
}
