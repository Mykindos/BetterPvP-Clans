package net.betterpvp.clans.skills.selector.skills.knight;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.Arrays;
import java.util.WeakHashMap;


public class PowerChop extends Skill {

    private WeakHashMap<Player, Long> charge = new WeakHashMap<>();

    public PowerChop(Clans i) {
        super(i, "Power Chop", "Knight", getAxes, rightClick, 5, true, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Put more strength into your",
                "next axe attack, causing it",
                "to deal " + ChatColor.GREEN + (Math.min(1, (level))) + ChatColor.GRAY + " bonus damage.",
                "",
                "Attack must be made within",
                "0.5 seconds of being used.",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level),
                "Energy: " + ChatColor.GREEN + getEnergy(level)
        };
    }

    @Override
    public Types getType() {

        return Types.AXE;
    }

    @Override
    public double getRecharge(int level) {

        return 12 - ((level - 1) * 1.5);
    }

    @Override
    public float getEnergy(int level) {

        return 25 - ((level - 1) * 2);
    }

    @Override
    public void activateSkill(Player player) {
        charge.put(player, System.currentTimeMillis());
        UtilMessage.message(player, getClassType(), "You prepared " + ChatColor.GREEN + getName() + " " + getLevel(player));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(CustomDamageEvent e) {
        if (e.getCause() != DamageCause.ENTITY_ATTACK) return;
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            if (hasSkill(p, this)) {
                if (charge.containsKey(p)) {
                    if (Arrays.asList(getMaterials()).contains(p.getInventory().getItemInMainHand().getType())) {
                        if (!UtilTime.elapsed(charge.get(p), 1000)) {
                            if (e.getDamagee() instanceof Player) {
                                if (!ClanUtilities.canHurt(p, (Player) e.getDamagee())) {

                                    return;
                                }
                            }
                            LogManager.addLog(e.getDamagee(), p, "Power Chop");
                            e.setDamage(e.getDamage() + ((Math.min(1, getLevel(p)) * 0.75)));
                            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_IRON_GOLEM_HURT, 1.0F, 1.0F);
                            charge.remove(p);
                        }
                    }
                }
            }

        }
    }

    @Override
    public boolean usageCheck(Player player) {
        if (player.getLocation().getBlock().isLiquid()) {
            UtilMessage.message(player, getClassType(), "You cannot use " + getName() + " in water.");
            return false;
        }
        return true;
    }

}
