package net.betterpvp.clans.skills.selector.skills.assassin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilItem;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilVelocity;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Leap extends Skill {

	public Leap(Clans i) {
		super(i, "Leap", "Assassin",
				getAxes,
				rightClick, 5, true, true);
	}

	@Override
	public String[] getDescription(int level) {
		// TODO Auto-generated method stub
		return new String[]{
				"Right click with Axe to Activate.",
				"",
				"You take a great leap",
				"Cooldown: " + ChatColor.GREEN + getRecharge(level),
				"Energy: " + ChatColor.GREEN + getEnergy(level)
		};

	}

	public void DoLeap(Player player, boolean wallkick) {
		if(ClanUtilities.canCast(player)){
			if (!wallkick) {
				UtilVelocity.velocity(player, 1.3D, 0.2D, 1.0D, true);
				UtilMessage.message(player, getClassType(), "You used " + ChatColor.GREEN + getName(getLevel(player)) +  ChatColor.GRAY + ".");
			} else {
				Vector vec = player.getLocation().getDirection();
				vec.setY(0);
				UtilVelocity.velocity(player, vec, 0.9D, false, 0.0D, 0.8D, 2.0D, true);
				UtilMessage.message(player, getClassType(), "You used " + ChatColor.GREEN + "Wall Kick" + ChatColor.GRAY + ".");
			}



			player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, 80);
			player.getWorld().playSound(player.getLocation(), Sound.BAT_TAKEOFF, 2.0F, 1.2F);
		}
	}



	public boolean wallKick(Player player) {


		if (UtilItem.isAxe(player.getItemInHand().getType())) {
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
		}
		
		/**
		 * 
		 */

		return false;
	}

	@Override
	public void activateSkill(Player player) {
		if (usageCheck(player)) {



			if (!wallKick(player)) {
				DoLeap(player, false);
			}
		}

	}

	@Override
	public boolean usageCheck(Player player) {

		if(!hasSkill(player, this)){
			return false;
		}

		if (player.getLocation().getBlock().getType() == Material.WATER
				|| player.getLocation().getBlock().getType() == Material.STATIONARY_WATER) {
			UtilMessage.message(player, "Skill", "You cannot use " + ChatColor.GREEN + getName() + " in water.");
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
		// TODO Auto-generated method stub
		return Types.AXE;
	}

	@Override
	public double getRecharge(int level) {
		return 8 - ((level -1) * 0.5);
	}

	@Override
	public float getEnergy(int level) {
		// TODO Auto-generated method stub
		return 20 - (level -1);
	}

	@Override
	public boolean requiresShield() {
		// TODO Auto-generated method stub
		return false;
	}
}
