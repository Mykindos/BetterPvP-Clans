package net.betterpvp.clans.skills.selector.skills.global;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class BreakFall extends Skill {

    public BreakFall(Clans i) {
        super(i, "Break Fall", "Global", noMaterials, noActions, 3,
                false, false);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "You roll when you hit the ground.",
                "",
                "Fall damage is reduced by " + ChatColor.GREEN + (5 + level)};
    }

    @Override
    public Types getType() {

        return Types.GLOBAL;
    }


    @Override
    public boolean usageCheck(Player player) {

        return false;
    }

    @EventHandler
    public void onFall(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getCause() == DamageCause.FALL) {
                Player p = (Player) e.getEntity();
                Role r = Role.getRole(p);
                if (r != null) {

                    Gamer g = GamerManager.getOnlineGamer(p);

                    if (g != null) {

                        if (g.getActiveBuild(r.getName()) != null) {
                            if (g.getActiveBuild(r.getName()).getGlobal() != null) {
                                if (g.getActiveBuild(r.getName()).getGlobal().getSkill().equals(this)) {
                                    e.setDamage(e.getDamage() - (3 + getLevel(p)));
                                }
                            }

                        }
                    }
                }
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
