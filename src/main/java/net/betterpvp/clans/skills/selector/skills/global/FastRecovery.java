package net.betterpvp.clans.skills.selector.skills.global;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.RegenerateEnergyEvent;
import net.betterpvp.clans.client.Client;
import net.betterpvp.clans.client.ClientUtilities;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.RoleBuild;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.weapon.Weapon;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class FastRecovery extends Skill{

	public FastRecovery(Clans i) {
		super(i, "Fast Recovery", "Global", noMaterials, noActions, 5, false, false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getDescription(int level) {

		return new String[]{
				"Increase your energy regeneration speed,",
				"by "+ ChatColor.GREEN + (20 * level) + "%",
				"",
				"Does not work with legendary items equipped."

		};
	}

	@EventHandler
	public void onEnergyRegen(RegenerateEnergyEvent e){
		Player p = e.getPlayer();
		Role r = Role.getRole(p);
		if(r != null){
			
			Client c = ClientUtilities.getOnlineClient(p);
			if(c != null){
				if(c.getGamer() != null){
					RoleBuild  rb = c.getGamer().getActiveBuild(r.getName());
					if(rb != null){
					if(rb.getGlobal() != null){
						if(rb.getGlobal().getSkill().equals(this)){
							if(Weapon.getWeapon(p.getItemInHand()) != null){
								return;
							}

							e.setEnergy(e.getEnergy() * (1.0 + (20.0 * getLevel(e.getPlayer()) / 100.0) ));
						}
					}
					}
				}
			}
		}
	}

	@Override
	public Types getType() {

		return Types.GLOBAL;
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
	public boolean requiresShield() {

		return false;
	}

	@Override
	public void activateSkill(Player p) {
	}

	@Override
	public boolean usageCheck(Player p) {

		return false;
	}

}
