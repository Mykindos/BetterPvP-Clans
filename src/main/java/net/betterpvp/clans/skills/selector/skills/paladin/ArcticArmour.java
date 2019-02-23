package net.betterpvp.clans.skills.selector.skills.paladin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.events.UpdateEvent;
import net.betterpvp.clans.events.UpdateEvent.UpdateType;
import net.betterpvp.clans.module.BlockRestoreData;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.utility.UtilBlock;
import net.betterpvp.clans.utility.UtilMessage;
import net.betterpvp.clans.utility.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;



public class ArcticArmour extends Skill{

	private Set<UUID> active = new HashSet<>();

	public ArcticArmour(Clans i) {
		super(i, "Arctic Armour", "Paladin", getSwordsAndAxes,
				noActions, 5,
				false, false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getDescription(int level) {
		// TODO Auto-generated method stub
		return  new String[] {
				"Drop Axe/Sword to Toggle.", 
				"", 
				"Create a freezing area around you", 
				"in a " + ChatColor.GREEN + (2 + level) + ChatColor.GRAY + " Block radius. Allies inside", 
				"this area receive Protection I.", 
				"Enemies inside this area receive",
				"Slowness I",
				"",
				"Energy / Second: " + ChatColor.GREEN + getEnergy(level)
		};
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e){
		Player p = e.getPlayer();
		if(Role.getRole(p) != null && Role.getRole(p).getName().equals(getClassType())){
			if(hasSkill(p, this)){
				if(Arrays.asList(getMaterials()).contains(e.getItemDrop().getItemStack().getType())){

					e.setCancelled(true);

					if (active.contains(p.getUniqueId())){
						active.remove(p.getUniqueId());
						UtilMessage.message(p, getClassType(), "Arctic Armour: " + ChatColor.RED + "Off");
					}else{
						if(ClanUtilities.canCast(p)){
							active.add(p.getUniqueId());
							UtilMessage.message(p, getClassType(), "Arctic Armour: " + ChatColor.GREEN + "On");
						}
					}
				}
			}

		}
	}

	@EventHandler
	public void audio(UpdateEvent event)
	{
		if (event.getType() == UpdateType.SEC) {
			for(UUID uuid : active){
				if(Bukkit.getPlayer(uuid) != null){
					Player cur = Bukkit.getPlayer(uuid);
					cur.getWorld().playSound(cur.getLocation(), Sound.AMBIENCE_RAIN, 0.3F, 0.0F);
				}
			}
		}
	}

	@EventHandler
	public void SnowAura(UpdateEvent event) {
		if (event.getType() != UpdateType.TICK) {
			return;
		}
		Iterator<UUID> iterator = active.iterator();
		while(iterator.hasNext()){
			UUID z = iterator.next();
			if(Bukkit.getPlayer(z) != null){
				Player cur = Bukkit.getPlayer(z);
				if(!hasSkill(cur, this)){

					iterator.remove();


				}else if (!Energy.use(cur, getName(), getEnergy(getLevel(cur)) / 2, true)){
					iterator.remove();

				}else if(cur == null){
					iterator.remove();
				
				}else if(Role.getRole(cur) == null || Role.getRole(cur) != null && !Role.getRole(cur).getName().equals(getClassType())){
					iterator.remove();
				}else if(EffectManager.isSilenced(cur)) {
					iterator.remove();
				}else{

					long duration = 2000;
					int level = (2 + getLevel(cur));
					HashMap<Block, Double> blocks = UtilBlock.getInRadius(cur.getLocation(),level);
					for(Player p : UtilPlayer.getInRadius(cur.getLocation(), (level))){
						if(!ClanUtilities.canHurt(cur, p)){
							p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 0));
							EffectManager.addEffect(p, EffectType.RESISTANCE, 1, 1000);
						}else{
							p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 0));
						}
					}
					for (Block block : blocks.keySet())
					{
						if(block.getLocation().getY() <= cur.getLocation().getY()){
							if(block.getRelative(BlockFace.DOWN).getType() != Material.SNOW && UtilBlock.isGrounded(cur) 
									&& block.getRelative(BlockFace.DOWN).getType() != Material.AIR){
								if(block.getType() == Material.AIR){

									new BlockRestoreData(block, 79, (byte)0, duration);


									block.setType(Material.SNOW);
								}

							}
						}
					}
				}
			}else{
				iterator.remove();
			}
		}

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

	@Override
	public double getRecharge(int level) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getEnergy(int level) {
		// TODO Auto-generated method stub
		return (float) (3 - ((level - 1) * 0.5));
	}

	@Override
	public boolean requiresShield() {
		// TODO Auto-generated method stub
		return false;
	}



}
