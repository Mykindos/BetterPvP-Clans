package net.betterpvp.clans.skills.selector.skills.paladin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.combat.throwables.ThrowableManager;
import net.betterpvp.clans.combat.throwables.events.ThrowableCollideEntityEvent;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.skills.selector.skills.ToggleSkill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class Immolate extends Skill implements ToggleSkill {

    private Set<UUID> active = new HashSet<>();

    public Immolate(Clans i) {
        super(i, "Immolate", "Paladin", getSwordsAndAxes, noActions, 5,
                false, false);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Drop Axe/Sword to Toggle.",
                "",
                "Ignite yourself in flaming fury.",
                "You receive Speed II and",
                "Fire Resistance",
                "",
                "You leave a trail of fire, which",
                "burns players that go near it.",
                "",
                "Energy / Second: " + ChatColor.GREEN + getEnergy(level)

        };
    }

    @EventHandler
    public void Combust(EntityCombustEvent e) {
        if (e.getEntity() instanceof Player) {
            if (this.active.contains(e.getEntity())) {
                e.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void audio(UpdateEvent event) {

        if (event.getType() != UpdateType.SEC) {
            return;
        }
        for (UUID uuid : active) {
            if (Bukkit.getPlayer(uuid) != null) {
                Player cur = Bukkit.getPlayer(uuid);

                cur.getWorld().playSound(cur.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 0.3F, 0.0F);

            }
        }
    }

    @EventHandler
    public void fire(UpdateEvent e) {
        if (e.getType() == UpdateType.FASTEST) {
            Iterator<UUID> it = active.iterator();
            while (it.hasNext()) {
                UUID z = it.next();
                if (Bukkit.getPlayer(z) != null) {
                    Player cur = Bukkit.getPlayer(z);

                    Item fire = cur.getWorld().dropItem(cur.getLocation().add(0.0D, 0.5D, 0.0D), new ItemStack(Material.BLAZE_POWDER));
                    ThrowableManager.addThrowable(fire, cur, getName(), 2000L);
                    fire.setVelocity(new Vector((Math.random() - 0.5D) / 3.0D, Math.random() / 3.0D, (Math.random() - 0.5D) / 3.0D));


                }
            }
        }
    }

    @EventHandler
    public void onCollide(ThrowableCollideEntityEvent e) {
        if (e.getThrowable().getSkillName().equals(getName())) {
            if (e.getThrowable().getThrower() instanceof Player) {
                Player damager = (Player) e.getThrowable().getThrower();
                if (damager != null) {
                    if (e.getCollision() instanceof Player) {
                        if (!ClanUtilities.canHurt(damager, (Player) e.getCollision())) {
                            return; //TODO cancel this event
                        }
                    }

                    if (e.getCollision().getFireTicks() > 0) return;
                    LogManager.addLog(e.getCollision(), damager, "Immolate", 0);
                    e.getCollision().setFireTicks(80);
                }
            }
        }
    }

    @EventHandler
    public void SnowAura(UpdateEvent event) {
        if (event.getType() != UpdateEvent.UpdateType.TICK) {
            return;
        }
        Iterator<UUID> iterator = active.iterator();
        while (iterator.hasNext()) {
            UUID z = iterator.next();
            if (Bukkit.getPlayer(z) != null) {
                Player cur = Bukkit.getPlayer(z);

                if (!hasSkill(cur, this)) {
                    iterator.remove();

                } else if (!Energy.use(cur, getName(), getEnergy(getLevel(cur)) / 5, true)) {
                    iterator.remove();
                    UtilMessage.message(cur, getClassType(), "Immolate: " + ChatColor.RED + "Off");
                } else if (cur == null) {
                    iterator.remove();
                } else if (EffectManager.hasEffect(cur, EffectType.SILENCE)) {
                    iterator.remove();
                    UtilMessage.message(cur, getClassType(), "Immolate: " + ChatColor.RED + "Off");
                } else if (Role.getRole(cur) == null || Role.getRole(cur) != null && !Role.getRole(cur).getName().equals(getClassType())) {
                    iterator.remove();
                    UtilMessage.message(cur, getClassType(), "Immolate: " + ChatColor.RED + "Off");
                } else {
                    UtilPlayer.addPotionEffect(cur, PotionEffectType.SPEED, 1, 25);
                    UtilPlayer.addPotionEffect(cur, PotionEffectType.FIRE_RESISTANCE, 0, 25);
                }
            } else {
                iterator.remove();
            }
        }

    }


    @Override
    public Types getType() {

        return Types.PASSIVE_B;
    }

    @Override
    public boolean usageCheck(Player player) {

        return true;
    }

    @Override
    public double getRecharge(int level) {

        return 0;
    }

    @Override
    public float getEnergy(int level) {

        return (float) 11.5 - ((level - 1));
    }


    @Override
    public void activateToggle(Player player, Gamer gamer) {
        if (active.contains(player.getUniqueId())) {
            active.remove(player.getUniqueId());
            UtilMessage.message(player, getClassType(), "Immolate: " + ChatColor.RED + "Off");
        } else {
            if (Energy.use(player, getName(), 10, false)) {
                active.add(player.getUniqueId());
                UtilMessage.message(player, getClassType(), "Immolate: " + ChatColor.GREEN + "On");
            }

        }
    }
}
