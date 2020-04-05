package net.betterpvp.clans.skills.selector.skills.assassin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
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

public class SilencingArrow extends Skill {

    private List<Arrow> arrows = new ArrayList<>();
    private Set<UUID> active = new HashSet<>();

    public SilencingArrow(Clans i) {
        super(i, "Silencing Arrow", "Assassin", getBow, leftClick, 5, true, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Your next arrow will silence your",
                "target for " + ChatColor.GREEN + (3 + level) + ChatColor.GRAY + " seconds.",
                "Making them unable to use any active skills",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level),
                "Energy: " + ChatColor.GREEN + getEnergy(level)

        };
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
                        if (p.getInventory().getItemInMainHand().getType() != Material.BOW) {
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

    @Override
    public Types getType() {

        return Types.BOW;
    }

    @Override
    public double getRecharge(int level) {

        return 15 - ((level - 1) * 0.5);
    }

    @Override
    public float getEnergy(int level) {

        return 30 - ((level - 1) * 2);
    }

    @Override
    public void activateSkill(Player player) {

        if (!active.contains(player.getUniqueId())) {
            UtilMessage.message(player, getClassType(), "You prepared " + ChatColor.GREEN + getName() + " " + getLevel(player));
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 2.5F, 2.0F);
            active.add(player.getUniqueId());
        }
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

                        RechargeManager.getInstance().removeCooldown(p.getName(), getName(), true);
                        RechargeManager.getInstance().add(p, getName(), getRecharge(getLevel(p)), showRecharge(), true, false);

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
                                    if (!EffectManager.hasEffect(ent, EffectType.INVULNERABILITY)) {
                                        EffectManager.addEffect(ent, EffectType.SILENCE, (3 + getLevel(p)) * 1000);
                                        LogManager.addLog(ent, p, "Silencing Arrow");
                                    } else {
                                        UtilMessage.message(p, getClassType(), ChatColor.GREEN + ent.getName() + ChatColor.GRAY + " is immune to your silence!");
                                    }
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
        if (player.getLocation().getBlock().isLiquid()) {
            UtilMessage.message(player, getClassType(), "You cannot use " + getName() + " in water.");
            return false;
        }
        return true;
    }

}
