package net.betterpvp.clans.skills.selector.skills.data;

import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RecallDataOLD {

	public static List<RecallDataOLD> recalls = new ArrayList<RecallDataOLD>();

	private String player;
	private Location location;
	private Long time;
	private Item item;

	public RecallDataOLD(Player player, Location location) {
		this.player = player.getName();
		this.location = location;
		this.time = System.currentTimeMillis() + 15000;
		this.item = location.getWorld().dropItem(location, new ItemStack(Material.RECORD_5));
		item.setPickupDelay(Integer.MAX_VALUE);
		recalls.add(this);
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player.getName();
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public static RecallDataOLD getRecallData(Player player) {
		for (RecallDataOLD data : recalls) {
			if (data.getPlayer().equalsIgnoreCase(player.getName())) {
				return data;
			}
		}
		return null;
	}

	public static boolean containsData(Player player) {
		for (RecallDataOLD data : recalls) {
			if (data.getPlayer().equalsIgnoreCase(player.getName())) {
				return true;
			}
		}
		return false;
	}

	public static void recall(Player player) {
		if (getRecallData(player) != null) {
			RecallDataOLD data = getRecallData(player);
			if(data.getLocation() != null){
				if(data.getItem() != null){
					if(data.getLocation().distance(player.getLocation()) <= 30){
						player.teleport(data.getLocation());
						data.getItem().getLocation().getWorld().playEffect(data.getItem().getLocation(), Effect.STEP_SOUND, Material.EMERALD_BLOCK);
						player.getWorld().playSound(player.getLocation(), Sound.ZOMBIE_UNFECT, 2.0F, 2.0F);
						data.getItem().remove();
					}else{
						UtilMessage.message(player, "Recall", "You need to be within 30 blocks of your original position to recall!");
					}
				}

			}
			recalls.remove(data);
		}
	}
}
