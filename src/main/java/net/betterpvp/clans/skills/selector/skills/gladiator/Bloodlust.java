package net.betterpvp.clans.skills.selector.skills.gladiator;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.WeakHashMap;

public class Bloodlust extends Skill {

    private WeakHashMap<Player, Long> time = new WeakHashMap<>();
    private WeakHashMap<Player, Integer> str = new WeakHashMap<>();

    public Bloodlust(Clans i) {
        super(i, "Bloodlust", "Gladiator", noMaterials, noActions, 5,
                false, false);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "When an enemy dies within 15 blocks,",
                "you go into a Bloodlust, receiving",
                "Speed 1 and Strength 1 for " + ChatColor.GREEN + (5 + level) + ChatColor.GRAY + " seconds.",
                "",
                "Bloodlust can stack up to 3 times,",
                "boosting the level of Speed and Strength."};
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        for (Player p : UtilPlayer.getInRadius(e.getEntity().getLocation(), 15)) {
            if (ClanUtilities.canHurt(e.getEntity(), p)) {

                if (hasSkill(p, this)) {
                    int tempStr = 0;
                    if (str.containsKey(p)) {
                        tempStr = str.get(p) + 1;
                    }
                    tempStr = Math.min(tempStr, 3);
                    str.put(p, tempStr);
                    long dur = 8000;
                    time.put(p, System.currentTimeMillis() + dur);
                    if (p.hasPotionEffect(PotionEffectType.SPEED)) {
                        p.removePotionEffect(PotionEffectType.SPEED);
                    }
                    EffectManager.addEffect(p, EffectType.STRENGTH, tempStr, (5 + getLevel(p)) * 1000);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (5 + getLevel(p)) * 20, tempStr));
                    UtilMessage.message(p, getClassType(), "You entered bloodlust at Level: " + ChatColor.YELLOW + tempStr + ChatColor.GRAY + ".");
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 2.0F, 0.6F);

                }
            }

        }
    }

    @EventHandler
    public void Update(UpdateEvent e) {
        if (e.getType() != UpdateType.FAST) {
            return;
        }
        for (Player cur : Bukkit.getOnlinePlayers()) {
            Expire(cur);
        }
    }

    public boolean Expire(Player p) {
        if (!time.containsKey(p)) {
            return false;
        }
        if (System.currentTimeMillis() > time.get(p)) {
            int tempStr = str.get(p);
            str.remove(p);
            UtilMessage.message(p, getClassType(), "Your bloodlust has ended at Level: " + ChatColor.YELLOW + tempStr + ChatColor.GRAY + ".");
            this.time.remove(p);

            return true;
        }
        return false;
    }

    @Override
    public Types getType() {

        return Types.PASSIVE_B;
    }

    @Override
    public boolean usageCheck(Player p) {

        return false;
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
