package net.betterpvp.clans.dailies.quests.gather;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.dailies.progression.Progress;
import net.betterpvp.clans.dailies.progression.types.GeneralProgression;
import net.betterpvp.clans.dailies.quests.General;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

public class FieldsCoalOre extends General{

	public FieldsCoalOre(Clans i) {
		super(i, "Fields Coal Ore",	new String[]{
				ChatColor.GRAY + "Mine " + ChatColor.GREEN + "5" + ChatColor.GRAY + " Coal nodes at the fields",
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
			Clan c = ClanUtilities.getClan(e.getBlock().getLocation());
			if(c != null){
				if(c.getName().equalsIgnoreCase("Fields")){
					if(e.getBlock().getType() == Material.COAL_ORE){
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
