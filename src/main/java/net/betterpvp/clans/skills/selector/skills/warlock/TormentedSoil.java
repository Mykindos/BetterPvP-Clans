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
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

public class TormentedSoil extends Skill implements InteractSkill {

    private List<Torment> tormentList = new ArrayList<>();

    public TormentedSoil(Clans i) {
        super(i, "Tormented Soil", "Warlock", getAxes, rightClick, 5, true, true);
    }

    @Override
    public void activate(Player player, Gamer gamer) {
        int level = getLevel(player);
        Location loc = player.getLocation().clone();
        if (!UtilBlock.solid(loc.getBlock())) {
            for (int i = 0; i < 10; i++) {
                loc.subtract(0, 1, 0);
                if (UtilBlock.solid(loc.getBlock())) {
                    break;
                }
            }
        }
        tormentList.add(new Torment(player.getUniqueId(), loc.add(0, 0.5, 0), level));
        UtilMessage.message(player, getClassType(), "You used " + ChatColor.GREEN + getName(level) + ChatColor.GRAY + ".");
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 2f, 1.3f);
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                "Right click with Axe to activate.",
                "",
                "Corrupt the earth around you, creating a ring that debuffs enemies within it for 7 seconds.",
                "Player within the ring take 33% more damage.",
                "",
                "Range: " + ChatColor.GREEN + (5 + (level / 2)) + ChatColor.GRAY + " blocks.",
                "Recharge: " + ChatColor.GREEN + getRecharge(level)
        };
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

        if (UtilBlock.isInLiquid(p)) {
            UtilMessage.message(p, getClassType(), "You cannot use " + ChatColor.GREEN + getName() + ChatColor.GRAY + " in water.");
            return false;
        }

        return true;
    }

    @EventHandler
    public void onDamage(CustomDamageEvent e) {
        if (isInTorment(e.getDamagee())) {
            e.setDamage(e.getDamage() * 1.33);
        }
    }

    private boolean isInTorment(LivingEntity e) {
        ListIterator<Torment> it = tormentList.listIterator();
        while (it.hasNext()) {
            Torment torment = it.next();
            if (torment.location.distance(e.getLocation()) <= (5 + (torment.level / 2))) {
                if (e instanceof Player) {
                    Player damagee = (Player) e;
                    Player tormentOwner = Bukkit.getPlayer(torment.caster);
                    if (tormentOwner != null) {
                        if (ClanUtilities.canHurt(tormentOwner, damagee)) {
                            return true;
                        } else {
                            continue;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }


    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.FAST) {
            ListIterator<Torment> it = tormentList.listIterator();
            while (it.hasNext()) {
                Torment torment = it.next();

                if (UtilTime.elapsed(torment.castTime, 7000)) {
                    it.remove();
                    continue;
                }

                int radius = (5 + (torment.level / 2));
                int particles = 50;
                Location loc = torment.location.clone();
                for (int i = 0; i < particles; i++) {
                    double angle, x, z;
                    angle = 2 * Math.PI * i / particles;
                    x = Math.cos(angle) * radius;
                    z = Math.sin(angle) * radius;

                    loc.add(x, 0, z);

                    if (UtilBlock.airFoliage(loc.getBlock()) && !loc.getBlock().isLiquid()) {
                        loc.subtract(0, 1, 0);
                    }
                    if (UtilBlock.solid(loc.getBlock())) {
                        loc.add(0, 1, 0);
                    }


                    ParticleEffect.END_ROD.display(loc);

                    loc.subtract(x, 0, z);

                }
            }
        }
    }

    private class Torment {

        public UUID caster;
        public Location location;
        public int level;
        public long castTime = System.currentTimeMillis();

        public Torment(UUID caster, Location loc, int level) {
            this.caster = caster;
            this.location = loc;
            this.level = level;
        }

    }
}
