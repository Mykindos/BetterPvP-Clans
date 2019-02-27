package net.betterpvp.clans.weapon.weapons.legendaries;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.combat.throwables.ThrowableManager;
import net.betterpvp.clans.combat.throwables.events.ThrowableCollideEntityEvent;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.core.framework.UpdateEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class FireWalkers extends Weapon{

	public FireWalkers(Clans i) {
		super(i, Material.DIAMOND_BOOTS, (byte) 0, ChatColor.RED + "Flamebringer Treads", 
				new String[]{"",
						ChatColor.GRAY + "Passive: " + ChatColor.YELLOW + "Fire Walking",
						"",
						ChatColor.WHITE + "Legend has it these mighty pair of boots were",
						ChatColor.WHITE + "forged from the volcano of Erebisa thousands of years ago",
						"",
						ChatColor.WHITE + "Wherever you may walk, fire shall shadow you."}, true, 4.0);
		// TODO Auto-generated constructor stub
	}

	@EventHandler
	public void fire(UpdateEvent e){
		if(e.getType() == UpdateEvent.UpdateType.TICK_2){
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(p.getEquipment().getBoots() != null) {
					if (WeaponManager.isWeapon(p.getEquipment().getBoots())
							&& WeaponManager.getWeapon(p.getEquipment().getBoots()).equals(this)) {
						Item fire = p.getWorld().dropItem(p.getLocation().add(0.0D, 0.5D, 0.0D), new ItemStack(Material.BLAZE_POWDER));
						ThrowableManager.addThrowable(fire, p, getName(), 2000L);
						fire.setVelocity(new Vector((Math.random() - 0.5D) / 3.0D, Math.random() / 3.0D, (Math.random() - 0.5D) / 3.0D));


					}
				}
			}
		}

	}
	
	@EventHandler
	public void onCollide(ThrowableCollideEntityEvent e){
		if(e.getThrowable().getSkillName().equals(getName())){
			if(e.getThrowable().getThrower() instanceof Player){
				Player damager = (Player) e.getThrowable().getThrower();
				if(damager != null){
					if(e.getCollision() instanceof Player){
						if(!ClanUtilities.canHurt(damager, (Player) e.getCollision())){
							return; //TODO cancel this event
						}
					}

					if(e.getCollision().getFireTicks() > 0) return;
					LogManager.addLog(e.getCollision(), damager, "Flamebringer Treads");
					e.getCollision().setFireTicks(80);
				}
			}}
	}

}
