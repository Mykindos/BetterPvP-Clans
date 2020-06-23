package net.betterpvp.clans.skills.selector.skills.paladin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.ChannelSkill;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class Blizzard extends ChannelSkill implements InteractSkill {

    private List<String> active = new ArrayList<>();
    private WeakHashMap<Snowball, Player> snow = new WeakHashMap<>();

    public Blizzard(Clans i) {
        super(i, "Blizzard", "Paladin", getSwords, rightClick, 5, false, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Hold Right click with Sword to Activate.",
                "",
                "While channeling, release a blizzard",
                "that slows anyone hit for 2 seconds.",
                "",
                "Energy: " + ChatColor.GREEN + getEnergy(level)
        };
    }

    @Override
    public Types getType() {

        return Types.SWORD;
    }

    @Override
    public double getRecharge(int level) {

        return 0;
    }

    @Override
    public float getEnergy(int level) {

        return 9 - ((level - 1) * 1);
    }


    @EventHandler
    public void onHit(CustomDamageEvent e) {
        if (e.getProjectile() instanceof Snowball) {

            Snowball s = (Snowball) e.getProjectile();
            if (snow.containsKey(s)) {
                e.setCancelled("Snowball");

                snow.remove(s);
                if (e.getDamagee().hasPotionEffect(PotionEffectType.SLOW)) {
                    e.getDamagee().removePotionEffect(PotionEffectType.SLOW);
                }
                e.getDamagee().setVelocity(e.getProjectile().getVelocity().multiply(0.1).add(new Vector(0, 0.25, 0)));
                e.getDamagee().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 2));
            }

        }

    }

    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateType.TICK) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (active.contains(p.getName())) {
                    if (p.isHandRaised()) {
                        if (!Energy.use(p, getName(), getEnergy(getLevel(p)) / 4, true)) {
                            active.remove(p.getName());
                        } else if (!hasSkill(p, this)) {
                            active.remove(p.getName());
                        } else if (!hasSwordInMainHand(p)) {
                            active.remove(p);
                        } else {
                            Snowball s = p.launchProjectile(Snowball.class);
                            s.getLocation().add(0, 1, 0);
                            s.setVelocity(p.getLocation().getDirection().add(new Vector(UtilMath.randDouble(-0.3, 0.3), UtilMath.randDouble(-0.2, 0.4), UtilMath.randDouble(-0.3, 0.3))));
                            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_SNOW_STEP, 1f, 0.4f);
                            snow.put(s, p);
                        }
                    } else {
                        active.remove(p.getName());
                    }
                }
            }
        }
    }

    @Override
    public boolean usageCheck(Player p) {
        if (p.getLocation().getBlock().isLiquid()) {
            UtilMessage.message(p, getClassType(), "You cannot use " + getName() + " in water.");
            return false;
        }

        return true;
    }

    @Override
    public void activate(Player p, Gamer gamer) {
        if (!active.contains(p.getName())) {
            active.add(p.getName());
        }
    }
}
