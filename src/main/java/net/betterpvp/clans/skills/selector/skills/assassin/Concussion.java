package net.betterpvp.clans.skills.selector.skills.assassin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.events.SkillDequipEvent;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Concussion extends Skill {

    private List<UUID> active = new ArrayList<>();

    public Concussion(Clans i) {
        super(i, "Concussion", "Assassin", getSwords, rightClick, 3, true, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Right click with Sword to Activate.",
                "",
                "Your next hit blinds the target for " + ChatColor.GREEN + (level) + ChatColor.GRAY + " seconds.",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level),
                "Energy: " + ChatColor.GREEN + getEnergy(level)
        };
    }

    @EventHandler
    public void onDequip(SkillDequipEvent e) {
        if (e.getSkill() == this) {
            if (active.contains(e.getPlayer().getUniqueId())) {
                active.remove(e.getPlayer().getUniqueId());
            }
        }
    }

    @Override
    public Types getType() {

        return Types.SWORD;
    }

    @Override
    public double getRecharge(int level) {

        return 16 - ((level - 1) * 3);
    }

    @Override
    public float getEnergy(int level) {

        return 30 - ((level - 1) * 5);
    }

    @Override
    public void activateSkill(Player p) {
        UtilMessage.message(p, getClassType(), "You prepared " + ChatColor.GREEN + getName(getLevel(p)));
        active.add(p.getUniqueId());

    }


    @EventHandler
    public void onDamage(CustomDamageEvent e) {
        if (e.getCause() != DamageCause.ENTITY_ATTACK) return;
        if (e.getDamager() instanceof Player) {
            if (e.getDamagee() instanceof Player) {
                Player p = (Player) e.getDamager();
                Player ent = (Player) e.getDamagee();
                if (hasSkill(p, this)) {
                    if (ClanUtilities.canHurt(p, ent)) {
                        if (active.contains(p.getUniqueId())) {
                            LogManager.addLog(ent, p, "Concussion");
                            ent.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, (int) ((getLevel(p)) * 20) + 20, 0));
                            UtilMessage.message(p, getName(), "You gave " + ChatColor.GREEN + ent.getName() + ChatColor.GRAY + " a concussion.");
                            UtilMessage.message(ent, getName(), ChatColor.GREEN + p.getName() + ChatColor.GRAY + " gave you a concussion.");
                            active.remove(p.getUniqueId());
                        }
                    }
                }
            }
        }

    }

    @EventHandler
    public void onSprint(PlayerToggleSprintEvent e) {
        if (!e.isSprinting()) {
            if (e.getPlayer().hasPotionEffect(PotionEffectType.BLINDNESS)) {
                e.setCancelled(true);
            }
        }
    }

    @Override
    public boolean usageCheck(Player p) {
        if (active.contains(p.getUniqueId())) {
            UtilMessage.message(p, getClassType(), ChatColor.GREEN + getName() + ChatColor.GRAY + " is already active.");
            return false;
        }

        return true;
    }

    @Override
    public boolean requiresShield() {

        return false;
    }

}
