package net.betterpvp.clans.skills.selector.skills.gladiator;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class Overwhelm extends Skill{

	public Overwhelm(Clans i) {
		super(i, "Overwhelm", "Gladiator", noMaterials, noActions, 3, false, false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getDescription(int level) {
		// TODO Auto-generated method stub
		return new String[]{
				"You deal 1 bonus damage for every",
				"2 more health you have than your",
				"target. You can deal a maximum of",
				ChatColor.GREEN.toString() + String.format( "%.1f", ( 0.0 + (level * 0.5)) ) + ChatColor.GRAY + " bonus damage."
		};
	}

	@Override
	public Types getType() {
		// TODO Auto-generated method stub
		return Types.PASSIVE_B;
	}

	@EventHandler (priority = EventPriority.HIGH)
	public void onDamage(CustomDamageEvent e){
		if(e.getCause() != DamageCause.ENTITY_ATTACK) return;
		if(e.getDamager() instanceof Player){
			Player p = (Player) e.getDamager();
			if(Role.getRole(p) != null && Role.getRole(p).getName().equals(getClassType())){
				if(hasSkill(p, this)){
					LivingEntity ent = e.getDamagee();
					if(e.getCause() == DamageCause.ENTITY_ATTACK){
						double difference = (p.getHealth() - ent.getHealth()) /2;
						if(difference > 0){
							difference = Math.min(difference, (getLevel(p) * 0.5));
							e.setDamage(e.getDamage() + difference);
						}
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
	public void activateSkill(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean usageCheck(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean requiresShield() {
		// TODO Auto-generated method stub
		return false;
	}

}
