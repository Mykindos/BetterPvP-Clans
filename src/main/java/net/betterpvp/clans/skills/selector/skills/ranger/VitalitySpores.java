package net.betterpvp.clans.skills.selector.skills.ranger;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilTime;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VitalitySpores extends Skill {

    public VitalitySpores(Clans i) {
        super(i, "Vitality Spores", "Ranger", noMaterials, noActions, 5, false, false);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String[] getDescription(int level) {
        // TODO Auto-generated method stub
        return new String[]{
                "After " + ChatColor.GREEN + (7 - level) + ChatColor.GRAY + " seconds of not taking damage,",
                "forest spores surround you, giving",
                "you Regeneration 1 for 6 seconds.",
                "",
                "This remains until you take damage."};
    }

    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateType.FAST) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (Role.getRole(p) != null && Role.getRole(p).getName().equals(getClassType())) {
                    if (hasSkill(p, this)) {
                        if (UtilTime.elapsed(ClientUtilities.getOnlineClient(p).getGamer().getLastDamaged(), (7 - getLevel(p)) * 1000)) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 120, 0));
                        }
                    }
                }
            }
        }
    }

    @Override
    public Types getType() {
        // TODO Auto-generated method stub
        return Types.PASSIVE_B;
    }

    @Override
    public double getRecharge(int level) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getEnergy(int level) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void activateSkill(Player p) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean usageCheck(Player p) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean requiresShield() {
        // TODO Auto-generated method stub
        return false;
    }

}
