package net.betterpvp.clans.skills.selector.skills.gladiator;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomKnockbackEvent;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class Colossus extends Skill{



	public Colossus(Clans i) {
		super(i, "Colossus", "Gladiator", noMaterials, noActions, 3,
				false, false);

	}

	@Override
	public String[] getDescription(int level) {
		// TODO Auto-generated method stub
		return new String[] {
				"You take " + ChatColor.GREEN + (25 * level) + "% " + ChatColor.GRAY + "reduced knockback."  };
	}

	@Override
	public Types getType() {
		// TODO Auto-generated method stub
		return Types.PASSIVE_A;
	}

	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onKB(CustomKnockbackEvent e) {
		if(e.getDamagee() instanceof Player){
			if(e.d.getCause() == DamageCause.ENTITY_ATTACK || e.d.getCause() == DamageCause.PROJECTILE){
				final Player p = (Player) e.getDamagee();
				if(Role.getRole(p) != null && Role.getRole(p).getName().equals(getClassType())){
					if(hasSkill(p, this)){
						e.setDamage(e.getDamage() * (1 - (0.25 * getLevel(p))));
					}
				}
			}
		}

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
