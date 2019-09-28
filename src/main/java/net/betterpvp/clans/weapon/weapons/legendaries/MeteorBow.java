package net.betterpvp.clans.weapon.weapons.legendaries;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.utilities.UtilGamer;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.core.utility.UtilPlayer;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;
import java.util.List;

public class MeteorBow extends Weapon {

    public List<Entity> projectiles = new ArrayList<Entity>();

    public MeteorBow(Clans i) {
        super(i, Material.BOW, (byte) 0, ChatColor.RED + "Meteor Bow",
                new String[]{"",
                        ChatColor.GRAY + "Damage: " + ChatColor.YELLOW + "10 (AoE)",
                        ChatColor.GRAY + "Passive: " + ChatColor.YELLOW + "Explosive Arrows",
                        "",
                        ChatColor.RESET + "The mythical bow that reigned down",
                        ChatColor.RESET + "hell from the heavens. Each shot",
                        ChatColor.RESET + "is as deadly as a meteor.",
                        ""}, true, 2.0);
    }

    @EventHandler
    public void ProjectileShoot(EntityShootBowEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity instanceof Player) {
            if (isThisWeapon((Player) entity)) {

                if (RechargeManager.getInstance().add((Player) entity, "MeteorBow", 3, false)) {
                    projectiles.add(event.getProjectile());
                }
            }
        }
    }

    @EventHandler
    public void ProjectileHit(ProjectileHitEvent event) {
        Projectile proj = event.getEntity();
        if (proj.getShooter() == null) {
            return;
        }

        if (!(proj.getShooter() instanceof Player)) {
            return;
        }


        if (projectiles.contains(proj)) {
            Player damager = (Player) proj.getShooter();

            proj.getWorld().playEffect(proj.getLocation(), Effect.EXPLOSION_LARGE, 1);
            proj.getWorld().playSound(proj.getLocation(), Sound.EXPLODE, 0.8F, 1F);
            for (Player p : UtilPlayer.getNearby(proj.getLocation(), 5)) {
                if (ClanUtilities.canHurt(damager, p)) {
                    if (p.getHealth() > 0) {
                        LogManager.addLog(p, damager, "Explosive Arrow");
                        p.damage(UtilGamer.getDamageReduced(5, p));
                        GamerManager.getOnlineGamer(p).setLastDamaged(System.currentTimeMillis());

                    }
                }
            }
            proj.remove();

            for (Player cur : UtilPlayer.getNearby(proj.getLocation(), 6.0D)) {
                LogManager.addLog(cur, damager, "Explosive Arrow");

            }
        }
    }
}
