package net.betterpvp.clans.skills.selector.skills.warlock;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.classes.roles.Warlock;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.utilities.UtilClans;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.particles.ParticleEffect;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilPlayer;
import net.betterpvp.core.utility.UtilVelocity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Siphon extends Skill {

    public Siphon(Clans i) {
        super(i, "Siphon", "Warlock", noMaterials, noActions, 5, false, false);
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                "Siphon energy from all enemies within " + ChatColor.GREEN + (4 + level) + ChatColor.GRAY + " blocks,",
                "Granting you Speed II and sometimes a small amount of health.",
                "",
                "Energy siphoned per second: " + ChatColor.GREEN + 5
        };
    }

    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.SEC_2) {
            for (Player warlock : Bukkit.getOnlinePlayers()) {
                Role role = Role.getRole(warlock);
                if (role != null && role instanceof Warlock) {
                    if (hasSkill(warlock, this)) {
                        int level = getLevel(warlock);
                        for (Player target : UtilPlayer.getInRadius(warlock.getLocation(), 4 + level)) {
                            if (!ClanUtilities.canHurt(warlock, target)) continue;
                            Energy.degenerateEnergy(target, 0.1f);

                            new BukkitRunnable() {
                                private Location position = target.getLocation().add(0, 1, 0);
                                private Player player = warlock;

                                @Override
                                public void run() {
                                    Location playerLoc = player.getLocation().clone().add(0, 1, 0);
                                    Vector v = UtilVelocity.getTrajectory(position, playerLoc);
                                    if (player == null) {
                                        this.cancel();
                                        return;
                                    }
                                    if (player.isDead()) {
                                        this.cancel();
                                        return;
                                    }
                                    if (position.distance(playerLoc) < 1) {
                                        if (UtilMath.randomInt(10) == 1) {
                                            UtilPlayer.health(player, 1);
                                        }
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 50, 1));
                                        this.cancel();
                                        return;
                                    }

                                    ParticleEffect.END_ROD.display(position);
                                    v.multiply(0.6);
                                    position.add(v);
                                }
                            }.runTaskTimer(getInstance(), 0l, 2);

                        }
                    }
                }
            }
        }
    }


    @Override
    public Types getType() {
        return Types.PASSIVE_B;
    }

    @Override
    public double getRecharge(int level) {
        return 0;
    }

    @Override
    public float getEnergy(int level) {
        return 0;
    }

    @Override
    public boolean usageCheck(Player p) {
        return false;
    }
}
