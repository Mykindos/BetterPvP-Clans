package net.betterpvp.clans.skills.selector.skills.warlock;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.classes.roles.Warlock;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.particles.ParticleEffect;
import net.betterpvp.core.particles.data.color.RegularColor;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;


public class Leech extends Skill implements InteractSkill {

    private List<UUID> active;
    private List<LeechData> leechData;
    private List<LeechData> removeList;

    public Leech(Clans i) {
        super(i, "Leech", "Warlock", getSwords, rightClick, 5, true, true);
        active = new ArrayList<>();
        leechData = new ArrayList<>();
        removeList = new ArrayList<>();
    }


    @Override
    public String[] getDescription(int level) {
        return new String[]{"Right click with Sword to activate.",
                "",
                "Create a soul link between all enemies within 7 blocks",
                "of your target, and all enemies within 7 blocks of them",
                "",
                "Linked targets have 1 health leeched per second.",
                "All leeched health is given to the caster.",
                "",
                "Recharge: " + ChatColor.GREEN + getRecharge(level)
        };
    }

    @Override
    public void activate(Player player, Gamer gamer) {
        active.add(player.getUniqueId());
        UtilMessage.message(player, "Skill", "You prepared " + ChatColor.GREEN + getName(getLevel(player)) + ChatColor.GRAY + ".");
    }

    @EventHandler
    public void onDamage(CustomDamageEvent e) {

        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        if (e.getDamager() instanceof Player) {
            Player damager = (Player) e.getDamager();
            Role role = Role.getRole(damager);
            if (role != null && role instanceof Warlock) {
                if (hasSkill(damager, this)) {
                    if (active.contains(damager.getUniqueId())) {
                        if (e.getDamagee() instanceof Player) {
                            if (!ClanUtilities.canHurt(damager, (Player) e.getDamagee())) {
                                return;
                            }
                        }

                        leechData.add(new LeechData(damager, damager, e.getDamagee()));
                        chainEnemies(damager, e.getDamagee());
                        active.remove(damager.getUniqueId());

                        RechargeManager.getInstance().removeCooldown(damager.getName(), getName(), true);
                        RechargeManager.getInstance().add(damager, getName(), getRecharge(getLevel(damager)), showRecharge());

                    }
                }
            }
        }
    }

    private void chainEnemies(Player player, LivingEntity link) {
        List<LivingEntity> temp = new ArrayList<>();
        for (LivingEntity entA : UtilPlayer.getAllInRadius(link.getLocation(), 7)) {
            if (entA instanceof Player) {
                if (!ClanUtilities.canHurt(player, (Player) entA)) {
                    return;
                }
            }
            if (!isLinked(player, entA)) {
                leechData.add(new LeechData(player, link, entA));
                temp.add(entA);
            }


        }

        for (LivingEntity entA : temp) {
            for (LivingEntity entB : UtilPlayer.getAllInRadius(entA.getLocation(), 7)) {
                if (entB instanceof Player) {
                    if (!ClanUtilities.canHurt(player, (Player) entA)) {
                        return;
                    }
                }
                if (!isLinked(player, entB)) {
                    leechData.add(new LeechData(player, entA, entB));
                }
            }
        }
    }

    private void removeLinks(LivingEntity link) {
        List<LivingEntity> children = new ArrayList<>();
        leechData.forEach(l -> {
            if (l.linkedTo.getUniqueId().equals(link.getUniqueId()) || l.target.getUniqueId().equals(link.getUniqueId())) {
                children.add(l.target);
                children.add(l.linkedTo);
                removeList.add(l);
            }
        });

        children.forEach(e -> {
            leechData.forEach(l -> {
                if (l.linkedTo.getUniqueId().equals(e.getUniqueId()) || l.target.getUniqueId().equals(e.getUniqueId())) {
                    removeList.add(l);
                }
            });
        });


    }

    private void breakChain(LeechData leech) {
        leechData.forEach(l -> {
            if (l.owner.getUniqueId().equals(leech.owner.getUniqueId())) {
                removeList.add(l);
            }
        });
    }

