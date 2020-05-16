package net.betterpvp.clans.skills.selector.skills.warlock;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.classes.roles.Warlock;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.events.SkillDequipEvent;
import net.betterpvp.clans.skills.events.SkillEquipEvent;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Frailty extends Skill {

    private List<UUID> active;

    public Frailty(Clans i) {
        super(i, "Frailty", "Warlock", noMaterials, noActions, 3, false, false);
        active = new ArrayList<>();
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                "Nearby enemies that fall below " + ChatColor.GREEN + (40 + ((level - 1) * 10)) + "%" + ChatColor.GRAY + " health",
                "take " + ChatColor.GREEN + (20 + ((level - 1) * 5)) + "%" + ChatColor.GRAY + " more damage from only you."
        };
    }

    @EventHandler
    public void onEquip(SkillEquipEvent e) {
        if (e.getSkill().equals(this)) {
            if (!active.contains(e.getPlayer().getUniqueId())) {
                active.add(e.getPlayer().getUniqueId());
            }
        }
    }

    @EventHandler
    public void onDequip(SkillDequipEvent e) {
        if (e.getSkill().equals(this)) {
            if (active.contains(e.getPlayer().getUniqueId())) {
                active.remove(e.getPlayer().getUniqueId());
            }
        }
    }

    @EventHandler
    public void monitorActives(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.SEC) {
            active.removeIf(u -> {

                Player player = Bukkit.getPlayer(u);
                if (player != null) {
                    if (hasSkill(player, this)) {
                        return false;
                    }
                }

                return true;
            });

            Bukkit.getOnlinePlayers().forEach(p -> {
                Role role = Role.getRole(p);
                if (role != null && role instanceof Warlock) {
                    if (hasSkill(p, this)) {
                        if (!active.contains(p.getUniqueId())) {
                            active.add(p.getUniqueId());
                        }
                    }
                }
            });
        }
    }

    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.SEC) {
            if (active.isEmpty()) return;
            Bukkit.getOnlinePlayers().forEach(p -> {

                active.forEach(u -> {
                    Player player = Bukkit.getPlayer(u);
                    if (p.getWorld().equals(player.getWorld())) {
                        if (player != null) {
                            if (p.getLocation().distance(player.getLocation()) < 5) {
                                if (ClanUtilities.canHurt(p, player)) {
                                    if (hasSkill(player, this)) {
                                        if (p.getHealth() / p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 100 < (40 + ((getLevel(player) - 1) * 10))) {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 30, 0));
                                        }
                                    }
                                }
                            }
                        }
                    }
                });


            });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(CustomDamageEvent e) {
        if (e.getDamager() instanceof Player) {

            if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                Player damager = (Player) e.getDamager();
                if (hasSkill(damager, this)) {
                    if (e.getDamagee() instanceof Player) {
                        if (EffectManager.hasEffect((Player) e.getDamagee(), EffectType.IMMUNETOEFFECTS)) {
                            return;
                        }
                    }
                    if (e.getDamagee().getHealth() / e.getDamagee().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 100 < (40 + ((getLevel(damager) - 1) * 10))) {
                        int level = getLevel(damager);
                        e.setDamage(e.getDamage() * 1.20 + ((level - 1) * 0.05));
                    }
                }
            }
        }
    }

    @Override
    public Types getType() {
        return Types.PASSIVE_A;
    }

    @Override
    public double getRecharge(int level) {
        return 0;
    }

    @Override
    public float getEnergy(int level) {
        return 0;
    }

    @Override
    public boolean usageCheck(Player p) {
        return true;
    }

}
