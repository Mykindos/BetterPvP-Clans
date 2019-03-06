package net.betterpvp.clans.skills.selector.skills.paladin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilTime;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Bat;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.Map.Entry;

public class Swarm extends Skill {

    private WeakHashMap<Player, Long> batCD = new WeakHashMap<>();
    private WeakHashMap<Player, ArrayList<BatData>> batData = new WeakHashMap<>();
    private List<UUID> current = new ArrayList<>();


    public Swarm(Clans i) {
        super(i, "Swarm", "Paladin", getSwords, rightClick, 5, true, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Hold Block with Sword to Channel",
                "",
                "Release a swarm of bats",
                "which damage, and knockback",
                "any enemies they come in contact with",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level),
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

        return 10 - ((level - 1) * 1);
    }

    @Override
    public void activateSkill(Player p) {
        if (hasSkill(p, this)) {
            if (Energy.use(p, getName(), 5.0, false)) {
                if (!current.contains(p.getUniqueId())) {
                    current.add(p.getUniqueId());
                    if (!batData.containsKey(p)) {
                        batData.put(p, new ArrayList<>());
                    }
                }
            }

        }
    }

    public boolean hitPlayer(Location loc, LivingEntity player) {
        if (loc.add(0, -loc.getY(), 0).toVector().subtract(player.getLocation()
                .add(0, -player.getLocation().getY(), 0).toVector()).length() < 0.8D) {
            return true;
        }
        if (loc.add(0, -loc.getY(), 0).toVector().subtract(player.getLocation()
                .add(0, -player.getLocation().getY(), 0).toVector()).length() < 1.2) {
            return (loc.getY() > player.getLocation().getY()) && (loc.getY() < player.getEyeLocation().getY());
        }
        return false;
    }


    @EventHandler
    public void Update(UpdateEvent event) {
        if (event.getType() == UpdateType.TICK) {
            for (Player cur : Bukkit.getOnlinePlayers()) {


                if (current.contains(cur.getUniqueId())) {
                    if (cur.isBlocking()) {
                        if (!Energy.use(cur, getName(), getEnergy(getLevel(cur)) / 2, true)) {
                            current.remove(cur.getUniqueId());
                        } else if (!hasSkill(cur, this)) {
                            current.remove(cur.getUniqueId());
                        } else if (!hasSwordInMainHand(cur)) {
                            current.remove(cur.getUniqueId());
                        } else {
                            if (batData.containsKey(cur)) {

                                Bat bat = cur.getWorld().spawn(cur.getLocation().add(0, 0.5, 0), Bat.class);
                                bat.setHealth(1);
                                bat.setVelocity(cur.getLocation().getDirection().multiply(2));
                                batData.get(cur).add(new BatData(bat, System.currentTimeMillis(), cur.getLocation()));

                            }
                        }
                    } else {
                        current.remove(cur.getUniqueId());
                    }
                }
            }


        }
    }

    @EventHandler
    public void batHit(UpdateEvent e) {
        if (e.getType() == UpdateType.TICK) {
            for (Player p : batData.keySet()) {
                for (BatData bat2 : batData.get(p)) {
                    Bat bat = bat2.bat;
                    Vector rand = new Vector((Math.random() - 0.5D) / 3.0D, (Math.random() - 0.5D) / 3.0D, (Math.random() - 0.5D) / 3.0D);
                    bat.setVelocity(bat2.loc.getDirection().clone().multiply(0.5D).add(rand));

                    for (LivingEntity other : p.getWorld().getLivingEntities()) {
                        if (other instanceof Bat) continue;
                        if (hitPlayer(bat.getLocation(), other)) {


                            LogManager.addLog(other, p, "Swarm");
                            if (other instanceof Player) {

                                if (other.equals(p)) continue;
                                if (batCD.containsKey(other)) {
                                    if (UtilTime.elapsed(batCD.get(other), 500)) {
                                        batCD.remove(other);
                                    } else {
                                        continue;
                                    }

                                }
                                if (ClanUtilities.canHurt(p, (Player) other)) {

                                    batCD.put((Player) other, System.currentTimeMillis());
                                } else {
                                    continue;
                                }
                            }


                            Vector v = bat.getLocation().getDirection();
                            v.normalize();
                            v.multiply(.4d);
                            v.setY(v.getY() + 0.2d);

                            if (v.getY() > 7.5)
                                v.setY(7.5);

                            if (other.isOnGround())
                                v.setY(v.getY() + 0.4d);

                            other.setFallDistance(0);
                            Bukkit.getPluginManager().callEvent(new CustomDamageEvent(other, p, null, DamageCause.CUSTOM, 1, false));
                            //other.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 0));
                            if (other instanceof Player) {
                                EffectManager.addEffect((Player) other, EffectType.SHOCK, 1000L);
                            }

                            other.setVelocity(bat.getLocation().getDirection().add(new Vector(0, .4F, 0)).multiply(0.50));


                            bat.getWorld().playSound(bat.getLocation(), Sound.BAT_HURT, 0.2F, 0.7F);

                            bat.remove();

                        }
                    }

                }
            }
        }
    }


    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateType.FAST) {

            Iterator<Entry<Player, ArrayList<BatData>>> iterator = batData.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<Player, ArrayList<BatData>> data = iterator.next();
                ListIterator<BatData> batIt = data.getValue().listIterator();
                while (batIt.hasNext()) {
                    BatData bat = batIt.next();

                    if (bat.bat == null || bat.bat.isDead()) {
                        batIt.remove();
                        continue;
                    }

                    if (UtilTime.elapsed(bat.timer, 2000)) {
                        bat.bat.remove();
                        batIt.remove();

                    }
                }

                if (data.getValue().isEmpty()) {
                    iterator.remove();
                }
            }

        }
    }

    @Override
    public boolean usageCheck(Player p) {

        return true;
    }

    @Override
    public boolean requiresShield() {

        return true;
    }


    private class BatData {

        public Bat bat;
        public long timer;
        public Location loc;

        public BatData(Bat bat, long spawn, Location loc) {
            this.bat = bat;
            this.timer = spawn;
            this.loc = loc;
        }
    }

}
