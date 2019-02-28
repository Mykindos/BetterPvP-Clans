package net.betterpvp.clans.skills.selector.skills.global;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.client.Client;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.client.ClientUtilities;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class BreakFall extends Skill {

    public BreakFall(Clans i) {
        super(i, "Break Fall", "Global", noMaterials, noActions, 3,
                false, false);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String[] getDescription(int level) {
        // TODO Auto-generated method stub
        return new String[]{
                "You roll when you hit the ground.",
                "",
                "Fall damage is reduced by " + ChatColor.GREEN + (5 + level)};
    }

    @Override
    public Types getType() {
        // TODO Auto-generated method stub
        return Types.GLOBAL;
    }

    @Override
    public void activateSkill(Player player) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean usageCheck(Player player) {
        // TODO Auto-generated method stub
        return false;
    }

    @EventHandler
    public void onFall(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getCause() == DamageCause.FALL) {
                Player p = (Player) e.getEntity();
                if (Role.getRole(p) != null) {
                    Role r = Role.getRole(p);
                    Client c = ClientUtilities.getOnlineClient(p);
                    if (c != null) {

                        if (c.getGamer().getActiveBuild(r.getName()) != null) {
                            if (c.getGamer().getActiveBuild(r.getName()).getGlobal() != null) {
                                if (c.getGamer().getActiveBuild(r.getName()).getGlobal().getSkill().equals(this)) {
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
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getEnergy(int level) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean requiresShield() {
        // TODO Auto-generated method stub
        return false;
    }

}
