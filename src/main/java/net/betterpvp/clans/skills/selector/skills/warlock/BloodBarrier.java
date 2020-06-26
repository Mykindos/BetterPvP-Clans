package net.betterpvp.clans.skills.selector.skills.warlock;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.particles.ParticleEffect;
import net.betterpvp.core.particles.data.color.RegularColor;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;


import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

public class BloodBarrier extends Skill implements InteractSkill {

    private List<ShieldData> shieldDataList;


    @Override
    public String[] getDescription(int level) {
        return new String[]{
                "Right click with Axe to activate.",
                "",
                "Sacrifice " + ChatColor.GREEN + UtilMath.round( 100 -(0.50 + (level * 0.05)) * 100, 2) + "%" + ChatColor.GRAY + " of your health to grant",
                "yourself and all nearby allies a barrier which reduces",
                "the damage of the next 3 incoming attacks by 30%",
                "",
                "Barrier lasts up to 1 minute total, and does not stack.",
                "",
                "Recharge: " + ChatColor.GREEN + getRecharge(level)
        };
    }

    public BloodBarrier(Clans i) {
        super(i, "Blood Barrier", "Warlock", getAxes, rightClick, 5, true, true);
        shieldDataList = new ArrayList<>();
    }

    @Override
    public void activate(Player player, Gamer gamer) {
        int level = getLevel(player);
        int range = 8 + level;
        double healthReduction = 0.50 + (level * 0.05);
        double proposedHealth = player.getHealth() - (20 - (20 * healthReduction));
        player.setHealth(Math.max(0.5, proposedHealth));


        UtilMessage.message(player, getClassType(), "You used " + ChatColor.GREEN + getName(level) + ChatColor.GRAY + ".");

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EVOKER_PREPARE_ATTACK, 2.0f, 1.0f);

        for (Player p : UtilPlayer.getInRadius(player.getLocation(), range)) {
            if (!ClanUtilities.canHurt(player, p)) {
                ShieldData shieldData = getShieldData(p);
                if (shieldData != null) {
                    shieldDataList.remove(shieldData);
                }
                System.out.println("Added blood barrier for " + p.getName());
                shieldDataList.add(new ShieldData(p, level));

            }
        }
    }

    @EventHandler
    public void removeOnDeath(PlayerDeathEvent e){
        ShieldData shieldData = getShieldData(e.getEntity());
        if (shieldData != null) {
            shieldDataList.remove(shieldData);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(CustomDamageEvent e) {
        if(e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        if (e.getDamagee() instanceof Player) {
            Player player = (Player) e.getDamagee();

            ShieldData shieldData = getShieldData(player);
            if (shieldData != null) {
                e.setDamage(e.getDamage() * 0.70);
                shieldData.count--;
            }
        }
    }

    /**
     * Might implement if stacking is allowed
     * <p>
     * public ShieldData getShieldData(Player owner, Player target) {
     * return shieldDataList.stream().filter(b -> owner.getUniqueId().equals(b.owner)
     * && target.getUniqueId().equals(b.player)).findFirst().orElse(null);
     * }
     */
    public ShieldData getShieldData(Player target) {
        return shieldDataList.stream().filter(b -> target.getUniqueId().equals(b.player) && b.count > 0).findFirst().orElse(null);
    }

    @EventHandler
    public void updateParticles(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.TICK) {
            if (shieldDataList.isEmpty()) return;
            ListIterator<ShieldData> it = shieldDataList.listIterator();
            while (it.hasNext()) {
                ShieldData next = it.next();

                if (next.count <= 0) {
                    it.remove();
                    continue;
                }

                if (next.endTime - System.currentTimeMillis() <= 0) {
                    Player p = Bukkit.getPlayer(next.player);
                    if (p != null) {
                        UtilMessage.message(p, "Skill", "Your blood barrier has expired.");
                    }
                    it.remove();
                    continue;
                }

                Player player = Bukkit.getPlayer(next.player);
                if (player != null) {

                    double oX = Math.sin(player.getTicksLived() / 10) * 1;
                    double oZ = Math.cos(player.getTicksLived() / 10) * 1;
                    ParticleEffect.REDSTONE.display(player.getLocation().add(oX, 0.4, oZ), new RegularColor(200, 0, 0));
                }

            }
        }
    }


    @Override
    public Types getType() {
        return Types.AXE;
    }

    @Override
    public double getRecharge(int level) {
        return 30 - (level * 2);
    }

    @Override
    public float getEnergy(int level) {
        return 0;
    }

    @Override
    public boolean usageCheck(Player p) {
        int level = getLevel(p);
        double healthReduction = 0.50 + (level * 0.05);
        double proposedHealth = p.getHealth() - (UtilPlayer.getMaxHealth(p) - (UtilPlayer.getMaxHealth(p) * healthReduction));

        if (proposedHealth <= 0.5) {
            UtilMessage.message(p, "Skill", "You do not have enough health to use " + ChatColor.GREEN + getName(level) + ChatColor.GRAY + ".");
            return false;
        }

        return true;
    }

    public class ShieldData {

        public UUID player;
        public int count = 3;
        public int level;
        public long endTime;

        public ShieldData(Player player, int level) {
            this.player = player.getUniqueId();
            this.level = level;
            this.endTime = System.currentTimeMillis() + 60_000;
        }
    }
}

