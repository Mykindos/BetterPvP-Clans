package net.betterpvp.clans.skills.selector.skills.assassin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilTime;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.HashSet;
import java.util.WeakHashMap;

public class RepeatedStrikes extends Skill {

    private WeakHashMap<Player, Integer> repeat = new WeakHashMap<>();
    private WeakHashMap<Player, Long> last = new WeakHashMap<>();

    public RepeatedStrikes(Clans i) {
        super(i, "Repeated Strikes", "Assassin", noMaterials, noActions, 3,
                false, false);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Each time you attack, your damage",
                "increases by 1",
                "You can get up to " + ChatColor.GREEN + level + ChatColor.GRAY + " bonus damage.",
                "",
                "Not attacking for 2 seconds clears",
                "your bonus damage."};
    }

    @Override
    public Types getType() {

        return Types.PASSIVE_A;
    }


    @Override
    public boolean usageCheck(Player player) {

        return false;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(CustomDamageEvent e) {
        if (e.getCause() != DamageCause.ENTITY_ATTACK) return;
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            Role r = Role.getRole(p);
            if (r != null && r.getName().equals(getClassType())) {
                if (hasSkill(p, this)) {
                    if (!repeat.containsKey(p)) {
                        repeat.put(p, 0);
                    }
                    e.setDamage(e.getDamage() + repeat.get(p));
                    e.setDamage(e.getDamage() * 0.90);
                    int level = getLevel(p);
                    repeat.put(p, Math.min(level, repeat.get(p) + 1));
                    last.put(p, System.currentTimeMillis());
                    e.setReason(getName());

                }
            }
        }

    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (event.getType() == UpdateType.FAST) {

            HashSet<Player> remove = new HashSet<>();

            for (Player p : repeat.keySet()) {
                if (UtilTime.elapsed(last.get(p), 2000)) {
                    remove.add(p);
                }
            }

            for (Player p : remove) {
                repeat.remove(p);
                last.remove(p);
            }
        }
    }

    @Override
    public double getRecharge(int level) {

        return 0;
    }

    @Override
    public float getEnergy(int level) {

        return 0;
    }

}
