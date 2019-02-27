package net.betterpvp.clans.skills.selector.skills.assassin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.classes.roles.Assassin;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilMath;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.Arrays;


public class Backstab extends Skill{


	public Backstab(Clans i) {
		super(i, "Backstab", "Assassin",
				getSwords,
				noActions, 5, true, false);
		// TODO Auto-generated constructor stub

	}

	
	@EventHandler 
	public void onEntDamage(CustomDamageEvent e){
		if(e.getCause() != DamageCause.ENTITY_ATTACK) return;
		if(e.getDamager() instanceof Player){
			Player dam = (Player) e.getDamager();
			Role role = Role.getRole(dam);
			if (role != null && role.getName().equals(getClassType())) {
				if(hasSkill(dam, this)){
					
					if (Arrays.asList(getMaterials()).contains(dam.getPlayer().getItemInHand().getType())) {
						
							if(UtilMath.getAngle(dam.getLocation().getDirection(), e.getDamagee().getLocation().getDirection()) < 60){
						
								LogManager.addLog(e.getDamagee(), dam, "Backstab");
								int level = getLevel(dam);
								e.setDamage(e.getDamage() * (1.2 + (level * 0.1)));
								dam.getWorld().playEffect(e.getDamagee().getLocation().add(0, 1, 0), Effect.STEP_SOUND, 152);
								
								if(e.getDamagee() instanceof Player) {
									Player player = (Player) e.getDamagee();
									Role r = Role.getRole(player);
									if(r != null) {
										if(r instanceof Assassin) {
											e.setDamage(e.getDamage()* 0.80); 
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
	public void activateSkill(Player p) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean usageCheck(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[] getDescription(int level) {
		// TODO Auto-generated method stub
		return new String[]{
				"Hitting an enemy from behind will increased",
				"your damage dealt by " + ChatColor.GREEN + (2 + level) + "0%"};
	}

	@Override
	public Types getType() {
		// TODO Auto-generated method stub
		return Types.PASSIVE_A;
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
