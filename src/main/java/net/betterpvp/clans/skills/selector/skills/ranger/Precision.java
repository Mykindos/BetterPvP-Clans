package net.betterpvp.clans.skills.selector.skills.ranger;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class Precision extends Skill{

	public Precision(Clans i) {
		super(i, "Precision", "Ranger", noMaterials, noActions, 5,
				false, false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getDescription(int level) {
		// TODO Auto-generated method stub
		return new String[]{"Your arrows deal " + ChatColor.GREEN + (level * 0.5) + ChatColor.GRAY + " bonus damage on hit"};
	}

	@Override
	public Types getType() {
		// TODO Auto-generated method stub
		return Types.PASSIVE_A;
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
	public void onSlow(CustomDamageEvent e){
		if(e.getProjectile() != null){
			
				
				if(e.getDamager() instanceof Player){
					Player p = (Player) e.getDamager();
					Role r = Role.getRole(p);
					if(r != null && r.getName().equals(getClassType())) {

						if (hasSkill(p, this)) {
							e.setDamage(e.getDamage() + (getLevel(p) * 0.5));
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
