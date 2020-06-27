package net.betterpvp.clans.skills.selector.skills.assassin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilItem;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilVelocity;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Leap extends Skill implements InteractSkill {

    public Leap(Clans i) {
        super(i, "Leap", "Assassin",
                getAxes,
                rightClick, 5, true, true);
    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Right click with a axe to activate.",
                "",
                "You take a great leap",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level)
        };

    }

    public void DoLeap(Player player, boolean wallkick) {

        if (!wallkick) {
            UtilVelocity.velocity(player, 1.3D, 0.2D, 1.0D, true);
            UtilMessage.message(player, getClassType(), "You used " + ChatColor.GREEN + getName(getLevel(player)) + ChatColor.GRAY + ".");
        } else {
            Vector vec = player.getLocation().getDirection();
            vec.setY(0);
            UtilVelocity.velocity(player, vec, 0.9D, false, 0.0D, 0.8D, 2.0D, true);
            UtilMessage.message(player, getClassType(), "You used " + ChatColor.GREEN + "Wall Kick" + ChatColor.GRAY + ".");
        }

        player.getWorld().spawnEntity(player.getLocation(), EntityType.LLAMA_SPIT);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 2.0F, 1.2F);
    }


    public boolean wallKick(Player player) {


            if (RechargeManager.getInstance().add(player, "Wall Kick", 0.15, false)) {
                Vector vec = player.getLocation().getDirection();

                boolean xPos = true;
                boolean zPos = true;

                if (vec.getX() < 0.0D) {
                    xPos = false;
                }
                if (vec.getZ() < 0.0D) {
                    zPos = false;
                }

                for (int y = 0; y <= 0; y++) {
                    for (int x = -1; x <= 1; x++) {
                        for (int z = -1; z <= 1; z++) {
                            if ((x != 0) || (z != 0)) {
                                if (((!xPos) || (x <= 0))
                                        && ((!zPos) || (z <= 0))
                                        && ((xPos) || (x >= 0)) && ((zPos) || (z >= 0))) {
                                    if (!UtilBlock.airFoliage(player.getLocation().getBlock().getRelative(x, y, z))) {
                                        Block forward = null;

                                        if (Math.abs(vec.getX()) > Math.abs(vec.getZ())) {
                                            if (xPos) {
                                                forward = player.getLocation().getBlock().getRelative(1, 0, 0);
                                            } else {
                                                forward = player.getLocation().getBlock().getRelative(-1, 0, 0);
                                            }

                                        } else if (zPos) {
                                            forward = player.getLocation().getBlock().getRelative(0, 0, 1);
                                        } else {
                                            forward = player.getLocation().getBlock().getRelative(0, 0, -1);
                                        }

                                        if (UtilBlock.airFoliage(forward)) {
                                            if (Math.abs(vec.getX()) > Math.abs(vec.getZ())) {
                                                if (xPos) {
                                                    forward = player.getLocation().getBlock().getRelative(1, 1, 0);
                                                } else {
                                                    forward = player.getLocation().getBlock().getRelative(-1, 1, 0);
                                                }
                                            } else if (zPos) {
                                                forward = player.getLocation().getBlock().getRelative(0, 1, 1);
                                            } else {
                                                forward = player.getLocation().getBlock().getRelative(0, 1, -1);
                                            }

                                            if (UtilBlock.airFoliage(forward)) {
                                                DoLeap(player, true);
                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }


        /**
         *
         */

        return false;
    }

    @Override
    public boolean usageCheck(Player player) {

        if (player.getLocation().getBlock().getType() == Material.WATER) {
            UtilMessage.message(player, "Skill", "You cannot use " + ChatColor.GREEN + getName() + ChatColor.GRAY + " in water.");
            return false;
        }

        if (player.hasPotionEffect(PotionEffectType.SLOW)) {
            UtilMessage.message(player, "Skill", "You cannot use " + ChatColor.GREEN + "Leap" + ChatColor.GRAY + " while Slowed.");
            return false;
        }

        return !wallKick(player);

    }

    @Override
    public Types getType() {
        return Types.AXE;
    }

    @Override
    public double getRecharge(int level) {
        return 8 - ((level - 1) * 0.5);
    }

    @Override
    public float getEnergy(int level) {

        return 0;
    }

    @Override
    public void activate(Player player, Gamer gamer) {
        if (!wallKick(player)) {
            DoLeap(player, false);
        }
    }
}
