package net.betterpvp.clans.worldevents.types.bosses.ads;

import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.gamer.combat.LogManager;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilPlayer;
import net.betterpvp.core.utility.UtilVelocity;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class SlimeRocket{
	
	private Location loc;
	private Slime s;
	private LivingEntity target;
	
	public SlimeRocket(Location loc, LivingEntity target){
		this.loc = loc;
		this.target = target;
		this.loc = loc;
		
		this.s = (Slime) loc.getWorld().spawnEntity(loc, EntityType.SLIME);
		s.setSize(1);
		s.setMaxHealth(2048);
		s.setHealth(2048);
	}
	
	public Slime getSlime(){
		return s;
	}
	
	public LivingEntity getTarget(){
		return target;
	}
	
	public Location getLocation(){
		return loc;
	}
	
	public void explode(){
		s.getWorld().createExplosion(s.getLocation(), 0f);
		for(LivingEntity ent : UtilPlayer.getAllInRadius(s.getLocation(), 2)){
			Bukkit.getPluginManager().callEvent(new CustomDamageEvent(ent, s, null, DamageCause.ENTITY_EXPLOSION, 4, false));
			LogManager.addLog(ent, s, ChatColor.RED.toString() + ChatColor.BOLD + "Slime King", "Slime Rocket" );
		}
		
		s.remove();
	}
	
	public void update(){
		if(s.getTicksLived() > 400 || getTarget() == null || s == null || s.isDead()){
			explode();
			return;
		}
		
		if(UtilMath.offset(s, target) < 2){
			explode();
		}else{
			UtilVelocity.velocity(s, UtilVelocity.getTrajectory(s, target), 0.6, false, 0, 0.2, 1, true);
		}
	}

}
