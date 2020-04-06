package net.betterpvp.clans.skills.selector.skills.paladin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.combat.throwables.ThrowableManager;
import net.betterpvp.clans.combat.throwables.events.ThrowableCollideEntityEvent;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.ChannelSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.nio.channels.Channel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;

public class Inferno extends ChannelSkill {

    private Set<String> active = new HashSet<String>();


    public Inferno(Clans i) {
        super(i, "Inferno", "Paladin", getSwords,
                rightClick, 5, false, true);

    }


    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Hold Block with Sword to Channel.",
                "",
                "You spray fire at high speed,",
                "igniting anything it hits.",
                "",
                "Energy / Second: " + ChatColor.GREEN + getEnergy(level)
        };
    }

    @Override
    public Types getType() {

        return Types.SWORD;
    }


    @Override
    public void activateSkill(Player p) {
        if (hasSkill(p, this)) {
            active.add(p.getName());
        }
    }

    @Override
    public boolean usageCheck(Player player) {
        if (UtilBlock.isInLiquid(player)) {
            UtilMessage.message(player, getName(), "You cannot use " + getName() + " in water.");
            return false;
        }

        return true;
    }

    private WeakHashMap<LivingEntity, Long> tempImmune = new WeakHashMap<>();

    @EventHandler
    public void onCollide(ThrowableCollideEntityEvent e) {
        if (e.getThrowable().getSkillName().equals(getName())) {
            if (e.getCollision() instanceof ArmorStand) {
                return;
            }
            if (e.getThrowable().getThrower() instanceof Player) {
                Player damager = (Player) e.getThrowable().getThrower();
                if (damager != null) {

                    if (e.getCollision().getFireTicks() <= 0) {
                        if (e.getCollision() instanceof Player) {
                            if (!ClanUtilities.canHurt(damager, (Player) e.getCollision())) {
                                return; //TODO cancel this event
                            }

                        }

                        e.getCollision().setFireTicks(80);
                    }
                    if (!e.getThrowable().getImmunes().contains(e.getCollision())) {
                        if (tempImmune.containsKey(e.getCollision())) return;
                        LogManager.addLog(e.getCollision(), damager, "Inferno");
                        CustomDamageEvent cde = new CustomDamageEvent(e.getCollision(), damager, null, DamageCause.FIRE, 1, false, "Inferno");
                        cde.setDamageDelay(0);
                        Bukkit.getPluginManager().callEvent(cde);
                        e.getThrowable().getImmunes().add(e.getCollision());
                        tempImmune.put(e.getCollision(), System.currentTimeMillis());
                    }
                }
            }
        }
    }


    @EventHandler
    public void updateImmunes(UpdateEvent e) {
        if (e.getType() == UpdateType.FASTEST) {
            Iterator<Entry<LivingEntity, Long>> it = tempImmune.entrySet().iterator();
            while (it.hasNext()) {
                Entry<LivingEntity, Long> next = it.next();
                if (UtilTime.elapsed(next.getValue(), 450)) {
                    it.remove();
                }
            }
        }
    }

    @EventHandler
    public void Update(UpdateEvent event) {
        if (event.getType() == UpdateType.TICK) {
            for (Player cur : Bukkit.getOnlinePlayers()) {


                if (active.contains(cur.getName())) {
                    if (cur.isHandRaised()) {
                        if (!Energy.use(cur, getName(), getEnergy(getLevel(cur)) / 2, true)) {
                            active.remove(cur.getName());
                        } else if (!hasSkill(cur, this)) {
                            active.remove(cur.getName());
                        } else if (!hasSwordInMainHand(cur)) {
                            active.remove(cur.getName());
                        } else {
                            Item fire = cur.getWorld().dropItem(cur.getEyeLocation(), new ItemStack(Material.BLAZE_POWDER));
                            ThrowableManager.addThrowable(fire, cur, getName(), 3000L);


                            fire.teleport(cur.getEyeLocation());

                            fire.setVelocity(cur.getLocation().getDirection().add(new Vector(UtilMath.randDouble(-0.2, 0.2), UtilMath.randDouble(-0.2, 0.3), UtilMath.randDouble(-0.2, 0.2))));
							/*
							double x = 0.10 - UtilMath.randomInt(20) / 100.0;
							double y = 0.5 - UtilMath.randomInt(20) / 100.0;
							double z = 0.10 - UtilMath.randomInt(20) / 100.0;
							fire.setVelocity(cur.getLocation().getDirection().add(new Vector(x,y,z)));
							 */
                            cur.getWorld().playSound(cur.getLocation(), Sound.ENTITY_GHAST_SHOOT, 0.1F, 1.0F);
                        }
                    } else {
                        active.remove(cur.getName());
                    }
                }
            }


        }
    }

    @Override
    public double getRecharge(int level) {

        return 0;
    }

    @Override
    public float getEnergy(int level) {

        return 9 - ((level - 1) * 1);
    }

}
