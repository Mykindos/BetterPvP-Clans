package net.betterpvp.clans.skills.selector.skills.assassin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.events.SkillDequipEvent;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class ToxicArrow extends Skill implements InteractSkill {

    private List<Arrow> arrows = new ArrayList<>();
    private Set<UUID> active = new HashSet<>();

    public ToxicArrow(Clans i) {
        super(i, "Toxic Arrow", "Assassin", getBow, leftClick, 5, true, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Your next arrow will give your",
                "target confusion for " + ChatColor.GREEN + (15 + level) + ChatColor.GRAY + " seconds.",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level),
                "Energy: " + ChatColor.GREEN + getEnergy(level)

        };
    }

    @EventHandler
    public void onDequip(SkillDequipEvent e) {
        if (e.getSkill() == this) {
            active.remove(e.getPlayer().getUniqueId());
        }
    }

    @Override
    public Types getType() {

        return Types.BOW;
    }

    @Override
    public double getRecharge(int level) {

        return 14 - ((level - 1));
    }

    @Override
    public float getEnergy(int level) {

        return 30 - ((level - 1) * 2);
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (hasSkill(p, this)) {
                if (e.getProjectile() instanceof Arrow) {
                    if (active.contains(p.getUniqueId())) {
                        arrows.add((Arrow) e.getProjectile());
                        active.remove(p.getUniqueId());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHit(CustomDamageEvent e) {
        if (e.getProjectile() != null) {
            if (e.getDamagee() instanceof Player) {
                if (e.getProjectile() instanceof Arrow) {
                    if (e.getDamager() instanceof Player) {
                        Player p = (Player) e.getDamager();
                        Player ent = (Player) e.getDamagee();
                        if (ClanUtilities.canHurt(p, ent)) {
                            if (hasSkill(p, this)) {
                                if (arrows.contains((Arrow) e.getProjectile())) {
                                    if (ent.hasPotionEffect(PotionEffectType.CONFUSION)) {
                                        ent.removePotionEffect(PotionEffectType.CONFUSION);
                                    }
                                    ent.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, (15 + getLevel(p)) * 20, 0));
                                    LogManager.addLog(ent, p, "Toxic Arrow");
                                    arrows.remove((Arrow) e.getProjectile());
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateType.SEC) {
            ListIterator<Arrow> iterator = arrows.listIterator();
            while (iterator.hasNext()) {
                Arrow next = iterator.next();
                if (next.isDead()) {
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public boolean usageCheck(Player player) {
        if (UtilBlock.isInLiquid(player)) {
            UtilMessage.message(player, getClassType(), "You cannot use " + getName() + " in water.");
            return false;
        }
        return true;
    }

    @Override
    public void activate(Player player, Gamer gamer) {
        UtilMessage.message(player, getClassType(), "You prepared " + ChatColor.GREEN + getName() + " " + getLevel(player));
        if (!active.contains(player.getUniqueId())) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 2.5F, 2.0F);
            active.add(player.getUniqueId());
        }
    }
}
