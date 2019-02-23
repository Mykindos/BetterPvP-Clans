package net.betterpvp.clans.skills.selector.skills.ranger;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.events.UpdateEvent;
import net.betterpvp.clans.events.UpdateEvent.UpdateType;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.utility.UtilMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Agility extends Skill{

	private static Set<UUID> active = new HashSet<>();

	public static Set<UUID> getActive(){
		return active;
	}

	public Agility(Clans i) {
		super(i, "Agility", "Ranger", getAxes, rightClick, 5, true, true);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getDescription(int level) {
		// TODO Auto-generated method stub
		return new String[]{
				"Right click with Axe to Activate.",
				"",
				"Sprint with great agility, gaining",
				"Speed I for " + ChatColor.GREEN + (3 + level) + ChatColor.GRAY + " seconds.",
				"You are immune to melee attacks while sprinting.",
				"Agility ends if you interact",
				"",
				"Cooldown: " + ChatColor.GREEN + getRecharge(level),
				"Energy: " + ChatColor.GREEN + getEnergy(level)
		};
	}

	@Override
	public Types getType() {
		// TODO Auto-generated method stub
		return Types.AXE;
	}

	@Override
	public double getRecharge(int level) {
		// TODO Auto-generated method stub
		return 30 - ((level -1));
	}

	@Override
	public float getEnergy(int level) {
		// TODO Auto-generated method stub
		return 40 - ((level -1) * 2);
	}

	@Override
	public void activateSkill(Player player) {
		if(!active.contains(player.getUniqueId())){
			UtilMessage.message(player, getClassType(), "You used " + ChatColor.GREEN + getName() + " " + getLevel(player));
			active.add(player.getUniqueId());
			player.getWorld().playSound(player.getLocation(), Sound.NOTE_PLING, 0.5F, 0.5F);
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (3 + getLevel(player)) * 20, 0));
		}

	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void endOnInteract(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		if (event.getAction() != Action.PHYSICAL) {


			if (active.contains(player.getUniqueId())) {
				active.remove(player.getUniqueId());
				player.removePotionEffect(PotionEffectType.SPEED);
			}

		}
	}
	
	

	@EventHandler
	public void onDamage(CustomDamageEvent e){
		if(e.getDamager() instanceof Player){
			if(e.getDamagee() instanceof Player){
				Player dam = (Player) e.getDamager();
				Player p = (Player) e.getDamagee();
				if(p.isSprinting()){


					if(active.contains(p.getUniqueId())){
						e.setCancelled("Agility");
						UtilMessage.message(dam, getClassType(), p.getName() + " is using " + getName());
						p.getWorld().playSound(p.getLocation(), Sound.BLAZE_BREATH, 0.5F, 2.0F);
					}
				}


			}
		}
	}

	@EventHandler
	public void onUpdate(UpdateEvent event){
		if (event.getType() == UpdateType.FAST) {

			HashSet<UUID> expired = new HashSet<>();
			for (UUID cur : active) {
				if(Bukkit.getPlayer(cur) != null){
					Player p = Bukkit.getPlayer(cur);
					if (!p.hasPotionEffect(PotionEffectType.SPEED)) {
						expired.add(p.getUniqueId());
					}else if(!hasSkill(p, this)){
						expired.add(p.getUniqueId());
					}
				}else{
					expired.add(cur);
				}
			}
			for (UUID cur : expired) {
				active.remove(cur);
			}
		}
	}

	@Override
	public boolean usageCheck(Player player) {
		if(player.getLocation().getBlock().isLiquid()){
			UtilMessage.message(player, getClassType(), "You cannot use " + getName() + " in water.");
			return false;
		}
		return true;
	}

	@Override
	public boolean requiresShield() {
		// TODO Auto-generated method stub
		return false;
	}

}
