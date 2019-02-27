package net.betterpvp.clans.skills.selector.skills.knight;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilPlayer;
import net.betterpvp.core.utility.UtilTime;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.HashSet;
import java.util.WeakHashMap;

public class Fortitude extends Skill{

	private WeakHashMap<Player, Integer> health = new WeakHashMap<>();
	private WeakHashMap<Player, Long> last = new WeakHashMap<>();

	public Fortitude(Clans i) {
		super(i, "Fortitude", "Knight", noMaterials, noActions, 3,
				false, false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getDescription(int level) {
		// TODO Auto-generated method stub
		return new String[] {
				"After taking damage, you slowly", 
				"regenerate up to " + ChatColor.GREEN + (3 + (level-1) )  + ChatColor.GRAY + " health, at a", 
				"rate of 1 health per 1 seconds.", 
				"", 
				"This does not stack, and is reset", 
				"if you are hit again." };
	}

	@Override
	public Types getType() {
		// TODO Auto-generated method stub
		return Types.PASSIVE_B;
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
	public void onHit(CustomDamageEvent e){
		if(e.getDamagee() instanceof Player){
			if(e.getDamager() instanceof LivingEntity){
				Player p = (Player) e.getDamagee();
				if(Role.getRole(p) != null && Role.getRole(p).getName().equals(getClassType())){
					if(hasSkill(p, this)){

						health.put(p, Math.min((2 + getLevel(p)),(int) e.getDamage() / 2));
						last.put(p, Long.valueOf(System.currentTimeMillis()));
					}
				}
			}
		}

	}

	@EventHandler
	public void Update(UpdateEvent event) {
		if (event.getType() != UpdateType.FASTER) {
			return;
		}
		HashSet<Player> remove = new HashSet<>();
		for (Player cur : health.keySet()){


			if (UtilTime.elapsed(last.get(cur), 2500L))
			{
				health.put(cur, health.get(cur)- 1);
				last.put(cur, Long.valueOf(System.currentTimeMillis()));
				if (health.get(cur) <= 0) {
					remove.add(cur);
				}
				UtilPlayer.health(cur, 1.0D);
			}
		}

		for (Player cur : remove)
		{
			health.remove(cur);
			last.remove(cur);
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
