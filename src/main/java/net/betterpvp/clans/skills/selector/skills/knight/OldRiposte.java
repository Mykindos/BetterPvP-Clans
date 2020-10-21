package net.betterpvp.clans.skills.selector.skills.knight;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.core.particles.ParticleEffect;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import net.betterpvp.core.utility.UtilTime.TimeUnit;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Arrays;
import java.util.HashMap;

public class OldRiposte extends Skill implements InteractSkill {

    public OldRiposte(Clans i) {
        super(i, "Riposte", "Knight", getSwords, rightClick, 3,
                true, true);
    }

    public HashMap<String, Long> prepare = new HashMap<String, Long>();
    public static HashMap<String, Long> god = new HashMap<String, Long>();

    @EventHandler
    public void interact(PlayerInteractEntityEvent e) {
        final Player player = e.getPlayer();
        if (Arrays.asList(getMaterials()).contains(player.getInventory().getItemInMainHand().getType())) {
            if (WeaponManager.getWeapon(player.getInventory().getItemInMainHand()) == null) {
                if (EffectManager.hasEffect(player, EffectType.SILENCE)) {
                    UtilMessage.message(player, getName(), "You are silenced!");
                    return;
                }
                if (hasSkill(player, this)) {
                    if (RechargeManager.getInstance().add(player, getName(), getRecharge(getLevel(player)), true)) {
                        UtilMessage.message(player, getClassType(), "You have prepared " + ChatColor.GREEN + getName(getLevel(player)) + ChatColor.GRAY + ".");
                        ParticleEffect.SMOKE_LARGE.display(player.getLocation().add(0, 0.25, 0).add(player.getLocation().getDirection().multiply(0.4)));
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_ATTACK, 2.0f, 1.3f);
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_ATTACK, 2.0f, 1.3f);
                        prepare.put(player.getName(), System.currentTimeMillis());

                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getInstance(), new Runnable() {
                            public void run() {
                                if (prepare.containsKey(player.getName())) {
                                    UtilMessage.message(player, getClassType(), "You failed " + ChatColor.GREEN + getName() + ChatColor.GRAY + ".");
                                    player.getWorld().playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 2.0f, 1.0f);
                                    prepare.remove(player.getName());
                                }
                            }
                        }, 20);
                    }
                }
            }
        }
    }


    @EventHandler
    public void onRiposteHit(CustomDamageEvent event) {

        if (event.isCancelled()) {
            return;
        }
        if (event.getCause() == DamageCause.ENTITY_ATTACK) {
            if (event.getDamagee() instanceof Player) {

                final LivingEntity damager = event.getDamager();
                final Player target = (Player) event.getDamagee();


                if (prepare.containsKey(target.getName())) {

                    prepare.remove(target.getName());
                    if (damager instanceof Player) {
                        UtilMessage.message(target, getClassType(), "Countered an attack from " + ChatColor.YELLOW + damager.getName() + ChatColor.GRAY + ".");


                        UtilMessage.message(target, getClassType(), "You " + ChatColor.LIGHT_PURPLE + "Riposted " + ChatColor.GRAY + "against "
                                + ChatColor.YELLOW + damager.getName() + ChatColor.GRAY + ".");

                    }
                    target.getWorld().playSound(target.getPlayer().getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1.0F, 1.0F);
                    god.put(target.getName(), (long) (System.currentTimeMillis() + ((getLevel(target) * 0.5)) * 1000));
                    event.setCancelled("Riposte");


                } else {

                    if (god.containsKey(target.getName())) {
                        target.getWorld().playSound(target.getPlayer().getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1.0F, 1.0F);
                        double remaining = UtilTime.convert(((god.get(target.getName())) - System.currentTimeMillis()),
                                TimeUnit.SECONDS, 1);

                        if (remaining <= 0) {
                            god.remove(target.getName());
                            return;
                        }
                        if (damager instanceof Player) {
                            UtilMessage.message((Player) damager, getClassType(), ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " is resistant to melee attacks for "
                                    + ChatColor.GREEN + ChatColor.GREEN + remaining + ChatColor.GRAY + " second.");
                        }
                        event.setDamage(event.getDamage() * 0.35);
                        event.setKnockback(false);
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getInstance(), new Runnable() {
                            public void run() {
                                if (god.containsKey(target.getName())) {
                                    god.remove(target.getName());
                                    UtilMessage.message(target, getClassType(), ChatColor.GRAY + "You are no longer invulnerable.");
                                }
                            }
                        }, 65);
                    }
                }
            }
        }

    }




    @EventHandler
    public void onRiposteDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        god.remove(player.getName());
    }

    @Override
    public boolean usageCheck(Player player) {
        return true;
    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Right click with a sword to activate.",
                "",
                "Reduce all melee damage by 75% for " + ChatColor.GREEN + (1 + (level * 0.5)) + ChatColor.GRAY + " seconds.",
                "Impervious to knockback while active.",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level)
        };
    }

    @Override
    public Types getType() {

        return Types.SWORD;
    }

    @Override
    public double getRecharge(int level) {

        return 20 - ((level - 1) * 1.5);
    }

    @Override
    public float getEnergy(int level) {

        return 0;
    }

    @Override
    public void activate(Player player, Gamer gamer) {
        UtilMessage.message(player, getClassType(), "You have prepared " + ChatColor.GREEN + getName(getLevel(player)) + ChatColor.GRAY + ".");
        prepare.put(player.getName(), System.currentTimeMillis());
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_ATTACK, 2.0f, 1.3f);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_ATTACK, 2.0f, 1.3f);
        ParticleEffect.SMOKE_LARGE.display(player.getLocation().add(0, 0.25, 0).add(player.getLocation().getDirection().multiply(0.4)));
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getInstance(), new Runnable() {
            public void run() {
                if (prepare.containsKey(player.getName())) {
                    UtilMessage.message(player, getClassType(), "You failed " + ChatColor.GREEN + getName() + ChatColor.GRAY + ".");
                    player.getWorld().playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 2.0f, 1.0f);
                    prepare.remove(player.getName());
                }
            }
        }, 20);
    }
}
