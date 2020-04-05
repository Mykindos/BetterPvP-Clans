package net.betterpvp.clans.skills.selector.skills.knight;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilFormat;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class BullsCharge extends Skill {

    private HashMap<String, Long> running = new HashMap<String, Long>();

    public BullsCharge(Clans i) {
        super(i, "Bulls Charge", "Knight",
                getAxes,
                rightClick, 5, true, true);
    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Right click with Axe to Activate.",
                "",
                "Enter a rage, gaining massive movement speed",
                "and slowing anything you hit for 3 seconds",
                "",
                "While charging, you take no knockback.",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level),
                "Energy: " + ChatColor.GREEN + getEnergy(level)};
    }

    @Override
    public void activateSkill(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 2));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1.5F, 0.0F);
        player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, 49);
        UtilMessage.message(player, getClassType(), "You used " + ChatColor.GREEN + getName() + ChatColor.GRAY + ".");
        running.put(player.getName(), System.currentTimeMillis() + 4000L);
    }

    @Override
    public boolean usageCheck(Player player) {
        if (player.getLocation().getBlock().getType() == Material.WATER ) {
            UtilMessage.message(player, "Skill", "You cannot use " + ChatColor.GREEN + getName() + " in water.");
            return false;
        }
        return true;
    }

    @EventHandler
    public void onDamage(CustomDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }


        if (event.getDamagee() instanceof Player) {
            Player player = (Player) event.getDamagee();
            if (running.containsKey(player.getName())) {
                event.setKnockback(false);

            }
        }

        if (event.getCause() != DamageCause.ENTITY_ATTACK) {
            return;
        }

        if (event.getDamagee() instanceof LivingEntity) {
            if (event.getDamager() instanceof Player) {

                Player damager = (Player) event.getDamager();
                final LivingEntity damagee = (LivingEntity) event.getDamagee();

                if (running.containsKey(damager.getName())) {
                    if (System.currentTimeMillis() >= running.get(damager.getName())) {
                        running.remove(damager.getName());
                        return;
                    }

                    event.setKnockback(false);
                    if (damagee instanceof Player) {
                        if (!ClanUtilities.canHurt(damager, (Player) damagee)) {
                            return;
                        }
                    }

                    damagee.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 2));
                    damager.removePotionEffect(PotionEffectType.SPEED);

                    damager.getWorld().playSound(damager.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1.5F, 0.0F);
                    damager.getWorld().playSound(damager.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1.5F, 0.5F);

                    if (event.getDamagee() instanceof Player) {
                        Player damaged = (Player) event.getDamagee();
                        UtilMessage.message(damaged, getClassType(), ChatColor.YELLOW + damager.getName() + ChatColor.GRAY + " hit you with " + ChatColor.GREEN + getName() + ".");
                        UtilMessage.message(damager, getClassType(), "You hit " + ChatColor.YELLOW + damaged.getName() + ChatColor.GRAY + " with " + ChatColor.GREEN + getName() + ChatColor.GRAY + ".");
                        running.remove(damager.getName());
                        return;
                    }

                    UtilMessage.message(damager, getClassType(), "You hit a " + ChatColor.YELLOW + UtilFormat.cleanString(damagee.getType().toString())
                            + ChatColor.GRAY + " with " + ChatColor.GREEN + getName() + ChatColor.GRAY + ".");
                    running.remove(damager.getName());
                }
            }
        }
    }

    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateType.SEC) {
            Iterator<Entry<String, Long>> it = running.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, Long> next = it.next();
                if (next.getValue() - System.currentTimeMillis() <= 0) {
                    it.remove();
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

        return 12 - ((level - 1) * 1);
    }

    @Override
    public float getEnergy(int level) {

        return (float) 35 - ((level - 1) * 5);
    }

}
