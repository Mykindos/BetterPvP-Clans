package net.betterpvp.clans.skills.selector.skills.global;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.dailies.perks.QuestPerkManager;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.BuildSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import net.betterpvp.core.utility.UtilVelocity;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.Iterator;
import java.util.WeakHashMap;

public class Swim extends Skill {

    WeakHashMap<Player, Long> swim = new WeakHashMap<>();

    public Swim(Clans i) {
        super(i, "Swim", "Global", noMaterials, noActions, 5,
                false, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Tap Crouch to Swim forwards.",
                "",
                "Energy: " + ChatColor.GREEN + getEnergy(level)};
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
    public void onSwim(PlayerToggleSneakEvent e) {
        if (Clans.getOptions().isFNG()) {
            return;
        }
        Player p = e.getPlayer();
        
        if (p.getLocation().getBlock().getType() != Material.WATER) {
            return;
        }

        if (EffectManager.hasEffect(p, EffectType.SILENCE)) {
            UtilMessage.message(p, getName(), "You cannot use Swim while silenced!");
            return;
        }

        if (Role.getRole(p) != null) {
            Role r = Role.getRole(p);

            Gamer g = GamerManager.getOnlineGamer(p);
            if (g != null) {
                BuildSkill globalSkill = g.getActiveBuild(r.getName()).getGlobal();
                if (globalSkill != null) {
                    if (globalSkill.getSkill().equals(this)) {
                        if (!swim.containsKey(p)) {
                            if (Energy.use(p, getName(), getEnergy(getLevel(p)), true)) {
                                UtilVelocity.velocity(p, 0.6D, 0.2D, 0.6D, false);
                                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_SPLASH, 0.3F, 2.0F);
                                swim.put(p, System.currentTimeMillis());
                                return;
                            }
                        }
                    }
                }
            }
        }
        if (QuestPerkManager.hasPerk(p, "Swim")) {
            if (!swim.containsKey(p)) {
                if (Energy.use(p, getName(), getEnergy(3), true)) {
                    UtilVelocity.velocity(p, 0.6D, 0.2D, 0.6D, false);
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_SPLASH, 0.3F, 2.0F);
                    swim.put(p, System.currentTimeMillis());
                }
            }
            return;
        }
    }

    @EventHandler
    public void swimRemove(UpdateEvent e) {
        if (e.getType() == UpdateType.FAST) {
            Iterator<Long> iterator = swim.values().iterator();
            while (iterator.hasNext()) {
                Long next = iterator.next();
                if (UtilTime.elapsed(next, 350)) {
                    iterator.remove();
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

        return 10 - ((level - 1) * 2);
    }

}