    private boolean isLinked(Player player, LivingEntity ent) {
        if (player.equals(ent)) return true;
        for (LeechData l : leechData) {
            if (l.owner.equals(player)) {
                if (l.linkedTo.equals(ent) || l.target.equals(ent)) {
                    return true;
                }
            }
        }

        return false;
    }


    @Override
    public Types getType() {
        return Types.SWORD;
    }

    @Override
    public double getRecharge(int level) {
        return 25 - (level * 2);
    }

    @Override
    public float getEnergy(int level) {
        return 0;
    }

    @Override
    public boolean usageCheck(Player p) {
        if (active.contains(p.getUniqueId())) {
            UtilMessage.message(p, "Skill", ChatColor.GREEN + getName() + ChatColor.GRAY + " is already active.");
            return false;
        }

        return true;
    }

    @EventHandler
    public void onLeech(UpdateEvent e) {
        if (!removeList.isEmpty()) {
            leechData.removeIf(l -> removeList.contains(l));
            removeList.clear();
        }
        if (e.getType() == UpdateEvent.UpdateType.FASTER) {
            if (leechData.isEmpty()) return;

            ListIterator<LeechData> leechIt = leechData.listIterator();
            while (leechIt.hasNext()) {
                LeechData l = leechIt.next();

                if (l.linkedTo == null || l.target == null || l.owner == null) {
                    removeList.add(l);
                    continue;
                }

                if (l.linkedTo.isDead() || l.owner.isDead() || l.target.isDead()) {
                    if (l.owner.isDead()) {
                        breakChain(l);
                    }
                    removeList.add(l);
                    continue;
                }

                if (l.target.getLocation().distance(l.linkedTo.getLocation()) > 7 || l.target.getLocation().distance(l.owner.getLocation()) > 21) {
                    if (l.linkedTo.getUniqueId().equals(l.owner.getUniqueId())) {
                        breakChain(l);
                    }
                    removeList.add(l);
                    continue;
                }

            }

        } else if (e.getType() == UpdateEvent.UpdateType.FASTEST) {
            ListIterator<LeechData> leechIt = leechData.listIterator();
            while (leechIt.hasNext()) {
                LeechData l = leechIt.next();

                if (l.linkedTo == null || l.target == null || l.owner == null) {
                    continue;
                }

                Location loc = l.linkedTo.getLocation();
                Vector v = l.target.getLocation().toVector().subtract(loc.toVector());
                double distance = l.linkedTo.getLocation().distance(l.target.getLocation());
                boolean remove = false;
                if (distance > 7) continue;
                for (double i = 0.5; i < distance; i += 0.5) {

                    v.multiply(i);
                    loc.add(v);
                    if (UtilBlock.solid(loc.getBlock()) && UtilBlock.solid(loc.clone().add(0, 1, 0).getBlock())) {
                        remove = true;
                    }
                    ParticleEffect.REDSTONE.display(loc.clone().add(0, 0.7, 0), new RegularColor(230, 0, 0));
                    loc.subtract(v);
                    v.normalize();

                }

                if (remove) {
                    removeList.add(l);
                }

            }
        } else if (e.getType() == UpdateEvent.UpdateType.SEC) {
            ListIterator<LeechData> it = leechData.listIterator();
            while (it.hasNext()) {
                LeechData l = it.next();
                LogManager.addLog(l.target, l.owner, "Leech");
                CustomDamageEvent leechDmg = new CustomDamageEvent(l.target, l.owner, null, EntityDamageEvent.DamageCause.MAGIC, 1, false);
                leechDmg.setIgnoreArmour(true);
                Bukkit.getPluginManager().callEvent(leechDmg);
                UtilPlayer.health(l.owner, 1);
            }
        }
    }

    @EventHandler
    public void removeOnDeath(EntityDeathEvent e) {
        removeLinks(e.getEntity());
    }


    public class LeechData {
        public Player owner;

        public LivingEntity linkedTo;
        public LivingEntity target;

        public LeechData(Player owner, LivingEntity linkedTo, LivingEntity target) {
            this.owner = owner;
            this.linkedTo = linkedTo;
            this.target = target;
        }
    }
}
