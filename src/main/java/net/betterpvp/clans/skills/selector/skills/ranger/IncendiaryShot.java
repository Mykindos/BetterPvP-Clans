package net.betterpvp.clans.skills.selector.skills.ranger;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.events.SkillDequipEvent;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IncendiaryShot extends Skill implements InteractSkill {

    public static List<UUID> active = new ArrayList<UUID>();
    private List<Arrow> incens = new ArrayList<>();

    public IncendiaryShot(Clans i) {
        super(i, "Incendiary Shot", "Ranger",
                getBow, leftClick
                , 5, true, true);
    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Left click to Activate.",
                "",
                "Shoot an ignited arrow",
                "burning anyone hit for " + ChatColor.GREEN + (0 + level) + ChatColor.GRAY + " seconds",
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

            if (e.getDamager() instanceof Player) {
                Player p = (Player) e.getDamager();
                if (hasSkill(p, this)) {
                    if (e.getProjectile() instanceof Arrow) {
                        Arrow a = (Arrow) e.getProjectile();
                        if (incens.contains(a)) {
                            e.setReason("Incendiary Shot");

                            //1.15.2 setting players on fire is weird
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    e.getDamagee().setFireTicks(getLevel(p) * 30);
                                }
                            }.runTaskLater(getInstance(), 2);

                            LogManager.addLog(e.getDamagee(), p, "Incendiary Shot");
                            incens.remove(a);
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

        UtilMessage.message(player, getClassType(), "You fired " + ChatColor.GREEN + getName() + ".");
        //event.getProjectile().setFireTicks(Integer.MAX_VALUE);
        active.remove(player.getUniqueId());
        incens.add((Arrow) event.getProjectile());
    }


    @Override
    public boolean usageCheck(Player player) {
        if (UtilBlock.isInLiquid(player)) {
            UtilMessage.message(player, "Skill", "You cannot use " + ChatColor.GREEN + getName() + " in liquid.");
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

        return 12 - ((level - 1));
    }

    @Override
    public float getEnergy(int level) {

        return 0;
    }

    @Override
    public void activate(Player player, Gamer gamer) {
        active.remove(player.getUniqueId());

        active.add(player.getUniqueId());
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 2.5F, 2.0F);
        UtilMessage.message(player, getClassType(), "You have prepared " + ChatColor.GREEN + getName(getLevel(player)) + ".");
    }
}
