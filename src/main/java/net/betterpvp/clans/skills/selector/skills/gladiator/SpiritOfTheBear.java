package net.betterpvp.clans.skills.selector.skills.gladiator;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpiritOfTheBear extends Skill{


	public SpiritOfTheBear(Clans i) {
		super(i, "Spirit of the Bear", "Gladiator", getAxes, rightClick, 5, true, true);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getDescription(int level) {
		// TODO Auto-generated method stub
		return new String[]{
				"Right click with Axe to Activate.",
				"",
				"Call upon the spirit of the bear",
				"granting all allies within " + ChatColor.GREEN + (5 + (level)) + ChatColor.GRAY + " blocks",
				"Resistance II for 5 seconds.",
				"",
				"Cooldown: " + ChatColor.GREEN + getRecharge(level),
				"Energy: " + ChatColor.GREEN + getEnergy(level)
		};
	}

	@Override
	public Types getType() {
		// TODO Auto-generated method stub
		return Types.AXE;
	}

	@Override
	public double getRecharge(int level) {
		// TODO Auto-generated method stub
		return 20 - ((level -1) * 2);
	}

	@Override
	public float getEnergy(int level) {
		// TODO Auto-generated method stub
		return 50 - ((level -1) * 3);
	}

	@Override
	public void activateSkill(Player player) {
		UtilMessage.message(player, getClassType(), "You used " + ChatColor.GREEN + getName() + " " + getLevel(player));
		player.getWorld().playSound(player.getLocation().add(0.0, -1.0, 0.0), Sound.ENDERDRAGON_GROWL, 1.8F, 2.5F);
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1));
		EffectManager.addEffect(player, EffectType.RESISTANCE, 5000);
		for(Player p : UtilPlayer.getInRadius(player.getLocation(), (5 + getLevel(player)))){
			if(!ClanUtilities.canHurt(player, p)){
				p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1));
				EffectManager.addEffect(p, EffectType.RESISTANCE, 5000);
				UtilMessage.message(p, getClassType(), "You received the spirit of the bear!");
			}
		}

	}

	@Override
	public boolean usageCheck(Player player) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean requiresShield() {
		// TODO Auto-generated method stub
		return false;
	}



}
