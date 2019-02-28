package net.betterpvp.clans.skills.selector.skills.paladin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.combat.throwables.ThrowableManager;
import net.betterpvp.clans.combat.throwables.events.ThrowableCollideEntityEvent;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
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
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class Immolate extends Skill {

    private Set<UUID> active = new HashSet<>();

    public Immolate(Clans i) {
        super(i, "Immolate", "Paladin", getSwordsAndAxes, noActions, 5,
                false, false);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String[] getDescription(int level) {
        // TODO Auto-generated method stub
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
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (hasSkill(p, this)) {
            if (Arrays.asList(getMaterials()).contains(e.getItemDrop().getItemStack().getType())) {

                e.setCancelled(true);

                if (active.contains(p.getUniqueId())) {
                    active.remove(p.getUniqueId());
                    UtilMessage.message(p, getClassType(), "Immolate: " + ChatColor.RED + "Off");
                } else {
                    if (ClanUtilities.canCast(p)) {
                        if (Energy.use(p, getName(), 10, false)) {
                            active.add(p.getUniqueId());
                            UtilMessage.message(p, getClassType(), "Immolate: " + ChatColor.GREEN + "On");
                        }
                    }
                }
            }


        }
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

                cur.getWorld().playSound(cur.getLocation(), Sound.FIZZ, 0.3F, 0.0F);

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
                    LogManager.addLog(e.getCollision(), damager, "Immolate");
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
                } else if (EffectManager.isSilenced(cur)) {
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
        // TODO Auto-generated method stub
        return Types.PASSIVE_B;
    }

    @Override
    public void activateSkill(Player player) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean usageCheck(Player player) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public double getRecharge(int level) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getEnergy(int level) {
        // TODO Auto-generated method stub
        return 8 - ((level - 1));
    }

    @Override
    public boolean requiresShield() {
        // TODO Auto-generated method stub
        return false;
    }


}
