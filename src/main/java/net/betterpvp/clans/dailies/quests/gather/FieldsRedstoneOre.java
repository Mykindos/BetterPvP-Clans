package net.betterpvp.clans.dailies.quests.gather;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.dailies.progression.Progress;
import net.betterpvp.clans.dailies.progression.types.GeneralProgression;
import net.betterpvp.clans.dailies.quests.General;
import net.md_5.bungee.api.ChatColor;

public class FieldsRedstoneOre extends General{

	public FieldsRedstoneOre(Clans i) {
		super(i, "Fields Redstone Ore",	new String[]{
				ChatColor.GRAY + "Mine " + ChatColor.GREEN + "5" + ChatColor.GRAY + " Redstone nodes at the fields",
		});

	}

	@Override
	public int getRequiredAmount() {
		// TODO Auto-generated method stub
		return 5;
	}

	@EventHandler (priority = EventPriority.LOWEST)
	public void onBreak(BlockBreakEvent e){
		if(isActive()){
			if(ClanUtilities.getClan(e.getBlock().getLocation()) != null){
				if(ClanUtilities.getClan(e.getBlock().getLocation()).getName().equals("Fields")){
					if(e.getBlock().getType() == Material.REDSTONE_ORE || e.getBlock().getType() == Material.GLOWING_REDSTONE_ORE){
						Progress p = getQuestProgression(e.getPlayer().getUniqueId(), getName());

						if(!p.isComplete()){
							if(p instanceof GeneralProgression){
								GeneralProgression gp = (GeneralProgression) p;
								gp.addCurrentAmount();

								if(gp.getCurrentAmount() >= gp.getRequiredAmount()){
									gp.onComplete(e.getPlayer().getUniqueId());
								}
							}
						}

					}
				}
			}
		}
	}


}
