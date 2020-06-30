package net.betterpvp.clans.skills.selector.skills.warlock;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.roles.Warlock;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.utilities.UtilClans;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Bloodthirst extends Skill {

    public Bloodthirst(Clans i) {
        super(i, "Bloodthirst", "Warlock", noMaterials, noActions, 5, false, false);
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                "Your senses are heightened, ",
                "allowing you to detect nearby enemies below " + ChatColor.GREEN + (25 + (5 * level)) + "% " + ChatColor.GRAY + "health.",
                "",
                "While running towards weak enemies, you receive Speed I."
        };
    }

    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.FAST) {
            for (Player warlock : Bukkit.getOnlinePlayers()) {
                Role role = Role.getRole(warlock);
                if (role != null && role instanceof Warlock) {
                    Gamer gamer = GamerManager.getOnlineGamer(warlock);
                    if (gamer != null) {
                        if (hasSkill(gamer, warlock, this)) {
                            int level = getLevel(warlock);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                if (ClanUtilities.canHurt(warlock, player)) {
                                    if (UtilPlayer.getHealthPercentage(player) < (25 + (level * 5))) {
                                        UtilClans.setGlowing(warlock, player, true);
                                        double distanceA = warlock.getLocation().distance(player.getLocation());
                                        double distanceB = warlock.getLocation().add(warlock.getLocation().getDirection()).distance(player.getLocation());
                                        if(distanceA - distanceB > 0.6){
                                            if (warlock.hasPotionEffect(PotionEffectType.SPEED)) {
                                                PotionEffect speed = player.getPotionEffect(PotionEffectType.SPEED);
                                                if(speed != null) {
                                                    if (speed.getAmplifier() < 2) {
                                                        warlock.removePotionEffect(PotionEffectType.SPEED);
                                                    }
                                                }
                                            }

                                            warlock.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30, 0));
                                            break;
                                        }
                                    }else{
                                        UtilClans.setGlowing(warlock, player, false);
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    @Override
    public Types getType() {
        return Types.PASSIVE_B;
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
        return false;
    }
}
