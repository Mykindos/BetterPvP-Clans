package net.betterpvp.clans.skills.selector.skills.gladiator;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.events.UpdateEvent;
import net.betterpvp.clans.events.UpdateEvent.UpdateType;
import net.betterpvp.clans.format.C;
import net.betterpvp.clans.gamer.combat.LogManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.utility.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.WeakHashMap;

public class Takedown extends Skill{

	private WeakHashMap<Player, Long> active = new WeakHashMap<>();

	public Takedown(Clans i) {
		super(i, "Takedown", "Gladiator", getSwords, rightClick, 5, true, true);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getDescription(int level) {
		// TODO Auto-generated method stub
		return new String[]{
				"Right click with Sword to Activate.",
				"",
				"Hurl yourself towards an opponent.", 
				"If you collide with them, you " + ChatColor.WHITE + "both", 
				"take damage and receive Slow 4", 
				"for " + ChatColor.GREEN + (1 + level)  + ChatColor.GRAY + " seconds.",
				"",
				"Cannot be used while grounded.",
				"",
				"Cooldown: " + ChatColor.GREEN + getRecharge(level),
				"Energy: " + ChatColor.GREEN + getEnergy(level)
		};
	}

	@Override
	public Types getType() {
		// TODO Auto-generated method stub
		return Types.SWORD;
	}

	@Override
	public double getRecharge(int level) {
		// TODO Auto-generated method stub
		return 25 - ((level -1) * 2);
	}

	@Override
	public float getEnergy(int level) {
		// TODO Auto-generated method stub
		return 40 - ((level -1) * 3);
	}

	@Override
	public void activateSkill(Player p) {
		Vector vec = p.getLocation().getDirection();

		UtilVelocity.velocity(p, vec, 1.8D, false, 0.0D, 0.4D, 0.6D, false);

		active.put(p, System.currentTimeMillis());
		UtilMessage.message(p, getClassType(), "You used " + C.cGreen + getName() + " " + getLevel(p));

	}

	@EventHandler
	public void end(UpdateEvent event)
	{
		if(event.getType() == UpdateType.TICK){

			Iterator<Entry<Player, Long>> it = active.entrySet().iterator();
			while(it.hasNext()){

				Entry<Player, Long> next = it.next();
				Player p = next.getKey();


				for (Player other : Bukkit.getOnlinePlayers()) {
					if(other.getName().equalsIgnoreCase(p.getName())) continue;
					if(UtilMath.offset(p, other) < 2.0){

						doTakeDown(next.getKey(), other);
						it.remove();
						return;


					}
				}


				if (UtilBlock.isGrounded(p)) {

					if (UtilTime.elapsed(next.getValue(), 750L)) {
						it.remove();
						continue;
					}
				}
			}

		}

	}

	public void doTakeDown(Player p, Player d){

		if(ClanUtilities.canHurt(p, d)){

			LogManager.addLog(p, d, "Takedown Recoil");
			LogManager.addLog(d, p, "Takedown");
			UtilMessage.message(p, getClassType(), "You hit " + ChatColor.GREEN + d.getName() + ChatColor.GRAY + " with " + ChatColor.GREEN + getName());

			Bukkit.getPluginManager().callEvent(new CustomDamageEvent(d, p, null, DamageCause.CUSTOM, 10, false));



			UtilMessage.message(d, getClassType(), ChatColor.GREEN + p.getName() + ChatColor.GRAY + " hit you with " + ChatColor.GREEN + getName(getLevel(p)));
			Bukkit.getPluginManager().callEvent(new CustomDamageEvent(p, d, null, DamageCause.CUSTOM, 10, false));

			PotionEffect pot = new PotionEffect(PotionEffectType.SLOW, (int) (1 + (getLevel(p) * 0.5)) * 20, 2);
			p.addPotionEffect(pot);
			d.addPotionEffect(pot);
		}
	}

	@Override
	public boolean usageCheck(Player p) {
		if(p.getLocation().getBlock().isLiquid()){
			UtilMessage.message(p, getClassType(), "You cannot use " + C.cGreen + getName() + C.cGray + " in water.");
			return false;
		}

		if(UtilBlock.isGrounded(p)){
			UtilMessage.message(p, getClassType(), "You cannot use " + C.cGreen + getName() + C.cGray + " while grounded.");
			return false;
		}
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean requiresShield() {
		// TODO Auto-generated method stub
		return false;
	}

}
