package net.betterpvp.clans.skills.selector.skills.assassin;

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
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilItem;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.recharge.Recharge;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.*;


public class MarkedForDeath extends Skill implements InteractSkill {

    private Set<UUID> active = new HashSet<>();
    private List<Arrow> arrows = new ArrayList<>();

    public MarkedForDeath(Clans i) {
        super(i, "Marked for Death", "Assassin", getBow, leftClick, 5, true, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Your next arrow will mark players",
                "for death, giving them Vulnerability II",
                "for " + ChatColor.GREEN + (7 + level) + ChatColor.GRAY + " seconds",
                "Causing them to take 50% additional damage",
                "from all targets.",
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

        return 30 - ((level - 1) * 2);
    }

    @Override
    public float getEnergy(int level) {

        return 50 - ((level - 1) * 3);
    }


    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (hasSkill(p, this)) {
                if (active.contains(p.getUniqueId())) {
                    if (e.getProjectile() instanceof Arrow) {
                        arrows.add((Arrow) e.getProjectile());
                        active.remove(p.getUniqueId());

                        RechargeManager.getInstance().removeCooldown(p.getName(), getName(), true);
                        RechargeManager.getInstance().add(p, getName(), getRecharge(getLevel(p)), showRecharge(), false);
                    }
                }
            }
        }
    }


    @EventHandler
    public void onCheckCancel(UpdateEvent e) {
        if (e.getType() == UpdateType.FASTEST) {
            Iterator<UUID> it = active.iterator();
            while (it.hasNext()) {
                UUID uuid = it.next();
                Player p = Bukkit.getPlayer(uuid);
                if (p != null) {
                    if (p.getInventory().getItemInMainHand() != null) {
                        if (!UtilItem.isRanged(p.getInventory().getItemInMainHand().getType())) {
                            Recharge recharge = RechargeManager.getInstance().getAbilityRecharge(p.getName(), getName());
                            if (recharge != null) {
                                if (recharge.isCancellable()) {
                                    RechargeManager.getInstance().removeCooldown(p.getName(), getName(), true);
                                    UtilMessage.message(p, "Assassin", ChatColor.GREEN + getName(getLevel(p)) + ChatColor.GRAY + " was cancelled.");

                                    Iterator<Arrow> it2 = arrows.iterator();
                                    while (it2.hasNext()) {
                                        Arrow a = it2.next();
                                        if (a.getShooter() instanceof Player) {
                                            Player p2 = (Player) a.getShooter();
                                            if (p2.getName().equalsIgnoreCase(p.getName())) {
                                                it2.remove();
                                            }
                                        }
                                    }
                                    it.remove();
                                }
                            }


                        }
                    }

                }
            }
        }
    }

    @Override
    public boolean isCancellable() {
        return true;
    }

    @EventHandler
    public void onHit(CustomDamageEvent e) {
        if (e.getProjectile() != null) {

            if (e.getDamagee() instanceof Player) {
                if (e.getProjectile() instanceof Arrow) {
                    if (e.getDamager() instanceof Player) {
                        Player p = (Player) e.getDamager();
                        Player ent = (Player) e.getDamagee();

                        if (hasSkill(p, this)) {
                            if (arrows.contains((Arrow) e.getProjectile())) {
                                EffectManager.addEffect(ent, EffectType.VULNERABILITY, 2, (7 + getLevel(p)) * 1000);
                                LogManager.addLog(ent, p, "Marked for Death");
                                UtilMessage.message(ent, getClassType(), p.getName() + " hit you with " + ChatColor.GREEN + getName());
                                arrows.remove((Arrow) e.getProjectile());
                            }
                        }


                    }
                }
            }
        }
    }

    @EventHandler
    public void update(UpdateEvent e) {
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
            UtilMessage.message(player, getClassType(), "You cannot use " + getName() + " in water");
            return false;
        }
        return true;
    }

    @Override
    public void activate(Player player, Gamer gamer) {
        active.add(player.getUniqueId());
        UtilMessage.message(player, getClassType(), "You prepared " + ChatColor.GREEN + getName() + " " + getLevel(player));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 2.5F, 2.0F);
    }
}
