package net.betterpvp.clans.skills.selector.skills.gladiator;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import net.betterpvp.core.utility.UtilVelocity;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class BattleTaunt extends Skill {

    private List<String> active = new ArrayList<String>();

    public BattleTaunt(Clans i) {
        super(i, "Battle Taunt", "Gladiator", getSwords, rightClick, 5, false, true);
    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{"Hold Block with Sword to Channel.",
                "",
                "While channelling, any enemies within " + ChatColor.GREEN + (2 + level) + ChatColor.GRAY + " blocks",
                "are slowly pulled in towards you",
                "",
                "Energy / Second: " + ChatColor.GREEN + getEnergy(level)};
    }

    private final PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 5, 2);

    @EventHandler
    public void Energy(UpdateEvent e) {
        if (e.getType() == UpdateType.TICK) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (active.contains(p.getName())) {

                    if (p.isBlocking()) {
                        if (!Energy.use(p, getName(), getEnergy(getLevel(p)) / 2, true)) {
                            active.remove(p.getName());
                        } else if (!hasSkill(p, this)) {
                            active.remove(p.getName());
                        } else if (!hasSwordInMainHand(p)) {
                            active.remove(p.getName());
                        } else {

                            p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, Material.DIAMOND_BLOCK);


                            for (int i = 0; i <= (2 + getLevel(p)); i++) {
                                pull(p, p.getEyeLocation().add(p.getLocation().getDirection().multiply(i)));
                            }
                        }
                    }

                }
            }
        }

    }

    private void pull(Player p, Location loc) {
        for (LivingEntity other : UtilPlayer.getInRadius(loc, 2.0)) {
            if (other instanceof Player) {
                Player target = (Player) other;
                if (!p.equals(target)) {
                    if (ClanUtilities.canHurt(p, target)) {
                        if (UtilMath.offset(p.getLocation(), other.getLocation()) >= 2.0D) {
                            UtilVelocity.velocity(other, UtilVelocity.getTrajectory(target, p), 0.3D, false, 0.0D, 0.0D, 1.0D, true);
                        }
                    }
                }
            } else {
                UtilVelocity.velocity(other, UtilVelocity.getTrajectory(other, p), 0.3D, false, 0.0D, 0.0D, 1.0D, true);
            }
        }
    }

    @Override
    public void activateSkill(Player p) {
        if (hasSkill(p, this)) {
            if (!active.contains(p.getName())) {
                active.add(p.getName());
            }
        }

    }

    @Override
    public boolean usageCheck(Player p) {
        Clan clan = ClanUtilities.getClan(p.getLocation());
        if (clan != null) {
            if (clan instanceof AdminClan) {
                AdminClan adminClan = (AdminClan) clan;

                if (adminClan.isSafe()) {
                    UtilMessage.message(p, getClassType(), "You cannot use " + ChatColor.GREEN + getName() + ChatColor.GRAY + " in Safe Zones.");
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public Types getType() {

        return Types.SWORD;
    }

    @Override
    public double getRecharge(int level) {

        return 0.5;
    }

    @Override
    public float getEnergy(int level) {

        return 15 - ((level - 1));
    }

}
