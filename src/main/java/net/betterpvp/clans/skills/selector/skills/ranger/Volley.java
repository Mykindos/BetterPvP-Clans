package net.betterpvp.clans.skills.selector.skills.ranger;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.events.SkillDequipEvent;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class Volley extends Skill implements InteractSkill {

    private Set<UUID> volleys = new HashSet<UUID>();
    private Clans i;

    public Volley(Clans i) {
        super(i, "Volley", "Ranger", getBow,
                leftClick, 5,
                true, true);
        this.i = i;
    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Left click with a bow to Prepare",
                "Your next shot is instant, and shoots",
                "a volley of arrows in the direction you are facing",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level)
        };
    }

    @EventHandler
    public void onDequip(SkillDequipEvent e) {
        if (e.getSkill() == this) {
            volleys.remove(e.getPlayer().getUniqueId());
        }
    }

    @Override
    public Types getType() {

        return Types.BOW;
    }

    private List<Arrow> arrows = new ArrayList<>();

    @EventHandler
    public void onShoot(final EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (ClanUtilities.canCast(p)) {
                if (hasSkill(p, this)) {
                    if (volleys.contains(p.getUniqueId())) {
                        Location c = p.getLocation();
                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                if (e.getProjectile() instanceof Arrow) {
                                    Arrow j = (Arrow) e.getProjectile();
                                    Longshot.getArrows().remove(j);
                                    j.remove();
                                }
                            }


                        }.runTaskLater(i, 1);


                        Vector v;

                        for (int i = 0; i < 10; i += 2) {
                            Arrow n = p.launchProjectile(Arrow.class);
                            n.setShooter(p);
                            c.setYaw(c.getYaw() + i);
                            v = c.getDirection();
                            n.setVelocity(v.multiply(2));
                            arrows.add(n);
                        }
                        c = p.getLocation();
                        for (int i = 0; i < 10; i += 2) {
                            Arrow n = p.launchProjectile(Arrow.class);
                            n.setShooter(p);
                            c.setYaw(c.getYaw() - i);
                            v = c.getDirection();
                            n.setVelocity(v.multiply(2));
                            arrows.add(n);
                        }

                        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_PISTON_EXTEND, 3F, 1F);
                        volleys.remove(p.getUniqueId());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHit(CustomDamageEvent e) {

        if (e.getProjectile() instanceof Arrow) {
            Arrow a = (Arrow) e.getProjectile();
            LivingEntity p = e.getDamagee();

            if (arrows.contains(a)) {
                if (a.getShooter() instanceof LivingEntity) {
                    e.setDamage(8);
                    LogManager.addLog(p, (LivingEntity) a.getShooter(), "Volley");
                }
            }
        }

    }


    @Override
    public boolean usageCheck(Player player) {
        if (player.getLocation().getBlock().getType() == Material.WATER) {
            UtilMessage.message(player, "Skill", "You cannot use " + ChatColor.GREEN + getName() + ChatColor.GRAY + " in water.");
            return false;
        }
        return true;
    }

    @Override
    public double getRecharge(int level) {

        return 15 - ((level - 1) * 2);
    }

    @Override
    public float getEnergy(int level) {

        return 0;
    }

    @Override
    public void activate(Player player, Gamer gamer) {
        volleys.remove(player.getUniqueId());
        volleys.add(player.getUniqueId());
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 2.5F, 2.0F);
        UtilMessage.message(player, getClassType(), "You prepared " + getName());
    }
}
