package net.betterpvp.clans.skills.selector.skills.ranger;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.events.SkillDequipEvent;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.recharge.RechargeManager;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilVelocity;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

public class RopedArrow extends Skill implements InteractSkill {

    private HashSet<Arrow> arrows = new HashSet<>();
    private HashSet<UUID> roped = new HashSet<>();

    public RopedArrow(Clans i) {
        super(i, "Roped Arrow", "Ranger", getBow, leftClick, 5, true, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Left click with Bow to prepare.",
                "",
                "Your next arrow will pull you",
                "in after it hits",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level),
                "Energy: " + ChatColor.GREEN + getEnergy(level)
        };
    }

    @EventHandler
    public void onDequip(SkillDequipEvent e) {
        if (e.getSkill() == this) {
            roped.remove(e.getPlayer().getUniqueId());
        }
    }


    @Override
    public boolean usageCheck(Player player) {
        if (player.getLocation().getBlock().getType() == Material.WATER ) {
            UtilMessage.message(player, "Skill", "You cannot use " + ChatColor.GREEN + getName() + " in water.");
            return false;
        }
        return true;
    }

    @EventHandler
    public void handleShootBow(EntityShootBowEvent event) {

        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (!(event.getProjectile() instanceof Arrow)) {
            return;
        }
        Player player = (Player) event.getEntity();

        if (roped.contains(player.getUniqueId())) {
            roped.remove(player.getUniqueId());


            UtilMessage.message(player, getClassType(), "You fired " + ChatColor.GREEN + getName() + " " + getLevel(player));

            arrows.add((Arrow) event.getProjectile());
            RechargeManager.getInstance().removeCooldown(player.getName(), getName(), true);
            RechargeManager.getInstance().add(player, getName(), getRecharge(getLevel(player)), showRecharge());
        }
    }

    @EventHandler
    public void ArrowHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            if (arrows.contains(event.getEntity())) {
                Arrow proj = (Arrow) event.getEntity();
                if (proj.getShooter() == null) {
                    return;
                }
                if (!(proj.getShooter() instanceof Player)) {
                    return;
                }
                Player p = (Player) proj.getShooter();
                if (hasSkill(p, this)) {

                    Vector vec = UtilVelocity.getTrajectory(p, proj);
                    double mult = proj.getVelocity().length() / 3.0D;

                    UtilVelocity.velocity(p, vec,
                            2.5D + mult, false, 0.4D, 0.3D * mult, 1.5D * mult, true);

                    proj.getWorld().playSound(proj.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 2.5F, 2.0F);
                    arrows.remove(proj);
                    EffectManager.addEffect(p, EffectType.NOFALL, 5000);
                }
            }
        }
    }

    @EventHandler
    public void Clean(UpdateEvent event) {
        if (event.getType() == UpdateType.SEC) {


            Iterator<Arrow> arrowIterator = arrows.iterator();
            while (arrowIterator.hasNext()) {

                Entity arrow = arrowIterator.next();
                if ((arrow.isDead()) || (!arrow.isValid())) {
                    arrowIterator.remove();
                }
            }
        }
    }


    @Override
    public Types getType() {

        return Types.BOW;
    }

    @Override
    public double getRecharge(int level) {

        return 17 - ((level - 1));
    }

    @Override
    public float getEnergy(int level) {

        return 30 - ((level - 1) * 2);
    }


    @Override
    public void activate(Player player, Gamer gamer) {
        roped.add(player.getUniqueId());
        UtilMessage.message(player, getClassType(), "You prepared " + ChatColor.GREEN + getName() + " " + getLevel(player));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 2.5F, 2.0F);
    }
}
