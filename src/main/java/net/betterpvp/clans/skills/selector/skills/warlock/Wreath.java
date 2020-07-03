package net.betterpvp.clans.skills.selector.skills.warlock;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.events.SkillDequipEvent;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilEntity;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.WeakHashMap;

public class Wreath extends Skill implements InteractSkill {

    private WeakHashMap<Player, Integer> actives;
    private WeakHashMap<Player, Long> cooldowns;

    public Wreath(Clans i) {
        super(i, "Wreath", "Warlock", getSwords, rightClick, 5, true, true);
        actives = new WeakHashMap<>();
        cooldowns = new WeakHashMap<>();

    }

    @Override
    public void activate(Player player, Gamer gamer) {
        actives.put(player, 3);
        UtilMessage.message(player, getClassType(), "You prepared " + ChatColor.GREEN + getName(getLevel(player)) + ChatColor.GRAY + ".");
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_RAVAGER_ATTACK, 2.0f, 1.8f);
    }

    @EventHandler
    public void onDequip(SkillDequipEvent e){
        if(e.getSkill().equals(this)){
            actives.remove(e.getPlayer());
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        if(actives.containsKey(e.getEntity())){
            actives.remove(e.getEntity());
        }
    }

    @EventHandler
    public void onSwing(PlayerInteractEvent e) {
        if (e.getHand() != EquipmentSlot.HAND) return;
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            Player player = e.getPlayer();
            if (!Arrays.asList(getMaterials()).contains(player.getEquipment().getItemInMainHand().getType())) return;
            if (!actives.containsKey(player)) return;
            if (!hasSkill(player, this)) return;
            if (actives.get(player) <= 0) {
                actives.remove(player);
                return;
            }
            if(cooldowns.containsKey(player)){
                if(cooldowns.get(player) - System.currentTimeMillis() > 0){
                    return;
                }
            }

            cooldowns.put(player, System.currentTimeMillis() + 600);
            actives.put(player, actives.get(player) - 1);

            final Location startPos = player.getLocation().clone();
            final Vector vector = player.getLocation().clone().getDirection().normalize().multiply(1);
            vector.setY(0);
            final Location loc = player.getLocation().subtract(0, 1, 0).add(vector);

            final BukkitTask runnable = new BukkitRunnable() {

                @Override
                public void run() {
                    loc.add(vector);
                    if ((!UtilBlock.airFoliage(loc.getBlock()))
                            && UtilBlock.solid(loc.getBlock())) {

                        loc.add(0.0D, 1.0D, 0.0D);
                        if ((!UtilBlock.airFoliage(loc.getBlock()))
                                && UtilBlock.solid(loc.getBlock())) {

                            cancel();
                            return;
                        }

                    }
                    if ((loc.clone().add(0.0D, -1.0D, 0.0D).getBlock().getType() == Material.AIR)) {
                        loc.add(0.0D, -1.0D, 0.0D);
                    }

                    if(loc.distance(startPos) > 20){
                        cancel();
                    }

                    EvokerFangs fangs = (EvokerFangs) player.getWorld().spawnEntity(loc, EntityType.EVOKER_FANGS);
                    for(LivingEntity ent : UtilEntity.getAllInRadius(fangs.getLocation(), 1.5)){
                        CustomDamageEvent dmg = new CustomDamageEvent(ent, player, null, EntityDamageEvent.DamageCause.CUSTOM, 2 + (getLevel(player) / 1.5), false);
                        LogManager.addLog(ent, player, "Wreath");
                        EffectManager.addPotionEffect(ent, new PotionEffect(PotionEffectType.SLOW, 40, 1));
                        Bukkit.getPluginManager().callEvent(dmg);
                    }

                }

            }.runTaskTimer(getInstance(), 0, 1);

            new BukkitRunnable() {

                @Override
                public void run() {
                    runnable.cancel();

                }

            }.runTaskLater(getInstance(), 60);
        }
    }


    @Override
    public String[] getDescription(int level) {
        return new String[]{
                "Right click with a sword to prepare.",
                "",
                "Your next 3 attacks will release a barrage of teeth",
                "that deal " + ChatColor.GREEN + String.format("%.2f", (2 + (level /1.5))) + ChatColor.GRAY + " damage and slow their target.",
                "",
                "Recharge: " + ChatColor.GREEN + getRecharge(level)
        };
    }

    @Override
    public Types getType() {
        return Types.SWORD;
    }

    @Override
    public double getRecharge(int level) {
        return 26 - (level * 2);
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

        if (actives.containsKey(p)) {
            if (actives.get(p) > 0) {
                UtilMessage.message(p, getClassType(), ChatColor.GREEN + "Wreath" + ChatColor.GRAY + " is already active with "
                        + ChatColor.GREEN + actives.get(p) + ChatColor.GRAY + " stacks remaining");
                return false;
            }
        }
        return true;
    }
}
