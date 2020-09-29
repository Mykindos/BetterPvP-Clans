package net.betterpvp.clans.effects;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.effects.events.EffectReceiveEvent;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.particles.ParticleEffect;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class EffectManager extends BPVPListener<Clans> {

    private static List<Effect> effects = new ArrayList<>();

    public EffectManager(Clans i) {
        super(i);
    }

    public static List<Effect> getEffects() {
        return effects;
    }

    public static void addEffect(Player p, EffectType type, long length) {
        addEffect(p, type, 1, length);
    }

    public static void addEffect(Player p, EffectType type, int level, long length) {
        Bukkit.getPluginManager().callEvent(new EffectReceiveEvent(p,
                new Effect(p.getUniqueId(), type, level, length)));

    }

    public static Effect getEffect(Player p, EffectType type) {
        return effects.stream().filter(e -> e.getPlayer().equals(p.getUniqueId())
                && e.getType() == type).findAny().orElse(null);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onReceiveEffect(EffectReceiveEvent e) {
        if (e.isCancelled()) {
            return;
        }

        Effect effect = e.getEffect();
        Player p = e.getPlayer();

        if (hasEffect(p, effect.getType())) {
            removeEffect(p, effect.getType());
        }
        if (effect.getType() == EffectType.STRENGTH) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (int) ((effect.getRawLength() / 1000) * 20), effect.getLevel() - 1));
        }
        if (effect.getType() == EffectType.SILENCE) {
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1F, 1.5F);
        }

        if (effect.getType() == EffectType.VULNERABILITY) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, (int) ((effect.getRawLength() / 1000) * 20), 0));
        }

        if(effect.getType() == EffectType.LEVITATION){
            p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, (int) ((effect.getRawLength() / 1000) * 20), effect.getLevel()));
        }
        getEffects().add(effect);
    }

    public static void addPotionEffect(LivingEntity ent, PotionEffect effect){


        if(ent instanceof Player) {
            if (hasEffect((Player) ent, EffectType.IMMUNETOEFFECTS)) {
                if (effect.getType() == PotionEffectType.SLOW || effect.getType() == PotionEffectType.BLINDNESS
                        || effect.getType() == PotionEffectType.WITHER || effect.getType() == PotionEffectType.CONFUSION
                        || effect.getType() == PotionEffectType.WEAKNESS || effect.getType() == PotionEffectType.POISON
                        || effect.getType() == PotionEffectType.LEVITATION) {
                    return;
                }
            }
        }

        ent.addPotionEffect(effect);
    }


    public static boolean hasEffect(Player p, EffectType type) {
        return getEffect(p, type) != null;
    }

    public static void removeEffect(Player p, EffectType type) {
        effects.removeIf(e -> e.getPlayer().equals(p.getUniqueId()) && e.getType() == type);

    }


    public static double getShardBoost(Player p) {
        double d = 1.0;
        for (Effect e : effects) {
            if (e.getPlayer().equals(p.getUniqueId())) {
                if (e.getType() == EffectType.SHARD_50) {
                    d = 1.5;
                }

                if (e.getType() == EffectType.SHARD_100) {
                    d = 2.0;
                }
            }

        }

        return d;
    }


    public static boolean hasShardBoost(Effect type) {
        return type.getType() == EffectType.SHARD_50 || type.getType() == EffectType.SHARD_100;
    }

    public static boolean hasProtection(Player p) {
        return hasEffect(p, EffectType.PROTECTION);
    }

    public static void clearEffect(Player p) {
        ListIterator<Effect> iterator = effects.listIterator();
        while (iterator.hasNext()) {
            Effect next = iterator.next();
            if (next.getPlayer() == p.getUniqueId() && !hasShardBoost(next)) {
                iterator.remove();
            }
        }
    }


    @EventHandler
    public void onFall(EntityDamageEvent e) {
        if (e.getCause() == DamageCause.FALL) {
            if (e.getEntity() instanceof Player) {
                if (hasEffect((Player) e.getEntity(), EffectType.NOFALL)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        clearEffect(e.getEntity());
    }

    @EventHandler
    public void showTag(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.SEC) {
            for (final Player p : Bukkit.getOnlinePlayers()) {

                Gamer g = GamerManager.getOnlineGamer(p);
                if (g != null) {

                    if (!UtilTime.elapsed(g.getLastDamaged(), 15000)) {
                        if (EffectManager.hasEffect(p, EffectType.INVISIBILITY)) continue;

                        ParticleEffect.VILLAGER_HAPPY.display(p.getLocation().add(0, 4, 0));
                    }
                }
            }
        }


    }

    @EventHandler
    public void shockUpdate(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.TICK) {
            for (Effect ef : getEffects()) {
                if (ef.getType() == EffectType.SHOCK) {
                    if (Bukkit.getPlayer(ef.getPlayer()) != null) {
                        Player p = Bukkit.getPlayer(ef.getPlayer());
                        p.playEffect(EntityEffect.HURT);
                    }
                }
            }
        }
    }


    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.TICK) {
            ListIterator<Effect> iterator = effects.listIterator();
            while (iterator.hasNext()) {
                Effect next = iterator.next();
                if (next.hasExpired()) {
                    if (next.getType() == EffectType.VULNERABILITY) {
                        if (Bukkit.getPlayer(next.getPlayer()) != null) {
                            UtilMessage.message(Bukkit.getPlayer(next.getPlayer()), "Condition", "Your vulnerability has worn off!");
                        }
                    }
                    iterator.remove();
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()
                || e.getFrom().getY() != e.getTo().getY()) {
            if (hasEffect(e.getPlayer(), EffectType.STUN)) {
                e.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void entityDamageEvent(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (hasProtection(p)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void entDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player dam = (Player) e.getDamager();
            Player p = (Player) e.getEntity();
            if (hasEffect(p, EffectType.PROTECTION)) {
                UtilMessage.message(dam, "Protected", "This is a new player and has protection!");
                e.setCancelled(true);
            }

            if (hasEffect(dam, EffectType.PROTECTION)) {
                UtilMessage.message(dam, "Protected", "You cannot damage other players while you have protection!");
                UtilMessage.message(dam, "Protected", "Type '/protection' to disable this permanently.");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onStrengthDamage(CustomDamageEvent e) {
        if (e.getCause() != DamageCause.ENTITY_ATTACK) return;
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            Effect effect = getEffect(p, EffectType.STRENGTH);
            if (effect != null) {
                e.setDamage(e.getDamage() + (1.5 * effect.getLevel()));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(CustomDamageEvent e) {
        if (e.getDamagee() instanceof Player) {
            Player p = (Player) e.getDamagee();
            Effect effect = getEffect(p, EffectType.VULNERABILITY);
            if (effect != null) {
                if (e.getCause() == DamageCause.LIGHTNING) return;
                e.setDamage((e.getDamage() * (1.0 + (effect.getLevel() * 0.25))));

            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLoadingTextureDamage(CustomDamageEvent e) {
        if (e.getDamagee() instanceof Player) {
            Player player = (Player) e.getDamagee();
            if (hasEffect(player, EffectType.TEXTURELOADING)) {
                e.setCancelled("Player is loading the server texture pack");
            }
        }

        if (e.getDamager() instanceof Player) {
            Player player = (Player) e.getDamager();
            if (hasEffect(player, EffectType.TEXTURELOADING)) {
                removeEffect(player, EffectType.TEXTURELOADING);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void resistanceReduction(CustomDamageEvent e) {
        if (e.getDamagee() instanceof Player) {

            Player p = (Player) e.getDamagee();
            Effect effect = getEffect(p, EffectType.RESISTANCE);
            if (effect != null) {
                e.setDamage(e.getDamage() * (1.0 - (effect.getLevel() * 20) * 0.01));

            }
        }
    }

    @EventHandler
    public void onImmuneToNegativity(EffectReceiveEvent e) {
        if (hasEffect(e.getPlayer(), EffectType.IMMUNETOEFFECTS)) {
            EffectType type = e.getEffect().getType();

            if (type == EffectType.SILENCE || type == EffectType.SHOCK || type == EffectType.VULNERABILITY
                    || type == EffectType.STUN || type == EffectType.FRAILTY || type == EffectType.IMMUNETOEFFECTS){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onReceiveImmuneToEffect(EffectReceiveEvent e){
        if(e.getEffect().getType() == EffectType.IMMUNETOEFFECTS){
            for (PotionEffect pot : e.getPlayer().getActivePotionEffects()) {
                if (pot.getType().getName().contains("SLOW")
                        || pot.getType().getName().contains("CONFUSION")
                        || pot.getType().getName().contains("POISON")
                        || pot.getType().getName().contains("BLINDNESS")
                        || pot.getType().getName().contains("WITHER")
                        || pot.getType().getName().contains("LEVITATION")) {
                    e.getPlayer().removePotionEffect(pot.getType());
                }
            }

            EffectManager.removeEffect(e.getPlayer(), EffectType.SHOCK);
            EffectManager.removeEffect(e.getPlayer(), EffectType.SILENCE);
            EffectManager.removeEffect(e.getPlayer(), EffectType.STUN);
            EffectManager.removeEffect(e.getPlayer(), EffectType.VULNERABILITY);
            EffectManager.removeEffect(e.getPlayer(), EffectType.FRAILTY);
            EffectManager.removeEffect(e.getPlayer(), EffectType.LEVITATION);
        }
    }


}
