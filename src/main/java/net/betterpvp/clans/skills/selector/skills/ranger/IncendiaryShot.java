package net.betterpvp.clans.skills.selector.skills.ranger;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.gamer.combat.LogManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.events.SkillDequipEvent;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IncendiaryShot extends Skill {

	public static List<UUID> active = new ArrayList<UUID>();
	private List<Arrow> incens = new ArrayList<>();

	public IncendiaryShot(Clans i) {
		super(i,"Incendiary Shot", "Ranger",
				getBow, leftClick
				, 5, true, true);
	}

	@Override
	public String[] getDescription(int level) {
		// TODO Auto-generated method stub
		return new String[] {
				"Left click to Activate.",
				"",
				"Shoot an ignited arrow",
				"burning anyone hit for " + ChatColor.GREEN + (0 + level) + ChatColor.GRAY + " seconds",
				"",
				"Cooldown: " + ChatColor.GREEN + getRecharge(level)
		};
	}

	@EventHandler
	public void onDequip(SkillDequipEvent e){
		if(e.getSkill() == this){
			if(active.contains(e.getPlayer().getUniqueId())){
				active.remove(e.getPlayer().getUniqueId());
			}
		}
	}

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onDamage(CustomDamageEvent e){
		if(e.getProjectile() != null){

			if(e.getDamager() instanceof Player){
				Player p = (Player) e.getDamager();
				if(hasSkill(p, this)){
					if(e.getProjectile() instanceof Arrow){
						Arrow a = (Arrow) e.getProjectile();
						if(incens.contains(a)){
							e.setReason("Incendiary Shot");
							e.getDamagee().setFireTicks(getLevel(p) * 30);
							LogManager.addLog(e.getDamagee(), p, "Incendiary Shot");


						}
					}
				}
			}
		}
	}

	@EventHandler
	public void ShootBow(EntityShootBowEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		if (!(event.getProjectile() instanceof Arrow)) {
			return;
		}

		Player player = (Player) event.getEntity();
		if (!active.contains(player.getUniqueId())) {
			return;
		}

		UtilMessage.message(player, getClassType(), "You fired " + ChatColor.GREEN + getName() + ".");
		//event.getProjectile().setFireTicks(Integer.MAX_VALUE);
		active.remove(player.getUniqueId());
		incens.add((Arrow) event.getProjectile());
	}

	@Override
	public void activateSkill(Player player) {
		active.remove(player.getUniqueId());

		active.add(player.getUniqueId());
		player.getWorld().playSound(player.getLocation(), Sound.BLAZE_BREATH, 2.5F, 2.0F);
		UtilMessage.message(player, getClassType(), "You have prepared " + ChatColor.GREEN + getName(getLevel(player)) + ".");
	}

	@Override
	public boolean usageCheck(Player player) {
		if (player.getLocation().getBlock().getType() == Material.WATER || player.getLocation().getBlock().getType() == Material.STATIONARY_WATER) {
			UtilMessage.message(player, "Skill", "You cannot use " + ChatColor.GREEN + getName() + " in water.");
			return false;
		}
		return true;
	}



	@Override
	public Types getType() {
		// TODO Auto-generated method stub
		return Types.BOW;
	}

	@Override
	public double getRecharge(int level) {
		// TODO Auto-generated method stub
		return 12 - ((level -1));
	}

	@Override
	public float getEnergy(int level) {
		// TODO Auto-generated method stub
		return 30 - ((level -1) * 2);
	}

	@Override
	public boolean requiresShield() {
		// TODO Auto-generated method stub
		return false;
	}

}
