package net.betterpvp.clans.dailies.quests.gather;

import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.material.NetherWarts;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.dailies.progression.Progress;
import net.betterpvp.clans.dailies.progression.types.GeneralProgression;
import net.betterpvp.clans.dailies.quests.General;
import net.md_5.bungee.api.ChatColor;

public class Harvest30Netherwarts extends General{

	public Harvest30Netherwarts(Clans i) {
		super(i, "Harvest 20 Nether Warts",	new String[]{
				ChatColor.GRAY + "Harvest " + ChatColor.GREEN + "20" + ChatColor.GRAY + " batches of Nether Wart.",
		});

	}

	@Override
	public int getRequiredAmount() {
		// TODO Auto-generated method stub
		return 20;
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e){
		if(isActive()){
			if(e.getBlock().getType() == Material.NETHER_WART_BLOCK){
				NetherWarts n = (NetherWarts) e.getBlock().getState().getData();
				if( n.getState() == NetherWartsState.RIPE){
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
