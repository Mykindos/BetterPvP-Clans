package net.betterpvp.clans.skills.selector.skills.knight;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.HashSet;
import java.util.WeakHashMap;


public class Riposte extends Skill implements InteractSkill {

    private WeakHashMap<Player, Long> prepare = new WeakHashMap<>();
    public static WeakHashMap<Player, LivingEntity> block = new WeakHashMap<>();


    public Riposte(Clans i) {
        super(i, "Riposte", "Knight",
                getSwords, rightClick, 5, true, true);
    }

    public long godTime = 30L;
    public double prepareTime = 1;


    @EventHandler(priority = EventPriority.LOW)
    public void onParry(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getCause() == DamageCause.ENTITY_ATTACK) {
            if (event.getEntity() instanceof Player) {
                Player damagee = (Player) event.getEntity();
                if (hasSkill(damagee, this)) {
                    if (event.getDamager() instanceof Player) {
                        if (!ClanUtilities.canHurt((Player) event.getDamager(), damagee)) {
                            return;
                        }
                    }
                    if (damagee.isHandRaised()) {
                        if (prepare.containsKey(damagee)) {
                            if (event.getDamager() instanceof LivingEntity) {
                                LivingEntity damager = (LivingEntity) event.getDamager();
                                if (!block.containsKey(damagee)) {
                                    event.setCancelled(true);
                                    block.put(damagee, damager);
                                    prepare.put(damagee, System.currentTimeMillis() + 2000);

                                    UtilMessage.message(damagee, getClassType(), "You parried with " + getName() + ".");
                                    if (damager instanceof Player) {
                                        Player temp = (Player) damager;
                                        UtilMessage.message(temp, getClassType(), damagee.getName() + " parried with " + getName() + ".");
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRiposte(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) {
            return;
        }

        if (e.getCause() == DamageCause.ENTITY_ATTACK) {
            if (e.getDamager() instanceof Player) {
                Player damager = (Player) e.getDamager();
                Player damagee = null;
                if (e.getEntity() instanceof Player) {
                    damagee = (Player) e.getEntity();
                    if (!ClanUtilities.canHurt(damager, damagee)) {
                        return;
                    }
                }
                if (e.getEntity() instanceof LivingEntity) {

                    if (prepare.containsKey(damager)) {
                        if (block.containsKey(damager)) {
                            LivingEntity ent = block.get(damager);
                            if (ent.equals(e.getEntity())) {
                                block.remove(damager);
                                prepare.remove(damager);
                                int level = getLevel(damager);

                                damager.getWorld().playSound(damager.getLocation(), Sound.ENTITY_ZOMBIE_DEATH, 1.0F, 1.2F);
                                ent.damage((1 + (level * 0.5)));
                                UtilPlayer.health(damager, (1 + (level * 0.5)));
                                Energy.regenerateEnergy(damager, 20.0);
                                UtilMessage.message(damager, getClassType(), "You countered with " + getName() + ".");
                                if (damagee != null) {
                                    UtilMessage.message(damagee, getClassType(), damager.getName() + " countered with " + getName() + ".");
                                    LogManager.addLog(damagee, damager, "Riposte");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void End(UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }
        HashSet<Player> expired = new HashSet<>();

        for (Player cur : prepare.keySet()) {
            if (System.currentTimeMillis() > prepare.get(cur)) {
                expired.add(cur);
            }
        }
        for (Player cur : expired) {

            prepare.remove(cur);
            block.remove(cur);


            UtilMessage.message(cur, getClassType(), "You failed to " + getName() + ".");
        }
    }


    @Override
    public boolean usageCheck(Player player) {
        if (!hasSkill(player, this)) {
            return false;
        }

        if (UtilBlock.isInLiquid(player)) {
            UtilMessage.message(player, getClassType(), "");
            return false;
        }

        return true;
    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Block an incoming attack to parry,",
                "then quickly return the attack ",
                "to riposte.",
                "",
                "If successful, you gain " + ChatColor.GREEN + (1 + ((level - 1) * 0.5)) + ChatColor.GRAY + " health,",
                "and deal " + ChatColor.GREEN + (1 + ((level - 1) * 0.5)) + ChatColor.GRAY + " bonus damage.",
                "",
                "You must block, parry, then riposte",
                "all within 2 second of each other."
        };
    }

    @Override
    public Types getType() {

        return Types.SWORD;
    }

    @Override
    public double getRecharge(int level) {

        return 14;
    }

    @Override
    public float getEnergy(int level) {

        return 0;
    }

    @Override
    public void activate(Player player, Gamer gamer) {
        prepare.put(player, System.currentTimeMillis() + 2000L);
        UtilMessage.message(player, getClassType(), "You prepared to " + getName() + ".");
    }
}
