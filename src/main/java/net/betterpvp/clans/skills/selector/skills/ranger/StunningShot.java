package net.betterpvp.clans.skills.selector.skills.ranger;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.events.SkillDequipEvent;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.recharge.RechargeManager;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StunningShot extends Skill implements InteractSkill {

    public List<UUID> active = new ArrayList<>();
    private List<Arrow> arrows = new ArrayList<>();

    public StunningShot(Clans i) {
        super(i, "Stunning Shot", "Ranger",
                getBow, leftClick
                , 5, true, true);
    }


    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Left click to Activate.",
                "",
                "Shoot an arrow",
                "stunning anyone hit for " + ChatColor.GREEN + String.format("%.2f", (level * 0.40)) + ChatColor.GRAY + " seconds",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level)
        };
    }

    @EventHandler
    public void onDequip(SkillDequipEvent e) {
        if (e.getSkill() == this) {
            if (active.contains(e.getPlayer().getUniqueId())) {
                active.remove(e.getPlayer().getUniqueId());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(CustomDamageEvent e) {
        if (e.getProjectile() != null) {
            if (e.getDamagee() instanceof Player) {
                if (e.getDamager() instanceof Player) {
                    Player p = (Player) e.getDamager();
                    if (hasSkill(p, this)) {
                        if (e.getProjectile() instanceof Arrow) {

                            if (arrows.contains((Arrow) e.getProjectile())) {
                                LogManager.addLog(e.getDamagee(), p, "Stunning Shot");
                                UtilMessage.message((Player) e.getDamagee(), getClassType(), "You were hit by a " + getName());
                                EffectManager.addEffect((Player) e.getDamagee(), EffectType.STUN, (long) ((getLevel(p) * 0.40) * 1000));
                                arrows.remove((Arrow) e.getProjectile());
                            }
                        }
                    }
                }
            }

        }
    }

    @EventHandler
    public void ShootBow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (!(event.getProjectile() instanceof Arrow)) {
            return;
        }

        Player player = (Player) event.getEntity();
        if (!active.contains(player.getUniqueId())) {
            return;
        }

        active.remove(player.getUniqueId());

        arrows.add((Arrow) event.getProjectile());
        UtilMessage.message(player, getClassType(), "You fired " + ChatColor.GREEN + getName(getLevel(player)) + ".");
        RechargeManager.getInstance().removeCooldown(player.getName(), getName(), true);
        if (RechargeManager.getInstance().add(player, getName(), getRecharge(getLevel(player)), showRecharge())) {

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


    @Override
    public Types getType() {

        return Types.BOW;
    }

    @Override
    public double getRecharge(int level) {

        return 25 - ((level - 1));
    }

    @Override
    public float getEnergy(int level) {

        return 30 - ((level - 1) * 2);
    }


    @Override
    public void activate(Player player, Gamer gamer) {
        active.remove(player.getUniqueId());

        active.add(player.getUniqueId());
        UtilMessage.message(player, getClassType(), "You have prepared " + ChatColor.GREEN + getName(getLevel(player)) + ".");
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 2.5F, 2.0F);
    }
}
