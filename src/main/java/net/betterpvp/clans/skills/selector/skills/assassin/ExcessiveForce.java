package net.betterpvp.clans.skills.selector.skills.assassin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.classes.roles.Assassin;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilMessage;
import net.minecraft.server.v1_15_R1.EntityIllagerIllusioner;
import net.minecraft.server.v1_15_R1.EntityIllagerWizard;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Illusioner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;

import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class ExcessiveForce extends Skill implements InteractSkill {

    private WeakHashMap<Player, Long> active = new WeakHashMap<>();

    public ExcessiveForce(Clans i) {
        super(i, "Excessive Force", "Assassin", getSwords, rightClick, 3, true, true);
    }

    @Override
    public void activate(Player player, Gamer gamer) {
        active.put(player, System.currentTimeMillis() + (1500 + (getLevel(player) * 500)));
        UtilMessage.message(player, getClassType(), "You activated " + ChatColor.GREEN + getName(getLevel(player)) + ChatColor.GRAY + ".");
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, 1f, 1.7f);
    }

    @EventHandler
    public void setKnockback(CustomDamageEvent e){
        if(e.getDamager() instanceof Player){
            Player damager = (Player) e.getDamager();
            if(active.containsKey(damager)){
                e.setKnockback(true);
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onUpdate(UpdateEvent e){
        if(e.getType() == UpdateEvent.UpdateType.FASTEST){
            Iterator<Map.Entry<Player, Long>> it = active.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<Player, Long> next = it.next();
                if(next.getValue() - System.currentTimeMillis() <= 0){
                    it.remove();
                    continue;
                }

                Role role = Role.getRole(next.getKey());
                if(role == null){
                    it.remove();
                }else{
                    if(!(role instanceof Assassin)){
                        it.remove();
                    }
                }
            }
        }
    }

    @Override
    public String[] getDescription(int level) {
        return  new String[]{
                "Right click with a Sword to activate.",
                "",
                "For the next " + ChatColor.GREEN + (2 + ((level -1) * 0.5)) + ChatColor.GRAY + " seconds",
                "your attacks deal knockback to enemies",
                "",
                "Does not ignore anti-knockback abilities.",
                "",
                "Recharge: " + ChatColor.GREEN + getRecharge(level)};
    }

    @Override
    public Types getType() {
        return Types.SWORD;
    }

    @Override
    public double getRecharge(int level) {
        return 16 - (level * 2);
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
