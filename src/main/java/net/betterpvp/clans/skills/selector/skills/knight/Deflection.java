package net.betterpvp.clans.skills.selector.skills.knight;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.classes.roles.Knight;
import net.betterpvp.clans.client.Client;
import net.betterpvp.clans.client.ClientUtilities;
import net.betterpvp.clans.events.UpdateEvent;
import net.betterpvp.clans.events.UpdateEvent.UpdateType;
import net.betterpvp.clans.module.RechargeManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.utility.UtilMessage;
import net.betterpvp.clans.utility.UtilTime;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.WeakHashMap;

public class Deflection extends Skill{

	public Deflection(Clans i) {
		super(i, "Deflection", "Knight", noMaterials, noActions, 3, false, false);
		// TODO Auto-generated constructor stub
	}

	private WeakHashMap<Player, Integer> charges = new WeakHashMap<>();

	@Override
	public String[] getDescription(int level) {
		// TODO Auto-generated method stub
		return new String[]{
				"Prepare to deflect incoming attacks",
				"You gain 1 charge every 3 seconds.",
				"You can store a maximum of " + ChatColor.GREEN + (level) + ChatColor.GRAY + " charges",
				"",
				"When attacked, the damage you take is",
				"reduced by the number of your charges",
				"and your charges are reset to 0."
		};
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
	public void activateSkill(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean usageCheck(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onDamage(CustomDamageEvent e){
		if(e.getDamagee() instanceof Player){
			if(e.getCause() != DamageCause.ENTITY_ATTACK) return;
			Player p = (Player) e.getDamagee();
			if(hasSkill(p, this)){
				if(charges.containsKey(p)){

					e.setDamage(e.getDamage() - charges.get(p));
					charges.remove(p);
				}
			}
		}

	}


	@EventHandler
	public void addCharge(UpdateEvent event)
	{
		if (event.getType() == UpdateType.TICK) {

			for (Player cur : Bukkit.getOnlinePlayers()) {
				Role r = Role.getRole(cur);
				if(r != null){
					if(r instanceof Knight){
						Client c = ClientUtilities.getOnlineClient(cur);



						if(hasSkill(c, cur, this)){
							if(charges.containsKey(cur)){
								if(UtilTime.elapsed(c.getGamer().getLastDamaged(), 2000)){
									if (RechargeManager.getInstance().add(cur, getName(), 3, false)){
										if(charges.get(cur) < getLevel(cur)){
											int charge = 1;

											charge += charges.get(cur);

											charge = Math.min((getLevel(cur)), charge);
											UtilMessage.message(cur, getClassType(), "Deflection charge: " + ChatColor.YELLOW + charge);
											charges.put(cur, charge);
										}
									}
								}
							}else{
								charges.put(cur, 0);
							}
						}
					}
				}

			}
		}
	}

	@Override
	public boolean requiresShield() {
		// TODO Auto-generated method stub
		return false;
	}

}
