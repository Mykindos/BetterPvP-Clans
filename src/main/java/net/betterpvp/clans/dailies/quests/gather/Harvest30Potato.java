package net.betterpvp.clans.dailies.quests.gather;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.dailies.progression.Progress;
import net.betterpvp.clans.dailies.progression.types.GeneralProgression;
import net.betterpvp.clans.dailies.quests.General;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class Harvest30Potato extends General{

	public Harvest30Potato(Clans i) {
		super(i, "Harvest 20 Potatoes",	new String[]{
				ChatColor.GRAY + "Harvest " + ChatColor.GREEN + "20" + ChatColor.GRAY + " batches of potatoes.",
		});

	}

	@Override
	public int getRequiredAmount() {
		// TODO Auto-generated method stub
		return 20;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBreak(BlockBreakEvent e){
		if(isActive()){
			if(e.getBlock().getType() == Material.POTATOES){
				if(e.getBlock().getData() == CropState.RIPE.getData()){
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
